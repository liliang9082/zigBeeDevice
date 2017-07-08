package sy.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import sy.dao.BaseDaoI;
import sy.model.FarmUser;
import sy.model.FarmWarnHandle;
import sy.service.FarmAreaI;
import sy.util.ChartAttribute;
import sy.util.DeviceAttrUtils;
import sy.util.HouseieeeListener;
import sy.util.PropertiesUtil;
import sy.util.TestHttpclient;

@Service("farmAreaService")
public class FarmAreaImpl implements FarmAreaI {
	private static Logger logger = Logger.getLogger(FarmAreaImpl.class);
    
	@Autowired
	private BaseDaoI<FarmUser> mapDao2;
	@Autowired
	private BaseDaoI<FarmWarnHandle> mapDao1;

	private BaseDaoI<Map> mapDao;

	public BaseDaoI<Map> getMapDao() {
		return mapDao;
	}
	@Autowired
	public void setMapDao(BaseDaoI<Map> mapDao) {
		this.mapDao = mapDao;
	}

	@Override
	public List<Map> getAreaList(String username) throws Exception {
		//获取该用户下的所有区域IEEE
		String sql = "select h.houseName as name,h.houseIEEE as house_ieee from farmuserarea a left join farmuser u on a.user_id=u.id left join modefarmhouse h on a.house_ieee = h.houseIEEE and (u.id=h.userId or u.parent_id=h.userId) where u.user_name=:username";
		Map<String,Object> params = new HashMap<>();
		params.put("username", username);
		List<Map> IEEEList = mapDao.executeSql(sql, params);
		Map<String, List<String>> cloudIpCache = new HashMap<String, List<String>>();
		for(Map hIeeeMap : IEEEList) {
			String hIeee = (String) hIeeeMap.get("house_ieee");
			String cloudIp = (String) HouseieeeListener.houseieeeProxyserverMap.get(hIeee);
			if(cloudIp != null) {
				List<String> hIeeeList = cloudIpCache.get(cloudIp);
				if(hIeeeList == null || hIeeeList.isEmpty()) {
					hIeeeList = new ArrayList<String>();
					hIeeeList.add(hIeee);
					cloudIpCache.put(cloudIp, hIeeeList);
				}
				else {
					hIeeeList.add(hIeee);
				}
			}
		}
		String cloudPort = PropertiesUtil.getProperty("cloud.port");
		Map<String, Object> areaMap = new HashMap<String, Object>();
		//获取属性
		Iterator<String> itor = cloudIpCache.keySet().iterator();
		while(itor.hasNext()) {
			String cloudIp = itor.next();
			String callUrl = "http://" + cloudIp + ":" + cloudPort + "/zigBeeDevice/farmArea/getAreaAttrs.do";
			Map<String, Object> jMap = new HashMap<String, Object>();
			List<String> hIeeeList = cloudIpCache.get(cloudIp);
			jMap.put("json", JSON.toJSONStringWithDateFormat(hIeeeList, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteMapNullValue));
			String resultStr = TestHttpclient.postUrlWithParams(callUrl, jMap, "utf-8");
			resultStr = resultStr.trim();
			Map<String, Object> tMap = JSON.parseObject(resultStr, Map.class);
			Integer result = (Integer) tMap.get("result");
			if(result == null || result.intValue() == 0) {
				continue;
			}
			Map<String, Object> aMap = (Map<String, Object>) tMap.get("areas");
			areaMap.putAll(aMap);
		}
		//组装温室度属性
		Iterator<Map> ieeeItor = IEEEList.iterator();
		while(ieeeItor.hasNext()) {
			Map<String, Object> ieeeMap = ieeeItor.next();
			String hIeee = (String) ieeeMap.get("house_ieee");
			Map<String, Object> attrMap = (Map<String, Object>) areaMap.get(hIeee);
			if(attrMap == null || attrMap.isEmpty()) {
				continue;
			}
			ieeeMap.putAll(attrMap);
		}
		return IEEEList;
	}
	
