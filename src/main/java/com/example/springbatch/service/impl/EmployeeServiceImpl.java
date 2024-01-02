package com.example.springbatch.service.impl;

import com.example.springbatch.entity.Employee;
import com.example.springbatch.repo.EmployeeRepo;
import com.example.springbatch.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

@Service
@Profile("local")
public class EmployeeServiceImpl implements IEmployeeService {
    @Autowired
    EmployeeRepo employeeRepo;

    @Value("${job.data.path}")
    public String path;
    @Override
    public void save(Employee employee) {
        employeeRepo.save(employee);
    }

    @Override
    public void dataInit() throws IOException {
        File file = new File(path, "employee.csv");
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        FileOutputStream out = new FileOutputStream(file);
        String txt = "";
        Random ageR = new Random();
        Random boolR = new Random();
        // 给文件中生产50万条数据
        long beginTime = System.currentTimeMillis();
        System.out.println("开始时间：【 " + beginTime + " 】");
        for (int i = 1; i <= 500000; i++) {
            if(i == 500000){
                txt = i+",employee_"+ i +"," + ageR.nextInt(100) + "," +
                        (boolR.nextBoolean()?1:0);
            }else{
                txt = i+",employee_"+ i +"," + ageR.nextInt(100) + "," +
                        (boolR.nextBoolean()?1:0) +"\n";
            }
            out.write(txt.getBytes());
            out.flush();
        }
        out.close();
        System.out.println("总共耗时：【 " + (System.currentTimeMillis() -
                beginTime) + " 】毫秒");
    }

    @Override
    public void truncateAll() {
        employeeRepo.deleteAll();
    }

    @Override
    public void truncateTemp() {

    }
}

