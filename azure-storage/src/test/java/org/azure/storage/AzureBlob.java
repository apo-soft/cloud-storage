package org.azure.storage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

import org.azure.storage.storetype.AzureCloudBlob;

import com.microsoft.azure.storage.StorageException;

/**
 * 二进制存储部分
 * 
 * @author Yu Jinshui
 * @createTime 2016年4月2日 下午3:42:40
 */
public class AzureBlob {
	AzureConfig config = new AzurePropertiesConfig("E:\\environments\\properties\\store\\azure_store\\azure.properties", "utf-8");
	AzureCloudBlob acBlob = new AzureCloudBlob(config);

	public void createContainer(String containerName) {
		try {
			acBlob.createContainer(containerName);
		} catch (URISyntaxException | StorageException e) {
			e.printStackTrace();
		}
		System.out.println("createContainer is ok.");
	}

	public void createPublicContainer(String containerName) {
		try {
			acBlob.createPublicContainer(containerName);
		} catch (StorageException | URISyntaxException e) {
			e.printStackTrace();
		}
		System.out.println("createPublicContainer is ok.");
	}

	public void uploadBlob(String containerName, final String filePathName, String outputName) {
		System.out.println("文件上传开始。。。");
		try {
			acBlob.uploadBlob(containerName, filePathName, outputName);
		} catch (URISyntaxException | StorageException | IOException e) {
			e.printStackTrace();
		}
		System.out.println("uploadBlob - " + outputName + " is ok.");
	}

	public List<String> blobItemList(String containerName) {
		List<String> list = null;
		try {
			list = acBlob.blobItemList(containerName);
		} catch (MalformedURLException | URISyntaxException | StorageException e) {
			e.printStackTrace();
		}
		for (String li : list) {
			System.out.println(li);
		}
		System.out.println("blobItemList is ok.");
		return list;
	}

	public List<List<String>> blobList() {
		List<List<String>> list = null;
		try {
			list = acBlob.blobList();
		} catch (MalformedURLException | URISyntaxException | StorageException e) {
			e.printStackTrace();
		}
		for (List<String> li : list) {
			for (String s : li) {
				System.out.println(s);
			}
		}
		return list;
	}

	public void deleteContainerBlob(String containerName, String blobName) {
		try {
			acBlob.deleteContainerBlob(containerName, blobName);
		} catch (URISyntaxException | StorageException e) {
			e.printStackTrace();
		}
		System.out.println("Delete containerBlob is over");
	}

	public void deleteContainer(String containerName) {
		try {
			acBlob.deleteContainer(containerName);
		} catch (URISyntaxException | StorageException e) {
			e.printStackTrace();
		}
		System.out.println("Delete container is over");
	}
}
