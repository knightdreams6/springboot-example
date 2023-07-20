package example.canal.message;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import example.canal.message.table.TableHandleContext;
import example.canal.message.table.TableHandler;
import example.canal.orm.CanalRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 消息处理程序
 *
 * @author knight
 * @since 2023/05/16
 */
@Slf4j
@RequiredArgsConstructor
public class JdbcMessageHandler implements MessageHandler<Message> {

	/**
	 * 表处理上下文
	 */
	private final TableHandleContext tableHandleContext;

	@Override
	public boolean handlerMessage(Message message) {
		// 过滤出数据变更类型记录
		List<CanalEntry.Entry> rowDataEntryList = message.getEntries().stream()
				.filter(entry -> Objects.equals(entry.getEntryType(), CanalEntry.EntryType.ROWDATA)).toList();
		// 不进行处理
		if (CollUtil.isEmpty(rowDataEntryList)) {
			log.info("未获取到数据变更消息");
			return true;
		}

		List<CanalRecord> canalRecords = rowDataEntryList.stream().map(entry -> {
			CanalEntry.Header header = entry.getHeader();

			TableHandler tableHandler = tableHandleContext.get(header.getTableName());

			CanalRecord bean = CanalRecord.builder().operationSchemaName(header.getSchemaName())
					.operationTableName(header.getTableName()).eventType(header.getEventType().toString())
					.eventTimestamp(header.getExecuteTime()).binlogFile(header.getLogfileName())
					.binlogOffset(header.getLogfileOffset()).build();

			CanalEntry.RowChange rowChange;
			try {
				rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
			}
			catch (Exception e) {
				log.error("CanalEntry.RowChange.parseFrom data:{} error: {}", entry, e.getMessage());
				return null;
			}
			CanalEntry.EventType eventType = rowChange.getEventType();

			// 删除
			if (eventType == CanalEntry.EventType.DELETE) {
				bean.setOperationSql(tableHandler.deleteSql(header.getTableName(), entry, rowChange.getRowDatasList()));
			}
			// 新增
			else if (eventType == CanalEntry.EventType.INSERT) {
				bean.setOperationSql(tableHandler.insertSql(header.getTableName(), entry, rowChange.getRowDatasList()));
			}
			// 更新
			else if (eventType == CanalEntry.EventType.UPDATE) {
				bean.setOperationSql(tableHandler.updateSql(header.getTableName(), entry, rowChange.getRowDatasList()));
			}
			// 不处理其它事件
			else {
				return null;
			}
			return bean;
		}).filter(Objects::nonNull).collect(Collectors.toList());

		if (CollUtil.isEmpty(canalRecords)) {
			return true;
		}
		log.info("MessageHandler #handlerMessage");
		log.info("接收到消息--- {}", canalRecords);
		return true;
	}

}
