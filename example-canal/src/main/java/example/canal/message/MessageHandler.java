package example.canal.message;

/**
 * 消息处理程序
 *
 * @author knight
 * @since 2023/05/16
 */
public interface MessageHandler<T> {

	/**
	 * 消息处理
	 * @param t t
	 * @return boolean 消息是否处理成功
	 */
	boolean handlerMessage(T t);

}
