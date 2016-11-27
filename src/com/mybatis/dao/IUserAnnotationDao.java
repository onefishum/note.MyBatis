package com.mybatis.dao;

import org.apache.ibatis.annotations.Select;

import com.mybatis.domain.User;

public interface IUserAnnotationDao {
	@Select(value="select * from l_user where id=#{id}")
	public User findUserByIdA(int id);
}
