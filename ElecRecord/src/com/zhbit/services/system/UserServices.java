package com.zhbit.services.system;

import com.zhbit.entity.User;
import com.zhbit.services.BaseServices;

/** 
 * 项目名称：ElecRecord
 * 类名称：UserServices 
 * 类描述： 用户管理的Service层接口
 * 创建人：谭柳
 * 创建时间：2016年5月24日 上午10:42:59
 * 修改人：TanLiu 
 * 修改时间：2016年5月24日 上午10:42:59
 * 修改备注： 
 * @version 
 */ 
public interface UserServices extends BaseServices<User> {
   public final static String SERVER_NAME="com.zhbit.services.system.impl.UserServicesImpl";
}