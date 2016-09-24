package com.yujie.heromanager.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.yujie.heromanager.HeroApplication;
import com.yujie.heromanager.I;
import com.yujie.heromanager.R;
import com.yujie.heromanager.adapter.ExamAdapter;
import com.yujie.heromanager.bean.ClassObj;
import com.yujie.heromanager.bean.ExamBean;
import com.yujie.heromanager.utils.OkHttpUtils;
import com.yujie.heromanager.utils.Utils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExamActivity extends AppCompatActivity {
    public static final String TAG = ExamActivity.class.getSimpleName();
    @Bind(R.id.add_Exam)
    ImageView addExam;
    @Bind(R.id.content_bottom)
    RecyclerView contentBottom;
    private Context mContext = ExamActivity.this;
    private ArrayList<ExamBean> examList;
    private ExamAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        examList = new ArrayList<>();
        OkHttpUtils<ExamBean[]> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST,I.Request.REQUEST_GET_ALL_EXAM)
                .targetClass(ExamBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<ExamBean[]>() {
                    @Override
                    public void onSuccess(ExamBean[] result) {
                        if (result!=null & result.length!=0){
                            examList = Utils.array2List(result);
                            adapter = new ExamAdapter(mContext,examList);
                            contentBottom.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    private void initView() {
        contentBottom.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
    }

    @OnClick(R.id.add_Exam)
    public void onClick() {

    }
}
