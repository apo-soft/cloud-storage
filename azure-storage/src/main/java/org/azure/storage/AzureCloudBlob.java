package org.azure.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlobDirectory;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;

/**
 * 云存储 - Blob
 * 
 * @author Yu Jinshui
 * @createTime 2016年4月2日 下午4:45:06
 */
public class AzureCloudBlob {
	private CloudBlobClient blobClient;

	public AzureCloudBlob(AzureConfig config) {
		try {
			blobClient = AzureCloudAccount.getCloudStorageAccount(config).createCloudBlobClient();
		} catch (InvalidKeyException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建容器【默认情况下，容器的权限已配置为允许进行私有访问】
	 * <p>
	 * Azure 存储中的每个 Blob 必须驻留在一个容器中。该容器构成 Blob 名称的一部分。例如，在这些示例 Blob URI 中，<br>
	 * mycontainer 是容器的名称：<br>
	 * 
	 * https://storagesample.blob.core.chinacloudapi.cn/mycontainer/blob1.txt
	 * <br>
	 * https://storagesample.blob.core.chinacloudapi.cn/mycontainer/photos/
	 * myphoto.jpg<br>
	 * 容器名称必须是有效的 DNS 名称，并符合以下命名规则：<br>
	 * 
	 * 容器名称必须以字母或数字开头，并且只能包含字母、数字和短划线 (-) 字符。<br>
	 * 每个短划线 (-) 字符的前面和后面都必须是一个字母或数字；在容器名称中不允许连续的短划线 (-)。<br>
	 * 容器名称中的所有字母都必须为小写。【器的名称必须始终为小写。如果你在容器名称中包括大写字母或以其他方式违反了容器命名规则，则可能会收到 400
	 * 错误（错误请求）】<br>
	 * 容器名称必须介于 3 到 63 个字符。 <br>
	 *
	 * @param containerName
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月2日 下午4:50:42
	 */
	public CloudBlobContainer createContainer(String containerName) throws URISyntaxException, StorageException {
		CloudBlobContainer container = getContainer(containerName);
		// Create the container if it does not exist.
		if (container.createIfNotExists()) {
			System.out.println("container created is ok");
		} else {
			System.out.println("container is exists");
		}

		return container;
	}

	/**
	 * 得到容器【默认情况下，容器的权限已配置为允许进行私有访问】
	 *
	 * @param containerName
	 *            容器名：必须全部为小写
	 * @return
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月2日 下午4:54:09
	 */
	public CloudBlobContainer getContainer(String containerName) throws URISyntaxException, StorageException {
		CloudBlobContainer container = blobClient.getContainerReference(containerName);
		return container;
	}

	/**
	 * 配置进行公共访问的容器
	 *
	 * @param containerName
	 *            容器名称
	 * @return
	 * @throws StorageException
	 * @throws URISyntaxException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月3日 上午10:11:03
	 */
	public CloudBlobContainer createPublicContainer(String containerName) throws StorageException, URISyntaxException {
		// Create a permissions object.
		BlobContainerPermissions containerPermissions = new BlobContainerPermissions();

		// Include public access in the permissions object.
		containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);

		CloudBlobContainer container = createContainer(containerName);
		// Set the permissions on the container.
		container.uploadPermissions(containerPermissions);
		return container;
	}

	/**
	 * 配置进行公共访问的容器
	 *
	 * @param containerName
	 *            容器名称
	 * @return
	 * @throws StorageException
	 * @throws URISyntaxException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月3日 上午10:11:03
	 */
	public CloudBlobContainer createPublicContainer(CloudBlobContainer container)
			throws StorageException, URISyntaxException {
		// Create a permissions object.
		BlobContainerPermissions containerPermissions = new BlobContainerPermissions();

		// Include public access in the permissions object.
		containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);

		// Set the permissions on the container.
		container.uploadPermissions(containerPermissions);
		return container;
	}

	/**
	 * 将 Blob 上传到容器中
	 * <p>
	 * 若要将文件上传到 Blob，请获取容器引用，并使用它获取 Blob 引用。获取 Blob 引用后，可以通过对该 Blob 引用调用 upload
	 * 来上传任何数据流。此操作将创建 Blob（如果该 Blob 不存在），或者覆盖它（如果该 Blob
	 * 存在）。下面的代码示例演示了这一点，并假定已创建容器。
	 *
	 * @param container
	 *            容器
	 * @param filePathName
	 *            待上传的文件路径
	 * @param outputName
	 *            文件展示名称
	 * @throws FileNotFoundException
	 * @throws StorageException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月3日 上午10:21:09
	 */
	public void uploadBlob(CloudBlobContainer container, final String filePathName, String outputName)
			throws FileNotFoundException, StorageException, IOException, URISyntaxException {

		CloudBlockBlob blob = container.getBlockBlobReference(outputName);
		File source = new File(filePathName);
		blob.upload(new FileInputStream(source), source.length());
	}

