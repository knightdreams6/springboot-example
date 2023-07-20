package example.canal.orm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CanalRecord {

	/** 数据库名 */
	private String operationSchemaName;

	/** 表名 */
	private String operationTableName;

	/** 事件类型（INSERT/UPDATE/DELETE） */
	private String eventType;

	/** 事件时间戳 */
	private Long eventTimestamp;

	/** binlog文件名 */
	private String binlogFile;

	/** binlog偏移量 */
	private Long binlogOffset;

	/** sql */
	private String operationSql;

}
