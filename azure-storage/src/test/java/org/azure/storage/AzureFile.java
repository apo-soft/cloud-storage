package org.azure.storage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.List;

import org.azure.storage.storetype.AzureCloudFile;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.file.CloudFileShare;

public class AzureFile {
	AzureConfig config = new AzurePropertiesConfig("E:\\environments\\properties\\store\\azure_store\\azure.properties",
			"utf-8");
	AzureCloudFile acfile = new AzureCloudFile(config);

	public void createPublicPermission() {
		try {
			acfile.createPublicPermission("sampleshare");
		} catch (URISyntaxException | StorageException | InvalidKeyException e) {
			e.printStackTrace();
		}
	}

	public void azureItemList() {
		CloudFileShare share;
		try {
			share = acfile.getShare("sampleshare");
			List<String> list = acfile.fileItemList(share);

			for (String item : list) {
				System.out.println(item);
			}

		} catch (URISyntaxException | StorageException | MalformedURLException e) {
			e.printStackTrace();
		}

	}

	public void download(String shareName, String dirName, String fileName, String charset) {
		CloudFileShare share = null;
		try {
			share = acfile.getShare(shareName);
		} catch (URISyntaxException | StorageException e1) {
			e1.printStackTrace();
		}
		try {
			// String content = acfile.downloadFile(share, dirName,http://60portalvhdsc21y82cjz14w.file.core.chinacloudapi.cn/sampleshare/3e9d780c-c6b3-41a6-8f71-578d708d5478.jpg
			// fileName).downloadText();//以云平台默认编码格式访问
			// String content = acfile.downloadFile(share, dirName,
			// fileName).downloadText(charset, null, null, null);// 自定义访问编码
			acfile.downloadFile(share, dirName, fileName).downloadToFile("f:/");
			// System.out.println("内容：" + content);
		} catch (StorageException | URISyntaxException | IOException e) {
			System.out.println("下载异常");
			e.printStackTrace();
		}
	}

	public void download(String filePath,String shareName, String dirName, String fileName, String charset) {
		CloudFileShare share = null;
		try {
			share = acfile.getShare(shareName);
		} catch (URISyntaxException | StorageException e1) {
			e1.printStackTrace();
		}
		try {
			// String content = acfile.downloadFile(share, dirName,
			// fileName).downloadText();//以云平台默认编码格式访问
			// String content = acfile.downloadFile(share, dirName,
			// fileName).downloadText(charset, null, null, null);// 自定义访问编码
			acfile.downloadFile(share, dirName, fileName).downloadToFile(filePath);
			// System.out.println("内容：" + content);
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

	public void createDir(String shareName, String dirName) {
		try {
			acfile.createDir(shareName, dirName);
		} catch (URISyntaxException | StorageException e) {
			e.printStackTrace();
		}
	}

	public void uploadFile(String shareName, String itemName, String filePathName, String outputName) {
		try {
			acfile.uploadFile(shareName, itemName, filePathName, outputName);
		} catch (URISyntaxException | StorageException | IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteDir(String shareName, String dirName) {
		try {
			acfile.deleteDir(shareName, dirName);
		} catch (URISyntaxException | StorageException e) {
			e.printStackTrace();
		}
	}

	public void createPublicPermission(String shareName) {
		try {
			acfile.createPublicShare(shareName);
		} catch (URISyntaxException | StorageException e) {
			e.printStackTrace();
		}
	}

}
