package example.canal.message.table;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 表处理上下文
 *
 * @author knight
 * @since 2023/06/05
 */
@Component
@RequiredArgsConstructor
public class TableHandleContext implements InitializingBean {

	/**
	 * 表处理程序
	 */
	private final List<TableHandler> tableHandlers;

	/**
	 * 表名处理与处理程序Map
	 */
	private Map<String, TableHandler> tableNameHandleMap = new HashMap<>();

	/**
	 * 根据表名获取表处理程序
	 * @param tableName 表名
	 * @return {@link TableHandler}
	 */
	public TableHandler get(String tableName) {
		return tableNameHandleMap.getOrDefault(tableName, new DefaultTableHandler());
	}

	@Override
	public void afterPropertiesSet() {
		tableNameHandleMap = tableHandlers.stream()
				.collect(Collectors.toMap(TableHandler::tableName, Function.identity()));
	}

}
