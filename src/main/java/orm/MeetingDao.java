package orm;

import java.util.Iterator;

import DaoExceptions.DaoConnectionException;
import DaoExceptions.MeetingAvailabilityDaoException;
import DaoExceptions.MeetingDaoException;
import DaoExceptions.ParentDaoException;
import DaoExceptions.TeacherDaoException;
import domainModel.Meeting;
import domainModel.MeetingAvailability;
import domainModel.Parent;
import domainModel.Teacher;

public interface MeetingDao {

	void bookMeeting(MeetingAvailability meetingAvailability, Parent parent) throws MeetingDaoException, ParentDaoException, MeetingAvailabilityDaoException;

	Iterator<Meeting> getAllMeetingsByParent(Parent parent) throws MeetingDaoException, ParentDaoException, TeacherDaoException, MeetingAvailabilityDaoException;

	Iterator<Meeting> getMeetingsByTeacher(Teacher teacher) throws DaoConnectionException, TeacherDaoException, MeetingDaoException;

}