	/**
	 * 将 Blob 上传到容器中
	 * <p>
	 * 若要将文件上传到 Blob，请获取容器引用，并使用它获取 Blob 引用。获取 Blob 引用后，可以通过对该 Blob 引用调用 upload
	 * 来上传任何数据流。此操作将创建 Blob（如果该 Blob 不存在），或者覆盖它（如果该 Blob
	 * 存在）。下面的代码示例演示了这一点，并假定已创建容器。
	 *
	 * @param containerName
	 *            容器名，如果不存在底层会自动创建
	 * @param filePathName
	 *            待上传的文件路径
	 * @param outputName
	 *            文件展示名称
	 * @throws FileNotFoundException
	 * @throws StorageException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月3日 上午10:21:09
	 */
	public void uploadBlob(String containerName, final String filePathName, String outputName)
			throws URISyntaxException, StorageException, FileNotFoundException, IOException {
		CloudBlobContainer container = getContainer(containerName);
		uploadBlob(container, filePathName, outputName);
	}

	/**
	 * 列出容器中的 Blob
	 * <p>
	 * 若要列出容器中的 Blob，请先获取容器引用，就像上传 Blob 时执行的操作一样。可将容器的 listBlobs 方法用于 for
	 * 循环。以下代码将容器中每个 Blob 的 URI 输出到控制台。
	 *
	 * @param container
	 *            容器
	 * @throws MalformedURLException
	 * @throws StorageException
	 * @throws URISyntaxException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月3日 上午10:26:19
	 */
	public List<String> blobItemList(CloudBlobContainer container)
			throws MalformedURLException, URISyntaxException, StorageException {
		List<String> list = new ArrayList<String>();
		Iterable<ListBlobItem> listItemBlob = container.listBlobs();
		Iterator<ListBlobItem> itemBlob = listItemBlob.iterator();
		while (itemBlob.hasNext()) {
			ListBlobItem item = itemBlob.next();
			list.add(item.getUri().toURL().toString());
			if (item instanceof CloudBlobDirectory) {
				blobRecursive((CloudBlobDirectory) item, list);
			}
		}

		return list;
	}

	/**
	 * 列出容器中的 Blob
	 * <p>
	 * 若要列出容器中的 Blob，请先获取容器引用，就像上传 Blob 时执行的操作一样。可将容器的 listBlobs 方法用于 for
	 * 循环。以下代码将容器中每个 Blob 的 URI 输出到控制台。
	 *
	 * @param containerName
	 *            容器名称
	 * @return
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月3日 上午11:53:19
	 */
	public List<String> blobItemList(String containerName)
			throws MalformedURLException, URISyntaxException, StorageException {
		CloudBlobContainer container = getContainer(containerName);
		return blobItemList(container);
	}

	/**
	 * 列出全部容器以及容器中的文件
	 *
	 * @return
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月3日 下午12:47:51
	 */
	public List<List<String>> blobList() throws MalformedURLException, URISyntaxException, StorageException {
		List<List<String>> list = new ArrayList<List<String>>();
		Iterable<CloudBlobContainer> containlist = blobClient.listContainers();
		Iterator<CloudBlobContainer> ator = containlist.iterator();
		while (ator.hasNext()) {
			list.add(blobItemList(ator.next()));
		}
		return list;
	}

	/**
	 * 递归展示全部容器以及容器中的文件URL
	 *
	 * @param dir
	 * @param list
	 * @throws StorageException
	 * @throws URISyntaxException
	 * @throws MalformedURLException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月3日 上午11:27:07
	 */
	private void blobRecursive(CloudBlobDirectory dir, List<String> list)
			throws StorageException, URISyntaxException, MalformedURLException {
		Iterable<ListBlobItem> listItemBlob = dir.listBlobs();
		Iterator<ListBlobItem> itemBlob = listItemBlob.iterator();
		while (itemBlob.hasNext()) {
			ListBlobItem item = itemBlob.next();
			list.add(item.getUri().toURL().toString());
			if (item instanceof CloudBlobDirectory) {
				blobRecursive((CloudBlobDirectory) item, list);
			}
		}

	}

	/**
	 * 删除指定容器的blob文件
	 *
	 * @param containerName
	 *            容器名称
	 * @param blobName
	 *            blob文件名
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月3日 下午1:02:41
	 */
	public void deleteContainerBlob(String containerName, String blobName) throws URISyntaxException, StorageException {
		// Retrieve reference to a previously created container.
		CloudBlobContainer container = getContainer(containerName);
		deleteContainerBlob(container, blobName);
	}

	/**
	 * 删除指定容器的blob文件
	 *
	 * @param container
	 *            容器
	 * @param blobName
	 *            blob文件名
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月3日 下午1:03:13
	 */
	public void deleteContainerBlob(CloudBlobContainer container, String blobName)
			throws URISyntaxException, StorageException {
		// Retrieve reference to a blob named blobName.
		CloudBlockBlob blob = container.getBlockBlobReference(blobName);
		// Delete the blob.
		if (blob.deleteIfExists()) {
			System.out.println(blobName + " delete ok");
		} else {
			System.out.println(blobName + " is not exists");
		}
	}

	/**
	 * 删除 Blob 容器
	 *
	 * @param container
	 * @throws StorageException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月3日 下午1:28:04
	 */
	public void deleteContainer(CloudBlobContainer container) throws StorageException {
		if (container.deleteIfExists()) {
			System.out.println("container is deleted");
		} else {
			System.out.println("container not exist");
		}
	}

	/**
	 * 删除 Blob 容器
	 *
	 * @param containerName
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月3日 下午1:29:43
	 */
	public void deleteContainer(String containerName) throws URISyntaxException, StorageException {
		CloudBlobContainer container = blobClient.getContainerReference(containerName);
		deleteContainer(container);
	}
}
