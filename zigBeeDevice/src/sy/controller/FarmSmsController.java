package sy.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import sy.model.DevicewarnhistoryHouseidYear;
import sy.model.FarmUser;
import sy.service.DevicewarnhistoryHouseidYearServiceI;
import sy.service.FarmSmsServiceI;
import sy.util.Constants;
import sy.util.Httpproxy;
import sy.util.PropertiesUtil;

@Controller
@RequestMapping({"/farmSmsController"})
public class FarmSmsController
{
	private static final Logger logger = Logger.getLogger(FarmSmsController.class);
	private FarmSmsServiceI farmSmsService;
	private DevicewarnhistoryHouseidYearServiceI devicewarnhistoryHouseidYearService;

	public FarmSmsServiceI getFarmSmsService()
	{
		return this.farmSmsService;
	}
	@Autowired
	public void setFarmSmsService(FarmSmsServiceI farmSmsService) {
		this.farmSmsService = farmSmsService;
	}
	
	public DevicewarnhistoryHouseidYearServiceI getDevicewarnhistoryHouseidYearService() {
		return devicewarnhistoryHouseidYearService;
	}
	@Autowired
	public void setDevicewarnhistoryHouseidYearService(
			DevicewarnhistoryHouseidYearServiceI devicewarnhistoryHouseidYearService) {
		this.devicewarnhistoryHouseidYearService = devicewarnhistoryHouseidYearService;
	}

	@RequestMapping({"/addPhone"})
	public void addPhone(String json, HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=utf-8");
		try {
			FarmUser farmuser = (FarmUser)JSON.parseObject(json, FarmUser.class);
			String username = farmuser.getUsername();
			String phone = farmuser.getPhone();
			if (StringUtils.isBlank(username)) {
				String resultJson = "{\"result\":0}";
				String callback = request.getParameter("callback");
				out.println(callback + "({\"request_id\": 1234, \"response_params\":" + resultJson + "})");
				return;
			}
			if ((StringUtils.isBlank(phone)) || (phone.length() != 11)) {
				String resultJson = "{\"result\":0}";
				String callback = request.getParameter("callback");
				out.println(callback + "({\"request_id\": 1234, \"response_params\":" + resultJson + "})");
				return;
			}
			int status = this.farmSmsService.addPhone(username, phone);
			if (status == -1) {
				String resultJson1 = "{\"result\":-1}";
				String callback = request.getParameter("callback");
				out.println(callback + "({\"request_id\": 1234, \"response_params\":" + resultJson1 + "})");
			} else {
				String resultJson1 = "{\"result\":1}";
				String callback = request.getParameter("callback");
				out.println(callback + "({\"request_id\": 1234, \"response_params\":" + resultJson1 + "})");
			}
		} catch (Exception e) {
			logger.info("addPhone", e);
			String resultJson1 = "{\"result\":0}";
			String callback = request.getParameter("callback");
			out.println(callback + "({\"request_id\": 1234, \"response_params\":" + resultJson1 + "})");
		}
	}

	@RequestMapping({"/deletePhone"})
	public void deletePhone(String json, HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		PrintWriter out = response.getWriter();
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=utf-8");
		try {
			FarmUser farmuser = (FarmUser)JSON.parseObject(json, FarmUser.class);
			String username = farmuser.getUsername();
			String phone = farmuser.getPhone();
			if (StringUtils.isBlank(username)) {
				String resultJson = "{\"result\":0}";
				String callback = request.getParameter("callback");
				out.println(callback + "({\"request_id\": 1234, \"response_params\":" + resultJson + "})");
				return;
			}
			if ((StringUtils.isBlank(phone)) || (phone.length() != 11)) {
				String resultJson = "{\"result\":0}";
				String callback = request.getParameter("callback");
				out.println(callback + "({\"request_id\": 1234, \"response_params\":" + resultJson + "})");
				return;
			}
			int status = this.farmSmsService.deletePhone(username, phone);
			if (status == -1) {
				String resultJson1 = "{\"result\":-1}";
				String callback = request.getParameter("callback");
				out.println(callback + "({\"request_id\": 1234, \"response_params\":" + resultJson1 + "})");
			} else {
				String resultJson1 = "{\"result\":1}";
				String callback = request.getParameter("callback");
				out.println(callback + "({\"request_id\": 1234, \"response_params\":" + resultJson1 + "})");
			}
		}
		catch (Exception e) {
			logger.info("deletePhone", e);
			String resultJson1 = "{\"result\":0}";
			String callback = request.getParameter("callback");
			out.println(callback + "({\"request_id\": 1234, \"response_params\":" + resultJson1 + "})");
		}
	}

