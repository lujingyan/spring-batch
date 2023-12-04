package com.example.springbatch._03_param_validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

/*
*进行name参数校验：
* 规则：当校验不通过时（null或空串），抛出异常*
*  */
public class NameParamValidator implements JobParametersValidator {
    @Override
    public void validate(JobParameters jobParameters) throws JobParametersInvalidException {
        String name = jobParameters.getString("name");
        if (!StringUtils.hasText(name)){
            throw new JobParametersInvalidException("name 参数值不能为空或空串");
        }
    }
}
