package org.azure.storage.table;

import com.microsoft.azure.storage.table.TableServiceEntity;

/**
 * 实体将映射到使用实现了 TableEntity 的自定义类的 Java 对象。为方便起见，TableServiceEntity 类实现
 * TableEntity，并使用反射将属性映射到以它们本身命名的 getter 和 setter
 * 方法。若要将实体添加到表，首先要创建用于定义实体的属性的类。以下代码定义了将客户的名字和姓氏分别用作行键和分区键的实体类。
 * 实体的分区键和行键共同唯一地标识表中的实体。查询分区键相同的实体的速度可以快于查询分区键不同的实体的速度
 * 
 * @author Yu Jinshui
 * @createTime 2016年4月17日 上午11:37:10
 */
public class CustomerEntity extends TableServiceEntity {
	public CustomerEntity(String firstName, String lastName) {
		this.rowKey = firstName;
		this.partitionKey = lastName;
	}

	public CustomerEntity() {
	}

	String email;
	String phoneNumber;

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
