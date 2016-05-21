package org.azure.storage.table;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.azure.storage.AzureConfig;
import org.azure.storage.config.AzurePropertiesConfig;
import org.azure.storage.storetype.AzureCloudTable;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.TableServiceEntity;

public class AzureTable {
	AzureConfig config = new AzurePropertiesConfig("E:\\environments\\properties\\store\\azure_store\\azure.properties",
			"utf-8");
	AzureCloudTable acTable = new AzureCloudTable(config);

	public void tableList() {
		List<String> tablelist = acTable.tableList();
		System.out.println("table列表：");
		for (String s : tablelist) {
			System.out.println(s);
		}
	}

	public void createTable(String tableName) {
		try {
			acTable.createTable(tableName);
		} catch (URISyntaxException | StorageException e) {
			e.printStackTrace();
		}
		System.out.println("table创建完成。");
	}

	/**
	 * 向表中添加实体demo
	 *
	 * @param tableName
	 * @Author Yu Jinshui
	 * @createTime 2016年5月20日 下午8:57:39
	 */
	public void addEntity(String tableName) {
		CustomerEntity customer1 = new CustomerEntity("Walter", "Harp");
		customer1.setEmail("harp@126.com");
		customer1.setPhoneNumber("400-123-123");
		try {
			acTable.addEntity(tableName, customer1);
		} catch (URISyntaxException | StorageException e) {
			e.printStackTrace();
		}
		System.out.println("table中实体添加完成");
	}

	/**
	 * 像table中批量添加实体
	 * <p>
	 * 此demo不可随意更改参数，关于table的理解请参见截图【个人整理，仅供参考，不完全保证图片的准确性】
	 * 
	 * @param tableName
	 * @Author Yu Jinshui
	 * @createTime 2016年5月21日 上午10:41:29
	 */
	public void addListEntities(String tableName) {
		List<CustomerEntity> list = new ArrayList<CustomerEntity>();

		CustomerEntity customer1 = new CustomerEntity("hello", "world");
		customer1.setEmail("world@126.com");
		customer1.setPhoneNumber("100-123-123");
		list.add(customer1);
		CustomerEntity customer2 = new CustomerEntity("hello", "java");
		customer2.setEmail("java@126.com");
		customer2.setPhoneNumber("200-123-123");
		list.add(customer2);
		CustomerEntity customer3 = new CustomerEntity("hello", "azure");
		customer3.setEmail("azure@126.com");
		customer3.setPhoneNumber("300-123-123");
		list.add(customer3);

		try {
			acTable.addBatchEntities(tableName, list);
		} catch (URISyntaxException | StorageException e) {
			e.printStackTrace();
		}
		System.out.println("批量添加实体完成");
	}

	public void findAllEntities(String tableName, String partitionKey) {
		try {
			Iterable<CustomerEntity> entityList = acTable.findEntitiesByPartitionKey(tableName, partitionKey,
					CustomerEntity.class);
			printResult(entityList);

		} catch (URISyntaxException | StorageException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 检索分区中的一部分实体
	 * <p>
	 * 如果不想查询分区中的所有实体，则可以在筛选器中使用比较运算符来指定一个范围。以下代码组合了两个筛选器来获取分区“Smith”中的、行键（名字）
	 * 以字母“E”及字母“E”前面的字母开头的所有实体。然后，该代码打印了查询结果。如果使用添加到本指南的批量插入部分的表的实体，则此次只返回两个实体（
	 * Ben 和 Denise Smith），而不会包括 Jeff Smith。
	 *
	 * @param tableName
	 * @param partitionKey
	 * @Author Yu Jinshui
	 * @createTime 2016年5月21日 下午6:30:07
	 */
	public void findEntitiesByFilter(String tableName, String partitionKey) {
		try {
			Iterable<CustomerEntity> entityList = acTable.findEntitiesByFilter(tableName, partitionKey,
					CustomerEntity.class, null, null, "java");
			printResult(entityList);
		} catch (URISyntaxException | StorageException e) {
			e.printStackTrace();
		}
	}

	private void printResult(Iterable<CustomerEntity> entityList) {
		for (CustomerEntity entity : entityList) {
			System.out.println(entity.getPartitionKey() + //
					" " + entity.getRowKey() + //
					"\t" + entity.getEmail() + //
					"\t" + entity.getPhoneNumber());
		}
	}
}
