package com.example.demo7jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

public class StudentDAOImp implements StudentDAO {
    //spring封装的jdbc实现
    private JdbcTemplate jdbcTemplate;
    //spring的事务管理器
    private PlatformTransactionManager transactionManager;

    public StudentDAOImp(JdbcTemplate jdbcTemplate, PlatformTransactionManager transactionManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionManager = transactionManager;
    }

    public void create(String... names) {
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        try {
            int i = 0;
            for (String name : names) {
                String SQL = "insert into student (name) values (?)";
                jdbcTemplate.update(SQL, name);
                System.out.println("Created Record Name = " + name);
                if (i++ >= 3) {
                    throw new RuntimeException("too many records");
                }
            }
            transactionManager.commit(transactionStatus);
        } catch (Exception e) {
            System.out.println(e + "  ROLL BACK!");
            transactionManager.rollback(transactionStatus);
        }
    }

    public void create1(String... names) {
        int i = 0;
        for (String name : names) {
            String SQL = "insert into student (name) values (?)";
            jdbcTemplate.update(SQL, name);
            System.out.println("Created Record Name = " + name);
            if (i++ >= 3) {
                throw new RuntimeException("too many records");
            }
        }
    }

    public Student getStudent(Integer id) {
        String SQL = "select * from student where id = ?";
        Student student;
        try {
            student = jdbcTemplate.queryForObject(SQL, new Object[]{id}, new StudentMapper());
        } catch (IncorrectResultSizeDataAccessException e) {
            if (e.getActualSize() == 0) {
                return null;
            } else {
                throw e;
            }
        }
        return student;
    }

    public List<Student> listStudents() {
        String SQL = "select * from student";
        List<Student> students = jdbcTemplate.query(SQL, new StudentMapper());
        return students;
    }

    public void deleteAll() {
        String SQL = "delete from student";
        jdbcTemplate.update(SQL);
        System.out.println("Deleted All Record");
    }

    public void update(Integer id, String name) {
        String SQL = "update student set name = ? where id = ?";
        int update = jdbcTemplate.update(SQL, name, id);
        if (update > 0) {
            System.out.println("Updated Record with ID = " + id);
        } else {
            System.out.println("NO RECORD");
        }
    }
}