package com.example.springbatch._06_context;

import com.example.springbatch._05_job_listener.JobStateListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Map;

@SpringBootApplication
@EnableBatchProcessing
public class ExecutionContextJob {
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
    public Tasklet tasklet1(){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                //步骤
                //通过上下文执行对象获取设置参数
                ExecutionContext stepEC = chunkContext.getStepContext().getStepExecution().getExecutionContext();
                stepEC.put("key-step1-step","value-step1-step");
                //作业
                ExecutionContext jobEC = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
                jobEC.put("key-step1-job","value-step1-job");

                return RepeatStatus.FINISHED;
            }
        };
    }
    @Bean
    public Tasklet tasklet2(){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                ExecutionContext stepEC = chunkContext.getStepContext().getStepExecution().getExecutionContext();
                System.err.println(stepEC.get("key-step1-step"));
                //作业
                ExecutionContext jobEC = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
                System.err.println(stepEC.get("key-step1-job"));
                return RepeatStatus.FINISHED;
            }
        };
    }

    //构建step对象
    @Bean
    public Step step1(){
        return stepBuilderFactory.get("step1").tasklet(tasklet1()).build();
    }

    @Bean
    public Step step2(){
        return stepBuilderFactory.get("step2").tasklet(tasklet2()).build();
    }

    @Bean
    public JobStateListener jobStateListener(){
        return new JobStateListener();
    }
    @Bean
    public Job job(){
        return jobBuilderFactory.get("increment-params-job")
                .start(step1())
                .next(step2())
                .incrementer(new RunIdIncrementer())
                .build();
        //job之间可以共享，step之间不能共享
    }


    public static void main(String[] args) {
        SpringApplication.run(ExecutionContextJob.class,args);
    }
}
