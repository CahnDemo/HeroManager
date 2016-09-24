package com.yujie.heromanager.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yujie.heromanager.HeroApplication;
import com.yujie.heromanager.I;
import com.yujie.heromanager.R;
import com.yujie.heromanager.adapter.MoreAdapter;
import com.yujie.heromanager.bean.AreasBean;
import com.yujie.heromanager.bean.ClassObj;
import com.yujie.heromanager.bean.CourseBean;
import com.yujie.heromanager.bean.ExerciseBean;
import com.yujie.heromanager.bean.Result;
import com.yujie.heromanager.bean.StartTimeBean;
import com.yujie.heromanager.utils.OkHttpUtils;
import com.yujie.heromanager.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private Activity mContext = MainActivity.this;
    @Bind(R.id.leftMenu)
    RecyclerView leftMenu;
    @Bind(R.id.right_content)
    RecyclerView rightContent;
    int flag = 0;
    boolean hasOpt = false;
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
        initData();
    }

    private void initData() {
        showClass();
        showCourse();
        showArea();
        showStartTime();
    }


    private void intView() {
        leftMenu.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        leftMenu.setItemAnimator(new DefaultItemAnimator());
        leftMenu.setAdapter(new MenuAdapter());
        rightContent.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        rightContent.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onResume() {
        super.onResume();
        hasOpt = true;
        showClass();
    }

    /**
     * listener
     */
    private void showOneDialog(Object o, int position) {
        if (o instanceof ClassObj){
            ClassObj obj = (ClassObj) o;
            initMapData();
            Intent intent = new Intent(mContext,ModClassActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("classObj",obj);
            intent.putExtras(bundle);
            startActivity(intent);
            return;
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_one_layout, null);
        final EditText text1 = (EditText) view.findViewById(R.id.oneEdit);
        final EditText text2 = (EditText) view.findViewById(R.id.twoEdit);
        if (o instanceof AreasBean){
            text1.setHint("请输入地区名称");
            text2.setHint("请输入地区简称");
            text1.setText(((AreasBean) o).getArea_name());
            text2.setText(((AreasBean) o).getSimple_name());
            flag = 1;
        }else if (o instanceof CourseBean){
            text1.setHint("请输入课程名称");
            text2.setHint("请输入课程简称");
            text1.setText(((CourseBean) o).getCourse_name());
            text2.setText(((CourseBean) o).getSimple_name());
            flag = 2;
        }else if (o instanceof StartTimeBean){
            text1.setHint("请输入开班时间,请不要输错,开班时间无法修改");
            text2.setVisibility(View.GONE);
            flag = 3;
        }
        final String primary = text2.getText().toString();
        Dialog dialog = new AlertDialog.Builder(mContext)
                .setView(view)
                .setTitle("添加和修改")
                .setNegativeButton("添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String var1 = text1.getText().toString();
                        String var2 = text2.getText().toString();
                        if (var1.isEmpty()){
                            text1.setError("请输入数据");
                            text1.requestFocus();
                            return;
                        }
                        addDataToServer(var1,var2);
                    }
                })
                .setPositiveButton("修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String var1 = text1.getText().toString();
                        String var2 = text2.getText().toString();
                        if (var1.isEmpty()){
                            text1.setError("请输入数据");
                            text1.requestFocus();
                            return;
                        }
                        if (!primary.equals(var2)){
                            text2.setError("简称不可修改");
                            text2.requestFocus();
                            return;
                        }
                        modDataToServer(var1,var2);
                    }
                })
                .create();
        dialog.show();
    }

    private void initMapData() {
        HashMap<String,String> areaMap = new HashMap<>();
        for (AreasBean a:areas){
            areaMap.put(a.getArea_name(),a.getSimple_name());
        }
        HashMap<String,String> courseMap = new HashMap<>();
        for (CourseBean b:courses){
            courseMap.put(b.getCourse_name(),b.getSimple_name());
        }
        ArrayList<String> timeList = new ArrayList<>();
        for (StartTimeBean s:times){
            timeList.add(s.getStart_time());
        }
        HeroApplication.getInstance().setAreaMap(areaMap);
        HeroApplication.getInstance().setCourseMap(courseMap);
        HeroApplication.getInstance().setTimelist(timeList);
    }


    private void modDataToServer(String var1, String var2) {
        if (flag == 0){
            return;
        }else if (flag == 1){
            OkHttpUtils<Result> utils = new OkHttpUtils<>();
            utils.url(HeroApplication.SERVER_ROOT)
                    .addParam(I.REQUEST,I.Request.REQUEST_MOD_AREA)
                    .addParam(I.Area.AREA_NAME,var1)
                    .addParam(I.Area.SIMPLE_NAME,var2)
                    .post()
                    .targetClass(Result.class)
                    .execute(new OkHttpUtils.OnCompleteListener<Result>() {
                        @Override
                        public void onSuccess(Result result) {
                            if (result!=null&result.isFlag()){
                                Toast.makeText(mContext,"修改成功,请点击左侧按钮重新刷新",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(mContext,"无法连接到服务器，请稍后再试",Toast.LENGTH_LONG).show();
                        }
                    });
        }else if (flag == 2){
            OkHttpUtils<Result> utils = new OkHttpUtils<>();
            utils.url(HeroApplication.SERVER_ROOT)
                    .addParam(I.REQUEST,I.Request.REQUEST_MOD_COURSE)
                    .addParam(I.Course.COURSE_NAME,var1)
                    .addParam(I.Course.SIMPLE_NAME,var2)
                    .addParam(I.Course.MARK,var2)
                    .post()
                    .targetClass(Result.class)
                    .execute(new OkHttpUtils.OnCompleteListener<Result>() {
                        @Override
                        public void onSuccess(Result result) {
                            if (result!=null&result.isFlag()){
                                Toast.makeText(mContext,"修改成功,请点击左侧按钮重新刷新",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(mContext,"无法连接到服务器，请稍后再试",Toast.LENGTH_LONG).show();
                        }
                    });
        }else if (flag == 3){
            Toast.makeText(mContext,"开班时间不允许修改",Toast.LENGTH_LONG).show();
        }
    }



    private void addDataToServer(String var1, String var2) {
        if (flag == 0){
            return;
        }else if (flag == 1){
            OkHttpUtils<Result> utils = new OkHttpUtils<>();
            utils.url(HeroApplication.SERVER_ROOT)
                    .addParam(I.REQUEST,I.Request.REQUEST_ADD_AREA)
                    .addParam(I.Area.AREA_NAME,var1)
                    .addParam(I.Area.SIMPLE_NAME,var2)
                    .post()
                    .targetClass(Result.class)
                    .execute(new OkHttpUtils.OnCompleteListener<Result>() {
                        @Override
                        public void onSuccess(Result result) {
                            if (result!=null&result.isFlag()){
                                Toast.makeText(mContext,"添加成功",Toast.LENGTH_LONG).show();
                                hasOpt = true;
                                showArea();
                            }
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(mContext,"无法连接到服务器，请稍后再试",Toast.LENGTH_LONG).show();
                        }
                    });
        }else if (flag == 2){
            OkHttpUtils<Result> utils = new OkHttpUtils<>();
            utils.url(HeroApplication.SERVER_ROOT)
                    .addParam(I.REQUEST,I.Request.REQUEST_ADD_COURSE)
                    .addParam(I.Course.COURSE_NAME,var1)
                    .addParam(I.Course.SIMPLE_NAME,var2)
                    .post()
                    .targetClass(Result.class)
                    .execute(new OkHttpUtils.OnCompleteListener<Result>() {
                        @Override
                        public void onSuccess(Result result) {
                            if (result!=null&result.isFlag()){
                                Toast.makeText(mContext,"添加成功",Toast.LENGTH_LONG).show();
                                hasOpt = true;
                                showCourse();
                            }
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(mContext,"无法连接到服务器，请稍后再试",Toast.LENGTH_LONG).show();
                        }
                    });
        }else if (flag == 3){
            OkHttpUtils<Result> utils = new OkHttpUtils<>();
            utils.url(HeroApplication.SERVER_ROOT)
                    .addParam(I.REQUEST,I.Request.REQUEST_ADD_START_TIME)
                    .addParam(I.StartTime.START_TIME,var1)
                    .post()
                    .targetClass(Result.class)
                    .execute(new OkHttpUtils.OnCompleteListener<Result>() {
                        @Override
                        public void onSuccess(Result result) {
                            if (result!=null&result.isFlag()){
                                Toast.makeText(mContext,"添加成功",Toast.LENGTH_LONG).show();
                                hasOpt = true;
                                showStartTime();
                            }
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(mContext,"无法连接到服务器，请稍后再试",Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void delDataToServer(final Object o, int position) {
        if (o instanceof AreasBean){
            Toast.makeText(MainActivity.this,"不允许随意删除校区，请联系系统管理员进行删除",Toast.LENGTH_LONG).show();
            hasOpt = true;
            showArea();
        }else if (o instanceof CourseBean){
            OkHttpUtils<ExerciseBean[]> utils = new OkHttpUtils<>();
            utils.url(HeroApplication.SERVER_ROOT)
                    .addParam(I.REQUEST,I.Request.REQUEST_GET_SORT_IN_COURSE)
                    .addParam(I.Exercise.B_CLASS,((CourseBean) o).getSimple_name())
                    .targetClass(ExerciseBean[].class)
                    .execute(new OkHttpUtils.OnCompleteListener<ExerciseBean[]>() {
                        @Override
                        public void onSuccess(ExerciseBean[] result) {
                            if (result!=null & result.length!=0){
                                Toast.makeText(MainActivity.this,"数据未清除，不能删除学科",Toast.LENGTH_LONG).show();
                            }else {
                                delDataUtils(((CourseBean) o).getSimple_name(),I.Course.SIMPLE_NAME,I.Request.REQUEST_DEL_COURSE);
                                hasOpt = true;
                                showCourse();
                            }
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(MainActivity.this,"不能连接到服务器，请稍后再试",Toast.LENGTH_LONG).show();
                        }
                    });
        }else if (o instanceof StartTimeBean){
            OkHttpUtils<ExerciseBean[]> utils = new OkHttpUtils<>();
            utils.url(HeroApplication.SERVER_ROOT)
                    .addParam(I.REQUEST,I.Request.REQUEST_GET_SORT_IN_TIME)
                    .addParam(I.Exercise.B_CLASS,((StartTimeBean) o).getStart_time())
                    .targetClass(ExerciseBean[].class)
                    .execute(new OkHttpUtils.OnCompleteListener<ExerciseBean[]>() {
                        @Override
                        public void onSuccess(ExerciseBean[] result) {
                            if (result!=null & result.length!=0){
                                Toast.makeText(MainActivity.this,"数据未清除，不能删除学期",Toast.LENGTH_LONG).show();
                            }else {
                                delDataUtils(((StartTimeBean) o).getStart_time(),I.StartTime.START_TIME,I.Request.REQUEST_DEL_TIME);
                                hasOpt = true;
                                showStartTime();
                            }
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(MainActivity.this,"不能连接到服务器，请稍后再试",Toast.LENGTH_LONG).show();
                        }
                    });
        }else if (o instanceof ClassObj){
            OkHttpUtils<ExerciseBean[]> utils = new OkHttpUtils<>();
            utils.url(HeroApplication.SERVER_ROOT)
                    .addParam(I.REQUEST,I.Request.REQUEST_GET_SORT_IN_CLASS)
                    .addParam(I.Exercise.B_CLASS,((ClassObj) o).getId()+"")
                    .targetClass(ExerciseBean[].class)
                    .execute(new OkHttpUtils.OnCompleteListener<ExerciseBean[]>() {
                        @Override
                        public void onSuccess(ExerciseBean[] result) {
                            if (result!=null & result.length!=0){
                                Toast.makeText(MainActivity.this,"数据未清除，不能删除班级",Toast.LENGTH_LONG).show();
                            }else {
                                delDataUtils(((ClassObj) o).getSimple_name(),I.IClass.SIMPLE_NAME,I.Request.REQUEST_DEL_CLASS);
                                hasOpt = true;
                                showClass();
                            }
                        }

                        @Override
                        public void onError(String error) {
                        Toast.makeText(MainActivity.this,"不能连接到服务器，请稍后再试",Toast.LENGTH_LONG).show();
                        }
                    });

        }
    }

    private void delDataUtils(String params, String key, final String request_value) {
        OkHttpUtils<Result> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST,request_value)
                .addParam(key,params)
                .post()
                .targetClass(Result.class)
                .execute(new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result!=null & result.isFlag()){
                            Toast.makeText(mContext,"删除成功",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(mContext,"删除失败",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
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
                            initAdapter(areas,0);
                            break;
                        case 1:
                            initAdapter(courses,1);
                            break;
                        case 2:
                            initAdapter(times,2);
                            break;
                        case 3:
                            initAdapter(classes,3);
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
        startActivity(new Intent(this,ExamActivity.class));
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
                            if (hasOpt){
                                initAdapter(classes,3);
                                hasOpt = false;
                            }
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
                            if (hasOpt){
                                initAdapter(times,2);
                                hasOpt = false;
                            }
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
                            if (hasOpt){
                                initAdapter(courses,1);
                                hasOpt = false;
                            }
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
                            if (hasOpt){
                                initAdapter(areas,0);
                                hasOpt = false;
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    private void initAdapter(ArrayList<?> list,int position) {
        adapter = new MoreAdapter(list,mContext,position);
        rightContent.setAdapter(adapter);
        setAdapterListener();
    }

    private void setAdapterListener() {
        adapter.setListener(new MoreAdapter.OnItemActionListener() {
            @Override
            public void onItemClickListener(View v, int position, Object o) {
                showOneDialog(o,position);
            }

            @Override
            public void onItemLongClickListener(View v, int position, Object o) {
                delDataToServer(o,position);
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
