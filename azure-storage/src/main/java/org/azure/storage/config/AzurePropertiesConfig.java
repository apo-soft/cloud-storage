package org.azure.storage.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.azure.storage.AzureConfig;

/**
 * 配置实现类
 * 
 * @author Yu Jinshui
 * @createTime 2016年4月1日 上午11:19:11
 */
public class AzurePropertiesConfig implements AzureConfig {
	private static final Logger logger = Logger.getLogger(AzurePropertiesConfig.class);

	private String PROTOCOL;

	private String ACCOUNT_NAME;

	private String ACCOUNT_KEY;

	private String ENDPOINTSUFFIX;

	public AzurePropertiesConfig(Map<String, String> map) {
		ACCOUNT_NAME = map.get("ACCOUNT_NAME");
		ACCOUNT_KEY = map.get("ACCOUNT_KEY");
		ENDPOINTSUFFIX = map.get("ENDPOINTSUFFIX");
		PROTOCOL = map.get("PROTOCOL");

	}

	/**
	 * 自定义项目内配置文件名
	 * 
	 * @param fileName
	 */
	public AzurePropertiesConfig(String fileName) {
		getProperties(fileName);
	}

	public AzurePropertiesConfig(String fileName, String encoding) {
		getOutFileProperties(fileName, encoding);
	}

	public String toString() {
		return "ACCOUNT_NAME=" + ACCOUNT_NAME//
				+ ";ACCOUNT_KEY=" + ACCOUNT_KEY//
				+ ";ENDPOINTSUFFIX=" + ENDPOINTSUFFIX//
				+ ";PROTOCOL=" + PROTOCOL;
	}

	/**
	 * 
	 * 读取项目配置文件参数
	 * 
	 * @param fileName
	 * @Author Yu Jinshui
	 * @createTime 2016年4月1日 上午11:16:26
	 */
	private void getProperties(String fileName) {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
		Properties p = new Properties();
		try {
			p.load(inputStream);
		} catch (IOException e1) {
			System.out.println("配置文件读取失败，请检查.");
			e1.printStackTrace();
		}
		setPropertiesValues(p);
	}

	private void getOutFileProperties(String fileName, String encoding) {
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(new FileInputStream(fileName), encoding);
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			System.out.println("读取指定路径配置失败。" + fileName);
			logger.error("读取指定路径配置文件失败，请检查文件是否存在。" + fileName);
			e.printStackTrace();
		}
		Properties p = new Properties();
		try {
			p.load(reader);
		} catch (IOException e1) {
			System.out.println("配置文件读取失败，请检查.");
			e1.printStackTrace();
		}
		setPropertiesValues(p);

	}

	/**
	 * 设置属性值
	 * 
	 * @param p
	 * @author Yujinshui
	 * @time 2016年4月1日 上午11:01:36
	 */
	private void setPropertiesValues(Properties p) {
		ACCOUNT_NAME = p.getProperty("ACCOUNT_NAME");
		ACCOUNT_KEY = p.getProperty("ACCOUNT_KEY");
		ENDPOINTSUFFIX = p.getProperty("ENDPOINTSUFFIX");
		PROTOCOL = p.getProperty("PROTOCOL");
	}

	/**
	 * 默认实现-读取azure.properties配置文件
	 */
	public AzurePropertiesConfig() {
		getProperties("azure.properties");
	}

	/**
	 * 
	 * @see org.azure.storage.AzureConfig#accountName()
	 */
	@Override
	public String accountName() {
		return ACCOUNT_NAME;
	}

	/**
	 * 
	 * @see org.azure.storage.AzureConfig#accountKey()
	 */
	@Override
	public String accountKey() {
		return ACCOUNT_KEY;
	}

	/**
	 * 
	 * @see org.azure.storage.AzureConfig#endpointSuffix()
	 */
	@Override
	public String endpointSuffix() {
		return ENDPOINTSUFFIX;
	}

	@Override
	public String protocol() {
		return PROTOCOL;
	}

}
