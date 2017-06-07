package com.bcit.dao.impl;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bcit.dao.UserDao;
import com.bcit.entity.Users;

@Repository("userDao")
public class UserDaoImpl extends BaseDaoImpl<Users> implements UserDao {
	
	@Override
	public void doQueryCondition(DetachedCriteria dcri, Users g) {
		if(g != null){
			if(g.getUname() != null && !"".equals(g.getUname())){
				dcri.add(Restrictions.eq("uname", g.getUname()));
			}
			if(g.getPassword() != null && !"".equals(g.getPassword())){
				dcri.add(Restrictions.eq("password", g.getPassword()));
			}
		}
	}
}
