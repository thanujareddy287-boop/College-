package com.studentmanagement.service;

import com.studentmanagement.dao.StudentDAO;
import com.studentmanagement.model.Student;

import java.sql.SQLException;
import java.util.List;

public class StudentService {
    private final StudentDAO studentDAO = new StudentDAO();

    public List<Student> getAllStudents() throws SQLException {
        return studentDAO.findAll();
    }

    public Student getStudent(int id) throws SQLException {
        return studentDAO.findById(id);
    }

    public int addStudent(Student student) throws SQLException {
        validate(student);
        return studentDAO.create(student);
    }

    public boolean updateStudent(int id, Student student) throws SQLException {
        validate(student);
        return studentDAO.update(id, student);
    }

    public boolean deleteStudent(int id) throws SQLException {
        return studentDAO.delete(id);
    }

    private void validate(Student s) {
        if (s == null ||
            s.getName() == null || s.getName().isBlank() ||
            s.getEmail() == null || s.getEmail().isBlank() ||
            s.getCourse() == null || s.getCourse().isBlank() ||
            s.getAge() <= 0) {
            throw new IllegalArgumentException("Name, email, course and valid age are required.");
        }
    }
}
