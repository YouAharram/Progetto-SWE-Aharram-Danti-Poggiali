 package abstractOrm;

import domainModel.Parent;
import exceptions.ParentDaoException;
import exceptions.StudentDaoException;

public interface ParentDao {
	
	Parent getParentByUsernameWithPassword(String username, String password) throws ParentDaoException, StudentDaoException;


}