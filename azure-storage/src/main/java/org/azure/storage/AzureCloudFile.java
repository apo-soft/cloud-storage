package org.azure.storage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

import org.apache.log4j.Logger;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.file.CloudFile;
import com.microsoft.azure.storage.file.CloudFileClient;
import com.microsoft.azure.storage.file.CloudFileDirectory;
import com.microsoft.azure.storage.file.CloudFileShare;
import com.microsoft.azure.storage.file.ListFileItem;

public class AzureCloudFile {

	private String storageConnectionString;
	/**
	 * // 连接到 Azure 存储帐户<br>
	 * 若要连接到你的存储帐户，你需要使用 CloudStorageAccount 对象，以便将连接字符串传递到 parse 方法。 <br>
	 * Use the CloudStorageAccount object to connect to your storage account
	 * 
	 */
	private CloudStorageAccount storageAccount;

	/**
	 * // 如何：创建共享<br>
	 * // 文件存储中的所有文件和目录都位于名为 Share<br>
	 * // 的容器内。你的存储帐户可以拥有无数的共享，只要你的帐户容量允许。若要获得共享及其内容的访问权限，你需要使用文件存储客户端。 <br>
	 * // Create the file storage client.<br>
	 * 使用文件存储客户端以后，你就可以获得对共享的引用。
	 */
	private CloudFileClient fileClient;

	private static final Logger logger = Logger.getLogger(AzureCloudFile.class);

	public AzureCloudFile(AzureConfig config) {
		storageConnectionString = "DefaultEndpointsProtocol=" + config.protocol() + ";"//
				+ "AccountName=" + config.accountName() + ";"//
				+ "AccountKey=" + config.accountKey() + ";"//
				+ "EndpointSuffix=" + config.endpointSuffix()//
		;

		try {
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			fileClient = storageAccount.createCloudFileClient();
		} catch (InvalidKeyException | URISyntaxException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 创建共享引用
	 *
	 * @param shareName
	 *            共享名称
	 * @return
	 * @throws StorageException
	 * @throws URISyntaxException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月1日 下午2:34:47
	 */
	public CloudFileShare createShare(String shareName) throws URISyntaxException, StorageException {
		CloudFileShare share;
		share = fileClient.getShareReference(shareName);
		// 实际创建共享时，请使用 CloudFileShare 对象的 createIfNotExists 方法。

		if (share.createIfNotExists()) {
			System.out.println("New share created");
		}
		// 而在目前，share 保留对名为 sampleshare 的共享的引用。
		return share;

	}

	/**
	 * 创建共享中的目录
	 * <p>
	 * 你也可以将文件置于子目录中，不必将其全部置于根目录中，以便对存储进行有效的组织。Azure
	 * 文件存储服务可以创建任意数目的目录，只要你的帐户允许。以下代码将在根目录下创建名为 sampledir 的子目录。
	 * 
	 * @param share
	 * @param dirName
	 *            目录名
	 * @throws StorageException
	 * @throws URISyntaxException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月1日 下午3:47:02
	 */
	public CloudFileDirectory createDir(CloudFileShare share, String dirName)
			throws StorageException, URISyntaxException {
		// Get a reference to the root directory for the share.
		CloudFileDirectory rootDir = share.getRootDirectoryReference();

		// Get a reference to the sampledir directory
		CloudFileDirectory sampleDir = rootDir.getDirectoryReference(dirName);

		if (sampleDir.createIfNotExists()) {
			System.out.println("sampledir created");
		} else {
			System.out.println("sampledir already exists");
		}
		return sampleDir;
	}

	/**
	 * 上传文件
	 * 
	 * @param share
	 *            共享信息
	 * @param shareName
	 *            共享目录的名称
	 * @param filePathName
	 *            待上传文件路径
	 * @param outputName
	 *            上传后的展示名称
	 * @Author Yu Jinshui
	 * @createTime 2016年4月1日 下午2:38:35
	 */
	public void uploadFile(CloudFileShare share, String filePathName, String outputName) {

		// 上载文件的第一步是获取对文件所在的目录的引用。为此，你需要调用共享对象的 getRootDirectoryReference
		// 方法。

		// Get a reference to the root directory for the share.
		CloudFileDirectory rootDir;
		try {
			rootDir = share.getRootDirectoryReference();
			// 现在，你已经有了共享所在的根目录的引用，因此可以使用以下代码来上载文件。

			CloudFile cloudFile = rootDir.getFileReference(outputName);
			cloudFile.uploadFromFile(filePathName);
		} catch (IOException | StorageException | URISyntaxException e) {
			logger.error("文件上传异常。", e);
			e.printStackTrace();
		}

	}

	/**
	 * // 如何：列出共享中的文件和目录 <br>
	 * // 可以轻松获取共享中文件和目录的列表，只需针对 CloudFileDirectory 引用调用 <br>
	 * // listFilesAndDirectories 即可。该方法将返回你可以对其进行循环访问的 ListFileItem <br>
	 * // 对象的列表。例如，下面的代码将列出根目录中的文件和目录。
	 *
	 * 
	 * @param share
	 * @param shareName
	 * @return
	 * @Author Yu Jinshui
	 * @createTime 2016年4月1日 下午2:52:59
	 */
	public Iterable<ListFileItem> fileList(CloudFileShare share) {
		Iterable<ListFileItem> filelist = null;
		try {
			CloudFileDirectory rootDir = share.getRootDirectoryReference();
			filelist = rootDir.listFilesAndDirectories();
		} catch (StorageException | URISyntaxException e) {
			logger.error("获取共享文件列表失败", e);
			e.printStackTrace();
		}
		return filelist;
	}

	/**
	 * 下载并显示文件内容
	 *
	 * @param share
	 * @param dirName
	 *            目录名
	 * @param fileName
	 *            文件名
	 * @return String
	 * @throws StorageException
	 * @throws URISyntaxException
	 * @throws IOException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月1日 下午3:21:20
	 */
	public String downloadFile(CloudFileShare share, String dirName, String fileName)
			throws StorageException, URISyntaxException, IOException {
		// Get a reference to the root directory for the share.
		CloudFileDirectory rootDir = share.getRootDirectoryReference();

		// Get a reference to the directory that contains the file
		CloudFileDirectory sampleDir = rootDir.getDirectoryReference(dirName);

		// Get a reference to the file you want to download
		CloudFile file = sampleDir.getFileReference(fileName);

		// Write the contents of the file to the console.
		// System.out.println(file.downloadText());
		System.out.println(file.downloadText());
		return file.downloadText();
	}

	/**
	 * 删除文件
	 *
	 * @param share
	 * @param dirName
	 *            目录名
	 * @param fileName
	 *            文件名
	 * @throws StorageException
	 * @throws URISyntaxException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月1日 下午4:02:28
	 */
	public void deleteFile(CloudFileShare share, String dirName, String fileName)
			throws StorageException, URISyntaxException {
		// Get a reference to the root directory for the share.
		CloudFileDirectory rootDir = share.getRootDirectoryReference();

		// Get a reference to the directory where the file to be deleted is in
		CloudFileDirectory containerDir = rootDir.getDirectoryReference(dirName);

		CloudFile file;

		file = containerDir.getFileReference(fileName);
		if (file.deleteIfExists()) {
			System.out.println(fileName + " was deleted");
		}
	}

}
