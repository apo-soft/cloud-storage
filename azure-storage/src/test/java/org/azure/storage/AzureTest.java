package org.azure.storage;

import java.io.IOException;
import java.net.URISyntaxException;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.file.CloudFileShare;
import com.microsoft.azure.storage.file.ListFileItem;

public class AzureTest {
	AzureConfig config = new AzurePropertiesConfig("E:\\environments\\store\\azure_store\\azure.properties", "utf-8");
	AzureCloudFile acfile = new AzureCloudFile(config);

	public void azureList() {
		CloudFileShare share;
		try {
			share = acfile.createShare("sampleshare");
			Iterable<ListFileItem> list = acfile.fileList(share);
			for (ListFileItem file : list) {
				System.out.println(file.getUri());
			}

		} catch (URISyntaxException | StorageException e) {
			e.printStackTrace();
		}
	}

	public void download() {
		CloudFileShare share = null;
		try {
			share = acfile.createShare("sampleshare");
		} catch (URISyntaxException | StorageException e1) {
			e1.printStackTrace();
		}
		String dirName = "sampledir";
		String fileName = "hello,azure.txt";
		try {
			String content = acfile.downloadFile(share, dirName, fileName);
			System.out.println("内容："+content);
		} catch (StorageException | URISyntaxException | IOException e) {
			System.out.println("下载异常");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		AzureTest test = new AzureTest();
		test.azureList();
//		test.download();

	}

}
