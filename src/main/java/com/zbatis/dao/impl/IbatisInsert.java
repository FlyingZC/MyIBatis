package com.zbatis.dao.impl;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.zbatis.entity.Employee;

public class IbatisInsert
{
    public static void main(String[] args) throws IOException, SQLException
    {
        //1.读取SqlMapConfig.xml文件
        Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
        //2.创建SqlMapClient对象
        SqlMapClient sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(rd);

        /* This would insert one record in Employee table. */
        System.out.println("Going to insert record.....");
        
        Employee em = new Employee("Zara", "Ali", 5000);
        sqlMapClient.insert("Employee.insert", em);

        System.out.println("Record Inserted Successfully ");

    }
}
