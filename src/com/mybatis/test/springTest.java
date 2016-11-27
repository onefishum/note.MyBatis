package com.mybatis.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.mybatis.dao.IUserAnnotationDao;
import com.mybatis.domain.User;
import com.mybatis.service.IUserService;

public class springTest {
	@Test
	public void testSpring() {
		ApplicationContext ctx= new ClassPathXmlApplicationContext("beans.xml");
		IUserService userService = (IUserService) ctx.getBean("userService");
		User user = userService.findUserById(1);
		System.out.println(user);
	}

	/**
	 * Annotation 实现方法。
	 * @throws IOException
	 */
	@Test
	public void testSelectUserByIdAn() throws IOException{
		String resource = "sqlMapConfig.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		
		SqlSession openSession = sqlSessionFactory.openSession();
		// 调用getMapper
		IUserAnnotationDao annotationDao = openSession.getMapper(IUserAnnotationDao.class);
		User user = annotationDao.findUserByIdA(1);
		System.out.println(user);
	}
}
