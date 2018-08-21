package com.shidaiyintong.cn.common;

import com.shidaiyintong.cn.utils.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(value = 1)
public class SpringBootApplicationRunner implements ApplicationRunner {
	private static Map<String, List<Map<String, String>>> map = new ConcurrentHashMap();

	public Map<String, List<Map<String, String>>> getMap() {
		return map;
	}

	@Autowired
	private ExcelUtils excelUtils;

	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
		map = excelUtils.getCommonDataExcel();
	}
}
