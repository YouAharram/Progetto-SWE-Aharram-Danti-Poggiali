package abstractOrm;

import java.util.Iterator;

import domainModel.Student;
import domainModel.Teacher;
import domainModel.TeachingAssignment;
import exceptions.DaoConnectionException;
import exceptions.StudentDaoException;
import exceptions.TeacherDaoException;
import exceptions.TeachingAssignmentDaoException;

public interface TeachingAssignmentDao {

	Iterator<TeachingAssignment> getAllStudentTeachings(Student student)
			throws TeachingAssignmentDaoException, DaoConnectionException, StudentDaoException;

	Iterator<TeachingAssignment> getAllTeacherTeachings(Teacher teacher) throws TeachingAssignmentDaoException, TeacherDaoException;
}