package com.mybatis.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.type.PrimitiveType;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.omg.PortableServer.ID_ASSIGNMENT_POLICY_ID;

import com.mybatis.domain.Customer;
import com.mybatis.domain.Order;
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
		int id = openSession.delete(arg0, 1);
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
		// UserMapper.xml 中的命名空间名 + 唯一id 锁定一个SQL
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
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "map");
		map.put("address", "beijing");
		map.put("age", 1);

		openSession.insert(arg0, map);
		openSession.commit();
		System.out.print(map);
	}

	/**
	 * 测试字段引用(抽取字段名) 别名测试
	 */
	@Test
	public void testSelectFeild() {
		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.UserMapper.selectAllfield";
		List<User> selectList = openSession.selectList(arg0);
		for (User user : selectList) {
			System.out.println(user);
		}
	}

	/**
	 * 根据name进行模糊查询
	 */
	@Test
	public void testSelectLike() {
		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.UserMapper.selectUserByNameLike";

		User user2 = new User();
		user2.setName("l");
		List<User> selectList = openSession.selectList(arg0, user2);
		for (User user : selectList) {
			System.out.println(user);
		}
	}

	/**
	 * 关联查询 Order
	 */
	@Test
	public void testSelectOrderById() {
		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.OrderMapper.selectOrderById";

		Order order = openSession.selectOne(arg0, 2);
		System.out.println(order);
	}

	/**
	 * 关联集合查询 Customer
	 */
	@Test
	public void testSelectCustomerById() {
		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.CustomerMapper.selectCustomerById";

		Customer customer = openSession.selectOne(arg0, 1);
		System.out.println(customer);
	}

	/**
	 * 动态SQL 查询
	 */
	@Test
	public void testSelectUserCondition() {
		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.UserMapper.selectUserByCondition2";

		User user = new User();
		user.setName("onefish");

		List<User> user2 = openSession.selectList(arg0, user);
		System.out.println("MybatisTest.testSelectUserCondition()");
		for (User user3 : user2) {
			System.out.println(user3);
		}
	}

	/**
	 * 动态SQL update set语法
	 */
	@Test
	public void testUpdateUserByConition() {
		// 从会话工厂中得到一个会话对象
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.UserMapper.updateUserByConition";

		User user = new User();
		user.setId(6);
		user.setAge(35);
		user.setName("lcm");
		int id = openSession.update(arg0, user);
		openSession.commit();
		System.out.println(id);
	}
}
