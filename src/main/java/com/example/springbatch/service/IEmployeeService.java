package com.example.springbatch.service;

import com.example.springbatch.entity.Employee;

import java.io.IOException;

public interface IEmployeeService {
    void save (Employee employee);
    /**
     * 初始化数据：生成50w数据
     */
    void dataInit() throws IOException;
    /**
     * 清空数据
     */
    void truncateAll();
    /**
     * 清空employee_temp数据
     */
    void truncateTemp();
}
