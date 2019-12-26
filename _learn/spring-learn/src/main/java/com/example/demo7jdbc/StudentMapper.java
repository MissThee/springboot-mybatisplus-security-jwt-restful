package com.example.demo7jdbc;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentMapper implements RowMapper<Student> {
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {

            Student student = new Student();
            student.setId(rs.getInt("id"));
            student.setName(rs.getString("name"));
            return student;
    }


}