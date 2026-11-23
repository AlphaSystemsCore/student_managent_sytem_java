package com.studentmanagement.model;


public class Student {

    private int studentId;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String course;
    private int yearOfStudy;

    public Student(int studentId, String firstName, String lastName,
                   String email, String gender, String course,
                   int yearOfStudy) {

        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.course = course;
        this.yearOfStudy = yearOfStudy;
    }

    public int getStudentId() { return studentId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getGender() { return gender; }
    public String getCourse() { return course; }
    public int getYearOfStudy() { return yearOfStudy; }
}