package sy.service;

import java.util.List;
import java.util.Map;
import sy.model.FarmUser;

public abstract interface FarmSmsServiceI
{
  public abstract int addPhone(String paramString1, String paramString2);

  public abstract int deletePhone(String paramString1, String paramString2);

  public abstract List<FarmUser> getPhoneList(String paramString);

  public abstract List<Map> getSmsList(String paramString, Map paramMap) throws Exception;

  public abstract int selectFlag(String paramString1, String paramString2, String paramString3) throws Exception;

  public abstract List<Map> getsendPhone(String paramString);

  public abstract List<Map> getsmsflag(String paramString) throws Exception;
  /**
   * 根据服务器真实ip获取其短信推送方式
   * @param ipAddress 服务器ip
   * @return
   */
  public Map getSMSType(String ipAddress);
}