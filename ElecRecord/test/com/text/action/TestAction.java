/**
 * 
 */
package com.text.action;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.text.entity.TestUser;
import com.text.entity.excel.TestEntity;
import com.text.services.TestServices;
import com.text.services.impl.TestServicesImpl;
import com.zhbit.action.BaseAction;
import com.zhbit.action.BaseAndExcelAction;
import com.zhbit.excel.ExcelConfig;
import com.zhbit.services.ExcelServices;
import com.zhbit.services.ExcelServicesImpl;

/** 
 * 项目名称：ElecRecord
 * 类名称：TestAction 
 * 类描述： 测试的Action
 * 创建人：坛子
 * 创建时间：2016年4月25日 下午3:23:02
 * 修改人：TanLiu 
 * 修改时间：2016年4月25日 下午3:23:02
 * 修改备注： 
 * @version 
 */
public class TestAction extends BaseAndExcelAction  implements ModelDriven<TestUser> {

	private TestUser user=new TestUser();	
	
	@Resource(name="testServices")
	TestServices testServices;

	
	public TestUser getUser() {
		return user;
	}

	public void setUser(TestUser user) {
		this.user = user;
	}


	@Override
	public TestUser getModel() {
		return user;
	}
	
	

	public String login(){
		System.out.println(user.getUsername());
		testServices.test(user);
		return "test";
	}

	public String test(){
		
		return "test";
	}
	
	
	@Test
	public void tt(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		TestServices testServices=(TestServices) ac.getBean("testServices");
		TestUser user=new TestUser();
		user.setPassword("123546");
		user.setUsername("谭柳333333");
		System.out.println(user.getUsername());
		testServices.test(user);		
	}
	
	
	@Test
	public void trasaction(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		TestServices testServices=(TestServices) ac.getBean("testServices");
		TestUser user=testServices.findUserbyId("8a8fa1e0544d8d9e01544d8da0270000");

		System.out.println(user.getUsername());		
	}

	@Override
	public String listUI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addUI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String add() {
		return null;
	}

	@Override
	public String delete() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String editorUI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String editor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deleteAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String importExcel() {
		//excelServices.createExcel(config);
		System.out.println(excelFileName);
		try {
			ExcelConfig config=new ExcelConfig(TestEntity.class, "党团关系", 1, new FileInputStream(excel));
			List<Object> lists=excelServices.parseExcel(config);
			for (Object object : lists) {
				TestEntity testEntity=(TestEntity) object;
				System.out.println(testEntity.getName());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "importExcel";
	}

	@Override
	public void exportExcel() {
		List<TestEntity> list=new ArrayList<TestEntity>();
        list.add(new TestEntity("080105031031", "谭柳","20090501","共青团员",null));
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setContentType("application/x-execl");
		try {
			response.setHeader("Content-Disposition", "attachment;filename="+new String("谭柳的文件.xls".getBytes(),"ISO-8859-1"));
			ServletOutputStream outputStream=response.getOutputStream();
			ExcelConfig config=new ExcelConfig(TestEntity.class, "谭柳", outputStream);
			config.setEntitys(list);
			excelServices.createExcel(config);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
}