package businessLogic;

import java.time.LocalDate;
import java.util.Iterator;

import abstractOrm.DaoFactory;
import domainModel.Absence;
import domainModel.DisciplinaryReport;
import domainModel.Grade;
import domainModel.GradeAverageStrategy;
import domainModel.Homework;
import domainModel.Lesson;
import domainModel.Student;
import domainModel.TeachingAssignment;
import exceptions.AbsenceDaoException;
import exceptions.DaoConnectionException;
import exceptions.DisciplinaryReportException;
import exceptions.GradeDaoException;
import exceptions.HomeworkDaoException;
import exceptions.LessonDaoException;
import exceptions.SchoolClassDaoException;
import exceptions.StudentDaoException;
import exceptions.TeachingAssignmentDaoException;

public final class StudentController implements UserController{
	private Student student;
	private DaoFactory daoFactory;

	public StudentController(Student student, DaoFactory daoFactory) {
		this.student = student;
		this.daoFactory = daoFactory;
	}
	
	public Student getStudent() {
		return student;
	}
	
	public Iterator<TeachingAssignment> getTeachings() throws TeachingAssignmentDaoException, DaoConnectionException, StudentDaoException {
		return daoFactory.createTeachingAssignmentDao().getAllStudentTeachings(student);
	}
	
	public Iterator<Grade> getGradesByTeaching(TeachingAssignment teaching) throws GradeDaoException, DaoConnectionException, StudentDaoException, TeachingAssignmentDaoException {
		return daoFactory.createGradeDao().getStudentGradesByTeaching(student, teaching);
	}
	
	public Iterator<Grade> getAllStudentGrades() throws GradeDaoException, DaoConnectionException, StudentDaoException {
		return daoFactory.createGradeDao().getAllStudentGrades(student);
	}
	
	public double calculateTeachingGradeAverage(TeachingAssignment teaching, GradeAverageStrategy gradeAverageStrategy) throws GradeDaoException, DaoConnectionException, StudentDaoException, TeachingAssignmentDaoException {
		return gradeAverageStrategy.getAverage(getGradesByTeaching(teaching));
	}
	
	public double calculateTotalGradeAverage(GradeAverageStrategy gradeAverageStrategy) throws GradeDaoException, DaoConnectionException, StudentDaoException {
		return gradeAverageStrategy.getAverage(getAllStudentGrades());
	}
	
	public Iterator<DisciplinaryReport> getDisciplinaryReports() throws DisciplinaryReportException, DaoConnectionException, StudentDaoException {
		return daoFactory.createDisciplinaryReportDao().getDisciplinaryReportsByStudent(student);
	}
	
	public Iterator<Homework> getHomeworksBySubmissionDate(LocalDate date) throws DaoConnectionException, HomeworkDaoException, SchoolClassDaoException {
		return daoFactory.createHomeworkDao().getHomeworksBySubmissionDate(date, student.getSchoolClass());
	}
	
	public Iterator<Lesson> getLessonInDate(LocalDate date) throws DaoConnectionException, LessonDaoException, SchoolClassDaoException {
		return daoFactory.createLessonDao().getLessonsInDay(date, student.getSchoolClass());
	}
	
	public Iterator<Absence> getAllStudentAbsences() throws AbsenceDaoException, DaoConnectionException, StudentDaoException {
		return daoFactory.createAbsenceDao().getAbsencesByStudent(student);
	}
 	
	public boolean checkStudentAttendanceInDay(LocalDate date) throws AbsenceDaoException, DaoConnectionException, StudentDaoException {
		return daoFactory.createAbsenceDao().checkStudentAttendanceInDay(student, date);
	}

}