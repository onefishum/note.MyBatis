package com.mybatis.service;

import com.mybatis.domain.User;

public interface IUserService {
	public int saveUser(User user);
	public User findUserById(int id);
}
