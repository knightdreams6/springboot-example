package example.canal.message.table;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.otter.canal.protocol.CanalEntry;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;

import java.sql.Types;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 抽象的表处理程序
 *
 * @author knight
 * @since 2023/05/29
 */
@Slf4j
public abstract class AbstractTableHandler implements TableHandler {

	/**
	 * 格式化列值
	 * @param column 列
	 * @return {@link Object}
	 */
	protected Object formatColumnValue(CanalEntry.Column column) {
		switch (column.getSqlType()) {
		case Types.BIT, Types.BOOLEAN -> {
			return Convert.convert(Boolean.class, column.getValue(), false);
		}
		case Types.TINYINT, Types.SMALLINT, Types.INTEGER -> {
			return Convert.convert(Integer.class, column.getValue(), 0);
		}
		case Types.BIGINT -> {
			return Convert.convert(Long.class, column.getValue(), 0L);
		}
		case Types.FLOAT -> {
			return Convert.convert(Float.class, column.getValue(), 0.0f);
		}
		case Types.DOUBLE, Types.NUMERIC, Types.DECIMAL -> {
			return Convert.convert(Double.class, column.getValue(), 0.0d);
		}
		case Types.DATE, Types.TIME, Types.TIMESTAMP, Types.OTHER -> {
			if (StrUtil.isBlank(column.getValue())) {
				return "NULL";
			}
			else {
				return "'" + column.getValue() + "'";
			}
		}
		default -> {
			// 单引号转义
			String value = StrUtil.replace(column.getValue(), "'", "''");
			return "'" + value + "'";
		}
		}
	}

	/**
	 * 将一串名称用反引号括起来。
	 * @param name 名称
	 * @return 反引号括起来的名称
	 */
	public String quoteName(String name) {
		return "`" + name + "`";
	}

	/**
	 * 列名转换
	 * @param originColumnName 原列名
	 * @return {@link String}
	 */
	private String columnNameConvert(String originColumnName) {
		return columnNameMapping().getOrDefault(originColumnName, originColumnName);
	}

	/**
	 * 获取主键列
	 * @param columns 列
	 * @return {@link CanalEntry.Column}
	 */
	private CanalEntry.Column getPkColumn(List<CanalEntry.Column> columns) {
		return columns.stream().filter(CanalEntry.Column::getIsKey).findFirst().orElseThrow();
	}

	@Override
	public String insertSql(String originTableName, CanalEntry.Entry entry, List<CanalEntry.RowData> rowDataList) {
		StringBuilder sql = new StringBuilder();
		for (CanalEntry.RowData rowData : rowDataList) {
			// 构建INSERT语句
			sql.append("INSERT INTO ");
			// 表名
			sql.append(quoteName(targetTableName(originTableName)));
			sql.append("(");

			List<CanalEntry.Column> columns = rowData.getAfterColumnsList();
			// 过滤掉不存在的
			if (CollUtil.isNotEmpty(columnNameMapping())) {
				// 原始列名
				Set<String> originColumnNames = columnNameMapping().keySet();

				columns = columns.stream().filter(column -> CollUtil.contains(originColumnNames, column.getName()))
						.collect(Collectors.toList());

				if (CollUtil.isEmpty(columns)) {
					return null;
				}
			}

			// 获取所有列名
			for (CanalEntry.Column column : columns) {
				sql.append(columnNameConvert(column.getName()));
				sql.append(",");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(") VALUES (");

			// 获取所有列的值
			for (CanalEntry.Column column : columns) {
				sql.append(formatColumnValue(column));
				sql.append(",");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(");");
		}

		// 解析该语句，确保可执行
		String sqlStr = sql.toString();
		try {
			Insert insertSql = (Insert) CCJSqlParserUtil.parse(sqlStr);
			return insertSql.toString();
		}
		catch (Exception e) {
			log.error("AbstractTableHandler insert sql parse fail. {}", e.getMessage());
			return null;
		}
	}

	@Override
	public String updateSql(String originTableName, CanalEntry.Entry entry, List<CanalEntry.RowData> rowDataList) {
		StringBuilder sql = new StringBuilder();
		for (CanalEntry.RowData rowData : rowDataList) {
			// 构建UPDATE语句
			sql.append("UPDATE ");
			// 表名
			sql.append(quoteName(targetTableName(originTableName)));
			sql.append(" SET ");

			List<CanalEntry.Column> columns = rowData.getAfterColumnsList();
			// 过滤掉不存在的
			if (CollUtil.isNotEmpty(columnNameMapping())) {
				// 原始列名
				Set<String> originColumnNames = columnNameMapping().keySet();

				columns = columns.stream().filter(column -> CollUtil.contains(originColumnNames, column.getName()))
						.collect(Collectors.toList());

				if (CollUtil.isEmpty(columns)) {
					return null;
				}
			}

			// 获取变更前后的数据
			for (CanalEntry.Column column : columns) {
				// 如果列值有变更
				if (column.getUpdated()) {
					sql.append(columnNameConvert(column.getName()));
					sql.append("=");
					sql.append(formatColumnValue(column));
					sql.append(",");
				}
			}

			// 去掉最后一个逗号
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" WHERE ");

			// 构建WHERE条件
			CanalEntry.Column pkColumn = getPkColumn(rowData.getBeforeColumnsList());
			sql.append(columnNameConvert(pkColumn.getName()));
			sql.append("=");
			sql.append(formatColumnValue(pkColumn));
			sql.append(";");
		}
		// 解析该语句，确保可执行
		String sqlStr = sql.toString();
		try {
			Update updateSql = (Update) CCJSqlParserUtil.parse(sqlStr);
			return updateSql.toString();
		}
		catch (Exception e) {
			log.error("AbstractTableHandler update sql parse fail. {}", e.getMessage());
			return null;
		}
	}

	@Override
	public String deleteSql(String originTableName, CanalEntry.Entry entry, List<CanalEntry.RowData> rowDataList) {
		StringBuilder sql = new StringBuilder();

		for (CanalEntry.RowData rowData : rowDataList) {
			// 构建DELETE语句
			sql.append("DELETE FROM ");
			// 表名
			sql.append(quoteName(targetTableName(originTableName)));
			sql.append(" WHERE ");

			// 构建WHERE条件
			CanalEntry.Column pkColumn = getPkColumn(rowData.getBeforeColumnsList());
			sql.append(columnNameConvert(pkColumn.getName()));
			sql.append("=");
			sql.append(formatColumnValue(pkColumn));
			sql.append(";");
		}
		// 解析该语句，确保可执行
		String sqlStr = sql.toString();
		try {
			Delete deleteSql = (Delete) CCJSqlParserUtil.parse(sqlStr);
			return deleteSql.toString();
		}
		catch (Exception e) {
			log.error("AbstractTableHandler delete sql parse fail. {}", e.getMessage());
			return null;
		}
	}

}
