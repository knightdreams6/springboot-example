package example.canal.client;

/**
 * Canal客户端
 *
 * @author knight
 * @since 2023/05/16
 */
public interface CanalClient {

	/**
	 * 开始
	 */
	void start();

	/**
	 * 停止
	 */
	void stop();

	/**
	 * 处理
	 */
	void process();

}
