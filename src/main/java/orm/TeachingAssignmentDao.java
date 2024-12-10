package orm;

import java.util.Iterator;

import DaoExceptions.DaoConnectionException;
import DaoExceptions.StudentDaoException;
import DaoExceptions.TeacherDaoException;
import DaoExceptions.TeachingAssignmentDaoException;
import domainModel.Student;
import domainModel.Teacher;
import domainModel.TeachingAssignment;

public interface TeachingAssignmentDao {

	Iterator<TeachingAssignment> getAllStudentTeachings(Student student)
			throws TeachingAssignmentDaoException, DaoConnectionException, StudentDaoException;

	Iterator<TeachingAssignment> getAllTeacherTeachings(Teacher teacher) throws TeachingAssignmentDaoException, TeacherDaoException;
}