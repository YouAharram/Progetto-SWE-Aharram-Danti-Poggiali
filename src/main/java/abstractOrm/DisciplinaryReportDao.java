package abstractOrm;

import java.time.LocalDate;
import java.util.Iterator;

import domainModel.DisciplinaryReport;
import domainModel.Student;
import domainModel.Teacher;
import exceptions.DisciplinaryReportException;
import exceptions.StudentDaoException;
import exceptions.TeacherDaoException;

public interface DisciplinaryReportDao {

	void addNewReport(Teacher teacher, Student student, String description, LocalDate date) throws DisciplinaryReportException, StudentDaoException, TeacherDaoException;
	
	Iterator<DisciplinaryReport> getDisciplinaryReportsByStudent(Student student) throws DisciplinaryReportException, StudentDaoException;

	void deleteReport(DisciplinaryReport report) throws DisciplinaryReportException;


}