	@RequestMapping({"/setsmsFlag"})
	public void selectFlag(String json, HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		PrintWriter out = response.getWriter();
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=utf-8");
		try {
			FarmUser farmuser = (FarmUser)JSON.parseObject(json, FarmUser.class);
			String username = farmuser.getUsername();
			String phone = farmuser.getPhone();
			String flag = farmuser.getSmsflag();
			if (StringUtils.isBlank(username)) {
				String resultJson = "{\"result\":0}";
				String callback = request.getParameter("callback");
				out.println(callback + "({\"request_id\": 1234, \"response_params\":" + resultJson + "})");
				return;
			}
			if (StringUtils.isBlank(phone)) {
				String resultJson = "{\"result\":0}";
				String callback = request.getParameter("callback");
				out.println(callback + "({\"request_id\": 1234, \"response_params\":" + resultJson + "})");
				return;
			}
			int status = this.farmSmsService.selectFlag(username, phone, flag);
			if (status == -1) {
				String resultJson1 = "{\"result\":-1}";
				String callback = request.getParameter("callback");
				out.println(callback + "({\"request_id\": 1234, \"response_params\":" + resultJson1 + "})");
			} else {
				String resultJson1 = "{\"result\":1}";
				String callback = request.getParameter("callback");
				out.println(callback + "({\"request_id\": 1234, \"response_params\":" + resultJson1 + "})");
			}
		} catch (Exception e) {
			logger.info("setsmsFlag", e);
			String resultJson1 = "{\"result\":0}";
			String callback = request.getParameter("callback");
			out.println(callback + "({\"request_id\": 1234, \"response_params\":" + resultJson1 + "})");
		}
	}

	@RequestMapping({"/getSmsList"})
	public void getSmsList(String json, HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=utf-8");
		try {
			Map map = (Map)JSON.parseObject(json, Map.class);
			String username = (String)map.get("username");
			if (StringUtils.isBlank(username)) {
				String resultJson = "{\"result\":0}";
				String callback = request.getParameter("callback");
				out.println(callback + "({\"request_id\": 1234, \"response_params\":" + resultJson + "})");
				return;
			}
			List list = this.farmSmsService.getSmsList(username, map);
			String sflag = ((Map)list.get(0)).get("smsflag") == null ? null : ((Map)list.get(0)).get("smsflag").toString();
			String sphone = ((Map)list.get(0)).get("phone") == null ? null : ((Map)list.get(0)).get("phone").toString();
			if (StringUtils.isBlank(sflag)) {
				((Map)list.get(0)).put("smsflag", "0,0,0,0");
			}

			if (StringUtils.isBlank(sphone)) {
				((Map)list.get(0)).put("phone", "");
			}
			String resultJson = JSON.toJSONStringWithDateFormat(list, "yyyy-MM-dd HH:mm:ss", new SerializerFeature[0]);
			String callback = request.getParameter("callback");
			out.println(callback + "({\"request_id\": 1234, \"response_params\":" + "{\"result\":1" + "," + "\"getList\": " + resultJson + "}})");
		} catch (Exception e) {
			logger.info("getSmsList", e);
			String resultJson1 = "{\"result\":0}";
			String callback = request.getParameter("callback");
			out.println(callback + "({\"request_id\": 1234, \"response_params\":" + resultJson1 + "})");
		}
	}

