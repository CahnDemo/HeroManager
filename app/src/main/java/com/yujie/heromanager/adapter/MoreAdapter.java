package com.yujie.heromanager.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.Collections;
import java.util.Objects;

import dalvik.system.DexClassLoader;

/**
 * Created by yujie on 16-9-20.
 */
public class MoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<?> dataList;
    private Activity mContext;
    private LayoutInflater inflater;
    private int holdID;
    private OnItemActionListener listener;

    public void setListener(OnItemActionListener listener) {
        this.listener = listener;
    }

    public interface OnItemActionListener{
        void onItemClickListener(View v,int position,Object o);
        void onItemLongClickListener(View v,int position,Object o);
        void onItemSwipListener(int position, Object o);
    }
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Object o = dataList.get(position);
        switch (holdID){
            case 0:
                final AreasBean area = (AreasBean) o;
                AreaViewHolder areaViewHolder = (AreaViewHolder) holder;
                areaViewHolder.area_name.setText(area.getArea_name());
                areaViewHolder.area_simple_name.setText(area.getSimple_name());
                if (listener!=null){
                    areaViewHolder.root.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onItemClickListener(v,holder.getPosition(),o);
                        }
                    });
                    areaViewHolder.root.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            listener.onItemLongClickListener(v,holder.getPosition(),o);
                            return false;
                        }
                    });
                    areaViewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onItemSwipListener(holder.getPosition(),o);
                        }
                    });
                }
                break;
            case 1:
                final CourseBean course = (CourseBean) o;
                CourseViewHolder courseViewHolder = (CourseViewHolder) holder;
                courseViewHolder.course_name.setText(course.getCourse_name());
                courseViewHolder.course_simple_name.setText(course.getSimple_name());
                if (listener!=null){
                    courseViewHolder.root.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onItemClickListener(v,holder.getPosition(),o);
                        }
                    });
                    courseViewHolder.root.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            listener.onItemLongClickListener(v,holder.getPosition(),o);
                            return false;
                        }
                    });
                    courseViewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onItemSwipListener(holder.getPosition(),o);
                        }
                    });
                }
                break;
            case 2:
                final StartTimeBean timeBean = (StartTimeBean) o;
                TimeViewHolder timeViewHolder = (TimeViewHolder) holder;
                timeViewHolder.timeTxt.setText(timeBean.getStart_time());
                timeViewHolder.tag.setVisibility(View.GONE);
                if (listener!=null){
                    timeViewHolder.root.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onItemClickListener(v,holder.getPosition(),o);
                        }
                    });
                    timeViewHolder.root.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            listener.onItemLongClickListener(v,holder.getPosition(),o);
                            return true;
                        }
                    });
                    timeViewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onItemSwipListener(holder.getPosition(),o);
                        }
                    });
                }
                break;
            case 3:
                final ClassObj obj = (ClassObj) o;
                ClassViewHolder classViewHolder = (ClassViewHolder) holder;
                classViewHolder.classTxt.setText(obj.getClass_name());
                classViewHolder.tag.setVisibility(View.GONE);
                if (listener!=null){
                    classViewHolder.root.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            listener.onItemLongClickListener(v,holder.getPosition(),o);
                            return false;
                        }
                    });
                    classViewHolder.root.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onItemClickListener(v,holder.getPosition(),o);
                        }
                    });
                    classViewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onItemSwipListener(holder.getPosition(),o);
                        }
                    });
                }
                break;
        }
    }

    public void setDataList(ArrayList<?> dataList) {
        dataList.clear();
        this.dataList = dataList;
        notifyDataSetChanged();
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
    Button deleteBtn;
    public AreaViewHolder(View itemView) {
        super(itemView);
        area_name = (TextView) itemView.findViewById(R.id.area_name);
        area_simple_name = (TextView) itemView.findViewById(R.id.area_simple_name);
        root = (RelativeLayout) itemView.findViewById(R.id.root_layout);
        deleteBtn = (Button) itemView.findViewById(R.id.deleteBtn);
    }
}

/**
 * 课程类型的ViewHolder和AreaViewHolder的布局是一模一样的，为了减少工作量，使用相同的布局
 */
class CourseViewHolder extends RecyclerView.ViewHolder{
    TextView course_name;
    TextView course_simple_name;
    RelativeLayout root;
    Button deleteBtn;
    public CourseViewHolder(View itemView) {
        super(itemView);
        course_name = (TextView) itemView.findViewById(R.id.area_name);
        course_simple_name = (TextView) itemView.findViewById(R.id.area_simple_name);
        root = (RelativeLayout) itemView.findViewById(R.id.root_layout);
        deleteBtn = (Button) itemView.findViewById(R.id.deleteBtn);
    }
}

/**
 * 同上，但是对其中一个textview做了VIEW.GONE处理
 */
class TimeViewHolder extends RecyclerView.ViewHolder{
    TextView timeTxt;
    TextView tag;
    RelativeLayout root;
    Button deleteBtn;
    public TimeViewHolder(View itemView) {
        super(itemView);
        timeTxt = (TextView) itemView.findViewById(R.id.area_name);
        tag = (TextView) itemView.findViewById(R.id.area_simple_name);
        root = (RelativeLayout) itemView.findViewById(R.id.root_layout);
        deleteBtn = (Button) itemView.findViewById(R.id.deleteBtn);
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
    Button deleteBtn;
    public ClassViewHolder(View itemView) {
        super(itemView);
        classTxt = (TextView) itemView.findViewById(R.id.area_name);
        tag = (TextView) itemView.findViewById(R.id.area_simple_name);
        root = (RelativeLayout) itemView.findViewById(R.id.root_layout);
        deleteBtn = (Button) itemView.findViewById(R.id.deleteBtn);
    }
}
