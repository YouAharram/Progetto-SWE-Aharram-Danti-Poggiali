package abstractOrm;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Iterator;

import domainModel.MeetingAvailability;
import domainModel.Teacher;
import exceptions.MeetingAvailabilityDaoException;
import exceptions.TeacherDaoException;

public interface MeetingAvailabilityDao {

	void editBooking(MeetingAvailability meetingAvailability) throws MeetingAvailabilityDaoException;

	Iterator<MeetingAvailability> getAllMeetingsAvaialabilityByTeacher(Teacher teacher) throws TeacherDaoException, MeetingAvailabilityDaoException;

	void deleteMeetingAvailability(MeetingAvailability meetingAvailability) throws MeetingAvailabilityDaoException, TeacherDaoException;

	void addMeetingAvailabilityInDate(Teacher teacher, LocalDate date, LocalTime hour) throws TeacherDaoException, MeetingAvailabilityDaoException;

}
