package abstractOrm;
import exceptions.DaoConnectionException;
import orm.GradeDao;

public interface DaoFactory {
	StudentDao createStudentDao() throws DaoConnectionException;
	TeacherDao creatTeacherDao() throws DaoConnectionException;
	GradeDao createGradeDao() throws DaoConnectionException;
	DisciplinaryReportDao createDisciplinaryReportDao() throws DaoConnectionException;
	AbsenceDao createAbsenceDao() throws DaoConnectionException;
	HomeworkDao createHomeworkDao() throws DaoConnectionException;
	LessonDao createLessonDao() throws DaoConnectionException;
	ParentDao createParentDao() throws DaoConnectionException;
	TeachingAssignmentDao createTeachingAssignmentDao() throws DaoConnectionException;
	MeetingAvailabilityDao createMeetingAvailabilityDao() throws DaoConnectionException;
	MeetingDao createMeetingDao() throws DaoConnectionException;
}