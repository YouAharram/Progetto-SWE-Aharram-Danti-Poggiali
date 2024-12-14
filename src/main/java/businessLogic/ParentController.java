package businessLogic;

import java.time.LocalDate; 


import java.util.Iterator;

import DaoExceptions.AbsenceDaoException;
import DaoExceptions.DaoConnectionException;
import DaoExceptions.DisciplinaryReportException;
import DaoExceptions.GradeDaoException;
import DaoExceptions.HomeworkDaoException;
import DaoExceptions.LessonDaoException;
import DaoExceptions.ParentDaoException;
import DaoExceptions.SchoolClassDaoException;
import DaoExceptions.StudentDaoException;
import DaoExceptions.TeacherDaoException;
import DaoExceptions.TeachingAssignmentDaoException;
import daoFactory.DaoFactory;
import domainModel.Absence;
import domainModel.DisciplinaryReport;
import domainModel.Grade;
import domainModel.Homework;
import domainModel.Lesson;
import domainModel.Meeting;
import domainModel.MeetingAvailability;
import domainModel.Parent;
import domainModel.Student;
import domainModel.Teacher;
import domainModel.TeachingAssignment;
import DaoExceptions.MeetingAvailabilityDaoException;
import DaoExceptions.MeetingDaoException;
import strategyForGrade.GradeAverageStrategy;

public final class ParentController {
	private StudentController studentController;
	private Parent parent;
	private DaoFactory daoFactory;

	public ParentController(Parent parent, DaoFactory daoFactory)
			throws StudentDaoException, DaoConnectionException, ParentDaoException {
		this.parent = parent;
		this.daoFactory = daoFactory;
		Student student = parent.getStudent();
		studentController = new StudentController(student, daoFactory);
	}

	public Parent getParent() {
		return parent;
	}
	
	public Iterator<TeachingAssignment> getTeachings() throws TeachingAssignmentDaoException, DaoConnectionException, StudentDaoException {
		return studentController.getTeachings();
	}

	public Iterator<Grade> getGradesByTeaching(TeachingAssignment teaching)
			throws GradeDaoException, DaoConnectionException, StudentDaoException, TeachingAssignmentDaoException {
		return studentController.getGradesByTeaching(teaching);
	}

	public Iterator<Grade> getAllStudentGrades() throws GradeDaoException, DaoConnectionException, StudentDaoException {
		return studentController.getAllStudentGrades();
	}
	
	public double calculateTeachingGradeAverage(TeachingAssignment teaching, GradeAverageStrategy gradeAverageStrategy)
			throws GradeDaoException, DaoConnectionException, StudentDaoException, TeachingAssignmentDaoException {
		return studentController.calculateTeachingGradeAverage(teaching, gradeAverageStrategy);
	}

	public double calculateTotalGradeAverage(GradeAverageStrategy gradeAverageStrategy)
			throws GradeDaoException, DaoConnectionException, StudentDaoException {
		return studentController.calculateTotalGradeAverage(gradeAverageStrategy);
	}
	
	public Iterator<DisciplinaryReport> getDisciplinaryReports()
			throws DisciplinaryReportException, DaoConnectionException, StudentDaoException {
		return studentController.getDisciplinaryReports();
	}

	public Iterator<Homework> getHomeworksBySubmissionDate(LocalDate date) throws DaoConnectionException, HomeworkDaoException, SchoolClassDaoException {
		return studentController.getHomeworksBySubmissionDate(date);
	}

	public Iterator<Lesson> getLessonInDate(LocalDate date) throws DaoConnectionException, LessonDaoException, SchoolClassDaoException {
		return studentController.getLessonInDate(date);
	}

	public Iterator<MeetingAvailability> getAllMeetingsAvaialabilityByTeacher(Teacher teacher) throws TeacherDaoException, MeetingAvailabilityDaoException, DaoConnectionException {
		return daoFactory.createMeetingAvailabilityDao().getAllMeetingsAvaialabilityByTeacher(teacher);
	}

	public void bookAMeeting(MeetingAvailability meetingAvailability) throws AlreadyBookedMeetingException, MeetingAvailabilityDaoException, MeetingDaoException, ParentDaoException, DaoConnectionException {
		if (meetingAvailability.isBooked()) {
			throw new AlreadyBookedMeetingException();
		}
		daoFactory.createMeetingDao().bookMeeting(meetingAvailability, parent);
		daoFactory.createMeetingAvailabilityDao().editBooking(meetingAvailability);
	}

	public Iterator<Meeting> getAllMyMeetings() throws MeetingDaoException, ParentDaoException, TeacherDaoException, MeetingAvailabilityDaoException, DaoConnectionException {
		return daoFactory.createMeetingDao().getAllMeetingsByParent(parent);
	}

	public Iterator<Absence> getAllStudentAbsences() throws AbsenceDaoException, DaoConnectionException, StudentDaoException {
		return studentController.getAllStudentAbsences();
	}

	public void justifyAbsence(Absence absence) throws AbsenceDaoException, DaoConnectionException {
		daoFactory.createAbsenceDao().justifyAbsence(absence);
	}
	
	public boolean checkStudentAttendanceInDay(LocalDate date) throws AbsenceDaoException, DaoConnectionException, StudentDaoException {
		return studentController.checkStudentAttendanceInDay(date);
	}

	public class AlreadyBookedMeetingException extends Exception {

		private static final long serialVersionUID = 1L;

	}
}
