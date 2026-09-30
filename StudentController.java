package com.studentmanagement.controller;

import com.google.gson.Gson;
import com.studentmanagement.model.Student;
import com.studentmanagement.service.StudentService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/api/students/*")
public class StudentController extends HttpServlet {
    private final StudentService service = new StudentService();
    private final Gson gson = new Gson();

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) {
        setCors(resp);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        try {
            Integer id = getId(req);

            if (id == null) {
                List<Student> students = service.getAllStudents();
                writeJson(resp, HttpServletResponse.SC_OK, students);
            } else {
                Student student = service.getStudent(id);
                if (student == null) {
                    writeJson(resp, HttpServletResponse.SC_NOT_FOUND,
                            new ErrorResponse("Student not found"));
                } else {
                    writeJson(resp, HttpServletResponse.SC_OK, student);
                }
            }
        } catch (Exception e) {
            writeJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    new ErrorResponse(e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        try {
            Student student = gson.fromJson(req.getReader(), Student.class);
            int id = service.addStudent(student);
            student.setId(id);
            writeJson(resp, HttpServletResponse.SC_CREATED, student);
        } catch (IllegalArgumentException e) {
            writeJson(resp, HttpServletResponse.SC_BAD_REQUEST,
                    new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            writeJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    new ErrorResponse(databaseMessage(e)));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        try {
            Integer id = getId(req);
            if (id == null) {
                writeJson(resp, HttpServletResponse.SC_BAD_REQUEST,
                        new ErrorResponse("Student ID is required."));
                return;
            }

            Student student = gson.fromJson(req.getReader(), Student.class);
            boolean updated = service.updateStudent(id, student);

            if (!updated) {
                writeJson(resp, HttpServletResponse.SC_NOT_FOUND,
                        new ErrorResponse("Student not found"));
            } else {
                student.setId(id);
                writeJson(resp, HttpServletResponse.SC_OK, student);
            }
        } catch (IllegalArgumentException e) {
            writeJson(resp, HttpServletResponse.SC_BAD_REQUEST,
                    new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            writeJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    new ErrorResponse(databaseMessage(e)));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCors(resp);
        try {
            Integer id = getId(req);
            if (id == null) {
                writeJson(resp, HttpServletResponse.SC_BAD_REQUEST,
                        new ErrorResponse("Student ID is required."));
                return;
            }

            boolean deleted = service.deleteStudent(id);
            if (!deleted) {
                writeJson(resp, HttpServletResponse.SC_NOT_FOUND,
                        new ErrorResponse("Student not found"));
            } else {
                writeJson(resp, HttpServletResponse.SC_OK,
                        new MessageResponse("Student deleted successfully"));
            }
        } catch (Exception e) {
            writeJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    new ErrorResponse(databaseMessage(e)));
        }
    }

    private Integer getId(HttpServletRequest req) {
        String path = req.getPathInfo();
        if (path == null || path.equals("/") || path.isBlank()) return null;

        String value = path.substring(1).split("/")[0];
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void writeJson(HttpServletResponse resp, int status, Object data) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(data));
    }

    private void setCors(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    private String databaseMessage(Exception e) {
        if (e.getMessage() == null) return "Server error";
        if (e.getMessage().contains("Duplicate entry")) return "Email already exists.";
        return e.getMessage();
    }

    record ErrorResponse(String error) {}
    record MessageResponse(String message) {}
}
