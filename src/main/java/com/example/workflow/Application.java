package com.example.workflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;

@SpringBootApplication
@EnableZeebeClient
public class Application {
    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

    @ZeebeWorker(type = "serviceTask")     //согласно https://blog.bernd-ruecker.com/writing-good-workers-for-camunda-cloud-61d322cad862 на каждый Append Task создается класс с воркером, здесь легковесный пример
    public void handleJobFoo(final JobClient client, final ActivatedJob job) {
        client.newCompleteCommand(job.getKey())
                .variables("{}")
                .send()
                .exceptionally(throwable -> {
                    throw new RuntimeException("Could not complete job " + job, throwable);
                });
    }

}