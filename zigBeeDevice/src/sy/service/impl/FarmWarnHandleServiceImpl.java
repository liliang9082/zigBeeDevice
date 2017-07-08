package sy.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sy.dao.BaseDaoI;
import sy.model.Advertisement;
import sy.model.FarmWarnHandle;
import sy.service.FarmWarnHandleServiceI;
@Service("FarmWarnHandleService")
public class FarmWarnHandleServiceImpl implements FarmWarnHandleServiceI {
	private BaseDaoI<FarmWarnHandle> FarmWarnHandleDao;
	private BaseDaoI<Map> mapDao;
	public BaseDaoI<FarmWarnHandle> getFarmWarnHandleDao() {
		return FarmWarnHandleDao;
	}
	@Autowired
	public void setAdvertisementoDao(BaseDaoI<FarmWarnHandle> farmWarnHandleDao) {
		FarmWarnHandleDao = farmWarnHandleDao;
	}
	public BaseDaoI<Map> getMapDao() {
		return mapDao;
	}
	@Autowired
	public void setMapDao(BaseDaoI<Map> mapDao) {
		this.mapDao = mapDao;
	}

	@Override
	public List<FarmWarnHandle> find(FarmWarnHandle farmWarnHandle) {
		StringBuffer hql = new StringBuffer();
		hql.append("from FarmWarnHandle a where a.houseieee = :houseieee and a.handletime = :handletime and a.note = :note");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("houseieee", farmWarnHandle.getHouseieee());
		params.put("handletime", farmWarnHandle.getHandletime());
		params.put("note", farmWarnHandle.getNote());
		List<FarmWarnHandle> t = FarmWarnHandleDao.find(hql.toString(), params);
		return t;
	}

	@Override
	public int update(FarmWarnHandle farmWarnHandle) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(FarmWarnHandle farmWarnHandle) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<FarmWarnHandle> find1(String startRow, String pageSize, String orderBy, FarmWarnHandle farmWarnHandle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FarmWarnHandle add(FarmWarnHandle farmWarnHandle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addbatchapp(List<FarmWarnHandle> list) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public int appupdatereadstate(FarmWarnHandle farmWarnHandle) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCount(Map<String, Object> update) {
		// TODO Auto-generated method stub
		return 0;
	}

}
