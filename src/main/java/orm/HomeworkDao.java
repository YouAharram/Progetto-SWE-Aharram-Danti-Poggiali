package orm;


import java.time.LocalDate;

import java.util.Iterator;

import DaoExceptions.HomeworkDaoException;
import DaoExceptions.SchoolClassDaoException;
import DaoExceptions.TeachingAssignmentDaoException;
import domainModel.Homework;
import domainModel.SchoolClass;
import domainModel.TeachingAssignment;

public interface HomeworkDao {
	
	void addHomework(TeachingAssignment teaching, LocalDate date, String description, LocalDate subissionDate) throws HomeworkDaoException, TeachingAssignmentDaoException;

	void editHomeworkDescription(Homework homework, String description) throws HomeworkDaoException;
	
	void editHomeworkSubmissionDate(Homework homework, LocalDate submissionDate) throws HomeworkDaoException;

	Iterator<Homework> getHomeworksBySubmissionDate(LocalDate date, SchoolClass schoolClass) throws HomeworkDaoException, SchoolClassDaoException;

	void deleteHomework(Homework homework) throws HomeworkDaoException;


}
