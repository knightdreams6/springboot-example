package example.canal.client;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.Message;
import example.canal.message.MessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Canal TCP客户端
 *
 * @author knight
 * @since 2023/05/16
 */
@Slf4j
@RequiredArgsConstructor
public class TcpCanalClient implements CanalClient {

	/**
	 * 连接器
	 */
	private final CanalConnector connector;

	/**
	 * 消息处理程序
	 */
	private final MessageHandler<Message> messageHandler;

	/**
	 * 标识
	 */
	private volatile boolean flag;

	/**
	 * 工作线程
	 */
	private Thread workThread;

	@Override
	public void start() {
		log.info("start tcp canal client");
		workThread = new Thread(this::process);
		workThread.setName("canal-client-thread");
		workThread.start();
		flag = true;
	}

	@Override
	public void stop() {
		flag = false;
		if (null != workThread) {
			workThread.interrupt();
		}
		connector.disconnect();
	}

	@Override
	public void process() {
		// 打开连接
		connector.connect();
		// 订阅数据库表，来覆盖服务端初始化时的设置
		connector.subscribe("dolphin_tcm\\..*");
		while (flag) {
			// 回滚到未进行ack的地方，下次fetch的时候，可以从最后一个没有ack的地方开始拿
			connector.rollback();
			// 获取指定数量的数据
			Message message = connector.getWithoutAck(1000, 5L, TimeUnit.SECONDS);
			long batchId = message.getId();
			try {
				if (batchId == -1 || message.getEntries().isEmpty()) {
					log.info("未获取到变更消息");
					continue;
				}

				if (log.isDebugEnabled()) {
					log.debug("获取消息 {}", message);
				}
				boolean flag = messageHandler.handlerMessage(message);
				// 如果消息处理成功则进行确认
				if (flag) {
					// 进行 batch id 的确认
					connector.ack(batchId);
				}
				else {
					// 处理失败，回滚
					connector.rollback(batchId);
				}
			}
			catch (Exception e) {
				log.error("canal client 异常", e);
				// 处理失败，回滚
				connector.rollback(batchId);
			}
		}
	}

}
