package example.canal.message.table;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认的表处理程序
 *
 * @author knight
 * @since 2023/05/29
 */
@Slf4j
public class DefaultTableHandler extends AbstractTableHandler {

	@Override
	public String tableName() {
		return "DEFAULT";
	}

	@Override
	public String targetTableName(String originTableName) {
		return originTableName;
	}

}