	@Override
	public List<Map> getAreas(String userName, int pageIndex, int pageSize) {
		int startRow = (pageIndex - 1) * pageSize;
		//获取区域
		StringBuilder sql = new StringBuilder("select h.houseName,h.houseIEEE from farmuserarea a ");
		sql.append("inner join farmuser u on a.user_id=u.id ") 
		.append("inner join modefarmhouse h on a.house_ieee = h.houseIEEE AND (u.id=h.userId OR u.parent_id=h.userId) ")
		.append("where u.user_name=:userName order by a.id asc limit :startRow, :pageSize");
		Map<String,Object> params = new HashMap<>();
		params.put("userName", userName);
		params.put("startRow", startRow);
		params.put("pageSize", pageSize);
		List<Map> IEEEList = mapDao.executeSql(sql.toString(), params);
		
		return IEEEList;
	}
	
	@Override
	public Map<String, Map> getAreaAttrs(List<String> hIeees) {
		if(hIeees == null || hIeees.isEmpty()) {
			return Collections.emptyMap();
		}
		Map<String, Map> areaMap = new HashMap<String, Map>();
		for(String hIeee : hIeees) {
			Map<String,Object> rMap = new HashMap<>();
			String tableName = "devicenewestattribute_" + hIeee;
			try{
				StringBuilder strAllSql = new StringBuilder();
//				StringBuilder strSql = new StringBuilder("select a.attributeName,FORMAT(SUM(a.value)/COUNT(*),2)+0 value,a.opdatetime,b.solid_model_id,b.ep from ");
//				strSql.append(tableName).append(" d1 left join farmdevice f1 on d1.device_ieee=f1.ieee and d1.houseIEEE=f1.house_ieee and d1.device_ep=f1.ep  where opdatetime in (")
//					.append("select max(opdatetime) from ").append(tableName).append(" d2 left join farmdevice f2 on d2.device_ieee=f2.ieee and d2.houseIEEE=f2.house_ieee and d2.device_ep=f2.ep")
//					.append(" where d2.attribute_id='0000' and (d2.cluster_id='0400'or d2.cluster_id='0402' or d2.cluster_id='0405') group by f2.ep,d2.attributeName)")
//					.append(" and d1.attribute_id='0000' and (d1.cluster_id='0400'or d1.cluster_id='0402' or d1.cluster_id='0405')")
//					.append(" group by  f1.ep,d1.attributeName");
				//判断属性是否为空
				int[] charTypeArr = {0, 1, 5, 2};
				int charTypeArrLength = charTypeArr.length;
				for(int i = 0; i <  charTypeArrLength; i++) {
					StringBuilder strSql = new StringBuilder("select a.attributeName,FORMAT(SUM(a.value)/COUNT(*),2)+0 value,a.opdatetime,b.solid_model_id,b.ep from ");
					Map<String, Object> attrMap = ChartAttribute.getValue(charTypeArr[i]);
					if(attrMap == null || attrMap.get("clusterId") == null || attrMap.get("attributeId") == null) {
						continue;
					}
					strSql.append(tableName).append(" a INNER JOIN farmdevice b ON a.houseIEEE=b.house_ieee AND a.device_ieee=b.ieee AND a.device_ep=b.ep ")
					.append("WHERE a.cluster_id='").append(attrMap.get("clusterId")).append("' AND a.attribute_id='").append(attrMap.get("attributeId")).append("' ");
					String[][] solidModelIdArr = (String[][]) attrMap.get("solidModelIdArr");
					if(solidModelIdArr != null && solidModelIdArr.length != 0) {
						strSql.append("and (");
						Iterator<String[]> itor = Arrays.asList(solidModelIdArr).iterator();
						while(itor.hasNext()) {
							String[] dSmia = itor.next();
							String solidModelId = dSmia[0];
							String ep = dSmia[1];
							if(solidModelId == null) {
								strSql.append("(b.solid_model_id is null OR b.solid_model_id='') OR ");
							}
							else {
								strSql.append("(b.solid_model_id='").append(solidModelId).append("' AND b.ep='").append(ep).append("') OR ");
							}
						}
						String tempSqlStr = strSql.substring(0, strSql.length() - 4) + ") ";
						strAllSql.append(tempSqlStr).append("GROUP BY a.houseIeee UNION ALL ");
					}
					else {
						strAllSql.append(strSql).append("GROUP BY a.houseIeee UNION ALL ");
					}
				}
				String qAttrSqlStr = strAllSql.substring(0, strAllSql.length() - 11);
				List<Map> attrList = mapDao.executeSql(qAttrSqlStr);
				if(attrList!=null&&!attrList.isEmpty()) {
					for(Map m:attrList) {
						String attrbuteName = (String)m.get("attributeName");
						String solidModelId = (String)m.get("solid_model_id");
						String ep = (String)m.get("ep");
						//温度
						if(rMap.get("tem")==null)
							if("temp".equalsIgnoreCase(attrbuteName)){
								rMap.put("tem", m.get("value"));
								continue;
							}
						//空气湿度
						if(rMap.get("hum")==null){
							if("hum".equalsIgnoreCase(attrbuteName)) {
								if(!DeviceAttrUtils.isSoilHum(solidModelId, true)) {
									rMap.put("hum", m.get("value"));
									continue;
								}
								else if(DeviceAttrUtils.isSoilHum(solidModelId, false)) {
									if("01".equals(ep)){
										rMap.put("hum", m.get("value"));
										continue;
									}
								}
							}
						}
						//土壤湿度
						if(rMap.get("soil_hum")==null){
							if("hum".equalsIgnoreCase(attrbuteName)) {
								if(DeviceAttrUtils.isSoilHum(solidModelId, true)) {
									rMap.put("soil_hum", m.get("value"));
									continue;
								}else if(DeviceAttrUtils.isSoilHum(solidModelId, false)) {
									if("02".equals(ep)) {
										rMap.put("soil_hum", m.get("value"));
										continue;
									}
								}
							}
						}
						//紫外线
						if(rMap.get("lux")==null) {
							if("brightness".equalsIgnoreCase(attrbuteName)){
								rMap.put("lux", m.get("value"));
								continue;
							}
						}
					}
					//检查属性为空时，设置返回值为0
					if(rMap.get("tem")==null) {
						rMap.put("tem", 0);
					}
					//湿度
					if(rMap.get("hum")==null) {
						rMap.put("hum", 0);
					}
					//湿度
					if(rMap.get("soil_hum")==null) {
						rMap.put("soil_hum", 0);
					}
					//紫外线
					if(rMap.get("lux")==null) {
						rMap.put("lux", 0);
					}
				}
				else {
					rMap.put("tem", 0);
					rMap.put("hum", 0);
					rMap.put("soil_hum", 0);
					rMap.put("lux", 0);
				}
			}catch(Exception e){
				logger.error(e.getMessage());
				if(StringUtils.containsIgnoreCase(e.getMessage(), tableName)){
					rMap.put("tem", 0);
					rMap.put("hum", 0);
					rMap.put("soil_hum", 0);
					rMap.put("lux", 0);
				}
			}
			areaMap.put(hIeee, rMap);
		}
		return areaMap;
	}
	
	public int add(Map<String,Object> map){
		FarmWarnHandle farmWarnHandle = new FarmWarnHandle();
		mapDao1.save(farmWarnHandle);
		String userName= "ret";
		String houseIeee = "ee3";
		String sql = "select from farmuser where userName = '"+userName+"'";
	    FarmUser  farmUser =  (FarmUser) mapDao2.executeSql(sql);
	    Long id = farmUser.getId();
	    if(id>0){
	    	FarmUser farmUser2 = new FarmUser();
	    	mapDao2.save(farmUser2);
	    }
		return 0;
		
	}
}
