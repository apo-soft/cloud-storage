package org.azure.storage;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import com.microsoft.azure.storage.CloudStorageAccount;

/**
 * 账号初始化类
 * 
 * @author Yu Jinshui
 * @createTime 2016年4月2日 下午4:33:25
 */
public class AzureCloudAccount {
	private String storageConnectionString;
	/**
	 * // 连接到 Azure 存储帐户<br>
	 * 若要连接到你的存储帐户，你需要使用 CloudStorageAccount 对象，以便将连接字符串传递到 parse 方法。 <br>
	 * Use the CloudStorageAccount object to connect to your storage account
	 * 
	 */
	private CloudStorageAccount storageAccount;

	private AzureCloudAccount(AzureConfig config) throws InvalidKeyException, URISyntaxException {
		storageConnectionString = "DefaultEndpointsProtocol=" + config.protocol() + ";"//
				+ "AccountName=" + config.accountName() + ";"//
				+ "AccountKey=" + config.accountKey() + ";"//
				+ "EndpointSuffix=" + config.endpointSuffix()//
		;
		storageAccount = CloudStorageAccount.parse(storageConnectionString);
	}

	private static AzureCloudAccount getInstance(AzureConfig config) throws InvalidKeyException, URISyntaxException {
		return new AzureCloudAccount(config);
	}

	/**
	 * 返回账号配置信息
	 *
	 * @param config
	 * @return
	 * @throws InvalidKeyException
	 * @throws URISyntaxException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月2日 下午4:39:42
	 */
	public static CloudStorageAccount getCloudStorageAccount(AzureConfig config)
			throws InvalidKeyException, URISyntaxException {
		return getInstance(config).storageAccount;
	}

}
