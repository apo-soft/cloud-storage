package org.azure.storage.table;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.azure.storage.AzureConfig;
import org.azure.storage.config.AzurePropertiesConfig;
import org.azure.storage.storetype.AzureCloudTable;

import com.microsoft.azure.storage.StorageException;

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
		CustomerEntity customer1 = new CustomerEntity("Harp", "Walter");
		customer1.setEmail("harp@126.com");
		customer1.setPhoneNumber("400-123-123");
		try {
			acTable.addEntity(tableName, customer1);
		} catch (URISyntaxException | StorageException e) {
			e.printStackTrace();
		}
		System.out.println("table中实体添加完成");
	}

	public void addListEntities(String tableName) {
		List<CustomerEntity> list = new ArrayList<CustomerEntity>();
		for (int i = 0; i < 3; i++) {
			CustomerEntity customer = new CustomerEntity("hello" + i, "world" + i);
			customer.setEmail("email_" + i + "@126.com");
			customer.setPhoneNumber("400-123-123-" + i);
			list.add(customer);
		}
		try {
			acTable.addBatchEntities(tableName, list);
		} catch (URISyntaxException | StorageException e) {
			e.printStackTrace();
		}
	}

}
