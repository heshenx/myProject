package com.shidaiyintong.cn.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class ExcelDataObject implements Serializable {
	private String topic; //标题
	private List<ExcelData> list;//excel对象

}
