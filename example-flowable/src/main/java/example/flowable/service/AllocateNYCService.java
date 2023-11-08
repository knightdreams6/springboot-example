package example.flowable.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component("AllocateNYCService")
@RequiredArgsConstructor
public class AllocateNYCService implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) {
		log.info("allocate NYC: {}", execution.getVariable("clientName", String.class));
	}

}
