package com.bcit.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import com.bcit.dao.BaseDao;

@SuppressWarnings("unchecked")
public abstract class BaseDaoImpl<G> extends HibernateDaoSupport implements BaseDao<G> {

	@Override
	public void add(G g) {
		this.getHibernateTemplate().save(g);
	}

	@Override
	public void mod(G g) {
		this.getHibernateTemplate().update(g);
	}

	@Override
	public G get(Serializable sid) {
		Class<G> entityClass = (Class<G>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		return this.getHibernateTemplate().get(entityClass, sid);
	}

	@Override
	public void del(Serializable sid) {
		this.getHibernateTemplate().delete(this.get(sid));
	}

	@Override
	public List<G> query() {
		return query(null,null,null);
	}

	@Override
	public List<G> query(G g) {
		return query(g,null,null);
	}
	
	@Override
	public G queryOne(G g) {
		List<G> list = query(g,null,null);
		if(list.size() == 1)return list.get(0);
		return null;
	}
	
	public void doQueryCondition(DetachedCriteria dcri,G g){};

	@Override
	public List<G> query(G g, Integer start, Integer limit) {
		Class<G> entityClass = (Class<G>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		DetachedCriteria dcri = DetachedCriteria.forClass(entityClass);
		this.doQueryCondition(dcri, g);
		if(start != null && limit != null){
			return (List<G>)this.getHibernateTemplate().findByCriteria(dcri, start, limit);
		}
		return (List<G>)this.getHibernateTemplate().findByCriteria(dcri);
	}

	@Override
	public int getTotal(G g) {
		Class<G> entityClass = (Class<G>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		DetachedCriteria dcri = DetachedCriteria.forClass(entityClass);
		this.doQueryCondition(dcri, g);
		dcri.setProjection(Projections.rowCount());
		List<Long> list = (List<Long>)this.getHibernateTemplate().findByCriteria(dcri);
		return Integer.valueOf(list.get(0).toString());
	}

}
