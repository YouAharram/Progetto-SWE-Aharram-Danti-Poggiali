package abstractOrm;


import domainModel.Teacher;
import exceptions.TeacherDaoException;

public interface TeacherDao {
	
	Teacher getTeacherByUsernameAndPassword(String username, String password) throws TeacherDaoException;

}