package com.hsx.test;

import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Formatter;

public class Test {

	public static void main(String[] args) {
		LocalDate localDate = LocalDate.now();
		LocalDateTime localDateTime = LocalDateTime.now();
		int dayOfMonth = localDateTime.getDayOfMonth();
		System.out.println(localDateTime.getDayOfWeek());
		System.out.println(localDateTime.getDayOfYear());
		System.out.println(dayOfMonth);
		System.out.println(localDate);
		System.out.println(localDateTime);


		LocalDate start = LocalDate.of(2018, 7, 18);
		LocalDate end = LocalDate.of(2019, 2, 16);
		System.out.println(end.toEpochDay() - start.toEpochDay());
//		System.out.println(end.toInstant(ZoneOffset.of("+8")).toEpochMilli() - start.toEpochDay());
//		Formatter
		HttpStatus created = HttpStatus.CREATED;
	}
}
