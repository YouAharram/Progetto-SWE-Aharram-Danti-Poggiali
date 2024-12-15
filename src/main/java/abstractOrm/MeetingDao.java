package abstractOrm;

import java.util.Iterator;

import domainModel.Meeting;
import domainModel.MeetingAvailability;
import domainModel.Parent;
import domainModel.Teacher;
import exceptions.DaoConnectionException;
import exceptions.MeetingAvailabilityDaoException;
import exceptions.MeetingDaoException;
import exceptions.ParentDaoException;
import exceptions.TeacherDaoException;

public interface MeetingDao {

	void bookMeeting(MeetingAvailability meetingAvailability, Parent parent) throws MeetingDaoException, ParentDaoException, MeetingAvailabilityDaoException;

	Iterator<Meeting> getAllMeetingsByParent(Parent parent) throws MeetingDaoException, ParentDaoException, TeacherDaoException, MeetingAvailabilityDaoException;

	Iterator<Meeting> getMeetingsByTeacher(Teacher teacher) throws DaoConnectionException, TeacherDaoException, MeetingDaoException;

}
