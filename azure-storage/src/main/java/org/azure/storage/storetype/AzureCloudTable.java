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
import com.microsoft.azure.storage.table.TableBatchOperation;
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
	 *            表名称
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
	 * 删除表
	 *
	 * @param tableName
	 * @return
	 * @throws StorageException
	 * @throws URISyntaxException
	 * @Author Yu Jinshui
	 * @createTime 2016年5月20日 下午9:29:29
	 */
	public boolean deleteTable(String tableName) throws StorageException, URISyntaxException {
		CloudTable table = getTable(tableName);
		return table.deleteIfExists();
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

	/**
	 * 向表中添加实体
	 * <p>
	 * 涉及实体的表操作需要 TableOperation 对象。此对象用于定义要对实体执行的操作，该操作可使用 CloudTable
	 * 对象执行。以下代码创建了包含要存储的某些客户数据的 CustomerEntity 类的新实例。接下来，该代码调用
	 * TableOperation.insertOrReplace 创建一个 TableOperation 对象，以便将实体插入表中，并将新的
	 * CustomerEntity 与之关联。最后，该代码对 CloudTable 对象调用 execute 方法，并指定了“people”表和新的
	 * TableOperation，后者随后向存储服务发送将新客户实体插入“people”表或在实体已存在的情况下替换实体的请求。
	 * 
	 * @param tableName
	 * @param entity
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @Author Yu Jinshui
	 * @createTime 2016年5月20日 下午9:00:37
	 */
	public void addEntity(String tableName, TableEntity entity) throws URISyntaxException, StorageException {
		// Create a cloud table object for the table.
		CloudTable table = tableClient.getTableReference(tableName);
		// Create an operation to add the new customer to the people table.
		TableOperation insertCustomer1 = TableOperation.insertOrReplace(entity);
		// Submit the operation to the table service.
		table.execute(insertCustomer1);
	}

	/**
	 * 批量插入实体
	 * <p>
	 * 你可以通过一次写入操作将一批实体插入到表服务。以下代码创建一个 TableBatchOperation
	 * 对象，然后向其中添加三个插入操作。每个插入操作的添加方法如下：创建一个新的实体对象，设置它的值，然后对 TableBatchOperation
	 * 对象调用 insert 方法以将实体与新的插入操作相关联。然后，该代码对 CloudTable 调用 execute，并指定“people”表和
	 * TableBatchOperation 对象，后者将在一个请求中向存储服务发送一批表操作。
	 * <p>
	 * 批处理操作的注意事项如下：<br>
	 * <ul>
	 * <li>您在单次批处理操作中最多可以执行 100 个插入、删除、合并、替换、插入或合并以及插入或替换操作（可以是这些操作的任意组合）。</li>
	 * <li>批处理操作也可以包含检索操作，但前提是检索操作是批处理中仅有的操作。</li>
	 * <li>单次批处理操作中的所有实体都必须具有相同的分区键。</li>
	 * <li>批处理操作的数据负载限制为 4MB。</li>
	 * </ul>
	 * 
	 * @param tableName
	 * @param entities
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @Author Yu Jinshui
	 * @createTime 2016年5月20日 下午9:09:59
	 */
	public void addBatchEntities(String tableName, List<? extends TableEntity> entities)
			throws URISyntaxException, StorageException {
		CloudTable table = tableClient.getTableReference(tableName);
		TableBatchOperation batchOperation = new TableBatchOperation();
		for (TableEntity entity : entities) {
			batchOperation.insertOrReplace(entity);
		}

		table.execute(batchOperation);
	}
	// TODO 检索分区中的所有实体
	// TODO 检索分区中的一部分实体
	// TODO 检索单个实体
	// TODO 修改实体
	// TODO 查询实体属性子集
	// TODO 插入或替换实体
	// TODO 删除实体
	// TODO 删除表

}
