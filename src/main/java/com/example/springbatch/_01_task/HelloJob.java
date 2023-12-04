package com.example.springbatch._01_task;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableBatchProcessing
@SpringBootApplication
public class HelloJob {
    //作业启动器
    @Autowired
    private JobLauncher jobLauncher;
    //构建job对象
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    //构造step对象
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Bean
    public Tasklet tasklet(){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                System.out.println("hello, spring batch");
                return RepeatStatus.FINISHED;
            }
        };
    }

    //构建step对象
    @Bean
    public Step step1(){
        return stepBuilderFactory.get("step1").tasklet(tasklet()).build();
    }

    @Bean
    public Job job(){
        return jobBuilderFactory.get("hello-job").start(step1()).build();
    }

    public static void main(String[] args) {
        SpringApplication.run(HelloJob.class,args);
    }
}
