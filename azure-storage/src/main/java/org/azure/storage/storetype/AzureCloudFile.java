package org.azure.storage.storetype;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.azure.storage.AzureCloudAccount;
import org.azure.storage.AzureConfig;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.file.CloudFile;
import com.microsoft.azure.storage.file.CloudFileClient;
import com.microsoft.azure.storage.file.CloudFileDirectory;
import com.microsoft.azure.storage.file.CloudFileShare;
import com.microsoft.azure.storage.file.FileSharePermissions;
import com.microsoft.azure.storage.file.ListFileItem;
import com.microsoft.azure.storage.file.SharedAccessFilePermissions;
import com.microsoft.azure.storage.file.SharedAccessFilePolicy;

/**
 * 云存储 - file
 * 
 * @author Yu Jinshui
 * @createTime 2016年4月2日 下午4:44:46
 */
public class AzureCloudFile {

	private CloudFileClient fileClient;

	public AzureCloudFile(AzureConfig config) {
		try {
			fileClient = AzureCloudAccount.getCloudStorageAccount(config).createCloudFileClient();
		} catch (InvalidKeyException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取共享名
	 * 
	 * @see org.azure.storage.AzureCloud#getShare(java.lang.String)
	 */

	public CloudFileShare getShare(String shareName) throws URISyntaxException, StorageException {
		CloudFileShare share;
		share = fileClient.getShareReference(shareName);
		// 实际创建共享时，请使用 CloudFileShare 对象的 createIfNotExists 方法。

		// 而在目前，share 保留对名为 sampleshare 的共享的引用。
		return share;

	}

	/**
	 * // 如何：创建共享<br>
	 * // 文件存储中的所有文件和目录都位于名为 Share<br>
	 * // 的容器内。你的存储帐户可以拥有无数的共享，只要你的帐户容量允许。若要获得共享及其内容的访问权限，你需要使用文件存储客户端。 <br>
	 * // Create the file storage client.<br>
	 * 使用文件存储客户端以后，你就可以获得对共享的引用。
	 * 
	 * @see org.azure.storage.AzureCloud#createShare(java.lang.String)
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
	 * @see org.azure.storage.AzureCloud#createDir(com.microsoft.azure.storage.file.CloudFileShare,
	 *      java.lang.String)
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
	 * 创建共享以及目录名
	 * 
	 * @see org.azure.storage.AzureCloud#createDir(java.lang.String,
	 *      java.lang.String)
	 */

	public CloudFileDirectory createDir(String shareName, String dirName) throws URISyntaxException, StorageException {
		CloudFileShare share = getShare(shareName);
		return createDir(share, dirName);
	}

	/**
	 * 获取目录
	 * 
	 * @see org.azure.storage.AzureCloud#getDir(com.microsoft.azure.storage.file.CloudFileShare,
	 *      java.lang.String)
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
	 * @see org.azure.storage.AzureCloud#getDir(java.lang.String,
	 *      java.lang.String)
	 */

	public CloudFileDirectory getDir(String shareName, String dirName) throws StorageException, URISyntaxException {
		CloudFileShare share = getShare(shareName);
		return getDir(share, dirName);
	}

	/**
	 * 上传文件
	 * 
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @throws IOException
	 * 
	 * @see org.azure.storage.AzureCloud#uploadFile(com.microsoft.azure.storage.file.CloudFileShare,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */

	public void uploadFile(CloudFileShare share, String itemName, String filePathName, String outputName)
			throws StorageException, URISyntaxException, IOException {

		// 上载文件的第一步是获取对文件所在的目录的引用。为此，你需要调用共享对象的 getRootDirectoryReference
		// 方法。

		// Get a reference to the root directory for the share.
		CloudFileDirectory leafDir;
		leafDir = share.getRootDirectoryReference();
		// 现在，你已经有了共享所在的根目录的引用，因此可以使用以下代码来上载文件。
		if (itemName != null && !"".equals(itemName)) {
			leafDir = leafDir.getDirectoryReference(itemName);
		}
		CloudFile cloudFile = leafDir.getFileReference(outputName);
		cloudFile.uploadFromFile(filePathName);

	}

	/**
	 * 上传文件
	 * 
	 * @throws StorageException
	 * @throws URISyntaxException
	 * @throws IOException
	 * 
	 * @see org.azure.storage.AzureCloud#uploadFile(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */

	public void uploadFile(String shareName, String itemName, String filePathName, String outputName)
			throws URISyntaxException, StorageException, IOException {
		CloudFileShare share = getShare(shareName);
		uploadFile(share, itemName, filePathName, outputName);
	}

	/**
	 * // 如何：列出共享中的文件和目录 <br>
	 * // 可以轻松获取共享中文件和目录的列表，只需针对 CloudFileDirectory 引用调用 <br>
	 * // listFilesAndDirectories 即可。该方法将返回你可以对其进行循环访问的 ListFileItem <br>
	 * // 对象的列表。例如，下面的代码将列出根目录中的文件和目录。
	 * 
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @throws MalformedURLException
	 * 
	 * @see org.azure.storage.AzureCloud#fileItemList(com.microsoft.azure.storage.file.CloudFileShare)
	 */
	public List<String> fileItemList(CloudFileShare share)
			throws StorageException, URISyntaxException, MalformedURLException {
		List<String> list = new ArrayList<String>();
		CloudFileDirectory rootDir = share.getRootDirectoryReference();
		listFiles(rootDir, list);
		return list;
	}

	/**
	 * 递归查找全部目录及文件
	 *
	 * @param rootDir
	 * @param list
	 * @throws MalformedURLException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月2日 下午2:27:40
	 */
	private void listFiles(CloudFileDirectory dir, List<String> list) throws MalformedURLException {
		Iterable<ListFileItem> filelist = dir.listFilesAndDirectories();
		Iterator<ListFileItem> it = filelist.iterator();
		while (it.hasNext()) {
			ListFileItem file = it.next();
			list.add(file.getUri().toURL().toString());
			if (file instanceof CloudFileDirectory) {// 属于目录，则继续递归
				listFiles((CloudFileDirectory) file, list);
			}
		}
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

	/**
	 * 目录删除
	 * <p>
	 * 删除目录相当简单，但需注意的是，你不能删除仍然包含有文件或其他目录的目录。
	 *
	 * @param share
	 * @param dirName
	 *            目录名称
	 * @throws StorageException
	 * @throws URISyntaxException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月1日 下午11:07:57
	 */

	public void deleteDir(CloudFileShare share, String dirName) throws StorageException, URISyntaxException {
		// Get a reference to the root directory for the share.
		CloudFileDirectory rootDir = share.getRootDirectoryReference();
		// Get a reference to the directory you want to delete
		CloudFileDirectory containerDir = rootDir.getDirectoryReference(dirName);
		// Delete the directory
		if (containerDir.deleteIfExists()) {
			System.out.println("Directory deleted");
		} else {
			System.out.println("Directory is not exists");
		}
	}

	/**
	 * 目录删除
	 * <p>
	 * 删除目录相当简单，但需注意的是，你不能删除仍然包含有文件或其他目录的目录。
	 *
	 * @param shareName
	 * @param dirName
	 *            目录名
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月3日 下午1:38:33
	 */
	public void deleteDir(String shareName, String dirName) throws URISyntaxException, StorageException {
		CloudFileShare share = getShare(shareName);
		deleteDir(share, dirName);
	}

	/**
	 * 
	 * 尚未研究明白如何使用【权限设置】
	 *
	 * @param shareName
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月3日 下午2:53:17
	 * @deprecated
	 */
	public void createPublicShare(String shareName) throws URISyntaxException, StorageException {// SharedAccessFilePolicy
		CloudFileShare share = getShare(shareName);
		FileSharePermissions sharePermissions = new FileSharePermissions();
		SharedAccessFilePolicy policy = new SharedAccessFilePolicy();

		EnumSet<SharedAccessFilePermissions> enumSet = EnumSet.noneOf(SharedAccessFilePermissions.class);
		enumSet.add(SharedAccessFilePermissions.READ);
		policy.setPermissions(enumSet);

		HashMap<String, SharedAccessFilePolicy> sharedAccessPolicies = new HashMap<String, SharedAccessFilePolicy>();
		sharedAccessPolicies.put("read", policy);
		sharePermissions.setSharedAccessPolicies(sharedAccessPolicies);

		// HashMap<String, SharedAccessFilePolicy> per =
		// sharePermissions.getSharedAccessPolicies();
		// Set<Entry<String, SharedAccessFilePolicy>> set = per.entrySet();
		// for (Entry<String, SharedAccessFilePolicy> s : set) {
		// System.out.println(s.getKey() + "=" + s.getValue());
		// }
		share.uploadPermissions(sharePermissions);

	}

	public static void main(String[] args) {
	}
}
