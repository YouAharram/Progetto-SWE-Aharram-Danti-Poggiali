package orm;

import domainModel.Parent;
import domainModel.Student;

public interface ParentDao {
	
	Parent getParentByUsernameWithPassword(String username, String password) throws ParentDaoException, StudentDaoException;


}