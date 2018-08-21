package com.shidaiyintong.cn.utils;

import com.shidaiyintong.cn.common.SocketConfig;
import com.shidaiyintong.cn.model.ExcelCommonData;
import com.shidaiyintong.cn.model.ExcelData;
import com.shidaiyintong.cn.model.ExcelDataObject;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ExcelUtils {
	@Autowired
	private SocketConfig socketConfig;

	private static final Integer sheetZero = 0;//excel表格的sheet页码
	private static final Integer sheetOne = 1;//excel表格的sheet页码
	private static final Integer sheetTwo = 2;//excel表格的sheet页码
	private static final Integer sheetThree = 3;//excel表格的sheet页码

	public List<ExcelDataObject> excel(String interfaceCode, String systemFlag) throws Exception {
		//用流的方式先读取到你想要的excel的文件
		FileInputStream fis = new FileInputStream(new File(socketConfig.getFilePath() + systemFlag + "_" + interfaceCode + ".xls"));
		//解析excel
		POIFSFileSystem pSystem = new POIFSFileSystem(fis);
		//获取整个excel
		HSSFWorkbook hb = new HSSFWorkbook(pSystem);
		//获取当前excel共有多少个sheet
		int numberOfSheets = hb.getNumberOfSheets();
		List<ExcelData> excelDataSheetZeroList = new ArrayList<>();
		List<ExcelData> excelDataSheetOneList = new ArrayList<>();
		List<ExcelData> excelDataSheetTwoList = new ArrayList<>();
		//增加一个项目描述信息
		List<ExcelData> excelDataSheetThreeList = new ArrayList<>();
		List<ExcelDataObject> excelDataObjectList = new ArrayList<>();
		for (int num = 0; num < numberOfSheets; num++) {
			//获取表单sheet
			HSSFSheet sheet = hb.getSheetAt(num);
			//获取第一行
			int firstrow = sheet.getFirstRowNum();
			//获取最后一行
			int lastrow = sheet.getLastRowNum();
			//循环行数依次获取列数
			for (int i = firstrow; i < lastrow + 1; i++) {
				//获取哪一行
				Row row = sheet.getRow(i + 1);
				if (row != null) {
					//获取这一行的第一列
					int firstcell = row.getFirstCellNum();
					//获取这一行的最后一列
					int lastcell = row.getLastCellNum();
					//创建一个集合，用处将每一行的每一列数据都存入集合中
					List<String> list = new ArrayList<String>();
					for (int j = firstcell; j < lastcell; j++) {
						//获取第j列
						Cell cell = row.getCell(j);
						if (cell != null) {
							list.add(cell.toString());
						}
					}
					//把从excel中的获取的值放入ExcelData对象中
					ExcelData excelData = ExcelData.builder().fieldName(list.get(0)).length(list.get(1)).description(list.get(2)).type(list.get(3)).build();
					if (sheetZero == num) {
						excelDataSheetZeroList.add(excelData);
					} else if (sheetOne == num) {
						excelDataSheetOneList.add(excelData);
					} else if (sheetTwo == num) {
						excelDataSheetTwoList.add(excelData);
					} else if (sheetThree == num) {
						excelDataSheetThreeList.add(excelData);
					}
				}
			}
		}
		ExcelDataObject excelDataSheetZeroObject = ExcelDataObject.builder().topic("描述信息").list(excelDataSheetZeroList).build();
		ExcelDataObject excelDataSheetOneObject = ExcelDataObject.builder().topic("报文头信息").list(excelDataSheetOneList).build();
		ExcelDataObject excelDataSheetTwoObject = ExcelDataObject.builder().topic("报文体信息").list(excelDataSheetTwoList).build();
		ExcelDataObject excelDataSheetThreeObject = ExcelDataObject.builder().topic("回复包信息").list(excelDataSheetThreeList).build();

		excelDataObjectList.add(excelDataSheetZeroObject);
		excelDataObjectList.add(excelDataSheetOneObject);
		excelDataObjectList.add(excelDataSheetTwoObject);
		excelDataObjectList.add(excelDataSheetThreeObject);
		fis.close();
		return excelDataObjectList;
	}

	public Map<String, List<Map<String, String>>> getCommonDataExcel() throws Exception {
		List<ExcelCommonData> excelCommonDataList = new ArrayList<>();
		//用流的方式先读取到你想要的excel的文件
		FileInputStream fis = new FileInputStream(new File(socketConfig.getFilePath() + "common.xls"));
		//解析excel
		POIFSFileSystem pSystem = new POIFSFileSystem(fis);
		//获取整个excel
		HSSFWorkbook hb = new HSSFWorkbook(pSystem);
		//获取表单sheet
		HSSFSheet sheet = hb.getSheetAt(0);
		//获取第一行
		int firstrow = sheet.getFirstRowNum();
		//获取最后一行
		int lastrow = sheet.getLastRowNum();
		for (int i = firstrow; i < lastrow + 1; i++) {
			//获取哪一行
			Row row = sheet.getRow(i);
			if (row != null) {
				//获取这一行的第一列
				int firstcell = row.getFirstCellNum();
				//获取这一行的最后一列
				int lastcell = row.getLastCellNum();
				//创建一个集合，用处将每一行的每一列数据都存入集合中
				List<String> list = new ArrayList<String>();
				for (int j = firstcell; j < lastcell; j++) {
					//获取第j列
					Cell cell = row.getCell(j);
					if (2 == j) {
						cell = row.getCell(j);
						String reg = "^[0-9]+(.[0-9]+)?$";
						if (cell.toString().matches(reg)) {
							int numericCellValue = (int) cell.getNumericCellValue();
							list.add(numericCellValue + "");
						} else {
							list.add(cell.toString());
						}
					} else {
						list.add(cell.toString());
					}
				}
				//把从excel中的获取的值放入ExcelCommonData对象中
				ExcelCommonData excelCommonData = ExcelCommonData.builder().typeName(list.get(0)).type(list.get(1)).key(list.get(2)).value(list.get(3)).build();
				excelCommonDataList.add(excelCommonData);
			}
		}
		Map<String, List<ExcelCommonData>> map = excelCommonDataList.stream().collect(Collectors.groupingBy(ExcelCommonData::getType));
		Map<String, List<Map<String, String>>> listMap = new HashMap<>();
		for (String key : map.keySet()) {
			List<Map<String, String>> excelList = new ArrayList<>();
			List<ExcelCommonData> list = map.get(key);
			Map<String, String> collect = list.stream().collect(Collectors.toMap(ExcelCommonData::getKey, ExcelCommonData::getValue));
			excelList.add(collect);
			listMap.put(key, excelList);
		}
		return listMap;
	}
}