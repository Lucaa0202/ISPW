package org.example.project3.query;

import org.example.project3.model.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseQuery {
    private CourseQuery() {}

    public static ResultSet retrieveCourse(Connection conn) throws SQLException {
        String query = "SELECT c.id, c.name, c.remaining, c.duration, c.level, s.day, s.hour FROM course c JOIN session s ON c.id = s.course";
        PreparedStatement pstmt = conn.prepareStatement(query);
        return pstmt.executeQuery();
    }

    public static void insertCourse(Connection conn, Course course) throws SQLException{
        int courseId;

        // Inserisci il corso (senza specificare ID)
        String insertCourse = "INSERT INTO course (name, remaining, duration, level) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmtCourse = conn.prepareStatement(insertCourse)) {
            pstmtCourse.setString(1, course.getCourseName());
            pstmtCourse.setInt(2, course.getRemainingSlots());
            pstmtCourse.setString(3, course.getDuration());
            pstmtCourse.setString(4, course.getLevel());
            pstmtCourse.executeUpdate();
        }

        // Recupera l'ID del corso appena inserito tramite il nome
        String selectId = "SELECT id FROM course WHERE name = ? ORDER BY id DESC LIMIT 1";
        try (PreparedStatement pstmtSelect = conn.prepareStatement(selectId)) {
            pstmtSelect.setString(1, course.getCourseName());
            try (ResultSet rs = pstmtSelect.executeQuery()) {
                if (rs.next()) {
                    courseId = rs.getInt("id");
                } else {
                    throw new SQLException("Corso non trovato dopo l'inserimento.");
                }
            }
        }

        // Inserisci la sessione con l’ID appena ottenuto
        String insertSession = "INSERT INTO session (course, day, hour) VALUES (?, ?, ?)";
        try (PreparedStatement pstmtSession = conn.prepareStatement(insertSession)) {
            pstmtSession.setInt(1, courseId);
            pstmtSession.setString(2, course.getDay());
            pstmtSession.setString(3, course.getHour());
            pstmtSession.executeUpdate();
        }
    }

    public static void deleteCourse(Connection conn, int courseId) throws SQLException {


        String deleteSessionQuery = "DELETE FROM session WHERE course = ?";
        try (PreparedStatement psSession = conn.prepareStatement(deleteSessionQuery)) {
            psSession.setInt(1, courseId);
            psSession.executeUpdate();
        }

        // Poi cancella il corso
        String deleteCourseQuery = "DELETE FROM course WHERE id = ?";
        try (PreparedStatement psCourse = conn.prepareStatement(deleteCourseQuery)) {
            psCourse.setInt(1, courseId);
            psCourse.executeUpdate();
        }
    }

    public static void createAssociation(Connection conn, int courseId, String email) throws SQLException{
        String query = "UPDATE course SET trainer = ? WHERE id = ?";
        try(PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setString(1, email);
            pstmt.setInt(2, courseId);
            pstmt.executeUpdate();
        }
    }
}
