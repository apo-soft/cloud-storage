package org.azure.storage;

public class AzureTest {

	public static void main(String[] args) {
		AzureFile test = new AzureFile();
//		 test.createDir("sampleshare", "main/test");
		// test.uploadFile("sampleshare", "user", "f:/file.txt", "file.txt");
		// System.out.println("show file list:");
		// test.azureList();
		 test.azureItemList();
//		test.deleteDir("sampleshare", "main/test");
		// System.out.println("show file content:");
		// test.download("sampleshare", "", "az.txt");
		// test.deleteFile("", "");

	}
}
