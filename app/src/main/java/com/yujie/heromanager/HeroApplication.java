package com.yujie.heromanager;

import android.app.Application;

import com.yujie.heromanager.bean.SuperUser;

/**
 * Created by yujie on 16-9-7.
 */
public class HeroApplication extends Application {
    public static final String SERVER_ROOT = "http://115.28.2.61:8080/Hero/Server?";
    public static final String AVATAR_ROOT = "http://115.28.2.61:8080/Hero/";
    public static final String EXERCISE_CODE = "10010";
    public static final String EXAM_CODE = "10011";

    public int getCURRENT_EXAM_ID() {
        return CURRENT_EXAM_ID;
    }

    public void setCURRENT_EXAM_ID(int CURRENT_EXAM_ID) {
        this.CURRENT_EXAM_ID = CURRENT_EXAM_ID;
    }

    private int CURRENT_EXAM_ID = 0;
    @Override
    public void onCreate() {
        super.onCreate();
    }


    public String getCurrentSuperUser() {
        return currentSuperUser;
    }

    public void setCurrentSuperUser(String currentSuperUser) {
        this.currentSuperUser = currentSuperUser;
    }

    /**
     * current user who signed in
     */
    private String currentSuperUser;

    public String getCurrentTestCourse() {
        return currentTestCourse;
    }

    public void setCurrentTestCourse(String currentTestCourse) {
        this.currentTestCourse = currentTestCourse;
    }

    private String currentTestCourse;
    private static HeroApplication instance = null;
     private HeroApplication(){}
     public static HeroApplication getInstance() {
        synchronized (HeroApplication.class) {
            if (instance == null) {
                instance = new HeroApplication();
            }
        }
     
        return instance;}

}
