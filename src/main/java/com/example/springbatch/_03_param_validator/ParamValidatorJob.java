package com.example.springbatch._03_param_validator;

import com.example.springbatch._01_task.HelloJob;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Map;

@SpringBootApplication
@EnableBatchProcessing
public class ParamValidatorJob {
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
                //方案一：使用chunkcontext
                Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
                System.out.println("----paramjob:name-----"+jobParameters.get("name"));
                return RepeatStatus.FINISHED;
            }
        };
    }

    //构建step对象
    @Bean
    public Step step1(){
        return stepBuilderFactory.get("step1").tasklet(tasklet()).build();
    }
    //自定义参数校验器
    @Bean
    public NameParamValidator nameParamValidator(){
        return new NameParamValidator();
    }
    //默认参数校验器
    @Bean
    public DefaultJobParametersValidator defaultJobParametersValidator(){
        DefaultJobParametersValidator validator = new DefaultJobParametersValidator();
        //必须参数
        validator.setRequiredKeys(new String[]{"name"});
        //可选参数
        validator.setOptionalKeys(new String[]{"age"});
        return validator;
    }
    //复合参数校验
    @Bean
    public CompositeJobParametersValidator compositeJobParametersValidator() throws Exception {
        CompositeJobParametersValidator validator = new CompositeJobParametersValidator();
        validator.setValidators(Arrays.asList(nameParamValidator(),defaultJobParametersValidator()));
        validator.afterPropertiesSet();
        return validator;
    }

   /* @Bean
    public Job job(){
        return jobBuilderFactory.get("name-param-validate-job").start(step1())
                .validator(nameParamValidator())//指定参数校验器
                .build();
    }*/
/*
    @Bean
*/
/*    public Job job(){
        return jobBuilderFactory.get("default-name-age-validate-job")
                .start(step1())
                .validator(defaultJobParametersValidator())
                .build();

    }*/

/*    @Bean
    public Job job() throws Exception {
        return jobBuilderFactory.get("default-name-age-validate-job")
                .start(step1())
                .validator(compositeJobParametersValidator())
                .build();
    }*/

    public static void main(String[] args) {
        SpringApplication.run(ParamValidatorJob.class,args);
    }
}
