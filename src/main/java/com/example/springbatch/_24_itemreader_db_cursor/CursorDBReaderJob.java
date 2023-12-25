package com.example.springbatch._24_itemreader_db_cursor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;

import javax.sql.DataSource;
import java.util.List;
//读user.txt文件封装到对象并打印

@EnableBatchProcessing
@SpringBootApplication
public class CursorDBReaderJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    //job---step-chunk---reader---writer
    @Autowired
    DataSource dataSource;

    @Bean
    public ItemWriter<User> itemWriter(){
        return new ItemWriter<User>() {
            @Override
            public void write(List<? extends User> list) throws Exception {
                list.forEach(System.err::println);
            }
        };
    }

    @Bean
    public ItemReader<? extends User> itemReader(){
        return new JdbcCursorItemReaderBuilder<User>()
                .name("userItemReader")
                .dataSource(dataSource)
                .sql("select * from user where age < ?")
                .preparedStatementSetter(new ArgumentPreparedStatementSetter(new Object[]{16}))
                .rowMapper(userRowMapper())
                .build();

    }
    @Bean
    public UserRowMapper userRowMapper(){
        return new UserRowMapper();

    }

    @Bean
    public Step step(){
        return stepBuilderFactory.get("step1")
                .<User, User>chunk(1)
                .reader(itemReader())
                .writer(itemWriter())
                .build();
    }
    @Bean
    public Job job(){
        return jobBuilderFactory.get("mapper-flat-reade-database-job-with-condition")
                .start(step())
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(CursorDBReaderJob.class,args);
    }
}
