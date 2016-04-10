package org.azure.storage;

import java.net.URISyntaxException;

import org.azure.storage.storetype.AzureCloudQueue;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.queue.CloudQueueMessage;

public class AzureQueue {
	AzureConfig config = new AzurePropertiesConfig("E:\\environments\\properties\\store\\azure_store\\azure.properties",
			"utf-8");
	AzureCloudQueue acQueue = new AzureCloudQueue(config);

	public void createQueue(String queueName) {
		try {
			acQueue.createQueue(queueName);
		} catch (URISyntaxException | StorageException e) {
			e.printStackTrace();
		}
	}

	public void addMessage(String queueName, String message) {
		try {
			acQueue.addMessage(queueName, message);
		} catch (URISyntaxException | StorageException e) {
			e.printStackTrace();
		}
	}

	public void peekMessage(String queueName) {
		String result = "";
		try {
			result = acQueue.peekMessage(queueName).getMessageContentAsString();
		} catch (URISyntaxException | StorageException e) {
			e.printStackTrace();
		}
		System.out.println("扫视下一条消息结果为：" + result);
	}

	public void messagelist(String queueName) {
		System.out.println("queue list:");
		try {
			Iterable<CloudQueueMessage> list = acQueue.messageList(queueName);
			for (CloudQueueMessage mess : list) {
				System.out.println(mess.getMessageContentAsString());
			}
		} catch (URISyntaxException | StorageException e) {
			e.printStackTrace();
		}
	}

	public void getQueueLength(String queueName) {
		try {
			long len = acQueue.getQueueLength(queueName);
			System.out.println("queue长度：" + len);
		} catch (URISyntaxException | StorageException e) {
			e.printStackTrace();
		}
	}

	public void deleteMessage(String queueName) {
		try {
			acQueue.deleteMessage(queueName);
		} catch (URISyntaxException | StorageException e) {
			e.printStackTrace();
		}
	}
}
