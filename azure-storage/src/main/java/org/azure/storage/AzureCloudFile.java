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
	 * 获取共享名
	 *
	 * @param shareName
	 *            共享名称
	 * @return
	 * @throws StorageException
	 * @throws URISyntaxException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月1日 下午2:34:47
	 */
	public CloudFileShare getShare(String shareName) throws URISyntaxException, StorageException {
		CloudFileShare share;
		share = fileClient.getShareReference(shareName);
		// 实际创建共享时，请使用 CloudFileShare 对象的 createIfNotExists 方法。

		// 而在目前，share 保留对名为 sampleshare 的共享的引用。
		return share;

	}

	/**
	 * 创建共享引用
	 *
	 * @param shareName
	 *            共享名
	 * @return
	 * @throws StorageException
	 * @throws URISyntaxException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月1日 下午6:44:54
	 */
	public CloudFileShare createShare(String shareName) throws StorageException, URISyntaxException {
		CloudFileShare share = getShare(shareName);
		if (share.createIfNotExists()) {
			System.out.println("New share created");
		}
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
	 *            目录名(多级目录以 / 分割)
	 * @throws StorageException
	 * @throws URISyntaxException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月1日 下午3:47:02
	 */
	public CloudFileDirectory createDir(CloudFileShare share, String dirName)
			throws StorageException, URISyntaxException {
		CloudFileDirectory sampleDir = null;
		sampleDir = getDir(share, dirName);
		if (sampleDir.createIfNotExists()) {
			System.out.println(dirName + " created");
		} else {
			System.out.println(dirName + " already exists");
		}
		return sampleDir;
	}

	/**
	 * 获取目录
	 *
	 * @param share
	 * @param dirName
	 * @return
	 * @throws StorageException
	 * @throws URISyntaxException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月1日 下午7:25:32
	 */
	public CloudFileDirectory getDir(CloudFileShare share, String dirName) throws StorageException, URISyntaxException {
		// Get a reference to the root directory for the share.
		CloudFileDirectory rootDir = share.getRootDirectoryReference();
		// Get a reference to the sampledir directory
		CloudFileDirectory sampleDir = rootDir.getDirectoryReference(dirName);
		return sampleDir;
	}

	/**
	 * 获取共享目录
	 *
	 * @param shareName
	 *            共享名称
	 * @param dirName
	 *            目录名称
	 * @return
	 * @throws StorageException
	 * @throws URISyntaxException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月1日 下午7:28:14
	 */
	public CloudFileDirectory getDir(String shareName, String dirName) throws StorageException, URISyntaxException {
		CloudFileShare share = getShare(shareName);
		return getDir(share, dirName);
	}

	/**
	 * 创建共享以及目录名
	 * 
	 * @param shareName
	 *            共享名称
	 * @param dirName
	 *            共享下的目录
	 * @return
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月1日 下午6:34:23
	 */
	public CloudFileDirectory createDir(String shareName, String dirName) throws URISyntaxException, StorageException {
		CloudFileShare share = getShare(shareName);
		return createDir(share, dirName);
	}

	/**
	 * 上传文件
	 * 
	 * @param share
	 *            共享信息
	 * @param itemName
	 *            目录名(多级目录以 / 分割)
	 * @param filePathName
	 *            待上传文件路径
	 * @param outputName
	 *            上传后的展示名称
	 * @Author Yu Jinshui
	 * @createTime 2016年4月1日 下午2:38:35
	 */
	public void uploadFile(CloudFileShare share, String itemName, String filePathName, String outputName) {

		// 上载文件的第一步是获取对文件所在的目录的引用。为此，你需要调用共享对象的 getRootDirectoryReference
		// 方法。

		// Get a reference to the root directory for the share.
		CloudFileDirectory leafDir;
		try {
			leafDir = share.getRootDirectoryReference();
			// 现在，你已经有了共享所在的根目录的引用，因此可以使用以下代码来上载文件。
			if (itemName != null && !"".equals(itemName)) {
				leafDir = leafDir.getDirectoryReference(itemName);
			}
			CloudFile cloudFile = leafDir.getFileReference(outputName);
			cloudFile.uploadFromFile(filePathName);
		} catch (IOException | StorageException | URISyntaxException e) {
			logger.error("文件上传异常。", e);
			e.printStackTrace();
		}

	}

	/**
	 * 上传文件
	 *
	 * @param shareName
	 *            共享名
	 * @param itemName
	 *            目录名
	 * @param filePathName
	 *            待上传文件路径
	 * @param outputName
	 *            文件名
	 * @Author Yu Jinshui
	 * @createTime 2016年4月1日 下午6:39:28
	 */
	public void uploadFile(String shareName, String itemName, String filePathName, String outputName) {
		try {
			CloudFileShare share = getShare(shareName);
			uploadFile(share, itemName, filePathName, outputName);
		} catch (URISyntaxException | StorageException e) {
			logger.error("上传失败 ,共享名不可为空", e);
			e.printStackTrace();
		}
	}

	/**
	 * 
	 *
	 * @param shareName
	 * @param dirName
	 * @param fileName
	 * @Author Yu Jinshui
	 * @createTime 2016年4月1日 下午7:08:45
	 * @deprecated
	 */
	public void uploadFile_NotCheckDir(String shareName, String itemName, String dirName, String fileName) {
		try {
			CloudFileShare share = createShare(shareName);
			uploadFile(share, itemName, dirName, fileName);
		} catch (URISyntaxException | StorageException e) {
			logger.error("上传失败", e);
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
	public CloudFile downloadFile(CloudFileShare share, String dirName, String fileName)
			throws StorageException, URISyntaxException, IOException {
		// Get a reference to the root directory for the share.
		CloudFileDirectory rootDir = share.getRootDirectoryReference();

		CloudFile file;
		// Get a reference to the directory that contains the file
		if (dirName == null || "".equals(dirName)) {// dirName为空，则查找根共享下的文件
			file = rootDir.getFileReference(fileName);
		} else {
			CloudFileDirectory sampleDir = rootDir.getDirectoryReference(dirName);
			// Get a reference to the file you want to download
			file = sampleDir.getFileReference(fileName);
		}

		// Write the contents of the file to the console.
		// System.out.println(file.downloadText());
		// System.out.println(file.downloadText());
		return file;
	}

	/**
	 * 删除文件
	 *
	 * @param share
	 * @param dirName
	 *            目录名
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws StorageException
	 * @throws URISyntaxException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月1日 下午4:02:28
	 */
	public boolean deleteFile(CloudFileShare share, String dirName, String fileName)
			throws StorageException, URISyntaxException {
		// Get a reference to the root directory for the share.
		CloudFileDirectory rootDir = share.getRootDirectoryReference();

		// Get a reference to the directory where the file to be deleted is in
		CloudFile file;
		if (dirName == null || "".equals(dirName)) {// 没有目录，则直接删除共享下面的文件
			file = rootDir.getFileReference(fileName);
		} else {
			CloudFileDirectory containerDir = rootDir.getDirectoryReference(dirName);
			file = containerDir.getFileReference(fileName);
		}
		return file.deleteIfExists();
	}

	public void deleteDir(){
		
	}
}
