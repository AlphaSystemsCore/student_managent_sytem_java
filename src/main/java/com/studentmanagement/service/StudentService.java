package com.studentmanagement.service;

import com.studentmanagement.dao.StudentDAO;
import com.studentmanagement.model.Student;

import java.sql.SQLException;
import java.util.List;

public class StudentService {

    private final StudentDAO studentDAO = new StudentDAO();

    public void registerStudent(Student student) throws SQLException {
        studentDAO.addStudent(student);
    }

    public List<Student> getAllStudents() throws SQLException {
        return studentDAO.getAllStudents();
    }

    public void updateStudent(Student student) throws SQLException {
        studentDAO.updateStudent(student);
    }

    public void deleteStudent(int studentId) throws SQLException {
        studentDAO.deleteStudent(studentId);
    }
}