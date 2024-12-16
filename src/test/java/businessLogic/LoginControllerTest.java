package businessLogic;

import static org.easymock.EasyMock.createMock;
 
import static org.easymock.EasyMock.expect;

import org.junit.Before;
import org.junit.Test;

import abstractOrm.DaoFactory;
import abstractOrm.ParentDao;
import abstractOrm.StudentDao;
import abstractOrm.TeacherDao;
import businessLogic.LoginHandler.IllegalCredentialsException;
import domainModel.Parent;
import domainModel.Student;
import domainModel.Teacher;
import exceptions.DaoConnectionException;
import exceptions.ParentDaoException;
import exceptions.StudentDaoException;
import exceptions.TeacherDaoException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.easymock.EasyMock.*;

public class LoginControllerTest {

	private DaoFactory factoryMock;
	private StudentDao studentDaoMock;
	private TeacherDao teacherDaoMock;
	private ParentDao parentDaoMock;
	private LoginHandler loginHandler;
	private String username;
	private String password;
	private LoginController loginController;
	private InterfaceCreator interfaceCreatorMock;

	@Before
	public void setup() throws DaoConnectionException {
		factoryMock = createMock(DaoFactory.class);
		studentDaoMock = createMock(StudentDao.class);
		teacherDaoMock = createMock(TeacherDao.class);
		parentDaoMock = createMock(ParentDao.class);
		interfaceCreatorMock = createMock(InterfaceCreator.class);

		loginHandler = new StudentUsernameValidationHandler(
				new TeacherUsernameValidationHandler(new ParentUsernameValidationHandler(null)));

		loginController = new LoginController(loginHandler, factoryMock, interfaceCreatorMock);

		expect(factoryMock.createStudentDao()).andReturn(studentDaoMock).anyTimes();
		expect(factoryMock.creatTeacherDao()).andReturn(teacherDaoMock).anyTimes();
		expect(factoryMock.createParentDao()).andReturn(parentDaoMock).anyTimes();

		username = "username";
		password = "password";
	}

	@Test
	public void testStudentValidCredentials()
			throws StudentDaoException, DaoConnectionException, IllegalCredentialsException {
		Student student = new Student(1, "Mario", "Rossi", null);
	    
	    expect(studentDaoMock.getStudentByUsernameAndPassword(username, password))
	            .andReturn(student).once();
	    
	    interfaceCreatorMock.createStudentInterface(anyObject(StudentController.class));
	    expectLastCall().once();

	    replay(factoryMock, studentDaoMock, interfaceCreatorMock);

	    loginController.login(username, password);

	    verify(factoryMock, studentDaoMock, interfaceCreatorMock);
	}

	@Test
	public void testTeacherValidCredentials()
			throws StudentDaoException, DaoConnectionException, IllegalCredentialsException, TeacherDaoException {
		expect(studentDaoMock.getStudentByUsernameAndPassword(username, password)).andThrow(new StudentDaoException(""))
				.once();
		Teacher teacher = new Teacher(1, "Mario", "Rossi");
		expect(teacherDaoMock.getTeacherByUsernameAndPassword(username, password))
				.andReturn(teacher).once();

		interfaceCreatorMock.createTeacherInterface(new TeacherController(teacher, factoryMock));
		expectLastCall().once();
		 
		replay(factoryMock, studentDaoMock, teacherDaoMock);

		loginController.login(username, password);

		verify(factoryMock, studentDaoMock, teacherDaoMock);
	}

	@Test
	public void testParentValidCredentials() throws StudentDaoException, DaoConnectionException,
			IllegalCredentialsException, TeacherDaoException, ParentDaoException {
		expect(studentDaoMock.getStudentByUsernameAndPassword(username, password)).andThrow(new StudentDaoException(""))
				.once();
		expect(teacherDaoMock.getTeacherByUsernameAndPassword(username, password)).andThrow(new TeacherDaoException(""))
				.once();
		Parent parent = new Parent(1, "Mario", "Rossi", null);
		
		expect(parentDaoMock.getParentByUsernameWithPassword(username, password))
				.andReturn(parent).once();
		
		interfaceCreatorMock.createParentInterface(new ParentController(parent, factoryMock));
		expectLastCall().once();
		
		replay(factoryMock, studentDaoMock, teacherDaoMock, parentDaoMock);

		loginController.login(username, password);

		verify(factoryMock, studentDaoMock, teacherDaoMock, parentDaoMock);
	}

	@Test
	public void testInvalidCredentials() throws StudentDaoException, DaoConnectionException,
			IllegalCredentialsException, TeacherDaoException, ParentDaoException {
		expect(studentDaoMock.getStudentByUsernameAndPassword(username, password)).andThrow(new StudentDaoException(""))
				.once();
		expect(teacherDaoMock.getTeacherByUsernameAndPassword(username, password)).andThrow(new TeacherDaoException(""))
				.once();
		expect(parentDaoMock.getParentByUsernameWithPassword(username, password)).andThrow(new ParentDaoException(""))
				.once();

		replay(factoryMock, studentDaoMock, teacherDaoMock, parentDaoMock);

		assertThatThrownBy(() -> loginController.login(username, password))
				.isInstanceOf(IllegalCredentialsException.class).hasMessage("Invalid username or password");

		verify(factoryMock, studentDaoMock, teacherDaoMock, parentDaoMock);
	}

}
