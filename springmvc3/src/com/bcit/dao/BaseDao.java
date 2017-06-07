package com.bcit.dao;

import java.util.List;

public interface BaseDao<G> {

	public void add(G g);
	
	public void mod(G g);
	
	public G get(java.io.Serializable sid);
	
	public void del(java.io.Serializable sid);
	
	public List<G> query();
	
	public List<G> query(G g);
	
	public G queryOne(G g);
	
	public List<G> query(G g,Integer start,Integer limit);
	
	public int getTotal(G g);
}
