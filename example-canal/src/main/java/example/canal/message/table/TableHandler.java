package example.canal.message.table;

import com.alibaba.otter.canal.protocol.CanalEntry;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 表处理程序
 *
 * @author knight
 * @since 2023/05/30
 */
public interface TableHandler {

	/**
	 * 表名
	 * @return {@link String}
	 */
	String tableName();

	/**
	 * 目标表名
	 * @return {@link String}
	 */
	default String targetTableName(String originTableName) {
		return tableName();
	}

	/**
	 * 列名称映射
	 * @return {@link Map}<{@link String}, {@link String}>
	 */
	default Map<String, String> columnNameMapping() {
		return Collections.emptyMap();
	}

	/**
	 * 插入sql
	 * @param entry 条目
	 * @param rowDataList 行数据列表
	 * @return {@link String}
	 */
	String insertSql(String originTableName, CanalEntry.Entry entry, List<CanalEntry.RowData> rowDataList);

	/**
	 * 更新sql
	 * @return {@link String}
	 */
	String updateSql(String originTableName, CanalEntry.Entry entry, List<CanalEntry.RowData> rowDataList);

	/**
	 * 删除sql
	 * @return {@link String}
	 */
	String deleteSql(String originTableName, CanalEntry.Entry entry, List<CanalEntry.RowData> rowDataList);

}
