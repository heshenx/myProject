package com.shidaiyintong.cn.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shidaiyintong.cn.common.SpringBootApplicationRunner;
import com.shidaiyintong.cn.common.SystemConstants;
import com.shidaiyintong.cn.model.ExcelData;
import com.shidaiyintong.cn.model.ExcelDataObject;
import com.shidaiyintong.cn.utils.ClientUtils;
import com.shidaiyintong.cn.utils.ExcelUtils;
import com.shidaiyintong.cn.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller
@RequestMapping("/xpadgjl")
public class IndexController {
	//初始化一个map对象存放
	private final static Map<String, Object> map = new ConcurrentHashMap<>();
	//    @Autowired
//    private RedisUtils redisUtils;
	@Autowired
	private ClientUtils clientUtils;
	@Autowired
	private ExcelUtils excelUtils;

	//	@Autowired
//	private SystemFlagConfig systemFlagConfig;
	@Autowired
	private SpringBootApplicationRunner springBootApplicationRunner;

	@RequestMapping("/index")
	public String index() {
		return "index";
	}

	@RequestMapping("/indexTemplates")
	public String indexTemplates() {
		return "indexTemplates";
	}

	@RequestMapping("/getInterfaceTemplates")
	@ResponseBody
	public JSONObject getInterfaceTemplates(HttpServletRequest request) {
		String interfaceCode = request.getParameter("interfaceCode");
		String systemFlag = request.getParameter("systemFlag");
		JSONObject result = new JSONObject();
		try {
			//list.get(0)是报文头的信息，list.get(1)是报文体的信息
			List<ExcelDataObject> list = excelUtils.excel(interfaceCode, systemFlag);
			result.put(SystemConstants.RESULT, list);
			if (null != list && list.size() >= 3) {
				//把从excel中读取到的回复包的内容放到缓存中
//                redisUtils.setKey(interfaceCodeValue.toString(),list.get(2).toString());
				map.put(systemFlag + interfaceCode, list.get(3).getList());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("/getInterfaceCallBack")
	@ResponseBody
	public JSONObject getInterfaceCallBack(HttpServletRequest request) {
		JSONObject result = new JSONObject();
		//获取请求的接口号
		String interfaceCode = request.getParameter("interfaceCode");
		//获取请求地址和端口号
		String ipAddressPort = request.getParameter("ipAddressPort");
		//获取填写的请求报文
		String params = null;
		try {
			params = URLDecoder.decode(request.getParameter("params"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//获取系统标识
		String systemFlag = request.getParameter("systemFlag");
		//获取请求报文的每个的长度
		String[] headLengthStr = getLengthStr(request);
		//获取请求报文
		List<String> list = getParamsList(params);
		//获取请求报文体
		String msgBody = StringUtil.getMsgBody(headLengthStr, list);
		//从缓存中读取相应接口号的回复包信息
//        String bodyvlue = redisUtils.getValue(interfaceCodeValue.toString());
		List<ExcelData> excelDataList = (List<ExcelData>) map.get(systemFlag + interfaceCode);
		//从缓存中取得请求头的部分信息
//        String headValue = redisUtils.getValue(systemFlag+"_"+HEAD);
		String msgHead = (String) map.get(systemFlag + interfaceCode + "_" + SystemConstants.HEAD);
		if (StringUtils.isBlank(msgHead)) {
			result.put(SystemConstants.ERRORRESULT, "请先输入请求头的信息");
			return result;
		}
		//发送报文
		try {
			String msg = clientUtils.serverClient(msgHead, msgBody, ipAddressPort);
			//统计回复包中字段长度的和
			IntSummaryStatistics stats = excelDataList.stream()
					.mapToInt((p) -> Integer.parseInt(p.getLength().substring(p.getLength().indexOf("(") + SystemConstants.ONE, p.getLength().indexOf(")"))))
					.summaryStatistics();
//			int sum = (int) stats.getSum();
//			Integer characterLength = getCharacterLength(msg);
			//GBK编码中一个汉字占用两个字节，而UTF—8一个汉字占用三个字节
			String msgResult = msg.substring(0, 7);
			if (msgResult.contains("OK")) {
				List<ExcelData> excelDataParamList = getExcelDataList(msg, excelDataList);
				result.put(SystemConstants.RESULT, excelDataParamList);
			} else {
				result.put(SystemConstants.ERRORRESULT, msg.substring(msg.length() - 60, msg.length() - 30));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		finally {
		//最后从redis中删除相应的接口号
//            redisUtils.deleteKey(interfaceCode);
//			Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
//			while(it.hasNext()){
//				Map.Entry<String, Object> entry= it.next();
//				String key = entry.getKey();
//				map.remove(key);
//			}
//		}
		return result;
	}

	/**
	 * 把填写的报文头的信息存放在缓存里面
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveInterfaceHeadMessage")
	@ResponseBody
	public JSONObject saveInterfaceHeadMessage(HttpServletRequest request) {
		JSONObject result = new JSONObject();
		//获取请求的接口号
		String systemFlag = request.getParameter("systemFlag");
		//获取填写的请求报文
		String params = URLDecoder.decode(request.getParameter("params"));
		//获取请求的接口号
		String interfaceCode = request.getParameter("interfaceCode");
		//获取请求报文的每个的长度
		String[] headLengthStr = getLengthStr(request);
		//获取请求报文
		List<String> list = getParamsList(params);
		//获取请求报文体
		String msgBody = StringUtil.getMsgBody(headLengthStr, list);
		//把请求头的信息保存在redis中
//        redisUtils.setKey(systemFlag+"_"+HEAD,msgBody);
		map.put(systemFlag + interfaceCode + "_" + SystemConstants.HEAD, msgBody);
		result.put(SystemConstants.RESULT, "保存成功");
		return result;
	}

	/**
	 * 获取系统标识
	 *
	 * @return
	 */
	@RequestMapping("/getComboboxOption")
	@ResponseBody
	public JSONObject getComboboxOption(HttpServletRequest request) {
		Map<String, List<Map<String, String>>> map = springBootApplicationRunner.getMap();
		String type = request.getParameter("type");
		JSONObject result = new JSONObject();
		result.put(SystemConstants.RESULT, map.get(type));
		return result;
	}

	/**
	 * 解析每个填写的报文应该的长度
	 *
	 * @param request
	 * @return
	 */
	private String[] getLengthStr(HttpServletRequest request) {
		//获取请求报文的每个的长度
		String[] headLengthStr = request.getParameter("headLength").split(",");
		return headLengthStr;
	}

	/**
	 * 获取填写的每个报文的内容
	 *
	 * @param params
	 * @return
	 */
	private List<String> getParamsList(String params) {
		String[] paramsStr = params.split(",");
		List<String> list = new ArrayList<>();
		for (String param : paramsStr) {
			String[] paramStr = param.split("=");
			//paramStr[0]是参数名称,paramStr[1]是填写的报文
			if (paramStr.length == 1) {
				list.add("");
			} else {
				list.add(paramStr[1]);
			}
			//把渠道信息存起来，然后根据渠道信息发送报文
			if (SystemConstants.SIGN_WAY.equals(paramsStr[0]))
				map.put(paramsStr[0], paramsStr[1]);
		}
		return list;
	}

	private List<ExcelData> getExcelDataList(String msg, List<ExcelData> list) {
		//回复报文正常情况下的长度为70
		int i = 70;
		Integer characterLength = getCharacterLength(msg);
		i = i - characterLength;
		for (ExcelData excelData : list) {
			String length = excelData.getLength();
			length = length.substring(length.indexOf("(") + SystemConstants.ONE, length.indexOf(")"));
			Integer paramsLength = Integer.parseInt(length);
			if (i + paramsLength >= msg.length()) {
				excelData.setValue(msg.substring(msg.length() - paramsLength, msg.length()));
			} else {
				excelData.setValue(msg.substring(i, i + paramsLength));
			}
			i = i + paramsLength;
		}
		return list;
	}

	//获取一个字符串中中文的长度
	private Integer getCharacterLength(String msg) {
		Integer count = 0;
		String reg = "[\\u4e00-\\u9fa5]";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(msg);
		while (m.find()) {
			for (int i = 0; i <= m.groupCount(); i++) {
				count = count + 1;
			}
		}
		return count;
	}
}


