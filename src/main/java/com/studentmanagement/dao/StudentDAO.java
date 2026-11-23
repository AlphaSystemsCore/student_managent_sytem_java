package com.studentmanagement.dao;

import com.studentmanagement.database.DatabaseConnection;
import com.studentmanagement.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // CREATE
    public void addStudent(Student student) throws SQLException {

        String sql = """
                INSERT INTO student
                (first_name, last_name, email, gender, course, year_of_study)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getGender());
            stmt.setString(5, student.getCourse());
            stmt.setInt(6, student.getYearOfStudy());

            stmt.executeUpdate();
        }
    }

    // READ
    public List<Student> getAllStudents() throws SQLException {

        List<Student> students = new ArrayList<>();

        String sql = "SELECT * FROM student";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                students.add(new Student(
                        rs.getInt("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("gender"),
                        rs.getString("course"),
                        rs.getInt("year_of_study")
                ));
            }
        }

        return students;
    }

    // UPDATE
    public void updateStudent(Student student) throws SQLException {

        String sql = """
                UPDATE student
                SET first_name = ?,
                    last_name = ?,
                    email = ?,
                    gender = ?,
                    course = ?,
                    year_of_study = ?
                WHERE student_id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getGender());
            stmt.setString(5, student.getCourse());
            stmt.setInt(6, student.getYearOfStudy());
            stmt.setInt(7, student.getStudentId());

            stmt.executeUpdate();
        }
    }

    // DELETE
    public void deleteStudent(int studentId) throws SQLException {

        String sql = """
                DELETE FROM student
                WHERE student_id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);

            stmt.executeUpdate();
        }
    }
}