	/**
	 * IES发送短信接口
	 * @param json
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping({"/sendFlagSMS"})
	public void sendFlagSMS(String json, HttpServletRequest request, HttpServletResponse response) throws IOException{
		logger.info("进入到IES短信推送接口");
		PrintWriter out = response.getWriter();
		String sign = request.getParameter("sign");
		String prefixKey = PropertiesUtil.getProperty("prefixKey");
		String pKey = prefixKey + new SimpleDateFormat("yyyyMMdd").format(new Date());
		String decodeUri = Httpproxy.urlCodec16(sign, pKey);
		String reqUri = request.getRequestURI();
		String callback = request.getParameter("callback");
		String resultJson = "{\"result\":1}";
		if (!decodeUri.equals(reqUri)) {
			resultJson = "{\"result\":-1}";
			out.println(callback + "({\"request_id\": 1234, \"response_params\":" + resultJson + "})");
			return;
		}
		logger.info("sendFlagSMS解密成功-------------");
		try {
			/*获取请求客户端的真实ip地址*/
			String ipAddress = request.getHeader("x-forwarded-for");
			if (ipAddress == null) {
				ipAddress = request.getHeader("X_FORWARDED_FOR");
				if (ipAddress == null) {
					ipAddress = request.getHeader("X-Forward-For");
					if (ipAddress == null) {
						ipAddress = request.getRemoteAddr();
					}
				}
			}
			/*根据客户端真实ip地址判断使用何种方式进行短信推送1：第三方平台推送  2：3G/4G Dongle推送*/
			Map rMap = this.devicewarnhistoryHouseidYearService.getSMSType(ipAddress);
			int smstype = 1;
			if (rMap != null) {
				smstype = ((Byte)rMap.get("smstype")).byteValue();
				ipAddress = (String)rMap.get("smsip");
			}
			String cgiUrl = "";
			//用户名
			String username = PropertiesUtil.getProperty("dongle.username");
			//密码
			String pwd = PropertiesUtil.getProperty("dongle.password");

