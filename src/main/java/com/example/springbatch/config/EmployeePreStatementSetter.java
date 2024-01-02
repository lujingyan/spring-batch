package com.example.springbatch.config;

import com.example.springbatch.entity.Employee;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmployeePreStatementSetter implements ItemPreparedStatementSetter<Employee> {
    @Override
    public void setValues(Employee employee, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setLong(1,employee.getId());
        preparedStatement.setString(2,employee.getName());
        preparedStatement.setInt(3,employee.getAge());
        preparedStatement.setInt(4,employee.getAge());
    }
}
