package orm;

import static org.assertj.core.api.Assertions.*; 
import org.junit.Before;
import org.junit.Test;

import abstractOrm.AbsenceDao;
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

import java.sql.SQLException;

public class DatabaseDaoFactoryTest {

    private DatabaseDaoFactory daoFactory;

    @Before
    public void setUp() throws SQLException, DaoConnectionException {
        daoFactory = new DatabaseDaoFactory();
    }

    @Test
    public void testCreateStudentDao() throws DaoConnectionException {
        StudentDao studentDao = daoFactory.createStudentDao();
        assertThat(studentDao).isInstanceOf(StudentDaoDatabase.class);
    }

    @Test
    public void testCreateTeacherDao() throws DaoConnectionException {
        TeacherDao teacherDao = daoFactory.creatTeacherDao();
        assertThat(teacherDao).isInstanceOf(TeacherDaoDatabase.class);
    }

    @Test
    public void testCreateGradeDao() throws DaoConnectionException {
        GradeDao gradeDao = daoFactory.createGradeDao();
        assertThat(gradeDao).isInstanceOf(GradeDaoDatabase.class);
    }

    @Test
    public void testCreateDisciplinaryReportDao() throws DaoConnectionException {
        DisciplinaryReportDao disciplinaryReportDao = daoFactory.createDisciplinaryReportDao();
        assertThat(disciplinaryReportDao).isInstanceOf(DisciplinaryReportDaoDatabase.class);
    }

    @Test
    public void testCreateAbsenceDao() throws DaoConnectionException {
        AbsenceDao absenceDao = daoFactory.createAbsenceDao();
        assertThat(absenceDao).isInstanceOf(AbsenceDaoDatabase.class);
    }

    @Test
    public void testCreateHomeworkDao() throws DaoConnectionException {
        HomeworkDao homeworkDao = daoFactory.createHomeworkDao();
        assertThat(homeworkDao).isInstanceOf(HomeworkDaoDatabase.class);
    }

    @Test
    public void testCreateLessonDao() throws DaoConnectionException {
        LessonDao lessonDao = daoFactory.createLessonDao();
        assertThat(lessonDao).isInstanceOf(LessonDaoDatabase.class);
    }

    @Test
    public void testCreateParentDao() throws DaoConnectionException {
        ParentDao parentDao = daoFactory.createParentDao();
        assertThat(parentDao).isInstanceOf(ParentDaoDatabase.class);
    }

    @Test
    public void testCreateTeachingAssignmentDao() throws DaoConnectionException {
        TeachingAssignmentDao teachingAssignmentDao = daoFactory.createTeachingAssignmentDao();
        assertThat(teachingAssignmentDao).isInstanceOf(TeachingAssignmentDaoDatabase.class);
    }

    @Test
    public void testCreateMeetingAvailabilityDao() throws DaoConnectionException {
        MeetingAvailabilityDao meetingAvailabilityDao = daoFactory.createMeetingAvailabilityDao();
        assertThat(meetingAvailabilityDao).isInstanceOf(MeetingAvailabilityDaoDatabase.class);
    }

    @Test
    public void testCreateMeetingDao() throws DaoConnectionException {
        MeetingDao meetingDao = daoFactory.createMeetingDao();
        assertThat(meetingDao).isInstanceOf(MeetingDaoDatabase.class);
    }
}
