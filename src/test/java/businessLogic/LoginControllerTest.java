package businessLogic;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import DaoExceptions.DaoConnectionException;
import DaoExceptions.ParentDaoException;
import DaoExceptions.StudentDaoException;
import DaoExceptions.TeacherDaoException;
import businessLogic.LoginHandler.IllegalCredentialsException;
import daoFactory.DaoFactory;
import domainModel.Parent;
import domainModel.Student;
import domainModel.Teacher;
import orm.ParentDao;
import orm.StudentDao;
import orm.TeacherDao;

import static org.assertj.core.api.Assertions.assertThat;
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

	@Before
	public void setup() throws DaoConnectionException {
		factoryMock = createMock(DaoFactory.class);
		studentDaoMock = createMock(StudentDao.class);
		teacherDaoMock = createMock(TeacherDao.class);
		parentDaoMock = createMock(ParentDao.class);

		loginHandler = new StudentUsernameValidationHandler(
				new TeacherUsernameValidationHandler(new ParentUsernameValidationHandler(null)));

		loginController = new LoginController(loginHandler, factoryMock);

		expect(factoryMock.createStudentDao()).andReturn(studentDaoMock).anyTimes();
		expect(factoryMock.creatTeacherDao()).andReturn(teacherDaoMock).anyTimes();
		expect(factoryMock.createParentDao()).andReturn(parentDaoMock).anyTimes();

		username = "username";
		password = "password";
	}

	@Test
	public void testStudentValidCredentials()
			throws StudentDaoException, DaoConnectionException, IllegalCredentialsException {
		expect(studentDaoMock.getStudentByUsernameAndPassword(username, password))
				.andReturn(new Student(1, "Mario", "Rossi", null)).once();

		replay(factoryMock, studentDaoMock);

		assertThat(loginController.login(username, password)).isInstanceOf(StudentController.class);

		verify(factoryMock, studentDaoMock);
	}

	@Test
	public void testTeacherValidCredentials()
			throws StudentDaoException, DaoConnectionException, IllegalCredentialsException, TeacherDaoException {
		expect(studentDaoMock.getStudentByUsernameAndPassword(username, password)).andThrow(new StudentDaoException(""))
				.once();
		expect(teacherDaoMock.getTeacherByUsernameAndPassword(username, password))
				.andReturn(new Teacher(1, "Mario", "Rossi")).once();

		replay(factoryMock, studentDaoMock, teacherDaoMock);

		assertThat(loginController.login(username, password)).isInstanceOf(TeacherController.class);

		verify(factoryMock, studentDaoMock, teacherDaoMock);
	}

	@Test
	public void testParentValidCredentials() throws StudentDaoException, DaoConnectionException,
			IllegalCredentialsException, TeacherDaoException, ParentDaoException {
		expect(studentDaoMock.getStudentByUsernameAndPassword(username, password)).andThrow(new StudentDaoException(""))
				.once();
		expect(teacherDaoMock.getTeacherByUsernameAndPassword(username, password)).andThrow(new TeacherDaoException(""))
				.once();
		expect(parentDaoMock.getParentByUsernameWithPassword(username, password))
				.andReturn(new Parent(1, "Mario", "Rossi", null)).once();

		replay(factoryMock, studentDaoMock, teacherDaoMock, parentDaoMock);

		assertThat(loginController.login(username, password)).isInstanceOf(ParentController.class);

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
