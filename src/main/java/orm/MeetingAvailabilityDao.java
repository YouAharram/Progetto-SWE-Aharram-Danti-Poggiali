package orm;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Iterator;

import DaoExceptions.TeacherDaoException;
import domainModel.MeetingAvailability;
import domainModel.Teacher;
import orm.MeetingAvailabilityDaoDatabase.MeetingAvailabilityDaoException;

public interface MeetingAvailabilityDao {

	void editBooking(MeetingAvailability meetingAvailability) throws MeetingAvailabilityDaoException;

	Iterator<MeetingAvailability> getAllMeetingsAvaialabilityByTeacher(Teacher teacher) throws TeacherDaoException, MeetingAvailabilityDaoException;

	void deleteMeetingAvailability(MeetingAvailability meetingAvailability) throws MeetingAvailabilityDaoException, TeacherDaoException;

	void addMeetingAvailabilityInDate(Teacher teacher, LocalDate date, LocalTime hour) throws TeacherDaoException, MeetingAvailabilityDaoException;

}
