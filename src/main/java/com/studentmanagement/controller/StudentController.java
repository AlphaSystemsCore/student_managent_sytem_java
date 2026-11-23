package com.studentmanagement.controller;

import com.studentmanagement.model.Student;
import com.studentmanagement.service.StudentService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class StudentController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField yearField;

    @FXML private ComboBox<String> genderCombo;
    @FXML private ComboBox<String> courseCombo;

    @FXML private TableView<Student> studentTable;

    @FXML private TableColumn<Student, Integer> idColumn;
    @FXML private TableColumn<Student, String> firstNameColumn;
    @FXML private TableColumn<Student, String> lastNameColumn;
    @FXML private TableColumn<Student, String> emailColumn;
    @FXML private TableColumn<Student, String> genderColumn;
    @FXML private TableColumn<Student, String> courseColumn;
    @FXML private TableColumn<Student, Integer> yearColumn;

    private final StudentService studentService = new StudentService();

    private final ObservableList<Student> studentList =
            FXCollections.observableArrayList();

    // Keeps track of the currently selected student.
    private Student selectedStudent;

    @FXML
    public void initialize() {

        genderCombo.getItems().addAll("Male", "Female", "Other");

        courseCombo.getItems().addAll(
                "Computer Science",
                "Information Technology",
                "Software Engineering",
                "Computer Technology"
        );

        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("studentId"));

        firstNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("firstName"));

        lastNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("lastName"));

        emailColumn.setCellValueFactory(
                new PropertyValueFactory<>("email"));

        genderColumn.setCellValueFactory(
                new PropertyValueFactory<>("gender"));

        courseColumn.setCellValueFactory(
                new PropertyValueFactory<>("course"));

        yearColumn.setCellValueFactory(
                new PropertyValueFactory<>("yearOfStudy"));

        studentTable.setItems(studentList);

        // Listen for table selection changes.
        studentTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {

                    selectedStudent = newValue;

                    if (newValue != null) {
                        populateForm(newValue);
                    }
                });

        loadStudents();
    }

    // Populate the form using the selected student.
    private void populateForm(Student student) {

        firstNameField.setText(student.getFirstName());
        lastNameField.setText(student.getLastName());
        emailField.setText(student.getEmail());

        genderCombo.setValue(student.getGender());
        courseCombo.setValue(student.getCourse());

        yearField.setText(
                String.valueOf(student.getYearOfStudy())
        );
    }

    // Retrieve students.
    private void loadStudents() {

        try {

            studentList.setAll(studentService.getAllStudents());

        } catch (SQLException e) {

            showMessage(
                    Alert.AlertType.ERROR,
                    "Database Error",
                    e.getMessage()
            );
        }
    }

    // Validate input and construct a Student object.
    private Student getStudentFromForm(int studentId) {

        if (firstNameField.getText().isBlank()
                || lastNameField.getText().isBlank()
                || emailField.getText().isBlank()
                || genderCombo.getValue() == null
                || courseCombo.getValue() == null
                || yearField.getText().isBlank()) {

            throw new IllegalArgumentException(
                    "Please fill in all fields."
            );
        }

        int year = Integer.parseInt(yearField.getText().trim());

        if (year < 1 || year > 6) {
            throw new IllegalArgumentException(
                    "Year of study must be between 1 and 6."
            );
        }

        return new Student(
                studentId,
                firstNameField.getText().trim(),
                lastNameField.getText().trim(),
                emailField.getText().trim(),
                genderCombo.getValue(),
                courseCombo.getValue(),
                year
        );
    }

    // CREATE
    @FXML
    private void registerStudent() {

        try {

            Student student = getStudentFromForm(0);

            studentService.registerStudent(student);

            showMessage(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Student registered successfully!"
            );

            clearForm();
            loadStudents();

        } catch (NumberFormatException e) {

            showMessage(
                    Alert.AlertType.ERROR,
                    "Invalid Input",
                    "Year must be a number."
            );

        } catch (IllegalArgumentException e) {

            showMessage(
                    Alert.AlertType.WARNING,
                    "Validation Error",
                    e.getMessage()
            );

        } catch (SQLException e) {

            showMessage(
                    Alert.AlertType.ERROR,
                    "Database Error",
                    e.getMessage()
            );
        }
    }

    // UPDATE
    @FXML
    private void updateStudent() {

        if (selectedStudent == null) {

            showMessage(
                    Alert.AlertType.WARNING,
                    "No Selection",
                    "Please select a student to update."
            );

            return;
        }

        try {

            Student updatedStudent =
                    getStudentFromForm(selectedStudent.getStudentId());

            studentService.updateStudent(updatedStudent);

            showMessage(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Student updated successfully!"
            );

            clearForm();
            loadStudents();

        } catch (NumberFormatException e) {

            showMessage(
                    Alert.AlertType.ERROR,
                    "Invalid Input",
                    "Year must be a number."
            );

        } catch (IllegalArgumentException e) {

            showMessage(
                    Alert.AlertType.WARNING,
                    "Validation Error",
                    e.getMessage()
            );

        } catch (SQLException e) {

            showMessage(
                    Alert.AlertType.ERROR,
                    "Database Error",
                    e.getMessage()
            );
        }
    }

    // DELETE
    @FXML
    private void deleteStudent() {

        if (selectedStudent == null) {

            showMessage(
                    Alert.AlertType.WARNING,
                    "No Selection",
                    "Please select a student to delete."
            );

            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);

        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Delete Student");
        confirmation.setContentText(
                "Are you sure you want to delete "
                        + selectedStudent.getFirstName()
                        + " "
                        + selectedStudent.getLastName()
                        + "?"
        );

        confirmation.showAndWait().ifPresent(response -> {

            if (response == ButtonType.OK) {

                try {

                    studentService.deleteStudent(
                            selectedStudent.getStudentId()
                    );

                    showMessage(
                            Alert.AlertType.INFORMATION,
                            "Success",
                            "Student deleted successfully!"
                    );

                    clearForm();
                    loadStudents();

                } catch (SQLException e) {

                    showMessage(
                            Alert.AlertType.ERROR,
                            "Database Error",
                            e.getMessage()
                    );
                }
            }
        });
    }

    // CLEAR FORM
    @FXML
    private void clearForm() {

        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        yearField.clear();

        genderCombo.setValue(null);
        courseCombo.setValue(null);

        studentTable.getSelectionModel().clearSelection();

        selectedStudent = null;
    }

    // DISPLAY MESSAGES
    private void showMessage(
            Alert.AlertType type,
            String title,
            String message) {

        Alert alert = new Alert(type);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}