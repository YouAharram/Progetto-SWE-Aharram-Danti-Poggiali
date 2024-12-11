package orm;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DaoExceptions.DaoConnectionException;
import DaoExceptions.MeetingAvailabilityDaoException;
import DaoExceptions.MeetingDaoException;
import DaoExceptions.ParentDaoException;
import DaoExceptions.StudentDaoException;
import DaoExceptions.TeacherDaoException;
import domainModel.Meeting;
import domainModel.MeetingAvailability;
import domainModel.Parent;
import domainModel.Teacher;

public class MeetingDaoDatabase implements MeetingDao {
	
	private Connection conn;

	public MeetingDaoDatabase(Connection conn) {
		this.conn = conn;
		
	}

	@Override
	public void bookMeeting(MeetingAvailability meetingAvailability, Parent parent) throws MeetingDaoException, ParentDaoException, MeetingAvailabilityDaoException {
		DaoUtils.checkParentExist(parent, conn);
		DaoUtils.checkMeetingAvailabilityExist(meetingAvailability, conn);
		
		String query = "INSERT INTO Meetings (date, hour, id_teacher, id_parent) VALUES (?, ?, ?, ?);";
		
		try (PreparedStatement stmt = conn.prepareStatement(query)){
			stmt.setDate(1, Date.valueOf(meetingAvailability.getDate()));
			stmt.setString(2, meetingAvailability.getHour().toString());
			stmt.setInt(3, meetingAvailability.getTeacher().getId());
			stmt.setInt(4, parent.getId());
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			throw new MeetingDaoException("Error while booking meeting");
		}
	}

	@Override
	public Iterator<Meeting> getAllMeetingsByParent(Parent parent) throws MeetingDaoException, ParentDaoException {
		DaoUtils.checkParentExist(parent, conn);
		MeetingAvailabilityDaoDatabase meetingAvailabilityDaoDatabase = new MeetingAvailabilityDaoDatabase(conn);
		TeacherDaoDatabase teacherDaoDatabase = new TeacherDaoDatabase(conn);
		
		List<Meeting> meetings = new ArrayList<>();
		String query = "SELECT * FROM Meetings WHERE id_parent = ?";
		
		try(PreparedStatement stmt = conn.prepareStatement(query)){
			stmt.setInt(1, parent.getId());
			
			try(ResultSet rs = stmt.executeQuery()){
				Teacher teacher = teacherDaoDatabase.getTeacherById(rs.getInt("id_teacher"));
				
				MeetingAvailability meetingAvailability = meetingAvailabilityDaoDatabase.getMeetingAvailabilityByDateHourTeacher(
						rs.getDate("date").toLocalDate(), LocalTime.parse(rs.getString("hour")), 
						teacher);
				
				meetings.add(new Meeting(parent, meetingAvailability));
				
			} catch (TeacherDaoException | MeetingAvailabilityDaoException e) {
				throw new MeetingDaoException("Error getting meetings data");
			}
			
		} catch (SQLException e) {
			throw new MeetingDaoException("Error getting meetings data");
		}
		return meetings.iterator();
	}

	@Override
	public Iterator<Meeting> getMeetingsByTeacher(Teacher teacher) throws DaoConnectionException, TeacherDaoException, MeetingDaoException {
		DaoUtils.checkTeacherExist(teacher, conn);
		
		MeetingAvailabilityDaoDatabase meetingAvailabilityDaoDatabase = new MeetingAvailabilityDaoDatabase(conn);
		ParentDaoDatabase parentDaoDatabase = new ParentDaoDatabase(conn);
		
		List<Meeting> meetings = new ArrayList<>();
		String query = "SELECT * FROM Meetings WHERE id_teacher = ?";
		
		try(PreparedStatement stmt = conn.prepareStatement(query)){
			stmt.setInt(1, teacher.getId());
			
			try(ResultSet rs = stmt.executeQuery()){
				Parent parent = parentDaoDatabase.getParentById(rs.getInt("id_parent"));
				
				MeetingAvailability meetingAvailability = meetingAvailabilityDaoDatabase.getMeetingAvailabilityByDateHourTeacher(
						rs.getDate("date").toLocalDate(), LocalTime.parse(rs.getString("hour")), 
						teacher);
				
				meetings.add(new Meeting(parent, meetingAvailability));
			} catch (MeetingAvailabilityDaoException | ParentDaoException | StudentDaoException e) {
				throw new MeetingDaoException("Error getting meetings data");
			}
			
		} catch (SQLException e) {
			throw new MeetingDaoException("Error getting meetings data");
		}
		return meetings.iterator();
	}

}
