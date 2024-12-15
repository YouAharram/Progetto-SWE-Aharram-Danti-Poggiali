 package orm;

import DaoExceptions.ParentDaoException; 
import DaoExceptions.StudentDaoException;
import domainModel.Parent;

public interface ParentDao {
	
	Parent getParentByUsernameWithPassword(String username, String password) throws ParentDaoException, StudentDaoException;


}