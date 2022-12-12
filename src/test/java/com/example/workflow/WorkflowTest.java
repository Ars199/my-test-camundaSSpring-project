package com.example.workflow;

/*import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.spring.boot.starter.test.helper.AbstractProcessEngineRuleTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.test.context.junit4.SpringRunner;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class WorkflowTest extends AbstractProcessEngineRuleTest {

  @Autowired
  public RuntimeService runtimeService;

  @Test
  public void shouldExecuteHappyPath() {
    // given
    String processDefinitionKey = "my-test-camundaSSpring-project-process";

    // when
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);

    // then
    assertThat(processInstance).isStarted()
        .task()
        .hasDefinitionKey("say-hello")
        .hasCandidateUser("demo")
        .isNotAssigned();
  }

}*/

import io.camunda.zeebe.client.api.response.DeploymentEvent;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.process.test.assertions.BpmnAssert;
import io.camunda.zeebe.process.test.assertions.ProcessInstanceAssert;
import io.camunda.zeebe.process.test.extensions.ZeebeProcessTest;
import io.camunda.zeebe.process.test.testengine.InMemoryEngine;
import io.camunda.zeebe.process.test.testengine.RecordStreamSource;
import org.junit.jupiter.api.Test;
import io.camunda.zeebe.client.ZeebeClient;

@ZeebeProcessTest
public class WorkflowTest {
    private InMemoryEngine engine;
    private ZeebeClient client;
    private RecordStreamSource recordStreamSource;

    private void initDeployment(){
        client.newDeployCommand()
                .addResourceFromClasspath("my-test-camundaSSpring-project-process.bpmn")
                .send()
                .join();
    }

    private ProcessInstanceAssert initProcessInstanceStart(){
        ProcessInstanceEvent event = client.newCreateInstanceCommand()
                .bpmnProcessId("SendNotification")
                .latestVersion()
                .send()
                .join();
        return BpmnAssert.assertThat(event);
    }

    @Test
    public void testDeployment() {
        //When
        DeploymentEvent event = client.newDeployCommand()
                .addResourceFromClasspath("my-test-camundaSSpring-project-process.bpmn")
                .send()
                .join();

        //Then
        BpmnAssert.assertThat(event);
    }
}

