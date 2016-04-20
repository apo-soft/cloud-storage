package org.azure.storage.storetype;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

import org.azure.storage.AzureConfig;
import org.azure.storage.config.AzureCloudAccount;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;
import com.microsoft.azure.storage.table.TableEntity;
import com.microsoft.azure.storage.table.TableOperation;

/**
 * 云存储 - table
 * 
 * @author Yu Jinshui
 * @createTime 2016年4月3日 下午7:35:14
 */
public class AzureCloudTable {
	private CloudTableClient tableClient;

	/**
	 * 初始化账户配置信息，得到客户端
	 * 
	 * @param config
	 */
	public AzureCloudTable(AzureConfig config) {
		try {
			tableClient = AzureCloudAccount.getCloudStorageAccount(config).createCloudTableClient();
		} catch (InvalidKeyException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取表名
	 *
	 * @param tableName
	 *            表名称
	 * @return
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月17日 上午11:22:06
	 */
	public CloudTable getTable(String tableName) throws URISyntaxException, StorageException {
		return tableClient.getTableReference(tableName);
	}

	/**
	 * 创建表
	 *
	 * @param tableName
	 * @return
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月17日 上午11:23:52
	 */
	public CloudTable createTable(String tableName) throws URISyntaxException, StorageException {
		CloudTable table = getTable(tableName);
		table.createIfNotExists();
		return table;
	}

	/**
	 * 列出所有表名称
	 *
	 * @return
	 * @Author Yu Jinshui
	 * @createTime 2016年4月17日 上午11:27:57
	 */
	public List<String> tableList() {
		List<String> tableList = new ArrayList<String>();
		for (String table : tableClient.listTables()) {
			tableList.add(table);
		}
		return tableList;
	}

	// TODO
	public void addEntity(String tableName, TableEntity entity) throws URISyntaxException, StorageException {
		// Create a cloud table object for the table.
		CloudTable table = tableClient.getTableReference(tableName);

		// Create an operation to add the new customer to the people table.
		TableOperation insertCustomer1 = TableOperation.insertOrReplace(entity);
		// Submit the operation to the table service.
		table.execute(insertCustomer1);
	}

}
