package example.canal.client;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.otter.canal.client.rabbitmq.RabbitMQCanalConnector;
import com.alibaba.otter.canal.protocol.Message;
import example.canal.message.MessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Canal RabbitMq客户端
 *
 * @author knight
 * @since 2023/05/17
 */
@Slf4j
@RequiredArgsConstructor
public class RabbitmqCanalClient implements CanalClient {

	/**
	 * 连接器
	 */
	private final RabbitMQCanalConnector connector;

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
		log.info("start mq canal client");
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
		connector.unsubscribe();
		connector.disconnect();
	}

	@Override
	public void process() {
		// 打开连接
		connector.connect();
		// 订阅数据库表，来覆盖服务端初始化时的设置
		connector.subscribe("dolphin_tcm\\..*");
		while (flag) {
			// 回滚到未进行ack的地方
			connector.rollback();
			List<Message> messages = connector.getListWithoutAck(5L, TimeUnit.SECONDS);
			if (CollUtil.isEmpty(messages)) {
				log.debug("未获取到变更消息");
				continue;
			}

			for (Message message : messages) {
				try {
					if (log.isDebugEnabled()) {
						log.debug("获取消息 {}", message);
					}
					boolean flag = messageHandler.handlerMessage(message);
					// 如果消息处理成功则进行确认
					if (flag) {
						// 进行 batch id 的确认
						connector.ack();
					}
					else {
						// 处理失败，回滚
						connector.rollback();
					}
				}
				catch (Exception e) {
					log.error("canal client 异常", e);
					// 处理失败，回滚
					connector.rollback();
				}
			}
		}
	}

}
