package org.azure.storage;

import org.azure.storage.table.AzureTable;

public class AzureTest {

	private void AFile() {
		AzureFile test = new AzureFile();
		// test.createDir("sampleshare", "main/test");
		// test.uploadFile("sampleshare", "user/list", "f:/file.txt", "data");
		// System.out.println("show file list:");
		// test.createPublicPermission();
		test.azureItemList();
		// test.deleteDir("sampleshare", "user");
		// System.out.println("show file content:");
		test.download("/user", "sampleshare", "user", "file.txt", "utf-8");
		// test.deleteFile("", "");
		// test.createPublicPermission("sampleshare");
		System.out.println("AFile is over.");
	}

	private void ABlob() {
		AzureBlob test = new AzureBlob();
		// test.createContainer("blobcontainer");
		// test.uploadBlob("blobcontainer/music", "F:/龙梅子 - 泪满天.mp3",
		// test.uploadBlob("blobcontainer/music", "F:/1.jpg", "test.jpg");
		// test.createPublicContainer("blobcontainer");
		// test.blobItemList("blobcontainer-b");
		test.blobList();// image/jpeg
		// test.deleteContainer("blobcontainer-c");
		// test.deleteContainerBlob("blobcontainer/music", "test.jpg");
	}

	private void AQueue() {
		AzureQueue test = new AzureQueue();
		String queueName = "queuetest";
		// test.createQueue(queueName);
		// test.addMessage(queueName, "Queue test 2");
		// test.peekMessage(queueName);
		test.messagelist(queueName);
		// test.getQueueLength(queueName);
		test.deleteMessage(queueName);
	}

	private void ATable() {
		AzureTable table = new AzureTable();
		// table.createTable("people");//table创建不能有下划线
		table.tableList();
		table.addEntity("people");
	}

	public static void main(String[] args) {
		AzureTest a = new AzureTest();
		a.ATable();
		// a.AFile();
		// a.ABlob();
		// a.AQueue();
	}
}
