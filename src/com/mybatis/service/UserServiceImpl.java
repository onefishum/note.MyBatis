package com.mybatis.service;

import com.mybatis.dao.IUserDao;
import com.mybatis.domain.User;

public class UserServiceImpl implements IUserService {
	private IUserDao userDao;
	
	// 此方法不是必须
	public IUserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public int saveUser(User user) {
		// TODO Auto-generated method stub
		return userDao.saveUser(user);
	}

	@Override
	public User findUserById(int id) {
		// TODO Auto-generated method stub
		return userDao.findUserById(id);
	}

}
