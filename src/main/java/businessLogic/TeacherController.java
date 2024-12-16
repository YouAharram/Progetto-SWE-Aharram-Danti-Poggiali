package businessLogic;

import java.time.LocalDate;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import abstractOrm.DaoFactory;
import domainModel.Absence;
import domainModel.DisciplinaryReport;
import domainModel.Grade;
import domainModel.GradeAverageStrategy;
import domainModel.Homework;
import domainModel.Lesson;
import domainModel.Meeting;
import domainModel.MeetingAvailability;
import domainModel.SchoolClass;
import domainModel.Student;
import domainModel.Teacher;
import domainModel.TeachingAssignment;
import exceptions.AbsenceDaoException;
import exceptions.DaoConnectionException;
import exceptions.DisciplinaryReportException;
import exceptions.GradeDaoException;
import exceptions.HomeworkDaoException;
import exceptions.LessonDaoException;
import exceptions.MeetingAvailabilityDaoException;
import exceptions.MeetingDaoException;
import exceptions.SchoolClassDaoException;
import exceptions.StudentDaoException;
import exceptions.TeacherDaoException;
import exceptions.TeachingAssignmentDaoException;

public final class TeacherController {
	private static final int MINIMUM_GRADE = 1;
	private static final int MAXIMUM_GRADE = 10;
	private DaoFactory daoFactory;
	private Teacher teacher;

