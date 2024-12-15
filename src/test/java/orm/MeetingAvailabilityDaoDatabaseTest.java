package orm;

import static org.assertj.core.api.Assertions.assertThat;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import domainModel.MeetingAvailability;
import domainModel.Teacher;
import exceptions.MeetingAvailabilityDaoException;
import exceptions.TeacherDaoException;

public class MeetingAvailabilityDaoDatabaseTest {

	private Connection conn;
	private MeetingAvailabilityDaoDatabase meetingAvailabilityDao;
	private String url = "jdbc:sqlite:database/testDB.db";
	private MeetingAvailability meetingAvailability;
	private Teacher teacher;
	
	@Before
	public void setUp() throws SQLException {
		conn = DriverManager.getConnection(url);
		meetingAvailabilityDao = new MeetingAvailabilityDaoDatabase(conn);
		createTestData();
	}
	
	private void createTestData() throws SQLException {
		deleteTestData();
		
		String insertTeacherQuery = "INSERT INTO Teachers (username, password, name, surname) " + "VALUES (?, ?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(insertTeacherQuery)) {
			pstmt.setString(1, "tch001");
			pstmt.setString(2, "pass123");
			pstmt.setString(3, "Casimiro");
			pstmt.setString(4, "Grumaioli");
			pstmt.executeUpdate();
		}
		
		String getTeacherIdQuery = "SELECT id_teacher FROM Teachers WHERE username = 'tch001';";
		ResultSet rs = conn.createStatement().executeQuery(getTeacherIdQuery);
		rs.next();
		int teacherId = rs.getInt("id_teacher");
		
		teacher = new Teacher(teacherId, "Casimiro", "Grumaioli");
		

		LocalDate date = LocalDate.of(2023, 12, 12);
		LocalTime time = LocalTime.of(10, 30);
		String getMeetingsAvailabilityIdQuery = "INSERT INTO MeetingsAvailability (date, hour, id_teacher)"
				+ "VALUES(?, ?, ?)";
		try (PreparedStatement stmt = conn.prepareStatement(getMeetingsAvailabilityIdQuery)) {
			stmt.setDate(1, Date.valueOf(date));
			stmt.setString(2, time.toString());
			stmt.setInt(3, teacher.getId());
			stmt.executeUpdate();
		}
		
