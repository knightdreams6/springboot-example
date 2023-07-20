package example.canal.config;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.client.rabbitmq.RabbitMQCanalConnector;
import com.alibaba.otter.canal.protocol.Message;
import example.canal.client.RabbitmqCanalClient;
import example.canal.client.TcpCanalClient;
import example.canal.message.JdbcMessageHandler;
import example.canal.message.MessageHandler;
import example.canal.message.table.TableHandleContext;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

/**
 * canal客户端配置
 *
 * @author knight
 * @since 2023/05/17
 */
@Configuration
public class CanalClientAutoConfiguration {

	@Bean
	public MessageHandler<Message> jdbcMessageHandler(TableHandleContext tableHandleContext) {
		return new JdbcMessageHandler(tableHandleContext);
	}

	@Bean(initMethod = "start", destroyMethod = "stop")
	@ConditionalOnProperty(value = "canal.mode", havingValue = "tcp")
	public TcpCanalClient tcpCanalClient(MessageHandler<Message> jdbcMessageHandler) {
		CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress("192.168.50.44", 11111),
				"example", "", "");
		return new TcpCanalClient(connector, jdbcMessageHandler);
	}

	@Bean(initMethod = "start", destroyMethod = "stop")
	@ConditionalOnProperty(value = "canal.mode", havingValue = "rabbitMQ")
	public RabbitmqCanalClient rabbitmqCanalClient(MessageHandler<Message> jdbcMessageHandler,
			RabbitProperties rabbitProperties) {
		RabbitMQCanalConnector connector = new RabbitMQCanalConnector(rabbitProperties.getHost(),
				rabbitProperties.getVirtualHost(), RabbitConstant.CanalQueue, "", "", rabbitProperties.getUsername(),
				rabbitProperties.getPassword(), null, false);
		return new RabbitmqCanalClient(connector, jdbcMessageHandler);
	}

	/**
	 * 队列
	 */
	@Bean
	@ConditionalOnProperty(value = "canal.mode", havingValue = "rabbitMQ")
	public Queue canalQueue() {
		return new Queue(RabbitConstant.CanalQueue, true);
	}

	/**
	 * 交换机，这里使用直连交换机
	 */
	@Bean
	@ConditionalOnProperty(value = "canal.mode", havingValue = "rabbitMQ")
	DirectExchange canalExchange() {
		return new DirectExchange(RabbitConstant.CanalExchange, true, false);
	}

	/**
	 * 绑定交换机和队列，并设置匹配键
	 */
	@Bean
	@ConditionalOnProperty(value = "canal.mode", havingValue = "rabbitMQ")
	Binding bindingCanal() {
		return BindingBuilder.bind(canalQueue()).to(canalExchange()).with(RabbitConstant.CanalRouting);
	}

}
