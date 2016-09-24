package com.yujie.heromanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.yujie.heromanager.R;
import com.yujie.heromanager.bean.ExamBean;

import java.util.ArrayList;

/**
 * Created by yujie on 16-9-24.
 */
class ExamViewHolder extends RecyclerView.ViewHolder{
    TextView exam_id;
    TextView exam_name;
    TextView exam_time;
    TextView exam_course;
    CheckBox exam_status;
    public ExamViewHolder(View itemView) {
        super(itemView);
        exam_id = (TextView) itemView.findViewById(R.id.exam_id);
        exam_name = (TextView) itemView.findViewById(R.id.exam_name);
        exam_time = (TextView) itemView.findViewById(R.id.exam_time);
        exam_course = (TextView) itemView.findViewById(R.id.exam_course);
        exam_status = (CheckBox) itemView.findViewById(R.id.exam_status);
    }
}
public class ExamAdapter extends RecyclerView.Adapter<ExamViewHolder>{
    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<ExamBean> examList;

    public ExamAdapter(Context mContext,ArrayList<ExamBean> examList) {
        this.mContext = mContext;
        this.examList = examList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ExamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_exam_layout, parent, false);
        ExamViewHolder holder = new ExamViewHolder(view);
        holder.setIsRecyclable(true);
        return holder;
    }

    @Override
    public void onBindViewHolder(ExamViewHolder holder, int position) {
        ExamBean item = getItem(position);
        holder.exam_id.setText(item.getId()+"");
        holder.exam_name.setText(item.getExam_name());
        holder.exam_time.setText(item.getExam_time());
        holder.exam_course.setText(item.getCourse_id());
        if (item.getStatus()==1){
            holder.exam_status.setChecked(true);
        }else {
            holder.exam_status.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return examList==null?0:examList.size();
    }

    public ExamBean getItem(int position){
        return examList.get(position);
    }
}
