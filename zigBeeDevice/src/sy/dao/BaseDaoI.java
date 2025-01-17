package sy.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseDaoI<T> {

	public Serializable save(T o);

	public void delete(T o);

	public void update(T o);

	public void saveOrUpdate(T o);

	public T get(Class<T> c, Serializable id);

	public T get(String hql);

	public T get(String hql, Map<String, Object> params);

	public List<T> find(String hql);

	public List<T> find(String hql, Map<String, Object> params);
	
	public List<T> findSql(String sql, Map<String, Object> params,Class<T> c);
	
	public List<Object[]> findSql(String sql, Map<String, Object> params,Class s, Class e);
	
	public List<Object[]> findsep(String sql, Map<String, Object> params,Class s, Class e,Class p);

	public List<T> find(String hql, int page, int rows);

	public List<T> find(String hql, Map<String, Object> params, int page, int rows);

	public Long count(String hql);

	public Long count(String hql, Map<String, Object> params);
	
	public int executeSql2(String sql, Map<String, Object> params);
	
	public List<T> executeSql(String sql);
	
	public int executeSql2(String sql);

	public int executeHql(String hql);
	
	public int executeHql(String hql, Map<String, Object> params);
	
	public List<T> executeSql(String sql, Map<String, Object> params);	

}
