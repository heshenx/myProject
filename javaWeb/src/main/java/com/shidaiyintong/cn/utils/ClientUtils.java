package com.shidaiyintong.cn.utils;

import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;

@Component
public class ClientUtils {

//    @Autowired
//    private SocketConfig socketConfig;

	public String serverClient(String headmsg, String bodymsg, String ipAddressPort) throws IOException{
		String[] split = ipAddressPort.split(":");
		Socket socket = new Socket(split[0], Integer.parseInt(split[1]));
		Writer writer = null;
		Reader reader = null;
		boolean connected = socket.isConnected();
		if (!connected) {
			throw new RuntimeException();
		}
		// 建立连接后往服务端写数据
		StringBuffer sb = null;
		try {
			writer = new OutputStreamWriter(socket.getOutputStream(), "GBK");
			writer.write(headmsg + bodymsg);
			writer.flush();
			reader = new InputStreamReader(socket.getInputStream(), "GBK");
			char chars[] = new char[64];
			int len;
			sb = new StringBuffer();
			while ((len = reader.read(chars)) != -1) {
				sb.append(new String(chars, 0, len));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			writer.close();
			reader.close();
			socket.close();
		}
		return sb.toString();
	}
}
