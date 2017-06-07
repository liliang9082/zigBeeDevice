package com.bcit.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bcit.dao.UserDao;
import com.bcit.entity.Users;
import com.bcit.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Resource
	private UserDao userDao;
	
	@Override
	public Users queryOne(Users g) {
		return userDao.queryOne(g);
	}

}
