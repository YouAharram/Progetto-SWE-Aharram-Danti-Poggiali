package orm;

import java.sql.Connection;

import DaoExceptions.ParentDaoException;
import DaoExceptions.SchoolClassDaoException;
import DaoExceptions.StudentDaoException;
import DaoExceptions.TeacherDaoException;
import DaoExceptions.TeachingAssignmentDaoException;
import DaoExceptions.MeetingAvailabilityDaoException;
import domainModel.MeetingAvailability;
import domainModel.Parent;
import domainModel.SchoolClass;
import domainModel.Student;
import domainModel.Teacher;
import domainModel.TeachingAssignment;

class DaoUtils {
	
	static void checkStudentExist(Student student, Connection conn) throws StudentDaoException {
		StudentDaoDatabase studentDaoDatabase = new StudentDaoDatabase(conn);
		try {
			studentDaoDatabase.getStudentById(student.getId());
		} catch (Exception e) {
			throw new StudentDaoException("Student doesn't exist");
		}
	}

	static void checkScoolClassExist(SchoolClass schoolClass, Connection conn) throws SchoolClassDaoException {
		SchoolClassDaoDatabase schoolClassDaoDatabase = new SchoolClassDaoDatabase(conn);
		try {
			schoolClassDaoDatabase.getSchoolClassByName(schoolClass.getClassName());
		} catch (Exception e) {
			throw new SchoolClassDaoException("Class does not exist");
		}
	}
	
	static void checkTeacherExist(Teacher teacher, Connection conn) throws TeacherDaoException {
		TeacherDaoDatabase teacherDaoDatabase = new TeacherDaoDatabase(conn);
		try {
			teacherDaoDatabase.getTeacherById(teacher.getId());
		} catch (Exception e) {
			throw new TeacherDaoException("Teacher doesn't exist");
		}
	}
	
	static void checkTeachingAssignmentExist(TeachingAssignment teaching, Connection conn) throws TeachingAssignmentDaoException {
		TeachingAssignmentDaoDatabase teachingDaoDatabase = new TeachingAssignmentDaoDatabase(conn);
		try {
			teachingDaoDatabase.getTeachingById(teaching.getId());
		} catch (Exception e) {
			throw new TeachingAssignmentDaoException("Teaching doesn't exist");
		}
	}
	
	static void checkParentExist(Parent parent, Connection conn) throws ParentDaoException {
		ParentDaoDatabase parentDaoDatabase = new ParentDaoDatabase(conn);
		try {
			parentDaoDatabase.getParentById(parent.getId());
		} catch (Exception e) {
			throw new ParentDaoException("Parent doesn't exist");
		}
	}
	
	static void checkMeetingAvailabilityExist(MeetingAvailability meetingAvailability, Connection conn) throws MeetingAvailabilityDaoException {
		MeetingAvailabilityDaoDatabase meetingAvailabilityDaoDatabase = new MeetingAvailabilityDaoDatabase(conn);
		try {
			meetingAvailabilityDaoDatabase.getMeetingAvailabilityByDateHourTeacher(meetingAvailability.getDate(), meetingAvailability.getHour(), meetingAvailability.getTeacher());
		} catch (Exception e) {
			throw new MeetingAvailabilityDaoException("Meeting availability doesn't exist");
		}
	}
	

}
