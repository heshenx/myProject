package com.hsx.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class JsonTest {
	public static void main(String[] args) {
		try {
//			A a = A.class.newInstance();
			A a =new A();
			a.setName("asdsa");
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			Writer stringWriter = new StringWriter();
			objectMapper.writeValue(stringWriter,a);
			System.out.println(stringWriter.toString());
			A a1 = objectMapper.readValue(stringWriter.toString(), A.class);
			System.out.println(a1.toString());
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	static class A{
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
