package com.example.demo7jdbc;

import javax.sql.DataSource;
import java.util.List;

public interface StudentDAO {

    public void create(String... name);

    public void create1(String... name) ;

    public Student getStudent(Integer id);

    public List<Student> listStudents();

    public void deleteAll();

    public void update(Integer id, String Name);
}