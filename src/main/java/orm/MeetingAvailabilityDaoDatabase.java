package orm;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;

import abstractOrm.MeetingAvailabilityDao;
import domainModel.MeetingAvailability;
import domainModel.Teacher;
import exceptions.MeetingAvailabilityDaoException;
import exceptions.TeacherDaoException;

public class MeetingAvailabilityDaoDatabase implements MeetingAvailabilityDao {
	
	private Connection conn;

	public MeetingAvailabilityDaoDatabase(Connection conn) {
		this.conn = conn;
		
	}

	@Override
	public void addMeetingAvailabilityInDate(Teacher teacher, LocalDate date, LocalTime hour) throws TeacherDaoException, MeetingAvailabilityDaoException {
		DaoUtils.checkTeacherExist(teacher, conn);
		
		String query = "INSERT INTO MeetingsAvailability (date, hour, id_teacher)"
				+ "VALUES(?, ?, ?)";

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setDate(1, Date.valueOf(date));
			stmt.setString(2, hour.toString());
			stmt.setInt(3, teacher.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new MeetingAvailabilityDaoException("Database error while entering meeting availability data");
		}
		
	}

	@Override
	public void editBooking(MeetingAvailability meetingAvailability) throws MeetingAvailabilityDaoException {
		String query = "UPDATE MeetingsAvailability SET isBooked = 1 "
				+ "WHERE date = ? AND hour = ? AND id_teacher = ?";

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setDate(1, Date.valueOf(meetingAvailability.getDate()));
			stmt.setString(2, meetingAvailability.getHour().toString());
			stmt.setInt(3, meetingAvailability.getTeacher().getId());
			stmt.executeUpdate();
		}
		catch(SQLException e) {
			throw new MeetingAvailabilityDaoException("Database error while editing meeting availability data");
			
		}
	}

	
	@Override
	public Iterator<MeetingAvailability> getAllMeetingsAvaialabilityByTeacher(Teacher teacher) throws TeacherDaoException, MeetingAvailabilityDaoException {
		ArrayList<MeetingAvailability> meetingAvailabilities = new ArrayList<>();

	    DaoUtils.checkTeacherExist(teacher, conn);

	    String query = "SELECT * FROM MeetingsAvailability WHERE id_teacher = ?";
	    try (PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setInt(1, teacher.getId());          

	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                LocalDate date = rs.getDate("date").toLocalDate(); 
	                LocalTime hour = LocalTime.parse(rs.getString("hour"));
	                boolean isBooked = rs.getBoolean("isBooked");
	                meetingAvailabilities.add(new MeetingAvailability(teacher, date, hour, isBooked));
	            }
	        }
	    } catch (SQLException e) {
	        throw new MeetingAvailabilityDaoException("Database error while fetching meeting availabilities data");
	    }

	    return meetingAvailabilities.iterator();
	}

	@Override
	public void deleteMeetingAvailability(MeetingAvailability meetingAvailability) throws MeetingAvailabilityDaoException, TeacherDaoException {
		DaoUtils.checkTeacherExist(meetingAvailability.getTeacher(), conn);
		
		String query = "DELETE FROM MeetingsAvailability "
				+ "WHERE date = ? AND hour = ? AND id_teacher = ?";

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setDate(1, Date.valueOf(meetingAvailability.getDate()));
			stmt.setString(2, meetingAvailability.getHour().toString());
			stmt.setInt(3, meetingAvailability.getTeacher().getId());
			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected != 1) {
				throw new MeetingAvailabilityDaoException("Meeting availability not present");
			}
		}
		catch(SQLException e) {
			throw new MeetingAvailabilityDaoException("Database error while editing meeting availability data");
			
		}

	}
	
	
	public MeetingAvailability getMeetingAvailabilityByDateHourTeacher(LocalDate date, LocalTime hour, Teacher teacher) throws TeacherDaoException, MeetingAvailabilityDaoException {
		DaoUtils.checkTeacherExist(teacher, conn);
		String query = "SELECT isBooked FROM MeetingsAvailability WHERE date = ? AND hour = ? AND id_teacher = ?";
		try (PreparedStatement stmt = conn.prepareStatement(query)){
			stmt.setDate(1, Date.valueOf(date));
			stmt.setString(2, hour.toString());
			stmt.setInt(3, teacher.getId());
			
			try (ResultSet rs = stmt.executeQuery()){
				if(rs.next()) {
					return new MeetingAvailability(teacher, date, hour, rs.getBoolean("isBooked"));
				}
				else {
					throw new MeetingAvailabilityDaoException("Meeting availability doesn't exist");
				}
			}
		}
		catch (SQLException e) {
			throw new MeetingAvailabilityDaoException("Database error while getting meeting availability data");
		}
		
	}
	
}
