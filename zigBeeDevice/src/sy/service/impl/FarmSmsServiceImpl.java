package sy.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.dao.BaseDaoI;
import sy.model.FarmUser;
import sy.service.FarmSmsServiceI;

@Service("farmSmsService")
public class FarmSmsServiceImpl
implements FarmSmsServiceI
{
	private static final Logger logger = Logger.getLogger(FarmSmsServiceImpl.class);
	private BaseDaoI<FarmUser> farmuserDao;
	private BaseDaoI<Map> mapDao;

	public BaseDaoI<FarmUser> getFarmuserDao()
	{
		return this.farmuserDao;
	}

	@Autowired
	public void setFarmuserDao(BaseDaoI<FarmUser> farmuserDao) {
		this.farmuserDao = farmuserDao;
	}

	public BaseDaoI<Map> getMapDao() {
		return this.mapDao;
	}
	@Autowired
	public void setMapDao(BaseDaoI<Map> mapDao) {
		this.mapDao = mapDao;
	}

	public int addPhone(String username, String phone)
	{
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("select phone,user_name from farmuser where 1=1");
		if (StringUtils.isNotBlank(username)) {
			sql.append(" and user_name =:user_name");
			params.put("user_name", username);
		}
		List list = this.mapDao.executeSql(sql.toString(), params);
		String name = ((Map)list.get(0)).get("user_name") == null ? null : ((Map)list.get(0)).get("user_name").toString();
		String phoneNo = ((Map)list.get(0)).get("phone") == null ? null : ((Map)list.get(0)).get("phone").toString();
		logger.info("用户：" + name + "手机号码为：" + phoneNo);
		if (StringUtils.isBlank(phoneNo)) {
			Map map = new HashMap();
			map.put("user_name", username);
			map.put("phone", phone);
			map.put("smsflag", "0,0,0,0");
			String upSqlStr = "UPDATE farmuser SET phone=:phone,smsflag=:smsflag WHERE user_name=:user_name";
			int status = this.farmuserDao.executeSql2(upSqlStr, map);
			return status;
		}
		return -1;
	}

	public int deletePhone(String username, String phone)
	{
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("select 1 from farmuser where 1=1");
		if (StringUtils.isNotBlank(username)) {
			sql.append(" and user_name =:user_name");
			params.put("user_name", username);
		}
		if (StringUtils.isNotBlank(phone)) {
			sql.append(" and phone =:phone");
			params.put("phone", phone);
		}
		List list = this.farmuserDao.executeSql(sql.toString(), params);
		int length = list.size();
		if (list.size() != 0) {
			Map map = new HashMap();
			map.put("user_name", username);
			map.put("phone", null);
			map.put("smsflag", null);
			String delsql = "UPDATE farmuser SET phone=:phone,smsflag=:smsflag WHERE user_name=:user_name";
			int status = this.farmuserDao.executeSql2(delsql, map);
			return status;
		}
		return -1;
	}

	public List<FarmUser> getPhoneList(String username)
	{
		Map map = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("select t.user_name,t.phone,t.smsflag from farmuser").append(" t where 1=1");
		if (StringUtils.isNotBlank(username)) {
			sql.append(" and t.user_name =:user_name");
			map.put("user_name", username);
		}
		List list = this.farmuserDao.executeSql(sql.toString(), map);
		if (list != null) {
			return list;
		}
		return null;
	}

	public int selectFlag(String username, String phone, String flag) throws Exception
	{
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("select 1 from farmuser where 1=1");
		if (StringUtils.isNotBlank(username)) {
			sql.append(" and user_name =:user_name");
			params.put("user_name", username);
		}
		if (StringUtils.isNotBlank(phone)) {
			sql.append(" and phone =:phone");
			params.put("phone", phone);
		}
		List list = this.farmuserDao.executeSql(sql.toString(), params);
		if (list.size() != 0) {
			Map map = new HashMap();
			map.put("user_name", username);
			map.put("phone", phone);
			map.put("smsflag", flag);
			String delsql = "UPDATE farmuser SET smsflag=:smsflag WHERE user_name=:user_name and phone=:phone";
			int status = this.farmuserDao.executeSql2(delsql, map);
			return status;
		}
		return -1;
	}

	public List<Map> getSmsList(String username, Map para) throws Exception
	{
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("select t.user_name,t.phone,t.smsflag from ").append("farmuser t where 1=1");
		if (StringUtils.isNotBlank(username)) {
			sql.append(" and t.user_name =:user_name ");
			params.put("user_name", username);
		}
		List list = this.mapDao.executeSql(sql.toString(), params);
		if (list != null) {
			return list;
		}
		return null;
	}

	public List<Map> getsendPhone(String houseieee)
	{
		Map map = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("select t.phone,t.smsflag,t.user_name from farmuserarea d inner join farmuser t on t.id = d.user_id").append(" where 1=1");
		if (StringUtils.isNotBlank(houseieee)) {
			sql.append(" and d.house_ieee = :house_ieee");
			map.put("house_ieee", houseieee);
		}
		List list = this.mapDao.executeSql(sql.toString(), map);
		if (list != null) {
			return list;
		}
		return null;
	}

	public List<Map> getsmsflag(String houseieee) throws Exception
	{
		Map map = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("select t.smsflag,t.phone,t.user_name from farmuser t inner join farmuserarea d on t.id = d.user_id").append(" where 1=1");
		if (StringUtils.isNotBlank(houseieee)) {
			sql.append(" and d.house_ieee =:house_ieee");
			map.put("house_ieee", houseieee);
		}
		List list = this.mapDao.executeSql(sql.toString(), map);
		if (list != null) {
			return list;
		}
		return null;
	}

	@Override
	public Map getSMSType(String ipAddress){
		String sql = "SELECT smstype,smsip FROM serverlib WHERE serverip = :serverip";
	    Map<String,Object> params = new HashMap<>();
	    params.put("serverip", ipAddress);

	    List<Map> list = this.mapDao.executeSql(sql, params);
	    if ((list == null) || (list.isEmpty())) {
	      return null;
	    }
	    return (Map)list.get(0);

	}
}