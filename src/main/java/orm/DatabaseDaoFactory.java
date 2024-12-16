package orm;

import abstractOrm.AbsenceDao;
import abstractOrm.DaoFactory;
import abstractOrm.DisciplinaryReportDao;
import abstractOrm.GradeDao;
import abstractOrm.HomeworkDao;
import abstractOrm.LessonDao;
import abstractOrm.MeetingAvailabilityDao;
import abstractOrm.MeetingDao;
import abstractOrm.ParentDao;
import abstractOrm.StudentDao;
import abstractOrm.TeacherDao;
import abstractOrm.TeachingAssignmentDao;
import exceptions.DaoConnectionException;

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