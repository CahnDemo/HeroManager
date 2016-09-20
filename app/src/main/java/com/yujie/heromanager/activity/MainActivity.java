package com.yujie.heromanager.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yujie.heromanager.HeroApplication;
import com.yujie.heromanager.I;
import com.yujie.heromanager.R;
import com.yujie.heromanager.adapter.MoreAdapter;
import com.yujie.heromanager.bean.AreasBean;
import com.yujie.heromanager.bean.ClassObj;
import com.yujie.heromanager.bean.CourseBean;
import com.yujie.heromanager.bean.StartTimeBean;
import com.yujie.heromanager.utils.OkHttpUtils;
import com.yujie.heromanager.utils.Utils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private Activity mContext = MainActivity.this;
    @Bind(R.id.leftMenu)
    RecyclerView leftMenu;
    @Bind(R.id.right_content)
    RecyclerView rightContent;

    String[] menu_array = {
            "校区 ","学科","期数","班级","考试"
    };

    int[] menu_icon = {
            R.drawable.area,R.drawable.course,R.drawable.start_time,
            R.drawable.class_img,R.drawable.exam
    };

    /** data arraylist*/
    ArrayList<AreasBean> areas;
    ArrayList<CourseBean> courses;
    ArrayList<StartTimeBean> times;
    ArrayList<ClassObj> classes;

    /** data adapters*/
    MoreAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        intView();
    }


    private void intView() {
        leftMenu.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        leftMenu.setItemAnimator(new DefaultItemAnimator());
        leftMenu.setAdapter(new MenuAdapter());
        rightContent.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        rightContent.setItemAnimator(new DefaultItemAnimator());
    }

    class MenuAdapter extends RecyclerView.Adapter<ViewHolder>{
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_menu_layout, parent, false);
            ViewHolder holder = new ViewHolder(view);
            holder.setIsRecyclable(true);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.menu_icon.setImageDrawable(getResources().getDrawable(menu_icon[position]));
            holder.menu_title.setText(menu_array[position]);
            holder.rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position){
                        case 0:
                            showArea();
                            break;
                        case 1:
                            showCourse();
                            break;
                        case 2:
                            showStartTime();
                            break;
                        case 3:
                            showClass();
                            break;
                        case 4:
                            showExam();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return menu_array.length;
        }
    }

    private void showExam() {

    }

    private void showClass() {
        classes = new ArrayList<>();
        OkHttpUtils<ClassObj[]> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST,I.Request.REQUEST_GET_ALLCLASS)
                .targetClass(ClassObj[].class)
                .execute(new OkHttpUtils.OnCompleteListener<ClassObj[]>() {
                    @Override
                    public void onSuccess(ClassObj[] result) {
                        if (result!=null & result.length!=0){
                            classes = Utils.array2List(result);
                            adapter = new MoreAdapter(classes,mContext,3);
                            rightContent.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    private void showStartTime() {
        times = new ArrayList<>();
        OkHttpUtils<StartTimeBean[]> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST,I.Request.REQUEST_GET_TIME)
                .targetClass(StartTimeBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<StartTimeBean[]>() {
                    @Override
                    public void onSuccess(StartTimeBean[] result) {
                        if (result!=null & result.length!=0){
                            times = Utils.array2List(result);
                            adapter = new MoreAdapter(times,mContext,2);
                            rightContent.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    private void showCourse() {
        courses = new ArrayList<>();
        OkHttpUtils<CourseBean[]> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST,I.Request.REQUEST_GET_COURSE)
                .targetClass(CourseBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<CourseBean[]>() {
                    @Override
                    public void onSuccess(CourseBean[] result) {
                        if (result!=null & result.length!=0){
                            courses = Utils.array2List(result);
                            adapter = new MoreAdapter(courses,mContext,1);
                            rightContent.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    private void showArea() {
        areas = new ArrayList<>();
        OkHttpUtils<AreasBean[]> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST,I.Request.REQUEST_GET_AREA)
                .targetClass(AreasBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<AreasBean[]>() {
                    @Override
                    public void onSuccess(AreasBean[] result) {
                        if (result!=null & result.length!=0){
                            areas = Utils.array2List(result);
                            adapter = new MoreAdapter(areas,mContext,0);
                            rightContent.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView menu_icon;
        TextView menu_title;
        CardView rootLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            menu_icon = (ImageView) itemView.findViewById(R.id.menu_icon);
            menu_title = (TextView) itemView.findViewById(R.id.menu_title);
            rootLayout = (CardView) itemView.findViewById(R.id.root_layout);
        }
    }
}
