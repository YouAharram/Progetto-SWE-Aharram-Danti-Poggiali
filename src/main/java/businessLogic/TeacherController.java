package businessLogic;

import java.time.LocalDate;


import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DaoExceptions.AbsenceDaoException;
import DaoExceptions.DaoConnectionException;
import DaoExceptions.DisciplinaryReportException;
import DaoExceptions.GradeDaoException;
import DaoExceptions.HomeworkDaoException;
import DaoExceptions.LessonDaoException;
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
import domainModel.SchoolClass;
import domainModel.Student;
import domainModel.Teacher;
import domainModel.TeachingAssignment;
import DaoExceptions.MeetingAvailabilityDaoException;
import DaoExceptions.MeetingDaoException;
import strategyForGrade.GradeAverageStrategy;

public final class TeacherController {
	private static final int MINIMUM_GRADE = 1;
	private static final int MAXIMUM_GRADE = 10;
	private DaoFactory daoFactory;
	private Teacher teacher;

	public TeacherController(Teacher teacher, DaoFactory daoFactory)
			throws StudentDaoException, TeacherDaoException, DaoConnectionException {
		this.teacher = teacher;
		this.daoFactory = daoFactory;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	// TEACHINGS

	public Iterator<TeachingAssignment> getAllMyTeachings() throws TeachingAssignmentDaoException, TeacherDaoException, DaoConnectionException {
		return daoFactory.createTeachingAssignmentDao().getAllTeacherTeachings(teacher);
	}

	// STUDENTS

	public Iterator<Student> getStudentsByClass(SchoolClass schoolClass)
			throws StudentDaoException, DaoConnectionException, SchoolClassDaoException {
		return daoFactory.createStudentDao().getStudentsByClass(schoolClass);
	}

	// GRADES

	public Iterator<Grade> getAllStudentGradesByTeaching(Student student, TeachingAssignment teaching)
			throws GradeDaoException, DaoConnectionException, StudentDaoException, TeachingAssignmentDaoException {
		return daoFactory.createGradeDao().getStudentGradesByTeaching(student, teaching);
	}

	public void assignGradeToStudentInDate(double value, String description, TeachingAssignment teaching,
			Student student, LocalDate date)
			throws GradeDaoException, InvalidGradeValueException, DaoConnectionException, StudentDaoException {
		if (value >= MINIMUM_GRADE && value <= MAXIMUM_GRADE) {
			daoFactory.createGradeDao().addNewGrade(value, description, teaching, student, date);
		} else {
			throw new InvalidGradeValueException();
		}
	}

	public void assignGradeToStudentInDateWithWeight(double value, int weight, String description,
			TeachingAssignment teaching, Student student, LocalDate date) throws GradeDaoException,
			NegativeWeightException, InvalidGradeValueException, DaoConnectionException, StudentDaoException {
		if ((value >= MINIMUM_GRADE && value <= MAXIMUM_GRADE) && (weight >= 0)) {
			daoFactory.createGradeDao().addNewGradeWithWeight(value, weight, description, teaching, student, date);
		} else {
			if (weight < 0)
				throw new NegativeWeightException();
			else
				throw new InvalidGradeValueException();
		}
	}

	public void editGradeValue(Grade oldGrade, double value) throws GradeDaoException, DaoConnectionException {
		daoFactory.createGradeDao().editGradeValue(oldGrade, value);
	}

	public void editGradeWeight(Grade oldGrade, int weight) throws GradeDaoException, DaoConnectionException {
		daoFactory.createGradeDao().editGradeWeight(oldGrade, weight);
	}

	public void editGradeDescription(Grade oldGrade, String description)
			throws GradeDaoException, DaoConnectionException {
		daoFactory.createGradeDao().editGradeDescription(oldGrade, description);
	}

	public void deleteGrade(Grade grade) throws GradeDaoException, DaoConnectionException {
		daoFactory.createGradeDao().deleteGrade(grade);
	}

	public double calculateStudentTeachingGradeAverage(Student student, TeachingAssignment teaching,
			GradeAverageStrategy gradeAverageStrategy)
			throws GradeDaoException, DaoConnectionException, StudentDaoException, TeachingAssignmentDaoException {
		return gradeAverageStrategy.getAverage(getAllStudentGradesByTeaching(student, teaching));
	}

	// REPORTS

	public Iterator<DisciplinaryReport> getStudentDisciplinaryReports(Student student)
			throws DisciplinaryReportException, DaoConnectionException, StudentDaoException {
		return daoFactory.createDisciplinaryReportDao().getDisciplinaryReportsByStudent(student);
	}

	public void assignDisciplinaryReportToStudentInDate(Student student, String description, LocalDate date)
			throws DisciplinaryReportException, DaoConnectionException, StudentDaoException, TeacherDaoException {
		daoFactory.createDisciplinaryReportDao().addNewReport(teacher, student, description, date);
	}

	public void deleteDisciplinaryReport(DisciplinaryReport report)
			throws IllegalReportAccessException, DisciplinaryReportException, DaoConnectionException {
		if (report.getTeacher().equals(teacher)) {
			daoFactory.createDisciplinaryReportDao().deleteReport(report);
		} else {
			throw new IllegalReportAccessException();
		}
	}

	// ABSENCES

	public Iterator<Absence> getAbsencesByClassInDate(SchoolClass schoolClass, LocalDate date)
			throws AbsenceDaoException, DaoConnectionException, SchoolClassDaoException {
		return daoFactory.createAbsenceDao().getAbsencesByClassInDate(schoolClass, date);
	}

	public Iterator<Absence> getAbsencesByStudent(Student student)
			throws AbsenceDaoException, DaoConnectionException, StudentDaoException {
		return daoFactory.createAbsenceDao().getAbsencesByStudent(student);
	}

	public void assignAbsenceToStudentInDate(Student student, LocalDate date)
			throws AbsenceDaoException, DaoConnectionException, StudentDaoException {
		daoFactory.createAbsenceDao().addAbsence(student, date);
	}

	public void deleteAbsence(Student student, LocalDate date)
			throws AbsenceDaoException, DaoConnectionException, StudentDaoException {
		daoFactory.createAbsenceDao().deleteAbsence(student, date);
	}

	// HOMEWORKS

	public void assignNewHomework(TeachingAssignment teaching, LocalDate date, String description,
			LocalDate subissionDate) throws HomeworkDaoException, TeachingAssignmentDaoException, DaoConnectionException {
		daoFactory.createHomeworkDao().addHomework(teaching, date, description, subissionDate);
	}

	public void editHomeworkDescription(Homework homework, String description)
			throws HomeworkDaoException, IllegalHomeworkAccessException, DaoConnectionException {
		if (homework.getTeaching().getTeacher().equals(teacher)) {
			daoFactory.createHomeworkDao().editHomeworkDescription(homework, description);
		} else {
			throw new IllegalHomeworkAccessException();
		}
	}

	public void editHomeworkSubmissionDate(Homework homework, LocalDate date)
			throws HomeworkDaoException, IllegalHomeworkAccessException, DaoConnectionException {
		if (homework.getTeaching().getTeacher().equals(teacher)) {
			daoFactory.createHomeworkDao().editHomeworkSubmissionDate(homework, date);
		} else {
			throw new IllegalHomeworkAccessException();
		}
	}

	public Iterator<Homework> getClassHomeworksSubmissionDate(LocalDate date, SchoolClass schoolClass)
			throws DaoConnectionException, HomeworkDaoException, SchoolClassDaoException {
		return daoFactory.createHomeworkDao().getHomeworksBySubmissionDate(date, schoolClass);
	}

	public void deleteHomework(Homework homework) throws IllegalHomeworkAccessException, HomeworkDaoException, DaoConnectionException {
		if (homework.getTeaching().getTeacher().equals(teacher)) {
			daoFactory.createHomeworkDao().deleteHomework(homework);
		} else {
			throw new IllegalHomeworkAccessException();
		}
	}

	// LESSONS

	public void addNewLesson(TeachingAssignment teaching, LocalDate date, String description, LocalTime startHour,
			LocalTime endHour) throws LessonDaoException, DaoConnectionException {
		daoFactory.createLessonDao().addLesson(teaching, date, description, startHour, endHour);
	}

	public void editLessonDescription(Lesson lesson, String description)
			throws LessonDaoException, DaoConnectionException, IllegalLessonAccessException {
		if (lesson.getTeaching().getTeacher().equals(teacher)) {
			daoFactory.createLessonDao().editLessonDescription(lesson, description);
		} else {
			throw new IllegalLessonAccessException();
		}
	}

	public void editLessonDateTime(Lesson lesson, LocalDate date, LocalTime startHour, LocalTime endHour)
			throws LessonDaoException, DaoConnectionException, IllegalLessonAccessException {
		if (lesson.getTeaching().getTeacher().equals(teacher)) {
			daoFactory.createLessonDao().editLessonDateTime(lesson, date, startHour, endHour);
		} else {
			throw new IllegalLessonAccessException();
		}
	}

	public Iterator<Lesson> getClassLessonsInDay(LocalDate date, SchoolClass schoolClass)
			throws DaoConnectionException, LessonDaoException, SchoolClassDaoException {
		return daoFactory.createLessonDao().getLessonsInDay(date, schoolClass);
	}

	public void deleteLesson(Lesson lesson) throws LessonDaoException, IllegalLessonAccessException, DaoConnectionException {
		if (lesson.getTeaching().getTeacher().equals(teacher)) {
			daoFactory.createLessonDao().deleteLesson(lesson);
		} else {
			throw new IllegalLessonAccessException();
		}
	}

	// MEETING

	public void addNewMeetingAvailabilityInDate(LocalDate date, LocalTime time)
			throws TeacherDaoException, MeetingAvailabilityDaoException, DaoConnectionException {
		daoFactory.createMeetingAvailabilityDao().addMeetingAvailabilityInDate(teacher, date, time);
	}

	public Iterator<MeetingAvailability> getMeetingAvailabilities()
			throws TeacherDaoException, MeetingAvailabilityDaoException, DaoConnectionException {
		List<MeetingAvailability> unboockedMeetings = new ArrayList<MeetingAvailability>();
		Iterator<MeetingAvailability> allMeetings = daoFactory.createMeetingAvailabilityDao()
				.getAllMeetingsAvaialabilityByTeacher(teacher);
		while (allMeetings.hasNext()) {
			MeetingAvailability meetingAvailability = allMeetings.next();
			if (!meetingAvailability.isBooked()) {
				unboockedMeetings.add(meetingAvailability);
			}
		}
		return unboockedMeetings.iterator();
	}

	public Iterator<Meeting> getBookedMeetings() throws DaoConnectionException, TeacherDaoException, MeetingDaoException {
		return daoFactory.createMeetingDao().getMeetingsByTeacher(teacher);
	}

	public void deleteMeetingAvailability(MeetingAvailability meetingAvailability)
			throws MeetingAlreadyBookedException, MeetingAvailabilityDaoException, TeacherDaoException, DaoConnectionException {
		if (!meetingAvailability.isBooked()) {
			daoFactory.createMeetingAvailabilityDao().deleteMeetingAvailability(meetingAvailability);
		} else {
			throw new MeetingAlreadyBookedException();
		}
	}

	public class MeetingAlreadyBookedException extends Exception {
		private static final long serialVersionUID = 1L;
	}

	public class IllegalHomeworkAccessException extends Exception {
		private static final long serialVersionUID = 1L;
	}

	public class IllegalLessonAccessException extends Exception {
		private static final long serialVersionUID = 1L;
	}

	public class IllegalReportAccessException extends Exception {
		private static final long serialVersionUID = 1L;
	}

	public class InvalidGradeValueException extends Exception {

		private static final long serialVersionUID = 1L;

	}
	public class NegativeWeightException extends Exception {

		private static final long serialVersionUID = 1L;

	}

}
