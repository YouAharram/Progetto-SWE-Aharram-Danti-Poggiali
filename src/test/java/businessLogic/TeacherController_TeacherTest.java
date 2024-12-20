package businessLogic;

import static org.easymock.EasyMock.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import abstractOrm.DaoFactory;
import abstractOrm.StudentDao;
import abstractOrm.TeachingAssignmentDao;
import domainModel.SchoolClass;
import domainModel.Student;
import domainModel.Teacher;
import domainModel.TeachingAssignment;
import exceptions.DaoConnectionException;
import exceptions.SchoolClassDaoException;
import exceptions.StudentDaoException;
import exceptions.TeacherDaoException;
import exceptions.TeachingAssignmentDaoException;

import java.util.ArrayList;
import java.util.Iterator;

public class TeacherController_TeacherTest {

	private DaoFactory factoryMock;
	private StudentDao studentDaoMock;
	private Teacher teacher;
	private TeacherController teacherController;
	private TeachingAssignmentDao teachingAssignmentDaoMock;

	private ArrayList<TeachingAssignment> teachingAssignments;
	private TeachingAssignment teachingAssignment1;
	private TeachingAssignment teachingAssignment2;
	private ArrayList<SchoolClass> classes;
	private SchoolClass schoolClass1;
	private SchoolClass schoolClass2;
	private ArrayList<Student> students;
	private SchoolClass schoolClassForStudents;
	private Student student2;
	private Student student1;

	@Before
	public void setup() throws StudentDaoException, TeacherDaoException, DaoConnectionException {
		factoryMock = createMock(DaoFactory.class);
		studentDaoMock = createMock(StudentDao.class);
		teachingAssignmentDaoMock = createMock(TeachingAssignmentDao.class);

		teacher = new Teacher(1, "Mario", "Rossi");

		expect(factoryMock.createStudentDao()).andReturn(studentDaoMock).anyTimes();
		expect(factoryMock.createTeachingAssignmentDao()).andReturn(teachingAssignmentDaoMock).anyTimes();
		
		teacherController = new TeacherController(teacher, factoryMock);

		classes = new ArrayList<>();
		schoolClass1 = new SchoolClass("1A");
		schoolClass2 = new SchoolClass("2A");
		classes.add(schoolClass1);
		classes.add(schoolClass2);

		teachingAssignments = new ArrayList<>();
		teachingAssignment1 = new TeachingAssignment(1, "matematica", teacher, schoolClass1);
		teachingAssignment2 = new TeachingAssignment(2, "scienze", teacher, schoolClass2);
		teachingAssignments.add(teachingAssignment1);
		teachingAssignments.add(teachingAssignment2);
		
		students = new ArrayList<>();
		schoolClassForStudents = new SchoolClass("1A");
		student1 = new Student(1, "Fabio", "Rossi", schoolClassForStudents);
		student2 = new Student(2, "Gino", "Rossi", schoolClassForStudents);
		students.add(student1);
		students.add(student2);
	}
	
	@Test
	public void testGetTeacher() throws StudentDaoException, TeacherDaoException, DaoConnectionException {
		assertThat(teacherController.getTeacher()).isEqualTo(teacher);
	}

	@Test
	public void testGetAllMyTeachings() throws TeachingAssignmentDaoException, TeacherDaoException, DaoConnectionException {
		Iterator<TeachingAssignment> teachingsIterator = teachingAssignments.iterator();

		expect(teachingAssignmentDaoMock.getAllTeacherTeachings(teacher)).andReturn(teachingsIterator).once();

		replay(factoryMock, teachingAssignmentDaoMock);

		assertThat(teacherController.getAllMyTeachings()).toIterable().containsExactlyInAnyOrder(teachingAssignment1,
				teachingAssignment2);

		verify(factoryMock, teachingAssignmentDaoMock);
	}

	@Test
	public void testGetStudentByClass() throws DaoConnectionException, StudentDaoException, TeacherDaoException, SchoolClassDaoException {
		Iterator<Student> studentsIterator = students.iterator();

		expect(studentDaoMock.getStudentsByClass(schoolClassForStudents)).andReturn(studentsIterator).once();

		replay(factoryMock, studentDaoMock);

		assertThat(teacherController.getStudentsByClass(schoolClassForStudents)).toIterable().containsExactlyInAnyOrder(student1, student2);

		verify(factoryMock, studentDaoMock);
	}
}