			List<DevicewarnhistoryHouseidYear> warnList = JSON.parseArray(json, DevicewarnhistoryHouseidYear.class);
			if(warnList != null && !warnList.isEmpty())
				logger.info("短信消息发送开始-------------");
			StringBuilder sql = null;
			for(DevicewarnhistoryHouseidYear devicewarnhistoryHouseidYear : warnList) {
				Byte smsType = devicewarnhistoryHouseidYearService.getShcSmsType(devicewarnhistoryHouseidYear.getHouseIeee());
				if(smsType == 1){
					continue;
				}
				
				sql = new StringBuilder("insert into messagehistory(messageid,sendtime,type,phonenumber,content,state,remark,houseIeee,emailContent) VALUES");

				int wMode = Integer.parseInt(devicewarnhistoryHouseidYear.getWMode());

				StringBuilder msg = null;
				Date currentTime = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String sendtime = sdf.format(currentTime);
				//获取IES账号相关信息
				List list = this.farmSmsService.getsendPhone(devicewarnhistoryHouseidYear.getHouseIeee());
				if(list==null||list.isEmpty()){
					logger.info("no phone to send sms");
					break;
				}
				if (wMode == 1 || wMode == 2 || wMode == 3) {
					//火警、匪警与紧急告警
					try {
						for (int i = 0; i < list.size(); i++) {
							//获取推送目标手机号
							String phoneNum = ((Map)list.get(i)).get("phone") == null ? null : ((Map)list.get(i)).get("phone").toString();
							//是否开启短信推送功能
							String sflag = ((Map)list.get(i)).get("smsflag") == null ? null : ((Map)list.get(i)).get("smsflag").toString();
							//未设置手机号或没有开启推送功能，则不进行推送
							if (StringUtils.isBlank(phoneNum)||StringUtils.isBlank(sflag)) {
								logger.info("no phone to push");
								continue;
							}
							String[] s = new String[4];
							s = sflag.split(",");
							if ((s[0].equals("1")) && (s[1].equals("1"))) {
								String deviceName123 = devicewarnhistoryHouseidYear.getDeviceName();
								String roomName123 = devicewarnhistoryHouseidYear.getRoomName();
								String ep123 = devicewarnhistoryHouseidYear.getZoneEp();
								SimpleDateFormat warnTimeFmt = new SimpleDateFormat("M月d日H时m分");
								String tailNumber = devicewarnhistoryHouseidYear.getHouseIeee().substring(11);
								String warnTime = warnTimeFmt.format(devicewarnhistoryHouseidYear.getWarndatetime());
								String warnCtn = /*wMode == 5 ? "设备发生故障" : */wMode == 3 ? "触发紧急报警" : wMode == 2 ? "触发火警警报" : /*wMode == 1 ?*/ "有人入侵了" /*: "设备出现低压"*/;
								msg = new StringBuilder("您家IEEE尾号为：");
								msg.append(tailNumber).append("-").append(ep123).append("的").append(deviceName123).append("于").append(warnTime).append(":").append(warnCtn);
								String msgStr = msg.toString();
								String warnsend = "";
								if(smstype==2){
									//Dongle推送
									String param = "user_name=" + username + "&message=" + msgStr + "&phoneNumber=" + phoneNum + "&callback=1234";
									String encryStr = param.replaceAll("&", "");
									String context = Httpproxy.urlEncrypt(URLEncoder.encode(encryStr, "UTF-8"), Httpproxy.getKey(pwd));
									param = param + "&encodemethod=AES&sign=" + context;

									warnsend = SendSMSDongle.getDongle().sendSMS(msgStr, URLEncoder.encode(param, "UTF-8"), ipAddress);
								}else{
									//第三方平台推送
									warnsend = SendSMS.getSingleSendSMS().sendSMS(phoneNum, msgStr);
								}
								sql.append("(").append(devicewarnhistoryHouseidYear.getMessage_id()).append(",'");
								sql.append(sendtime).append("','");
								sql.append("0").append("','");
								sql.append(phoneNum).append("','");
								sql.append(msg).append("','");
								if (warnsend.equals(msgStr))
									sql.append("1").append("','");
								else {
									sql.append("0").append("','");
								}
								sql.append("预警短信").append("','");
								sql.append(devicewarnhistoryHouseidYear.getHouseIeee()).append("','");
								sql.append(msg).append("'),");
							}
						}
					}
					catch (Exception e) {
						logger.info("send SMS fail", e);
					}
					logger.info("IES短信发送结束----------");

				}else if (wMode == Constants.WMODE_CUSTOM.intValue()) {
					//自定义消息
					try {
						for (int i = 0; i < list.size(); i++) {
							String phoneNum = ((Map)list.get(i)).get("phone") == null ? null : ((Map)list.get(i)).get("phone").toString();

							String sflag = ((Map)list.get(i)).get("smsflag") == null ? null : ((Map)list.get(i)).get("smsflag").toString();
							if (StringUtils.isBlank(phoneNum)||StringUtils.isBlank(sflag)) {
								logger.info("no target phone to push");
								continue;
							}
							String[] s = new String[4];
							s = sflag.split(",");
							if (("1".equals(s[0])) && ("1".equals(s[2]))) {
								String deviceName123 = devicewarnhistoryHouseidYear.getDeviceName();
								String roomName123 = devicewarnhistoryHouseidYear.getRoomName();
								String ep123 = devicewarnhistoryHouseidYear.getZoneEp();
								SimpleDateFormat warnTimeFmt = new SimpleDateFormat("M月d日H时m分");
								String tailNumber = devicewarnhistoryHouseidYear.getHouseIeee().substring(11);
								String warnTime = warnTimeFmt.format(devicewarnhistoryHouseidYear.getWarndatetime());

								msg = new StringBuilder("您家IEEE尾号为：");

								msg.append(tailNumber).append("-").append(ep123).append("的").append(deviceName123).append("于").append(warnTime).append("触发条件设置：").append(devicewarnhistoryHouseidYear.getWDescription());
								logger.info("得到的信息是：" + msg);
									String msgStr = msg.toString();
									String warnsend = "";
									if(smstype==2){
										//Dongle推送
										String param = "user_name=" + username + "&message=" + msgStr + "&phoneNumber=" + phoneNum + "&callback=1234";
										String encryStr = param.replaceAll("&", "");
										String context = Httpproxy.urlEncrypt(URLEncoder.encode(encryStr, "UTF-8"), Httpproxy.getKey(pwd));
										param = param + "&encodemethod=AES&sign=" + context;

										warnsend = SendSMSDongle.getDongle().sendSMS(msgStr, URLEncoder.encode(param, "UTF-8"), ipAddress);
									}else{
										//第三方平台推送
										warnsend = SendSMS.getSingleSendSMS().sendSMS(phoneNum, msgStr);
									}
									sql.append("(").append(devicewarnhistoryHouseidYear.getMessage_id()).append(",'");
									sql.append(sendtime).append("','");
									sql.append("0").append("','");
									sql.append(phoneNum).append("','");
									sql.append(msg).append("','");
									if (warnsend.equals(msgStr))
										sql.append("1").append("','");
									else {
										sql.append("0").append("','");
									}
									sql.append("条件触发通知短信").append("','");
									sql.append(devicewarnhistoryHouseidYear.getHouseIeee()).append("','");
									sql.append(msg).append("'),");
								
							}
						}
						
					} catch (Exception e) {
						logger.info("send SMS fail", e);
					}
					logger.info("IES短信发送结束----------");
				}else if (wMode == 5) {
					//自定义消息
					try {
						for (int i = 0; i < list.size(); i++) {
							String phoneNum = ((Map)list.get(i)).get("phone") == null ? null : ((Map)list.get(i)).get("phone").toString();

							String sflag = ((Map)list.get(i)).get("smsflag") == null ? null : ((Map)list.get(i)).get("smsflag").toString();
							if (StringUtils.isBlank(phoneNum)||StringUtils.isBlank(sflag)) {
								logger.info("no target phone to push");
								continue;
							}
							String[] s = new String[4];
							s = sflag.split(",");
							if (("1".equals(s[0])) && ("1".equals(s[2]))) {
								String deviceName123 = devicewarnhistoryHouseidYear.getDeviceName();
								String roomName123 = devicewarnhistoryHouseidYear.getRoomName();
								String ep123 = devicewarnhistoryHouseidYear.getZoneEp();
								SimpleDateFormat warnTimeFmt = new SimpleDateFormat("M月d日H时m分");
								String tailNumber = devicewarnhistoryHouseidYear.getHouseIeee().substring(11);
								String warnTime = warnTimeFmt.format(devicewarnhistoryHouseidYear.getWarndatetime());

								msg = new StringBuilder("您家IEEE尾号为：");

								msg.append(tailNumber).append("-").append(ep123).append("的").append(deviceName123).append("于").append(warnTime).append("发生故障，请及时处理");
								logger.info("得到的信息是：" + msg);
									String msgStr = msg.toString();
									String warnsend = "";
									if(smstype==2){
										//Dongle推送
										String param = "user_name=" + username + "&message=" + msgStr + "&phoneNumber=" + phoneNum + "&callback=1234";
										String encryStr = param.replaceAll("&", "");
										String context = Httpproxy.urlEncrypt(URLEncoder.encode(encryStr, "UTF-8"), Httpproxy.getKey(pwd));
										param = param + "&encodemethod=AES&sign=" + context;

										warnsend = SendSMSDongle.getDongle().sendSMS(msgStr, URLEncoder.encode(param, "UTF-8"), ipAddress);
									}else{
										//第三方平台推送
										warnsend = SendSMS.getSingleSendSMS().sendSMS(phoneNum, msgStr);
									}
									sql.append("(").append(devicewarnhistoryHouseidYear.getMessage_id()).append(",'");
									sql.append(sendtime).append("','");
									sql.append("0").append("','");
									sql.append(phoneNum).append("','");
									sql.append(msg).append("','");
									if (warnsend.equals(msgStr))
										sql.append("1").append("','");
									else {
										sql.append("0").append("','");
									}
									sql.append("预警短信").append("','");
									sql.append(devicewarnhistoryHouseidYear.getHouseIeee()).append("','");
									sql.append(msg).append("'),");
								
							}
						}
						
					} catch (Exception e) {
						logger.info("send SMS fail", e);
					}
					logger.info("IES短信发送结束----------");
				}/*else{
					try {
						for (int i = 0; i < list.size(); i++) {
							String phoneNum = ((Map)list.get(i)).get("phone") == null ? null : ((Map)list.get(i)).get("phone").toString();

							String sflag = ((Map)list.get(i)).get("smsflag") == null ? null : ((Map)list.get(i)).get("smsflag").toString();
							if ((sflag == null) || (sflag == "")) {
								sflag = "0,0,0,0";
							}
							String[] s = new String[4];
							s = sflag.split(",");
							if ((s[0].equals("1")) && (s[3].equals("1"))) {
								String deviceName123 = devicewarnhistoryHouseidYear.getDeviceName();
								String roomName123 = devicewarnhistoryHouseidYear.getRoomName();
								String ep123 = devicewarnhistoryHouseidYear.getZoneEp();
								SimpleDateFormat warnTimeFmt = new SimpleDateFormat("M月d日H时m分");
								String tailNumber = devicewarnhistoryHouseidYear.getHouseIeee().substring(11);
								String warnTime = warnTimeFmt.format(devicewarnhistoryHouseidYear.getWarndatetime());

								msg = new StringBuilder("您家IEEE尾号为：");
								msg.append(tailNumber).append("-").append(ep123).append("的").append(deviceName123).append("于").append(warnTime).append("触发设备告警设置：").append(devicewarnhistoryHouseidYear.getWDescription());

								logger.info("得到的信息是：" + msg);
								if (StringUtils.isNotBlank(phoneNum)) {
									String msgStr = msg.toString();
									String warnsend = SendSMS.getSingleSendSMS().sendSMS(phoneNum, msgStr);
									sql.append("(").append(devicewarnhistoryHouseidYear.getMessage_id()).append(",'");
									sql.append(sendtime).append("','");
									sql.append("0").append("','");
									sql.append(phoneNum).append("','");
									sql.append(msg).append("','");
									if (warnsend.equals(msgStr))
										sql.append("1").append("','");
									else {
										sql.append("0").append("','");
									}
									sql.append("设备告警通知短信").append("','");
									sql.append(devicewarnhistoryHouseidYear.getHouseIeee()).append("','");
									sql.append(msg).append("'),");
								}
							}
						}
					} catch (Exception e) {
						logger.info("send SMS fail", e);
					}
					logger.info("IES短信发送结束----------");
				}*/
			}
			if(sql!=null&&StringUtils.endsWith(sql.toString(), ",")){
        		devicewarnhistoryHouseidYearService.batchSave(sql.deleteCharAt(sql.length() - 1).toString());
			}
			out.println(callback + "({\"request_id\": 1234, \"response_params\":" + resultJson + "})");
		} catch (Exception e) {
			logger.info("sendFlagSMS", e);
			resultJson = "{\"result\":0}";
			out.println(callback + "({\"request_id\": 1234, \"response_params\":" + resultJson + "})");
		}
	}
}