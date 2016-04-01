package org.azure.storage;

public interface AzureConfig {
	/**
	 * 协议
	 *
	 * @return
	 * @Author Yu Jinshui
	 * @createTime 2016年4月1日 下午12:04:22
	 */
	String protocol();

	/**
	 * 存储用户名
	 *
	 * @return
	 * @Author Yu Jinshui
	 * @createTime 2016年4月1日 上午11:09:45
	 */
	String accountName();

	/**
	 * 账户KEY
	 *
	 * @return
	 * @Author Yu Jinshui
	 * @createTime 2016年4月1日 上午11:09:55
	 */
	String accountKey();

	/**
	 * 连接地址
	 *
	 * @return
	 * @Author Yu Jinshui
	 * @createTime 2016年4月1日 上午11:10:06
	 */
	String endpointSuffix();
}
