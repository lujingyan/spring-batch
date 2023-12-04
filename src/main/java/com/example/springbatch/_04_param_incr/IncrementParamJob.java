package com.example.springbatch._04_param_incr;

import com.example.springbatch._01_task.HelloJob;
import org.springframework.batch.core.Job;
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
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Map;

@SpringBootApplication
@EnableBatchProcessing
public class IncrementParamJob {
    //作业启动器
    @Autowired
    private JobLauncher jobLauncher;
    //构建job对象
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    //构造step对象
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @StepScope
    @Bean
    public Tasklet tasklet(@Value("#{jobParameters['name']}") String valueName){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                //方案一：使用chunkcontext
                Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
                System.out.println("----paramjob-----run.id"+jobParameters.get("run.id"));

                return RepeatStatus.FINISHED;
            }
        };
    }

    //构建step对象
    @Bean
    public Step step1(){
        return stepBuilderFactory.get("step1").tasklet(tasklet(null)).build();
    }

    @Bean
    public DailyTimestampParamIncrementer dailyTimestampParamIncrementer(){
        return new DailyTimestampParamIncrementer();
    }
    @Bean
    public Job job(){
        return jobBuilderFactory.get("increment-params-job")
                .start(step1())
//                .incrementer(new RunIdIncrementer())//自动增长
                .incrementer(dailyTimestampParamIncrementer())//以时间戳增长
                .build();
    }


    public static void main(String[] args) {
        SpringApplication.run(IncrementParamJob.class,args);
    }
}
