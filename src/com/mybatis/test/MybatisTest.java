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

		// �ӻỰ�����еõ�һ���Ự����
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml �е������ռ��� + Ψһid
		String arg0 = "com.mybatis.domain.UserMapper.selectUserById";
		User user = openSession.selectOne(arg0, 1);
		System.out.println(user);
	}

	@Test
	public void testDelete() {
		// �ӻỰ�����еõ�һ���Ự����
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml �е������ռ��� + Ψһid
		String arg0 = "com.mybatis.domain.UserMapper.deleteUserById";
		// ��delete��Ĭ������Ϊ�ֹ��ύ
		int id = openSession.delete(arg0, 3);
		// �����ύ
		openSession.commit();
		System.out.println("ɾ��" + id + "��");
	}

	@Test
	public void testInsert() {
		// �ӻỰ�����еõ�һ���Ự����
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml �е������ռ��� + Ψһid
		String arg0 = "com.mybatis.domain.UserMapper.insertUser";
		// ��delete��Ĭ������Ϊ�ֹ��ύ
		User user = new User("onefish", "chaoyang", 40);
		int id = openSession.insert(arg0, user);
		// �����ύ
		openSession.commit();
		System.out.println("����" + id + "��, id:" + user.getId());
	}

	@Test
	public void testUpdate() {
		// �ӻỰ�����еõ�һ���Ự����
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml �е������ռ��� + Ψһid
		String arg0 = "com.mybatis.domain.UserMapper.updateUser";
		// ��delete��Ĭ������Ϊ�ֹ��ύ
		User user = new User(1, "lcm", "chengdu", 43);
		int id = openSession.update(arg0, user);
		// �����ύ
		openSession.commit();
		System.out.println("����" + id + "��");
	}

	@Test
	public void testSelectAll() {
		// �ӻỰ�����еõ�һ���Ự����
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml �е������ռ��� + Ψһid
		String arg0 = "com.mybatis.domain.UserMapper.selectAllUser";

		List<User> selectList = openSession.selectList(arg0);
		for (User user : selectList) {
			System.out.println(user);
		}
	}

	/**
	 * ����id��user,����ֵΪmap����
	 */
	@Test
	public void testSelectMap() {
		// �ӻỰ�����еõ�һ���Ự����
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml �е������ռ��� + Ψһid
		String arg0 = "com.mybatis.domain.UserMapper.selectUserById4Map";
		Map<String, Object> selectOne = openSession.selectOne(arg0, 5);
		System.out.println(selectOne);
	}

	/**
	 * ���룬ʹ��map����
	 */
	@Test
	public void testInsertMap() {
		// �ӻỰ�����еõ�һ���Ự����
		SqlSession openSession = sqlSessionFactory.openSession();
		// UserMapper.xml �е������ռ��� + Ψһid
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
