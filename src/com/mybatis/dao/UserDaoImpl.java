package com.mybatis.dao;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.mybatis.domain.User;

public class UserDaoImpl extends SqlSessionDaoSupport implements IUserDao{

	@Override
	/**
	 * 保存User数据，并返回主键id
	 */
	public int saveUser(User user) {
		// TODO Auto-generated method stub
		// 从父类中得到SqlSession
		SqlSession openSession = this.getSqlSession();
		// UserMapper.xml 中的命名空间名 + 唯一id
		String arg0 = "com.mybatis.domain.UserMapper.insertUser";
		// 在delete里默认设置为手工提交
		int id = openSession.insert(arg0, user);
		// 事物提交
		openSession.commit();
		System.out.println("插入" + id + "行, id:" + user.getId());
		return user.getId();
	}

	@Override
	public User findUserById(int id) {
		// TODO Auto-generated method stub
		// 从父类中得到SqlSession
		SqlSession openSession = this.getSqlSession();
		String arg0 = "com.mybatis.domain.UserMapper.selectUserById";
		User user1 = openSession.selectOne(arg0, id);
		// 在spring下。没有必要再关闭。会由spring进行关闭操作
		//openSession.close();
		return user1;
	}
}
