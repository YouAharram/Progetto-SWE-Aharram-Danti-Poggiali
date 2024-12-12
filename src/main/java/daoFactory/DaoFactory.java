package daoFactory;
import DaoExceptions.DaoConnectionException;
import orm.AbsenceDao;
import orm.DisciplinaryReportDao;
import orm.GradeDao;
import orm.HomeworkDao;
import orm.LessonDao;
import orm.MeetingAvailabilityDao;
import orm.MeetingDao;
import orm.ParentDao;
import orm.SchoolClassDao;
import orm.StudentDao;
import orm.TeacherDao;
import orm.TeachingAssignmentDao;

public interface DaoFactory {
	StudentDao createStudentDao() throws DaoConnectionException;
	TeacherDao creatTeacherDao() throws DaoConnectionException;
	GradeDao createGradeDao() throws DaoConnectionException;
	DisciplinaryReportDao createDisciplinaryReportDao() throws DaoConnectionException;
	SchoolClassDao createSchoolClassDao() throws DaoConnectionException;
	AbsenceDao createAbsenceDao() throws DaoConnectionException;
	HomeworkDao createHomeworkDao() throws DaoConnectionException;
	LessonDao createLessonDao() throws DaoConnectionException;
	ParentDao createParentDao() throws DaoConnectionException;
	TeachingAssignmentDao createTeachingAssignmentDao() throws DaoConnectionException;
	MeetingAvailabilityDao createMeetingAvailabilityDao() throws DaoConnectionException;
	MeetingDao createMeetingDao() throws DaoConnectionException;
}
