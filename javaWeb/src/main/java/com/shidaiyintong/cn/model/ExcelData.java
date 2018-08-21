package com.shidaiyintong.cn.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ExcelData implements Serializable {
	private String fieldName;//字段名
	private String length;//字段长度
	private String description;//说明
	private String type;//字段类型
	private String value;//回复包返回结果
}
