package sy.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import sy.model.FarmWarnHandle;
import sy.model.Houseieee;
import sy.service.AdvertisementServiceI;
import sy.service.FarmWarnHandleServiceI;

@Controller()
@RequestMapping("/FarmWarnHandleController")
public class FarmWarnHandleController {
	private static final Logger LOGGER = Logger.getLogger(FarmWarnHandleController.class);
	private FarmWarnHandleServiceI FarmWarnHandleService;
	
	public FarmWarnHandleServiceI getFarmWarnHandleService() {
		return FarmWarnHandleService;
	}
	@Autowired
	public void setFarmWarnHandleService(FarmWarnHandleServiceI farmWarnHandleService) {
		FarmWarnHandleService = farmWarnHandleService;
	}

	public static Logger getLogger() {
		return LOGGER;
	}
	
	@RequestMapping("/find")
	public void find(String json,HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
		
		PrintWriter out=response.getWriter(); 
		response.setContentType("text/html;charset=utf-8");  
		
        try {
        	FarmWarnHandle farmWarnHandle = JSON.parseObject(json,FarmWarnHandle.class); 
            List<FarmWarnHandle> t = FarmWarnHandleService.find(farmWarnHandle);
        	String resultJson= JSON.toJSONStringWithDateFormat(t, "yyyy-MM-dd HH:mm:ss");
			String callback = request.getParameter("callback");
			out.println(callback + "({\"request_id\": 1234, \"response_params\":" + resultJson + "})"); 
		} catch (Exception e) {
			LOGGER.info("find",e);
			String resultJson = "{\"result\":0}";//成功1 失败0
			String callback = request.getParameter("callback");out.println(callback + "({\"request_id\": 1234, \"response_params\":" + resultJson + "})");
		}   
        

}
