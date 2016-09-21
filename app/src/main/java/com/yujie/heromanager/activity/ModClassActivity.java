package com.yujie.heromanager.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.yujie.heromanager.HeroApplication;
import com.yujie.heromanager.I;
import com.yujie.heromanager.R;
import com.yujie.heromanager.bean.ClassObj;
import com.yujie.heromanager.bean.Result;
import com.yujie.heromanager.utils.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModClassActivity extends AppCompatActivity {
    public static final String TAG = ModClassActivity.class.getSimpleName();
    private Context mContext = ModClassActivity.this;
    @Bind(R.id.className)
    EditText className;
    @Bind(R.id.areaSpinner)
    Spinner areaSpinner;
    @Bind(R.id.courseSpinner)
    Spinner courseSpinner;
    @Bind(R.id.timeSpinner)
    Spinner timeSpinner;
    @Bind(R.id.getClassSimpleNameBtn)
    Button getClassSimpleNameBtn;
    @Bind(R.id.classnum)
    EditText classnum;
    @Bind(R.id.addClassBtn)
    Button addClassBtn;
    @Bind(R.id.modClassBtn)
    Button modClassBtn;
    private HashMap<String, String> areaMap;
    private HashMap<String, String> courseMap;
    private ArrayList<String> timeList;
    ArrayList<String> areaArr;
    ArrayList<String> courseArr;
    ClassObj obj;
    String time;
    String area_id;
    String course_id;
    String num;
    String classSimpleName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod_class);
        ButterKnife.bind(this);
        initSpinner();
        initData();
        initBtnListener();
        initSpinnerListener();
        initEditListener();
    }

    private void initEditListener() {
        classnum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getValue();
                getClassSimpleNameBtn.setText(course_id+time+area_id+num);
            }
        });
    }

    private void initSpinnerListener() {
        setListeners(areaSpinner);
        setListeners(courseSpinner);
        setListeners(timeSpinner);
    }

    private void setListeners(Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getValue();
                getClassSimpleNameBtn.setText(course_id+time+area_id+num);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initData() {
        obj = (ClassObj) getIntent().getSerializableExtra("classObj");
        if (obj!=null){
            className.setText(obj.getClass_name());
            Set<Map.Entry<String, String>> entries = areaMap.entrySet();
            for (Iterator i = entries.iterator(); i.hasNext();){
                Map.Entry next = (Map.Entry)i.next();
                if (next.getValue().equals(obj.getB_area())){
                    for (int j=0;j<areaArr.size();j++){
                        if (next.getKey().equals(areaArr.get(j))){
                            areaSpinner.setSelection(j);
                            break;
                        }
                    }
                    break;
                }
            }

            Set<Map.Entry<String, String>> entries1 = courseMap.entrySet();
            for (Iterator i = entries1.iterator(); i.hasNext();){
                Map.Entry next = (Map.Entry)i.next();
                if (next.getValue().equals(obj.getB_course())){
                    for (int j=0;j<courseArr.size();j++){
                        if (next.getKey().equals(courseArr.get(j))){
                            courseSpinner.setSelection(j);
                            break;
                        }
                    }
                    break;
                }
            }

            for (int i = 0;i<timeList.size();i++){
                if (obj.getStart_time().equals(timeList.get(i))){
                    timeSpinner.setSelection(i,true);
                }
            }
            getClassSimpleNameBtn.setText(obj.getSimple_name());
            classnum.setText(obj.getSimple_name().substring(obj.getSimple_name().length()-2,obj.getSimple_name().length()));
        }
    }

    private void initBtnListener() {
        getClassSimpleNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValue();
                if (num.isEmpty()){
                    classnum.setError("班级编号不能为空");
                    classnum.requestFocus();
                    return;
                }
                getClassSimpleNameBtn.setText(course_id+time+area_id+num);
            }
        });
    }

    private void getValue() {
        String area = areaSpinner.getSelectedItem().toString();
        String course = courseSpinner.getSelectedItem().toString();
        time = timeSpinner.getSelectedItem().toString();
        area_id = areaMap.get(area);
        course_id = courseMap.get(course);
        num = classnum.getText().toString();
    }

    private void initSpinner() {
        areaMap = HeroApplication.getInstance().getAreaMap();
        courseMap = HeroApplication.getInstance().getCourseMap();
        timeList = HeroApplication.getInstance().getTimelist();
        areaArr = new ArrayList<>();
        courseArr = new ArrayList<>();
        Set<Map.Entry<String, String>> entries = areaMap.entrySet();
        for (Iterator i = entries.iterator(); i.hasNext();){
            Map.Entry next = (Map.Entry)i.next();
            areaArr.add((String) next.getKey());
        }
        Set<Map.Entry<String, String>> entries1 = courseMap.entrySet();
        for (Iterator i = entries1.iterator();i.hasNext();){
            Map.Entry next = (Map.Entry)i.next();
            courseArr.add((String) next.getKey());
        }
        ArrayAdapter<String> areaAdapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, areaArr);
        areaAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        areaSpinner.setAdapter(areaAdapter);

        ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, courseArr);
        courseAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        courseSpinner.setAdapter(courseAdapter);

        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, timeList);
        timeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);
    }


    @OnClick({R.id.getClassSimpleNameBtn, R.id.addClassBtn, R.id.modClassBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addClassBtn:
                addClass();
                break;
            case R.id.modClassBtn:
                modClass();
                break;
        }
    }

    private void addClass() {
        getValue();
        if (num.isEmpty()){
            classnum.setError("班级编号不能为空");
            classnum.requestFocus();
            return;
        }
        String Name = className.getText().toString();
        if (Name.isEmpty()){
            className.setError("班级名称不能为空");
            className.requestFocus();
            return;
        }
        classSimpleName = getClassSimpleNameBtn.getText().toString();
        if (classSimpleName.equals("点击生成班级简称")){
            Toast.makeText(ModClassActivity.this,"班级简称不正确，请重新选择",Toast.LENGTH_LONG).show();
        }
        OkHttpUtils<Result> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST,I.Request.REQUEST_ADD_CLASS)
                .addParam(I.IClass.CLASS_NAME,Name)
                .addParam(I.IClass.B_AREA,area_id)
                .addParam(I.IClass.B_COURSE,course_id)
                .addParam(I.IClass.START_TIME,time)
                .addParam(I.IClass.CLASS_NUMBER,num)
                .addParam(I.IClass.SIMPLE_NAME,classSimpleName)
                .post()
                .targetClass(Result.class)
                .execute(new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result!=null & result.isFlag()){
                            Toast.makeText(ModClassActivity.this,"添加成功",Toast.LENGTH_LONG).show();
                            finish();
                        }else {
                            Toast.makeText(ModClassActivity.this,"添加失败，该班级已经存在",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(ModClassActivity.this,"无法连接到服务器，请稍后再试",Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void modClass() {
        getValue();
        if (num.isEmpty()){
            classnum.setError("班级编号不能为空");
            classnum.requestFocus();
            return;
        }
        String Name = className.getText().toString();
        if (Name.isEmpty()){
            className.setError("班级名称不能为空");
            className.requestFocus();
            return;
        }
        classSimpleName = getClassSimpleNameBtn.getText().toString();
        if (classSimpleName.equals("点击生成班级简称")){
            Toast.makeText(ModClassActivity.this,"班级简称不正确，请重新选择",Toast.LENGTH_LONG).show();
        }
        OkHttpUtils<Result> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST,I.Request.REQUEST_MOD_CLASS)
                .addParam(I.IClass.CLASS_NAME,Name)
                .addParam(I.IClass.B_AREA,area_id)
                .addParam(I.IClass.B_COURSE,course_id)
                .addParam(I.IClass.START_TIME,time)
                .addParam(I.IClass.CLASS_NUMBER,num)
                .addParam(I.IClass.SIMPLE_NAME,classSimpleName)
                .addParam(I.Mark.MARK,obj.getSimple_name())
                .post()
                .targetClass(Result.class)
                .execute(new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result!=null & result.isFlag()){
                            Toast.makeText(ModClassActivity.this,"修改成功",Toast.LENGTH_LONG).show();
                            finish();
                        }else {
                            Toast.makeText(ModClassActivity.this,"修改失败，请确认班级简称是否正确",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(ModClassActivity.this,"无法连接到服务器，请稍后再试",Toast.LENGTH_LONG).show();
                    }
                });
    }
}
