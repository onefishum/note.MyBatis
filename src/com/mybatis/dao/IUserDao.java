package com.mybatis.dao;

import com.mybatis.domain.User;

public interface IUserDao {
	public int saveUser(User user);

	public User findUserById(int id);
}
