package com.shidaiyintong.cn.common;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Data
@PropertySource({"classpath:applicationBasic.properties"})
public class SocketConfig {

	@Value("${socket.connect.ipaddress}")
	private String socketConnectIpAddress;//socket链接地址

	@Value("${socket.connect.port}")
	private Integer socketConnectPort;//socket链接地址端口号

	@Value("${filePath}")
	private String filePath;

}
