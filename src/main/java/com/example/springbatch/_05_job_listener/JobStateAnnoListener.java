package com.example.springbatch._05_job_listener;


import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
/*
* 作业监听器：用注解方式生成
* * */
public class JobStateAnnoListener {
    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("作业执行前的状态"+jobExecution.getStatus());
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        System.out.println("作业执行后的状态"+jobExecution.getStatus());
    }
}
