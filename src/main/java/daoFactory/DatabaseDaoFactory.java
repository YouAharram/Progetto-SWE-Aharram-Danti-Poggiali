package daoFactory;

import DaoExceptions.DaoConnectionException;
import orm.AbsenceDao;
import orm.AbsenceDaoDatabase;
import orm.DatabaseManager;
import orm.DisciplinaryReportDao;
import orm.DisciplinaryReportDaoDatabase;
import orm.GradeDao;
import orm.GradeDaoDatabase;
import orm.HomeworkDao;
import orm.HomeworkDaoDatabase;
import orm.LessonDao;
import orm.LessonDaoDatabase;
import orm.MeetingAvailabilityDao;
import orm.MeetingAvailabilityDaoDatabase;
import orm.MeetingDao;
import orm.MeetingDaoDatabase;
import orm.ParentDao;
import orm.ParentDaoDatabase;
import orm.SchoolClassDao;
import orm.SchoolClassDaoDatabase;
import orm.StudentDao;
import orm.StudentDaoDatabase;
import orm.TeacherDao;
import orm.TeacherDaoDatabase;
import orm.TeachingAssignmentDao;
import orm.TeachingAssignmentDaoDatabase;

public class DatabaseDaoFactory implements DaoFactory {

	@Override
	public StudentDao createStudentDao() throws DaoConnectionException {
		return new StudentDaoDatabase(DatabaseManager.getInstance().getConnection());
	}

	@Override
	public TeacherDao creatTeacherDao() throws DaoConnectionException {
		return new TeacherDaoDatabase(DatabaseManager.getInstance().getConnection());
	}

	@Override
	public GradeDao createGradeDao() throws DaoConnectionException {
		return new GradeDaoDatabase(DatabaseManager.getInstance().getConnection());
	}

	@Override
	public DisciplinaryReportDao createDisciplinaryReportDao() throws DaoConnectionException {
		return new DisciplinaryReportDaoDatabase(DatabaseManager.getInstance().getConnection());
	}

	@Override
	public SchoolClassDao createSchoolClassDao() throws DaoConnectionException {
		return new SchoolClassDaoDatabase(DatabaseManager.getInstance().getConnection());
	}

	@Override
	public AbsenceDao createAbsenceDao() throws DaoConnectionException {
		return new AbsenceDaoDatabase(DatabaseManager.getInstance().getConnection());
	}

	@Override
	public HomeworkDao createHomeworkDao() throws DaoConnectionException {
		return new HomeworkDaoDatabase(DatabaseManager.getInstance().getConnection());
	}

	@Override
	public LessonDao createLessonDao() throws DaoConnectionException {
		return new LessonDaoDatabase(DatabaseManager.getInstance().getConnection());
	}

	@Override
	public ParentDao createParentDao() throws DaoConnectionException {
		return new ParentDaoDatabase(DatabaseManager.getInstance().getConnection());
	}

	@Override
	public TeachingAssignmentDao createTeachingAssignmentDao() throws DaoConnectionException {
		return new TeachingAssignmentDaoDatabase(DatabaseManager.getInstance().getConnection());
	}

	@Override
	public MeetingAvailabilityDao createMeetingAvailabilityDao() throws DaoConnectionException {
		return new MeetingAvailabilityDaoDatabase(DatabaseManager.getInstance().getConnection());
	}

	@Override
	public MeetingDao createMeetingDao() throws DaoConnectionException {
		return new MeetingDaoDatabase(DatabaseManager.getInstance().getConnection());
	}
}
