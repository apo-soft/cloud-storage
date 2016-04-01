package org.azure.storage;

import java.io.IOException;
import java.net.URISyntaxException;

import com.microsoft.azure.storage.AccessCondition;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.file.CloudFileShare;
import com.microsoft.azure.storage.file.FileRequestOptions;
import com.microsoft.azure.storage.file.ListFileItem;

public class AzureTest {
	AzureConfig config = new AzurePropertiesConfig("E:\\environments\\store\\azure_store\\azure.properties", "utf-8");
	AzureCloudFile acfile = new AzureCloudFile(config);

	public void azureList() {
		CloudFileShare share;
		try {
			share = acfile.getShare("sampleshare");
			Iterable<ListFileItem> list = acfile.fileList(share);
			for (ListFileItem file : list) {
				System.out.println(file.getUri());// 列出共享中的全部目录以及文件名【不包含子目录以及文件】
			}

		} catch (URISyntaxException | StorageException e) {
			e.printStackTrace();
		}
	}

	public void fileList(){
		
	}
	public void download(String shareName, String dirName, String fileName) {
		CloudFileShare share = null;
		try {
			share = acfile.getShare(shareName);
		} catch (URISyntaxException | StorageException e1) {
			e1.printStackTrace();
		}
		try {
			// String content = acfile.downloadFile(share, dirName,
			// fileName).downloadText();//以云平台默认编码格式访问
			String content = acfile.downloadFile(share, dirName, fileName).downloadText("GBK", null, null, null);// 自定义访问编码
			System.out.println("内容：" + content);
		} catch (StorageException | URISyntaxException | IOException e) {
			System.out.println("下载异常");
			e.printStackTrace();
		}
	}

	public void deleteFile(String dirName, String fileName) {
		CloudFileShare share = null;
		try {
			share = acfile.getShare("sampleshare");
		} catch (URISyntaxException | StorageException e1) {
			e1.printStackTrace();
		}
		try {
			boolean flag = acfile.deleteFile(share, dirName, fileName);
			if (flag) {
				System.out.println("Delete file SUCCESS");
			} else {
				System.out.println("Delete file FAILURE.");
			}
		} catch (StorageException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	private void createDir(String shareName, String dirName) {
		try {
			acfile.createDir(shareName, dirName);
		} catch (URISyntaxException | StorageException e) {
			e.printStackTrace();
		}
	}

	private void uploadFile(String shareName, String itemName, String filePathName, String outputName) {
		acfile.uploadFile(shareName, itemName, filePathName, outputName);
	}

	public static void main(String[] args) {
		AzureTest test = new AzureTest();
		// test.createDir("sampleshare", "main/test");
//		test.uploadFile("sampleshare", "user", "f:/file.txt", "file.txt");
		// System.out.println("show file list:");
		test.azureList();
		// System.out.println("show file content:");
		// test.download("sampleshare", "", "az.txt");
		// test.deleteFile("", "");

	}

}