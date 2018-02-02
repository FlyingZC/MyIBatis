package com.zbatis.dao.impl;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.zbatis.entity.Employee;

public class T01IBatisCRUD
{
    /** <一句话功能简述>测试相对路径
     * <功能详细描述>
     * @throws IOException 
     * @see [类、类#方法、类#成员]
     */
    @Test
    public void testPath() throws IOException
    {
        //该文件在工程目录src/test/resources下
        //D:\workspace-e3\MyIBatis\src\test\resources\folder\SqlMapConfig.xml
        //说明 相对于 编译后的路径
        //test目录编译后的路径D:\workspace-e3\MyIBatis\target\test-classes\folder，相对于该路径
        Reader rd = Resources.getResourceAsReader("folder/SqlMapConfig.xml");

    }

    //增,包含selectKey 
    @Test
    public void testInsert() throws IOException, SQLException
    {
        //1.读取SqlMapConfig.xml文件
        Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
        //2.创建SqlMapClient对象
        SqlMapClient sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(rd);

        Employee em = new Employee("Zara", "Ali", 5000);
        //insert():内部使用<selectKey>标签后,被插入的实体类em中主键id自动被赋值,且该方法的返回值为被插入后的数据库主键id值
        //若去除insert标签中的selectKey,则获取不到主键id
        System.out.println(sqlMapClient.insert("Employee.insert", em));
    }

    //删
    @Test
    public void testDel() throws Exception
    {
        Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
        SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

        int id = 1;
        //delete()方法 返回 删除的记录数
        smc.delete("Employee.delete", id);

        System.out.println("Going to read records.....");
        List<Employee> ems = (List<Employee>) smc.queryForList("Employee.getAll", null);
        Employee em = null;
        for (Employee e : ems)
        {
            System.out.print("  " + e.getId());
            System.out.print("  " + e.getFirstName());
            System.out.print("  " + e.getLastName());
            System.out.print("  " + e.getSalary());
            em = e;
            System.out.println("");
        }

        System.out.println("Records Read Successfully ");
    }

    //改
    @Test
    public void testUpdate() throws Exception
    {
        Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
        SqlMapClient smc = SqlMapClientBuilder.buildSqlMapClient(rd);

        Employee rec = new Employee();
        rec.setId(1);
        rec.setFirstName("zcc");
        //update()方法返回 该操作影响的记录数
        System.out.println(smc.update("Employee.update", rec));

        System.out.println("Going to read records.....");
        List<Employee> ems = (List<Employee>) smc.queryForList("Employee.getAll", null);
        Employee em = null;
        for (Employee e : ems)
        {
            System.out.print("  " + e.getId());
            System.out.print("  " + e.getFirstName());
            System.out.print("  " + e.getLastName());
            System.out.print("  " + e.getSalary());
            em = e;
            System.out.println("");
        }

        System.out.println("Records Read Successfully ");
    }

    //查
    @Test
    public void testQueryForList() throws Exception
    {
        Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
        SqlMapClient sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(rd);

        //1.queryForList,namspace.xxx
        List<Employee> ems = (List<Employee>) sqlMapClient.queryForList("Employee.getAll", null);
        Employee em = null;
        for (Employee e : ems)
        {
            System.out.print("  " + e.getId());
            System.out.print("  " + e.getFirstName());
            System.out.print("  " + e.getLastName());
            System.out.print("  " + e.getSalary());
            em = e;
            System.out.println("");
        }

        System.out.println("Records Read Successfully ");

        List<Employee> ems2 = (List<Employee>) sqlMapClient.queryForList("Employee.getAll2", null);
        System.out.println(ems2);
    }
    
    //ibatis批量操作
    @Test
    public void testBatch() throws Exception
    {
        //将 批处理 语句 打包成一个事务,这样可以避免每条语句都会开始一个新的事务
        Reader rd = Resources.getResourceAsReader("SqlMapConfig.xml");
        SqlMapClient sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(rd);
        
        sqlMapClient.startTransaction();
        Employee emp = new Employee();
        long begin = System.currentTimeMillis();
        for(int i = 0;i<100;i++)
        {
            emp.setFirstName("f"+i);
            emp.setLastName("l"+i);
            emp.setSalary(i);
            sqlMapClient.insert("Employee.insert",emp);
        }
        //startBatch写在insert之前和之后时间相同,均为573.若不使用batch,耗时5358
        sqlMapClient.startBatch();
        sqlMapClient.executeBatch();
        sqlMapClient.commitTransaction();
        sqlMapClient.endTransaction();
        long end = System.currentTimeMillis();
        System.out.println(end-begin);
    }
    
}
