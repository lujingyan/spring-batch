package com.example.springbatch.config;

import com.example.springbatch.entity.Employee;
import com.example.springbatch.listener.CsvToDBJobListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.File;

@Configuration
@EnableBatchProcessing
@SpringBootApplication
public class CsvToDBJobConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private DataSource dataSource;
    @Value("${job.data.path}")
    private String path;
    @Bean
    public FlatFileItemReader<Employee> cvsToDBItemReader(){
        FlatFileItemReader<Employee> reader = new
                FlatFileItemReaderBuilder<Employee>()
                .name("employeeCSVItemReader")
                .saveState(false) //防止状态被覆盖
                .resource(new PathResource(new File(path,
                        "employee.csv").getAbsolutePath()))
                .delimited()
                .names("id", "name", "age", "sex")
                .targetType(Employee.class)
                .build();
        return reader;
    }
    //数据库写
    @Bean
    public JdbcBatchItemWriter<Employee> cvsToDBItemWriter(){
        JdbcBatchItemWriter<Employee> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(dataSource);
        itemWriter.setItemPreparedStatementSetter(preStatementSetter());
        itemWriter.setSql("insert into employee(id, name, age, sex) values(?,?,?,?)");
        return itemWriter;
    }
    @Bean
    public EmployeePreStatementSetter preStatementSetter(){
        return new EmployeePreStatementSetter();
    }
    @Bean
    public Step csvToDBStep(){
        return stepBuilderFactory.get("csvToDBStep")
                .<Employee, Employee>chunk(10000) //每个块10000个 共50个
                .reader(cvsToDBItemReader())
                .writer(cvsToDBItemWriter())
                .taskExecutor(new SimpleAsyncTaskExecutor()) //多线程读写
                .build();
    }
    //job监听器
    @Bean
    public CsvToDBJobListener csvToDBJobListener(){
        return new CsvToDBJobListener();
    }
    @Bean
    public Job csvToDBJob(){
        return jobBuilderFactory.get("csvToDB-step-job")
                .start(csvToDBStep())
                .incrementer(new RunIdIncrementer()) //保证可以多次执行
                .listener(csvToDBJobListener())
                .build();
    }
}
