package com.mybatis.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import com.mybatis.domain.User;

public class MybatisTest {
	private SqlSessionFactory sqlSessionFactory;

	@Before
	public void initSessionFactory() throws IOException {
		String resource = "sqlMapConfig.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
	}

	@Test
	public void testSelect() {

		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.UserMapper.selectUserById";
		User user = openSession.selectOne(arg0, 1);
		System.out.println(user);
	}

	@Test
	public void testDelete() {
		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.UserMapper.deleteUserById";
		// 在delete里默认设置为手工提交
		int id = openSession.delete(arg0, 3);
		// 事物提交
		openSession.commit();
		System.out.println("删除" + id + "行");
	}

	@Test
	public void testInsert() {
		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.UserMapper.insertUser";
		// 在delete里默认设置为手工提交
		User user = new User("onefish", "chaoyang", 40);
		int id = openSession.insert(arg0, user);
		// 事物提交
		openSession.commit();
		System.out.println("插入" + id + "行, id:" + user.getId());
	}

	@Test
	public void testUpdate() {
		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.UserMapper.updateUser";
		// 在delete里默认设置为手工提交
		User user = new User(1, "lcm", "chengdu", 43);
		int id = openSession.update(arg0, user);
		// 事物提交
		openSession.commit();
		System.out.println("插入" + id + "行");
	}

	@Test
	public void testSelectAll() {
		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.UserMapper.selectAllUser";

		List<User> selectList = openSession.selectList(arg0);
		for (User user : selectList) {
			System.out.println(user);
		}
	}

	/**
	 * 根据id查user,返回值为map类型
	 */
	@Test
	public void testSelectMap() {
		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.UserMapper.selectUserById4Map";
		Map<String, Object> selectOne = openSession.selectOne(arg0, 5);
		System.out.println(selectOne);
	}

	/**
	 * 插入，使用map类型
	 */
	@Test
	public void testInsertMap() {
		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.UserMapper.insertUser4Map";
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("name", "map");
		map.put("address", "beijing");
		map.put("age", 1);
		
		openSession.insert(arg0, map);
		openSession.commit();
		System.out.print(map);
	}

}
