package org.azure.storage;

public class AzureTest {

	private void AFile() {
		AzureFile test = new AzureFile();
		// test.createDir("sampleshare", "main/test");
		// test.uploadFile("sampleshare", "user/list", "f:/file.txt", "data");
		// System.out.println("show file list:");
		test.azureItemList();
		// test.deleteDir("sampleshare", "user");
		// System.out.println("show file content:");
		// test.download("sampleshare", "user", "dat","utf-8");
		// test.deleteFile("", "");
		// test.createPublicPermission("sampleshare");
	}

	private void ABlob() {
		AzureBlob test = new AzureBlob();
		// test.createContainer("blobcontainer");
		// test.uploadBlob("blobcontainer", "F:/img.jpg", "img.jpg");
		test.createPublicContainer("blobcontainer");
		// test.blobItemList("blobcontainer-b");
		test.blobList();
		// test.deleteContainer("blobcontainer-c");
	}

	public static void main(String[] args) {
		AzureTest a = new AzureTest();
		// a.AFile();
		a.ABlob();
	}
}
