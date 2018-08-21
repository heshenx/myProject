package com.shidaiyintong.cn.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class RedisUtils {
	@Autowired
	private StringRedisTemplate template;

	public void setKey(String key, String value) {
		ValueOperations<String, String> ops = template.opsForValue();
		//ops.set(key,value,5, TimeUnit.MINUTES);//5分钟过期
		ops.set(key, value);
	}

	public String getValue(String key) {
		ValueOperations<String, String> ops = this.template.opsForValue();
		return ops.get(key);
	}

	public void deleteKey(String key) {
		this.template.delete(key);
	}

	public void setKeyWithTime(String key, String value, Long time) {
		ValueOperations<String, String> ops = template.opsForValue();
		ops.set(key, value, time, TimeUnit.DAYS);//设置某个可以在多久后过期，则自动删除
		ops.set(key, value);
	}
}