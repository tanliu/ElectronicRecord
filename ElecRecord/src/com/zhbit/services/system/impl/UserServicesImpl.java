package com.zhbit.services.system.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.management.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zhbit.dao.system.UserDao;
import com.zhbit.entity.Role;
import com.zhbit.entity.User;
import com.zhbit.entity.UserRole;
import com.zhbit.services.BaseServicesImpl;
import com.zhbit.services.system.UserServices;
import com.zhbit.util.EncryptUtils;
import com.zhbit.util.PageUtils;
import com.zhbit.util.QueryUtils;

/** 
 * 项目名称：ElecRecord
 * 类名称：UserServicesImpl 
 * 类描述： 用户管理Services实现类
 * 创建人：谭柳
 * 创建时间：2016年5月24日 上午10:43:53
 * 修改人：TanLiu 
 * 修改时间：2016年5月24日 上午10:43:53
 * 修改备注： 
 * @version 
 */ 
@Service(value=UserServices.SERVER_NAME)
public class UserServicesImpl extends BaseServicesImpl<User> implements UserServices {
	UserDao userDao;
	@Resource(name=UserDao.DAO_NAME)
	public void setUserDao(UserDao userDao) {
		super.setBaseDao(userDao);
		this.userDao = userDao;
	}
	
	@Override
	public PageUtils queryList(User user, int pageNO, int pageSize) {
		String[] fields=null;
		String[] params=null;
		String proterty="createTime";		
		if(user!=null){ //user不为空时
			if(!StringUtils.isEmpty(user.getEmployNo())){ //查询条件是用户编号
				fields=new String[]{"employNo=?"};
				params=new String[]{user.getEmployNo()};
			}else if(!StringUtils.isEmpty(user.getEmployName())){ //查询条件是用户名
				fields=new String[]{"employName like ?"};
				params=new String[]{"%"+user.getEmployName()+"%"};
				
			}
			
		}
		// TODO Auto-generated method stub
		return getPageUtils(fields, params, proterty, QueryUtils.ORDER_BY_ASC, pageNO, pageSize);
	}

	@Override
	public boolean editorPwd(User user, String confirmpwd) {
		Boolean isEditor=false;//表示是否修改成功
		if(!StringUtils.isEmpty(user.getPassword())&&user.getPassword().endsWith(confirmpwd)){//判断两个密码是否相等
			if(!StringUtils.isEmpty(user.getUserId())){
				User temp=userDao.findObjectById(user.getUserId());
				temp.setPassword(EncryptUtils.MD5Encrypt(user.getPassword()));
				isEditor=true;
			}			
		}
		return isEditor;
	}

	@Override
	public void saveUser(User user, String[] roleIds) {
		//设置用户创建时间 
		user.setCreateTime(new Timestamp(new Date().getTime()));
		//对用户数据进行加密处理
		user.setPassword(EncryptUtils.MD5Encrypt(user.getPassword()));
		userDao.save(user);
		//保存
		if(roleIds!=null){
			for (String roleId : roleIds) {				
				UserRole userRole=new UserRole(user, new Role(roleId)) ;
				userDao.saveUserRole(userRole);
			}
			
		}
		
	}

	@Override
	public void editorUser(User user, String[] roleIds) {
		
		
		this.update(user);	
		//删除用户在用户角色表中的所有数据
		userDao.deleteUserRole(user.getUserId());		
		//新建用户在角色表中的数据
		if(roleIds!=null&&roleIds.length>0){
			for (String roleId : roleIds) {				
				UserRole userRole=new UserRole(user, new Role(roleId)) ;
				userDao.saveUserRole(userRole);
			}			
		}
	}

	@Override
	public void deleteUser(String[] selectedRow) {
		if(selectedRow!=null&&selectedRow.length>0){
			for (String userId : selectedRow) {
/*				//先删除角色用户表
				userDao.deleteUserRole(userId);
				//再删除用户
				userDao.deleteObjectByIds(userId);*/
				List<User> users=new ArrayList<User>();
				User user=findObjectById(userId);
				user.getUserRoles().remove(user);
				users.add(user);
				deleteObjectByCollection(users);
			}
		}
		
	}


	
	
}