		meetingAvailability = new MeetingAvailability(teacher, date, time, false);	
	
	}

	private void deleteTestData() throws SQLException {
		String deleteMeetingAvailabilitiesQuery = "DELETE FROM MeetingsAvailability;";
		conn.createStatement().executeUpdate(deleteMeetingAvailabilitiesQuery);
		
		String deleteTeachersQuery = "DELETE FROM Teachers;";
		conn.createStatement().executeUpdate(deleteTeachersQuery);
	}

	
	@Test
	public void testAddMeetingAvailability() throws SQLException, TeacherDaoException, MeetingAvailabilityDaoException {
	    LocalDate dateExpected = LocalDate.of(2023, 12, 15);
	    LocalTime timeExpected = LocalTime.of(14, 30);
	    MeetingAvailability meetingAvailabilityExpected = new MeetingAvailability(teacher, dateExpected, timeExpected, false);
	    
	    meetingAvailabilityDao.addMeetingAvailabilityInDate(teacher, dateExpected, timeExpected);

	    String query = "SELECT * FROM MeetingsAvailability WHERE date = ? AND id_teacher = ?;";
	    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
	        pstmt.setDate(1, Date.valueOf(dateExpected));
	        pstmt.setInt(2, teacher.getId());
	        ResultSet rs = pstmt.executeQuery();
	        
		    MeetingAvailability meetingAvailabilityActual = new MeetingAvailability(
		    		teacher, 
		    		rs.getDate("date").toLocalDate(), 
		    		LocalTime.parse(rs.getString("hour")), 
		    		false);

	        assertTrue(rs.next());
	        
	        assertEquals(meetingAvailabilityActual, meetingAvailabilityExpected);
	        
	        assertFalse(rs.next());
	    }
	}
	
	
	@Test
	public void testAddMeetingAvailability_invalidTeacher() {
		Teacher invalidTeacher = new Teacher(-1, "Invalid", "Teacher");
		
		assertThatThrownBy(() -> meetingAvailabilityDao.addMeetingAvailabilityInDate(invalidTeacher, 
				meetingAvailability.getDate(), 
				meetingAvailability.getHour())).isInstanceOf(TeacherDaoException.class)
				.hasMessage("Teacher doesn't exist");
	}

	
	
	@Test
	public void testEditBooking() throws MeetingAvailabilityDaoException, SQLException {
		meetingAvailabilityDao.editBooking(meetingAvailability);
		
		ResultSet rs = conn.prepareStatement("SELECT isBooked FROM MeetingsAvailability WHERE id_teacher = " + teacher.getId() + ";").executeQuery();
		assertTrue(rs.getBoolean("isBooked"));
	}
	
	
	@Test
	public void testGetMeetingAvailabilityByDateHourTeacher() throws TeacherDaoException, MeetingAvailabilityDaoException {
		MeetingAvailability newMeeting = meetingAvailabilityDao.getMeetingAvailabilityByDateHourTeacher(
				meetingAvailability.getDate(), meetingAvailability.getHour(), teacher);
		
		assertEquals(meetingAvailability, newMeeting);
	}
	
	@Test
	public void testGetMeetingAvailabilityByDateHourTeacher_invalidTeacher() throws TeacherDaoException, MeetingAvailabilityDaoException {
		Teacher invalidTeacher = new Teacher(-1, "", "");
		
		assertThatThrownBy(() -> meetingAvailabilityDao.getMeetingAvailabilityByDateHourTeacher(
				meetingAvailability.getDate(), meetingAvailability.getHour(), invalidTeacher))
			.isInstanceOf(TeacherDaoException.class).hasMessage("Teacher doesn't exist");
	}
	
	@Test
	public void testGetAllMeetingsAvaialabilityByTeacher() throws SQLException, TeacherDaoException, MeetingAvailabilityDaoException {
	    String firstMeetingQuery = "INSERT INTO MeetingsAvailability (date, hour, id_teacher, isBooked) "
	                              + "VALUES (?, ?, ?, ?)";
	    try (PreparedStatement pstmt = conn.prepareStatement(firstMeetingQuery)) {
	        pstmt.setDate(1, Date.valueOf("2023-12-12")); 
	        pstmt.setString(2, "11:30"); 
	        pstmt.setInt(3, teacher.getId()); 
	        pstmt.setBoolean(4, false); 
	        pstmt.executeUpdate();
	    }
	    MeetingAvailability expectedMeeting1 = new MeetingAvailability(
	        teacher, LocalDate.of(2023, 12, 12), LocalTime.of(11, 30), false
	    );


	    try (PreparedStatement pstmt = conn.prepareStatement(firstMeetingQuery)) {
	        pstmt.setDate(1, Date.valueOf("2023-12-14")); 
	        pstmt.setString(2, "14:00"); 
	        pstmt.setInt(3, -1);
	        pstmt.setBoolean(4, false); 
	        pstmt.executeUpdate();
	    }

	    Iterator<MeetingAvailability> iteratorMeetingsActual = meetingAvailabilityDao.getAllMeetingsAvaialabilityByTeacher(teacher);
	    
	    List<MeetingAvailability> meetingsActualList = new ArrayList<>();
	    while (iteratorMeetingsActual.hasNext()) {
	        meetingsActualList.add(iteratorMeetingsActual.next());
	    }

	    List<MeetingAvailability> expectedMeetings = List.of(expectedMeeting1, meetingAvailability);
	    
	    assertThat(meetingsActualList).containsExactlyInAnyOrderElementsOf(expectedMeetings);
	}
	
	@Test
	public void testGetAllMeetingsAvaialabilityByTeacher_invalidTeacher() throws TeacherDaoException, MeetingAvailabilityDaoException {
		Teacher invalidTeacher = new Teacher(-1, "", "");
		
		assertThatThrownBy(() -> meetingAvailabilityDao.getAllMeetingsAvaialabilityByTeacher(invalidTeacher))
			.isInstanceOf(TeacherDaoException.class).hasMessage("Teacher doesn't exist");
	}
	
	@Test
	public void testDeleteMeetingAvailability() throws MeetingAvailabilityDaoException, SQLException, TeacherDaoException {
		ResultSet rs = conn.prepareStatement("SELECT * FROM MeetingsAvailability;").executeQuery();
		assertTrue(rs.next());
		
		meetingAvailabilityDao.deleteMeetingAvailability(meetingAvailability);
		
		rs = conn.prepareStatement("SELECT * FROM MeetingsAvailability;").executeQuery();
		assertFalse(rs.next());
	}
	
	@Test
	public void testDeleteMeetingAvailability_teacherNotPresent() throws MeetingAvailabilityDaoException, SQLException, TeacherDaoException {
		MeetingAvailability notPresent = new MeetingAvailability(new Teacher(-1, "Not", "present"), 
				LocalDate.of(2023, 10, 10), LocalTime.of(11, 0), false);

		assertThatThrownBy(() -> meetingAvailabilityDao.deleteMeetingAvailability(notPresent))
		.isInstanceOf(TeacherDaoException.class).hasMessage("Teacher doesn't exist");
	}
	
	@Test
	public void testDeleteMeetingAvailability_meetingAvNotPresent() throws MeetingAvailabilityDaoException, SQLException, TeacherDaoException {
		MeetingAvailability notPresent = new MeetingAvailability(teacher, 
				LocalDate.of(2023, 10, 10), LocalTime.of(11, 0), false);

		assertThatThrownBy(() -> meetingAvailabilityDao.deleteMeetingAvailability(notPresent))
		.isInstanceOf(MeetingAvailabilityDaoException.class).hasMessage("Meeting availability not present");
	}

	
	
	@After
	public void closeConnection() throws SQLException {
		deleteTestData();
		conn.close();
	}
}
