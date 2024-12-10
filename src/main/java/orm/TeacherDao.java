package orm;


import DaoExceptions.TeacherDaoException;
import domainModel.Teacher;

public interface TeacherDao {
	
	Teacher getTeacherByUsernameAndPassword(String username, String password) throws TeacherDaoException;

}