package com.zbatis.dao.impl;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.zbatis.entity.Employee;

public class T02DynamicSQL
{
    static Reader rd = null;

    SqlMapClient sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(rd);
    static
    {
        try
        {
            rd = Resources.getResourceAsReader("SqlMapConfig.xml");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /** <一句话功能简述>使用parameterMap
     * 使用ParameterMap时,查询传入map,该map的键必须和<ParameterMap>中的键完全一致(包括数量)
     */
    @Test
    public void testParameterMap() throws SQLException
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("firstName", "testZc");
        map.put("lastName", "testZhang");
        map.put("salary", "1000");
        sqlMapClient.insert("Employee.useMapInsert", map);
    }

    /** <一句话功能简述>使用parameterClass="java.util.Map"进行插入
     * 
     */
    @Test
    public void testMapInsert() throws SQLException
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("firstName", "testZc2");
        map.put("lastName", "testZhang2");
        map.put("salary", "2000");
        sqlMapClient.insert("Employee.mapInsert", map);
    }

    /**
     * 使用ResultMap
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testResultMap() throws IOException, SQLException
    {
        int id = 1;
        System.out.println("Going to read record.....");
        Employee e = (Employee) sqlMapClient.queryForObject("Employee.useResultMap", id);
        if (e != null)
        {
            System.out.println("ID:  " + e.getId());
            System.out.println("First Name:  " + e.getFirstName());
            System.out.println("Last Name:  " + e.getLastName());
            System.out.println("Salary:  " + e.getSalary());
        }
        System.out.println("Record read Successfully ");
    }

    @Test
    public void testDynamicFind() throws Exception
    {
        Employee e1 = new Employee();
        e1.setId(1);
        Employee e = (Employee) sqlMapClient.queryForObject("Employee.findByID", e1);
        //返回值 若查不到结果,注意结果返回的字段名 和 该对象的属性名是否一致
        System.out.println(e);
    }

    @Test
    public void testDynamicFind2() throws Exception
    {
        Employee e1 = new Employee();
        e1.setId(1);
        e1.setFirstName("aaa");
        //queryForObject在returned too many results时会报错
        List<Employee> e = (List<Employee>) sqlMapClient.queryForList("Employee.findByID2", e1);
        //返回值 若查不到结果,注意结果返回的字段名 和 该对象的属性名是否一致
        System.out.println(e);
    }

    @Test
    public void testFindByName() throws Exception
    {
        Employee e1 = new Employee();
        e1.setId(1);
        e1.setFirstName("aaa");
        //queryForObject在returned too many results时会报错
        List<Employee> e = (List<Employee>) sqlMapClient.queryForList("Employee.findEmpByName", e1);
        //返回值 若查不到结果,注意结果返回的字段名 和 该对象的属性名是否一致
        System.out.println(e);
    }
}
