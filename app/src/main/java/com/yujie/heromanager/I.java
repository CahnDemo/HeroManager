package com.yujie.heromanager;

public class I {
	public static final String CONTENT_TYPE = "text/html;charset=utf-8";
	public static final String UTF_8 = "utf-8";
	public static final String SECRET = "nviKSJdoSJLKDJaiosjIOjkljdWDas";
	public static final String REGISTER_TOKEN = "980804224";
	public static final String REQUEST = "request";
	public interface Request{
		
		 String REQUEST_GET_AREA = "getAreas";
		 String REQUEST_GET_COURSE = "getCourse";
		 String REQUEST_GET_TIME = "getClassStartTime";
		 String REQUEST_GET_CLASS = "getClassList";
		 String REQUEST_GETCLASSNAME = "getClassNameById";
		 String REQUEST_GETNEARLYGRADES = "getNearlyGradeByUid";
		 String REQUEST_GET_TEN_GRADES = "getTenGrade";
		 String REQUEST_DOWNLOAD_CONTENT = "getWordContent";
		 String REQUEST_ADD_EXERCISE_GRADE = "addExercise";
		 String REQUEST_GET_SORT_IN_CLASS = "getSortInClass";
		 String REQUEST_GET_SORT_IN_COURSE = "getSortInCourse";
		 String REQUEST_GET_SORT_IN_TIME = "getSortInTime";
		 String REQUEST_ADD_EXAM_GRADE = "addExamGrade";
		 String REQUEST_GET_EXAM_NOW = "getExamNow";
		 String REQUEST_GET_EXAM_GRADE = "getExamGrade";
		 String REQUEST_GET_CLASS_AVG_GRADE = "getAvgExamGrade";
		 String REQUEST_GET_CLASS_GRADE_LIST = "getClassGradeList";
		 String REQUEST_UPDATE_USER = "updateUser";
		 String REQUEST_SUPER_USER_LOGIN = "superUserlogin";
		 String REQUEST_GET_ALLCLASS = "getAllClassList";
		 String REQUEST_ADD_AREA = "addArea";
		 String REQUEST_ADD_COURSE = "addCourse";
		String REQUEST_ADD_START_TIME = "addStartTime";
		String REQUEST_MOD_AREA = "modArea";
		String REQUEST_MOD_COURSE = "modCourse";
		String REQUEST_DEL_AREA = "delArea";
		String REQUEST_DEL_COURSE = "delCourse";
		String REQUEST_DEL_TIME = "delStartTime";
		String REQUEST_DEL_CLASS = "delClass";
		String REQUEST_MOD_CLASS = "modClass";
		String REQUEST_ADD_CLASS = "addClass";
	}

	public interface SuperUser{
		 String USER_NAME = "user_name";
		 String USER_PWD = "user_pwd";
	}
	
	public interface Area{
		 String SIMPLE_NAME = "simple_name";
		 String AREA_NAME = "area_name";
	}
	
	public interface IClass{
		 String CLASS_NAME = "class_name";
		 String B_AREA = "b_area";
		 String B_COURSE = "b_course";
		 String START_TIME = "start_time";
		 String CLASS_NUMBER = "class_number";
		 String SIMPLE_NAME = "simple_name";
	}
	
	public interface Mark{
		 String MARK = "mark";
	}
	
	public interface StartTime{
		 String START_TIME = "start_time";
	}
	
	public interface Course{
		 String SIMPLE_NAME = "simple_name";
		 String COURSE_NAME = "course_name";
		String MARK = "mark";
	}
	
	public interface Exam{
		 String EXAM_NAME = "exam_name";
		 String EXAM_TIME = "exam_time";
		 String COURSE_ID = "course_id";
		 String STATE = "status";
		 String ID = "id";
	}
	
	public interface Exercise{
		 String GRADE = "grade";
		 String EXE_TIME = "exe_time";
		 String USER_NAME = "user_name";
		 String COURSE_ID = "course_id";
		 String B_CLASS = "b_class";
		 String START_TIME = "start_time";
	}
	
	public interface ExamGrade{
		 String EXAM_ID = "exam_id";
		 String GRADE = "grade";
		 String SUBMIT_TIME = "submit_time";
		 String USER_NAME = "user_name";
		 String B_CLASS = "b_class";
		 String COURSE_ID = "course_id";
		 String B_START_TIME = "b_start_time";
	}
	
	public interface ClassExamGrade{
		 String EXAM_ID = "exam_id";
		 String USER_NAME = "user_name";
		 String GRADE = "grade";
		 String SUBMIT_TIME = "submit_time";
		 String CLASS_NAME = "class_name";
	}
}
