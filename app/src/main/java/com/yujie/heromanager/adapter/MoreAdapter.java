package com.yujie.heromanager.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yujie.heromanager.HeroApplication;
import com.yujie.heromanager.I;
import com.yujie.heromanager.R;
import com.yujie.heromanager.activity.MainActivity;
import com.yujie.heromanager.bean.AreasBean;
import com.yujie.heromanager.bean.ClassObj;
import com.yujie.heromanager.bean.CourseBean;
import com.yujie.heromanager.bean.Result;
import com.yujie.heromanager.bean.StartTimeBean;
import com.yujie.heromanager.utils.OkHttpUtils;

import java.util.ArrayList;

import dalvik.system.DexClassLoader;

/**
 * Created by yujie on 16-9-20.
 */
public class MoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<?> dataList;
    private Activity mContext;
    private LayoutInflater inflater;
    private int holdID;
    private int flag = 0;
    public MoreAdapter(ArrayList<?> dataList, Activity mContext,int holdID) {
        this.dataList = dataList;
        this.mContext = mContext;
        this.holdID = holdID;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_area_layout, parent, false);
        RecyclerView.ViewHolder holder = null;
        switch (holdID){
            case 0:
                holder = new AreaViewHolder(view);
                holder.setIsRecyclable(true);
                break;
            case 1:
                holder = new CourseViewHolder(view);
                holder.setIsRecyclable(true);
                break;
            case 2:
                holder = new TimeViewHolder(view);
                holder.setIsRecyclable(true);
                break;
            case 3:
                holder = new ClassViewHolder(view);
                holder.setIsRecyclable(true);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Object o = dataList.get(position);
        switch (holdID){
            case 0:
                final AreasBean area = (AreasBean) o;
                AreaViewHolder areaViewHolder = (AreaViewHolder) holder;
                areaViewHolder.area_name.setText(area.getArea_name());
                areaViewHolder.area_simple_name.setText(area.getSimple_name());
                areaViewHolder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showOneDialog(area);
                    }
                });
                areaViewHolder.root.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        delDataToServer(area);
                        return false;
                    }
                });
                break;
            case 1:
                final CourseBean course = (CourseBean) o;
                CourseViewHolder courseViewHolder = (CourseViewHolder) holder;
                courseViewHolder.course_name.setText(course.getCourse_name());
                courseViewHolder.course_simple_name.setText(course.getSimple_name());
                courseViewHolder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showOneDialog(course);
                    }
                });
                courseViewHolder.root.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        delDataToServer(course);
                        return false;
                    }
                });
                break;
            case 2:
                final StartTimeBean timeBean = (StartTimeBean) o;
                TimeViewHolder timeViewHolder = (TimeViewHolder) holder;
                timeViewHolder.timeTxt.setText(timeBean.getStart_time());
                timeViewHolder.tag.setVisibility(View.GONE);
                timeViewHolder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showOneDialog(timeBean);
                    }
                });
                timeViewHolder.root.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        delDataToServer(timeBean);
                        return false;
                    }
                });
                break;
            case 3:
                final ClassObj obj = (ClassObj) o;
                ClassViewHolder classViewHolder = (ClassViewHolder) holder;
                classViewHolder.classTxt.setText(obj.getClass_name());
                classViewHolder.tag.setVisibility(View.GONE);
                classViewHolder.root.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        delDataToServer(obj);
                        return false;
                    }
                });
                break;
        }
    }

    private void delDataToServer(Object o) {
        if (o instanceof AreasBean){
            delDataUtils(((AreasBean) o).getSimple_name(),I.Area.SIMPLE_NAME,I.Request.REQUEST_DEL_AREA);
        }else if (o instanceof CourseBean){
            delDataUtils(((CourseBean) o).getSimple_name(),I.Course.SIMPLE_NAME,I.Request.REQUEST_DEL_COURSE);
        }else if (o instanceof StartTimeBean){
            delDataUtils(((StartTimeBean) o).getStart_time(),I.StartTime.START_TIME,I.Request.REQUEST_DEL_TIME);
        }else if (o instanceof ClassObj){
            delDataUtils(((ClassObj) o).getSimple_name(),I.IClass.SIMPLE_NAME,I.Request.REQUEST_DEL_CLASS);
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

    private void showOneDialog(Object o) {
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
                            }
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(mContext,"无法连接到服务器，请稍后再试",Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return dataList==null?0:dataList.size();
    }
}
class AreaViewHolder extends RecyclerView.ViewHolder{
    TextView area_name;
    TextView area_simple_name;
    RelativeLayout root;
    public AreaViewHolder(View itemView) {
        super(itemView);
        area_name = (TextView) itemView.findViewById(R.id.area_name);
        area_simple_name = (TextView) itemView.findViewById(R.id.area_simple_name);
        root = (RelativeLayout) itemView.findViewById(R.id.root_layout);
    }
}

/**
 * 课程类型的ViewHolder和AreaViewHolder的布局是一模一样的，为了减少工作量，使用相同的布局
 */
class CourseViewHolder extends RecyclerView.ViewHolder{
    TextView course_name;
    TextView course_simple_name;
    RelativeLayout root;
    public CourseViewHolder(View itemView) {
        super(itemView);
        course_name = (TextView) itemView.findViewById(R.id.area_name);
        course_simple_name = (TextView) itemView.findViewById(R.id.area_simple_name);
        root = (RelativeLayout) itemView.findViewById(R.id.root_layout);
    }
}

/**
 * 同上，但是对其中一个textview做了VIEW.GONE处理
 */
class TimeViewHolder extends RecyclerView.ViewHolder{
    TextView timeTxt;
    TextView tag;
    RelativeLayout root;
    public TimeViewHolder(View itemView) {
        super(itemView);
        timeTxt = (TextView) itemView.findViewById(R.id.area_name);
        tag = (TextView) itemView.findViewById(R.id.area_simple_name);
        root = (RelativeLayout) itemView.findViewById(R.id.root_layout);
    }
}

/**
 * 偷个懒
 * tag 做隐藏处理
 */
class ClassViewHolder extends RecyclerView.ViewHolder{
    TextView classTxt;
    TextView tag;
    RelativeLayout root;
    public ClassViewHolder(View itemView) {
        super(itemView);
        classTxt = (TextView) itemView.findViewById(R.id.area_name);
        tag = (TextView) itemView.findViewById(R.id.area_simple_name);
        root = (RelativeLayout) itemView.findViewById(R.id.root_layout);
    }
}