	public TeacherController(Teacher teacher, DaoFactory daoFactory) {
		this.teacher = teacher;
		this.daoFactory = daoFactory;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public Iterator<TeachingAssignment> getAllMyTeachings()
			throws TeachingAssignmentDaoException, TeacherDaoException, DaoConnectionException {
		return daoFactory.createTeachingAssignmentDao().getAllTeacherTeachings(teacher);
	}

	public Iterator<Student> getStudentsByClass(SchoolClass schoolClass)
			throws StudentDaoException, DaoConnectionException, SchoolClassDaoException {
		return daoFactory.createStudentDao().getStudentsByClass(schoolClass);
	}

	public Iterator<Grade> getAllStudentGradesByTeaching(Student student, TeachingAssignment teaching)
			throws GradeDaoException, DaoConnectionException, StudentDaoException, TeachingAssignmentDaoException {
		return daoFactory.createGradeDao().getStudentGradesByTeaching(student, teaching);
	}

	public void assignGradeToStudentInDate(double value, String description, TeachingAssignment teaching,
			Student student, LocalDate date)
			throws GradeDaoException, InvalidGradeValueException, DaoConnectionException, StudentDaoException {
		validateGradeValue(value);
		daoFactory.createGradeDao().addNewGrade(value, description, teaching, student, date);
	}

	public void assignGradeToStudentInDateWithWeight(double value, int weight, String description,
			TeachingAssignment teaching, Student student, LocalDate date) throws GradeDaoException,
			NegativeWeightException, InvalidGradeValueException, DaoConnectionException, StudentDaoException {
		validateWeight(weight);
		validateGradeValue(value);
		daoFactory.createGradeDao().addNewGradeWithWeight(value, weight, description, teaching, student, date);
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
		checkTeacher(report.getTeacher(), () -> {
			throw new IllegalReportAccessException();
		});
		daoFactory.createDisciplinaryReportDao().deleteReport(report);
	}

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

	public void assignNewHomework(TeachingAssignment teaching, LocalDate date, String description,
			LocalDate subissionDate)
			throws HomeworkDaoException, TeachingAssignmentDaoException, DaoConnectionException {
		daoFactory.createHomeworkDao().addHomework(teaching, date, description, subissionDate);
	}

	public void editHomeworkDescription(Homework homework, String description)
			throws HomeworkDaoException, IllegalHomeworkAccessException, DaoConnectionException {
		checkTeacher(homework.getTeaching().getTeacher(), () -> {
			throw new IllegalHomeworkAccessException();
		});
		daoFactory.createHomeworkDao().editHomeworkDescription(homework, description);
	}

	public void editHomeworkSubmissionDate(Homework homework, LocalDate date)
			throws HomeworkDaoException, IllegalHomeworkAccessException, DaoConnectionException {
		checkTeacher(homework.getTeaching().getTeacher(), () -> {
			throw new IllegalHomeworkAccessException();
		});
		daoFactory.createHomeworkDao().editHomeworkSubmissionDate(homework, date);
	}

	public Iterator<Homework> getClassHomeworksSubmissionDate(LocalDate date, SchoolClass schoolClass)
			throws DaoConnectionException, HomeworkDaoException, SchoolClassDaoException {
		return daoFactory.createHomeworkDao().getHomeworksBySubmissionDate(date, schoolClass);
	}

	public void deleteHomework(Homework homework)
			throws IllegalHomeworkAccessException, HomeworkDaoException, DaoConnectionException {
		checkTeacher(homework.getTeaching().getTeacher(), () -> {
			throw new IllegalHomeworkAccessException();
		});
		daoFactory.createHomeworkDao().deleteHomework(homework);
	}

	public void addNewLesson(TeachingAssignment teaching, LocalDate date, String description, LocalTime startHour,
			LocalTime endHour) throws LessonDaoException, DaoConnectionException {
		daoFactory.createLessonDao().addLesson(teaching, date, description, startHour, endHour);
	}

	public void editLessonDescription(Lesson lesson, String description)
			throws LessonDaoException, DaoConnectionException, IllegalLessonAccessException {
		checkTeacher(lesson.getTeaching().getTeacher(), () -> {
			throw new IllegalLessonAccessException();
		});
		daoFactory.createLessonDao().editLessonDescription(lesson, description);
	}

	public void editLessonDateTime(Lesson lesson, LocalDate date, LocalTime startHour, LocalTime endHour)
			throws LessonDaoException, DaoConnectionException, IllegalLessonAccessException {
		checkTeacher(lesson.getTeaching().getTeacher(), () -> {
			throw new IllegalLessonAccessException();
		});
		daoFactory.createLessonDao().editLessonDateTime(lesson, date, startHour, endHour);
	}

	public Iterator<Lesson> getClassLessonsInDay(LocalDate date, SchoolClass schoolClass)
			throws DaoConnectionException, LessonDaoException, SchoolClassDaoException {
		return daoFactory.createLessonDao().getLessonsInDay(date, schoolClass);
	}

	public void deleteLesson(Lesson lesson)
			throws LessonDaoException, IllegalLessonAccessException, DaoConnectionException {
		checkTeacher(lesson.getTeaching().getTeacher(), () -> {
			throw new IllegalLessonAccessException();
		});
		daoFactory.createLessonDao().deleteLesson(lesson);
	}

	public void addNewMeetingAvailabilityInDate(LocalDate date, LocalTime time)
			throws TeacherDaoException, MeetingAvailabilityDaoException, DaoConnectionException {
		daoFactory.createMeetingAvailabilityDao().addMeetingAvailabilityInDate(teacher, date, time);
	}

	public Iterator<MeetingAvailability> getMeetingAvailabilities()
			throws TeacherDaoException, MeetingAvailabilityDaoException, DaoConnectionException {
		List<MeetingAvailability> unbookedMeetings = new ArrayList<MeetingAvailability>();
		Iterator<MeetingAvailability> allMeetings = daoFactory.createMeetingAvailabilityDao()
				.getAllMeetingsAvaialabilityByTeacher(teacher);
		allMeetings.forEachRemaining(unbookedMeetings::add);

		return unbookedMeetings.stream().filter(this::isNotBooked)
				.collect(Collectors.toList()).iterator();
	}

	public Iterator<Meeting> getBookedMeetings()
			throws DaoConnectionException, TeacherDaoException, MeetingDaoException {
		return daoFactory.createMeetingDao().getMeetingsByTeacher(teacher);
	}

	public void deleteMeetingAvailability(MeetingAvailability meetingAvailability) throws MeetingAlreadyBookedException,
			MeetingAvailabilityDaoException, TeacherDaoException, DaoConnectionException {
		if (isNotBooked(meetingAvailability))
			daoFactory.createMeetingAvailabilityDao().deleteMeetingAvailability(meetingAvailability);
		else throw new MeetingAlreadyBookedException();
	}

	private void validateGradeValue(double value) throws InvalidGradeValueException {
		if (value < MINIMUM_GRADE || value > MAXIMUM_GRADE) {
			throw new InvalidGradeValueException();
		}
	}

	private void validateWeight(int weight) throws NegativeWeightException {
		if (weight < 0) {
			throw new NegativeWeightException();
		}
	}

	private <E extends Exception> void checkTeacher(Teacher otherTeacher, ExceptionSupplier<E> exceptionSupplier)
			throws E {
		if (!teacher.equals(otherTeacher)) {
			exceptionSupplier.throwException();
		}
	}
	
	private boolean isNotBooked(MeetingAvailability meeting) {
		return !meeting.isBooked();
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

	@FunctionalInterface
	public interface ExceptionSupplier<E extends Exception> {
		void throwException() throws E;
	}
}
