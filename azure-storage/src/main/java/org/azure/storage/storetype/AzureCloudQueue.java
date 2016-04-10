package org.azure.storage.storetype;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.EnumSet;

import org.azure.storage.AzureConfig;
import org.azure.storage.config.AzureCloudAccount;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.queue.CloudQueue;
import com.microsoft.azure.storage.queue.CloudQueueClient;
import com.microsoft.azure.storage.queue.CloudQueueMessage;
import com.microsoft.azure.storage.queue.MessageUpdateFields;

/**
 * 云存储 - queue
 * 
 * @author Yu Jinshui
 * @createTime 2016年4月3日 下午7:35:40
 */
public class AzureCloudQueue {
	private CloudQueueClient queueClient;

	/**
	 * 初始化账户配置信息，得到客户端
	 * 
	 * @param config
	 */
	public AzureCloudQueue(AzureConfig config) {
		try {
			queueClient = AzureCloudAccount.getCloudStorageAccount(config).createCloudQueueClient();
		} catch (InvalidKeyException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建队列【必须小写名称，否则报错】
	 * <p>
	 * 利用 CloudQueueClient 对象，可以获取队列的引用对象。以下代码将创建 CloudQueueClient
	 * 对象。（注意：还有其他方式来创建 CloudStorageAccount 对象；有关详细信息，请参阅 Azure 存储客户端 SDK 参考中的
	 * CloudStorageAccount。） <br>
	 * 使用 CloudQueueClient 对象获取对你要使用的队列的引用。如果队列不存在，你可以创建它。
	 *
	 * @param queueName
	 * @return
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月10日 下午12:58:35
	 */
	public CloudQueue createQueue(String queueName) throws URISyntaxException, StorageException {
		CloudQueue queue = getQueue(queueName);
		queue.createIfNotExists();
		return queue;
	}

	/**
	 * 获取队列名
	 *
	 * @param queueName
	 * @return
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月10日 下午12:58:10
	 */
	public CloudQueue getQueue(String queueName) throws URISyntaxException, StorageException {
		return queueClient.getQueueReference(queueName);
	}

	/**
	 * 向队列里面添加消息内容
	 *
	 * @param queueName
	 *            队列名称
	 * @param messageContent
	 *            消息内容
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月10日 下午1:03:03
	 */
	public void addMessage(String queueName, String messageContent) throws URISyntaxException, StorageException {
		CloudQueue queue = createQueue(queueName);// 以官网代码规范为准
		CloudQueueMessage message = new CloudQueueMessage(messageContent);
		queue.addMessage(message);
	}

	/**
	 * 扫视下一条消息
	 * <p>
	 * 通过调用 peekMessage，你可以扫视队列前面的消息，而不会从队列中删除它。
	 *
	 * @param queueName
	 *            队列名称
	 * @return
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月10日 下午1:05:40
	 */
	public CloudQueueMessage peekMessage(String queueName) throws URISyntaxException, StorageException {
		CloudQueue queue = getQueue(queueName);// 以官网代码规范为准
		CloudQueueMessage peekedMessage = queue.peekMessage();
		return peekedMessage;// .getMessageContentAsString();
	}

	/**
	 * 返回全部消息列表
	 *
	 * @param queueName
	 *            队列名称
	 * @return
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月10日 下午2:26:22
	 */
	public Iterable<CloudQueueMessage> messageList(String queueName) throws URISyntaxException, StorageException {
		CloudQueue queue = getQueue(queueName);// 以官网代码规范为准
		final int MAX_NUMBER_OF_MESSAGES_TO_PEEK = 32;
		return queue.retrieveMessages(MAX_NUMBER_OF_MESSAGES_TO_PEEK, 1, null, null);

	}

	/**
	 * 更改已排队消息的内容
	 *
	 * @param messageContent
	 *            待更改内容
	 * @param updateMessage
	 *            更改后的内容
	 * @throws StorageException
	 * @throws URISyntaxException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月10日 下午2:35:52
	 */
	public void updateMessage(String queueName, String messageContent, String updateMessage)
			throws URISyntaxException, StorageException {
		CloudQueue queue = getQueue(queueName);// 以官网代码规范为准
		final int MAX_NUMBER_OF_MESSAGES_TO_PEEK = 32;
		Iterable<CloudQueueMessage> queuelist = queue.retrieveMessages(MAX_NUMBER_OF_MESSAGES_TO_PEEK, 1, null, null);
		// Loop through the messages in the queue
		for (CloudQueueMessage message : queuelist) {
			// Check for a specific string.
			if (message.getMessageContentAsString().contains(messageContent)) {
				// Modify the content of the first matching message.
				message.setMessageContent(updateMessage);
				// Set it to be visible in 30 seconds.
				EnumSet<MessageUpdateFields> updateFields = EnumSet.of(MessageUpdateFields.CONTENT,
						MessageUpdateFields.VISIBILITY);
				// Update the message.
				queue.updateMessage(message, 30, updateFields, null, null);
				break;
			}
		}
	}

	/**
	 * 获取队列长度/消息数
	 * <p>
	 * 你可以获取队列中消息的估计数。downloadAttributes
	 * 方法会询问队列服务一些当前值，包括队列中消息的计数。此计数仅为近似值，因为只能在队列服务响应您的请求后添加或删除消息。
	 * getApproximateMessageCount 方法返回通过调用 downloadAttributes
	 * 检索到的最后一个值，而不会调用队列服务。
	 *
	 * @param queueName
	 *            队列名称
	 * @return
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月10日 下午4:09:21
	 */
	public Long getQueueLength(String queueName) throws URISyntaxException, StorageException {
		CloudQueue queue = getQueue(queueName);
		// Download the approximate message count from the server.
		queue.downloadAttributes();
		// Retrieve the newly cached approximate message count.
		return queue.getApproximateMessageCount();
	}

	/**
	 * 取消对下一条消息的排队
	 * <p>
	 * 你的代码通过两个步骤来取消对队列中某条消息的排队。在调用 retrieveMessage 时，你将获得队列中的下一条消息。从
	 * retrieveMessage 返回的消息变得对从此队列读取消息的任何其他代码不可见。默认情况下，此消息将持续 30
	 * 秒不可见。若要从队列中删除消息，你还必须调用
	 * deleteMessage。此删除消息的两步过程可确保，如果你的代码因硬件或软件故障而无法处理消息，则你的代码的其他实例可以获取相同消息并重试。
	 * 你的代码在处理消息后会立即调用 deleteMessage。
	 *
	 * @param queueName
	 * @throws URISyntaxException
	 * @throws StorageException
	 * @Author Yu Jinshui
	 * @createTime 2016年4月10日 下午4:21:59
	 */
	public void deleteMessage(String queueName) throws URISyntaxException, StorageException {
		CloudQueue queue = getQueue(queueName);

		// Retrieve the first visible message in the queue.
		CloudQueueMessage retrievedMessage = queue.retrieveMessage();

		if (retrievedMessage != null) {
			// Process the message in less than 30 seconds, and then delete the
			// message.
			queue.deleteMessage(retrievedMessage);
		}
	}
	// TODO 用于取消对消息进行排队的其他选项
}
