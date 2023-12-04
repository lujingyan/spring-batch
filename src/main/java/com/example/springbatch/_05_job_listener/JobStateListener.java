package com.example.springbatch._05_job_listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/*
* 自定义作业状态监听器
* 作用：用于记录作业执行前执行后
*  */
public class JobStateListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("作业执行前的状态"+jobExecution.getStatus());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("作业执行后的状态"+jobExecution.getStatus());
    }
}
