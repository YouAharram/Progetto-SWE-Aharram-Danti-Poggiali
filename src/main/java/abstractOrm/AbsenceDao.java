package abstractOrm;

import java.time.LocalDate;
import java.util.Iterator;

import domainModel.Absence;
import domainModel.SchoolClass;
import domainModel.Student;
import exceptions.AbsenceDaoException;
import exceptions.SchoolClassDaoException;
import exceptions.StudentDaoException;

public interface AbsenceDao {
	
	void addAbsence(Student student, LocalDate date) throws AbsenceDaoException, StudentDaoException;

	void deleteAbsence(Student student, LocalDate date) throws AbsenceDaoException, StudentDaoException;

	Iterator<Absence> getAbsencesByClassInDate(SchoolClass schoolClass, LocalDate date) throws AbsenceDaoException, SchoolClassDaoException;

	Iterator<Absence> getAbsencesByStudent(Student student) throws AbsenceDaoException, StudentDaoException;

	boolean checkStudentAttendanceInDay(Student student, LocalDate date) throws AbsenceDaoException, StudentDaoException;

	void justifyAbsence(Absence absence) throws AbsenceDaoException;

}