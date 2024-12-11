package orm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import DaoExceptions.*;
import domainModel.*;

public class MeetingDaoDatabaseTest {

    private Connection conn;
    private MeetingDaoDatabase meetingDao;
    private String url = "jdbc:sqlite:database/testDB.db";
    private Parent parent;
    private Teacher teacher;
    private Student student;
    private MeetingAvailability meetingAvailability;

    @Before
    public void setUp() throws SQLException {
        conn = DriverManager.getConnection(url);
        meetingDao = new MeetingDaoDatabase(conn);
        createTestData();
    }

    private void createTestData() throws SQLException {
        deleteTestData();
        
        String insertClassQuery = "INSERT INTO Classes (name, classroom) " + "VALUES ('1A', 'A01');";
		conn.createStatement().executeUpdate(insertClassQuery);


        String insertStudentQuery = "INSERT INTO Students (username, password, name, surname, date_of_birth, class) "
				+ "VALUES ('stu001', 'pass123', 'Mario', 'Rossi', '2005-03-15', '1A');";
		conn.createStatement().executeUpdate(insertStudentQuery);
		int studentId = getLastInsertedId("Students", "id_student");
		student = new Student(studentId, "Mario", "Rossi", new SchoolClass("1A"));
        
        
        String insertTeacherQuery = "INSERT INTO Teachers (username, password, name, surname) "
        		+ "VALUES ('tch001', 'pass123', 'Mario', 'Rossi')";
        conn.createStatement().executeUpdate(insertTeacherQuery);
        int teacherId = getLastInsertedId("Teachers", "id_teacher");
        teacher = new Teacher(teacherId, "Mario", "Rossi");
        

        String insertParentQuery = "INSERT INTO Parents (username, password, name, surname, id_student) "
        		+ "VALUES ('par001', 'parent12345', 'Giorgio', 'Rossi', " + studentId + ")";
        conn.createStatement().executeUpdate(insertParentQuery);
        int parentId = getLastInsertedId("Parents", "id_parent");
        parent = new Parent(parentId, "Giorgio", "Rossi", student);
        

        LocalDate date = LocalDate.of(2023, 12, 15);
        LocalTime time = LocalTime.of(10, 30);
        String insertAvailabilityQuery = "INSERT INTO MeetingsAvailability (date, hour, id_teacher)"
        		+ " VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertAvailabilityQuery)) {
            pstmt.setDate(1, Date.valueOf(date));
            pstmt.setString(2, time.toString());
            pstmt.setInt(3, teacher.getId());
            pstmt.executeUpdate();
        }
        meetingAvailability = new MeetingAvailability(teacher, date, time, false);
        
    }

    
    
    private int getLastInsertedId(String tableName, String idColumn) throws SQLException {
        String query = "SELECT MAX(" + idColumn + ") as id FROM " + tableName;
        try (ResultSet rs = conn.createStatement().executeQuery(query)) {
            rs.next();
            return rs.getInt("id");
        }
    }

    private void deleteTestData() throws SQLException {
        conn.createStatement().executeUpdate("DELETE FROM Meetings;");
        conn.createStatement().executeUpdate("DELETE FROM MeetingsAvailability;");
        conn.createStatement().executeUpdate("DELETE FROM Teachers;");
        conn.createStatement().executeUpdate("DELETE FROM Parents;");
        conn.createStatement().executeUpdate("DELETE FROM Students;");
        conn.createStatement().executeUpdate("DELETE FROM Classes;");
    }

    @Test
    public void testBookMeeting() throws MeetingDaoException, ParentDaoException, MeetingAvailabilityDaoException, SQLException {
        meetingDao.bookMeeting(meetingAvailability, parent);
        Meeting meetingExpected = new Meeting(parent, meetingAvailability);
        
        String query = "SELECT * FROM Meetings WHERE id_parent = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, parent.getId());
            try (ResultSet rs = stmt.executeQuery()) {
            	assertThat(rs.next()).isTrue();
            	assertThat(rs.getInt("id_parent")).isEqualTo(parent.getId());
            	
            	Meeting meetingActual = new Meeting(parent, 
            			new MeetingAvailability(teacher, rs.getDate("date").toLocalDate(),
            			LocalTime.parse(rs.getString("hour")), 
            			false));
            	
            	assertThat(meetingActual).isEqualTo(meetingExpected);	
            }
        }
    }

    @Test
    public void testGetAllMeetingsByParent() throws MeetingDaoException, ParentDaoException, MeetingAvailabilityDaoException, SQLException {
    	String insertMeetingQuery = "INSERT INTO Meetings (date, hour, id_teacher, id_parent) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertMeetingQuery)) {
            pstmt.setDate(1, Date.valueOf(meetingAvailability.getDate()));
            pstmt.setString(2, meetingAvailability.getHour().toString());
            pstmt.setInt(3, teacher.getId());
            pstmt.setInt(4, parent.getId());
            pstmt.executeUpdate();
        }
        
        Meeting meeting = new Meeting(parent, meetingAvailability);
    	
        Iterator<Meeting> meetings = meetingDao.getAllMeetingsByParent(parent);
        assertThat(meetings.hasNext()).isTrue();

        assertThat(meetings).toIterable().containsExactlyInAnyOrder(meeting);
    }

    @Test
    public void testGetMeetingsByTeacher() throws DaoConnectionException, TeacherDaoException, MeetingDaoException, ParentDaoException, MeetingAvailabilityDaoException, SQLException {
    	String insertMeetingQuery = "INSERT INTO Meetings (date, hour, id_teacher, id_parent) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertMeetingQuery)) {
            pstmt.setDate(1, Date.valueOf(meetingAvailability.getDate()));
            pstmt.setString(2, meetingAvailability.getHour().toString());
            pstmt.setInt(3, teacher.getId());
            pstmt.setInt(4, parent.getId());
            pstmt.executeUpdate();
        }
        
        Meeting meeting = new Meeting(parent, meetingAvailability);
        
        Iterator<Meeting> meetings = meetingDao.getMeetingsByTeacher(teacher);
        assertThat(meetings.hasNext()).isTrue();

        assertThat(meetings).toIterable().containsExactlyInAnyOrder(meeting);
    }

    @Test
    public void testBookMeeting_invalidParent() {
        Parent invalidParent = new Parent(-1, "Invalid", "Parent", student);

        assertThatThrownBy(() -> meetingDao.bookMeeting(meetingAvailability, invalidParent))
                .isInstanceOf(ParentDaoException.class)
                .hasMessage("Parent doesn't exist");
    }
    
    @Test
    public void testGetAllMeetingsParent_invalidParent() {
        Parent invalidParent = new Parent(-1, "Invalid", "Parent", student);

        assertThatThrownBy(() -> meetingDao.getAllMeetingsByParent(invalidParent))
                .isInstanceOf(ParentDaoException.class)
                .hasMessage("Parent doesn't exist");
    }
    
    @Test
    public void testGetAllMeetingsByTeacher_invalidTeacher() {
    	Teacher teacherInvalid = new Teacher(-1, "Mario","Stordini");

        assertThatThrownBy(() -> meetingDao.getMeetingsByTeacher(teacherInvalid))
                .isInstanceOf(TeacherDaoException.class)
                .hasMessage("Teacher doesn't exist");
    }
    
    @Test
    public void testBookMeeting_invalidMeetingAvailability() {
        MeetingAvailability invalid = new MeetingAvailability(teacher, LocalDate.now(), LocalTime.now(), false);

        assertThatThrownBy(() -> meetingDao.bookMeeting(invalid, parent))
                .isInstanceOf(MeetingAvailabilityDaoException.class)
                .hasMessage("Meeting availability doesn't exist");
    }

    @After
    public void tearDown() throws SQLException {
        deleteTestData();
        conn.close();
    }
}
