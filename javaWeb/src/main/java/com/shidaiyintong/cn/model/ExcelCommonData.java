package com.shidaiyintong.cn.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExcelCommonData {
	private String type;//数据类型
	private String typeName;//数据类型名称
	private String key;//数据的key
	private String value;//数据的value
}
