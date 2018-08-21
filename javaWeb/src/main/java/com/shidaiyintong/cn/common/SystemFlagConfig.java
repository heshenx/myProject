package com.shidaiyintong.cn.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 现在不用了
 */
@Component
@Data
@ConfigurationProperties(prefix = "shidaiyintong", locations = "classpath:applicationBasic.properties")
public class SystemFlagConfig {
	private List<String> systemflag;//系统标识

	private Map<String, String> certtype;//证件类别

	private Map<String, String> status;//状态类别

	private Map<String, String> signway;//开户渠道

	private Map<String, String> tradetype;//交易类型

	private Map<String, String> cardtype;//关联账户类型

	private Map<String, String> issuetype;//签约产品产品大类

	private Map<String, String> limitunit;//期限时间单位

	private Map<String, String> issuekind;//签约产品产品小类

	private Map<String, String> periodtype;//期限类型

	private Map<String, String> ordtype;//排序方式

}
