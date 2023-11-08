package example.flowable.controller;

import lombok.RequiredArgsConstructor;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.variable.api.persistence.entity.VariableInstance;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequiredArgsConstructor
public class FlowableController {

	/**
	 * 运行时服务
	 */
	private final RuntimeService runtimeService;

	/**
	 * 任务服务
	 */
	private final TaskService taskService;

	/**
	 * 老客户列表
	 */
	private final List<String> oldClientList = new CopyOnWriteArrayList<>();

	public boolean isOldClient(String clientName) {
		return oldClientList.contains(clientName);
	}

	public void addOldClient(String clientName) {
		oldClientList.add(clientName);
	}

	@PostMapping("/start")
	public String start() {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Investment");
		return processInstance.getId();
	}

	@PostMapping("/enterClientDetails")
	public String enterClientDetails(@RequestParam String processInstanceId, @RequestParam String clientName) {
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).taskName("Enter Client Details")
				.singleResult();
		if (Objects.isNull(task)) {
			return "enterClientDetails task not found";
		}
		Map<String, Object> variables = new HashMap<>();
		variables.put("clientName", clientName);
		if (isOldClient(clientName)) {
			variables.put("newClient", "no");
		}
		else {
			variables.put("newClient", "yes");
		}
		taskService.complete(task.getId(), variables);
		return "ok";
	}

	@PostMapping("/preformKYCVerify")
	public String preformKYCVerify(@RequestParam String processInstanceId, @RequestParam boolean flag) {
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId)
				.taskName("Preform KYC Verification").singleResult();
		if (Objects.isNull(task)) {
			return "preformKYCVerify task not found";
		}
		Map<String, Object> variables = new HashMap<>();
		if (flag) {
			variables.put("KYCPass", "yes");
			VariableInstance variableInstance = runtimeService.createVariableInstanceQuery()
					.processInstanceId(processInstanceId).variableName("clientName").singleResult();
			if (Objects.isNull(variableInstance)) {
				return "clientName variable not found";
			}

			addOldClient(variableInstance.getValue().toString());
		}
		else {
			variables.put("KYCPass", "no");
		}
		taskService.complete(task.getId(), variables);
		return "ok";
	}

}
