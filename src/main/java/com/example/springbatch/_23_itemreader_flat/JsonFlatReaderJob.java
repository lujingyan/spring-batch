package com.example.springbatch._23_itemreader_flat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.util.List;
//读user.txt文件封装到对象并打印

@EnableBatchProcessing
@SpringBootApplication
public class JsonFlatReaderJob {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    //job---step-chunk---reader---writer

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
    public JsonItemReader itemReader(){
        //用阿里框架读取json
        JacksonJsonObjectReader<User> jsonObjectReader = new
                JacksonJsonObjectReader<>(User.class);
        ObjectMapper objectMapper = new ObjectMapper();
        jsonObjectReader.setMapper(objectMapper);
        return new JsonItemReaderBuilder<User>()
                .name("userItemReader")
                .resource(new ClassPathResource("user.json"))
                .jsonObjectReader(jsonObjectReader)
                .build();

    }

    @Bean
    public Step step(){
        return stepBuilderFactory.get("step1")
                .<com.example.springbatch._22_itemreader_flat_mapper.User, User>chunk(1)
                .reader(itemReader())
                .writer(itemWriter())
                .build();
    }
    @Bean
    public Job job(){
        return jobBuilderFactory.get("mapper-flat-reade-json-job")
                .start(step())
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(JsonFlatReaderJob.class,args);
    }
}
