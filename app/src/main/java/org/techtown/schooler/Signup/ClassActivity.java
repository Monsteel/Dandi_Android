package org.techtown.schooler.Signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.techtown.schooler.R;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import retrofit2.Call;
import retrofit2.Callback;

public class ClassActivity extends AppCompatActivity {

    Integer PickGrade;
    Integer PickClass;
    Integer Class;
    String OfficeId;
    String SchoolId;
    String SchoolKind;
    NumberPicker GradePicker;
    NumberPicker ClassPicker;
    TextView SchoolName;
    TextView DecideGrade;
    TextView DecideClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        GradePicker = findViewById(R.id.GradePicker);
        ClassPicker = findViewById(R.id.ClassPicker);
        SchoolName = findViewById(R.id.SchoolName);
        SchoolName.setText(getIntent().getStringExtra("School"));

        OfficeId = getIntent().getStringExtra("OfficeId");
        SchoolId = getIntent().getStringExtra("SchoolId");
        SchoolKind = getIntent().getStringExtra("SchoolKind");
        DecideClass = findViewById(R.id.Class);
        DecideGrade = findViewById(R.id.Grade);

        DecideGrade.setText("1학년");
        PickGrade = 1;
        DecideClass.setText("1반");
        PickClass = 1;



        if (SchoolKind.equals("중학교") || SchoolKind.equals("고등학교")) {
            GradePicker.setMinValue(1);
            GradePicker.setMaxValue(3);
        } else if (SchoolKind.equals("초등학교")) {
            GradePicker.setMinValue(1);
            GradePicker.setMaxValue(6);
        } else {
            GradePicker.setMinValue(1);
            GradePicker.setMaxValue(6);
        }


        final Call<Response<Data>> res = NetRetrofit.getInstance().getSignup().SearchClassGet(SchoolId, OfficeId, "1");
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {
                Log.d("Retrofit", response.toString());
                if (response.code() == 200) {
                    Class = response.body().getData().getClassCount();
                    Log.d("", Class + "");
                    ClassPicker.setMinValue(1);
                    ClassPicker.setMaxValue(Class);
                } else {
                    ClassPicker.setMinValue(1);
                    ClassPicker.setMaxValue(50);
                }

            }

            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {
                Log.e("Err", "네트워크 연결오류");
            }
        });

        GradePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                PickGrade = newVal;
                Log.d("Grade", newVal + "");

                DecideGrade.setText(PickGrade + "학년");


                final Call<Response<Data>> res = NetRetrofit.getInstance().getSignup().SearchClassGet(SchoolId, OfficeId, PickGrade + "");
                res.enqueue(new Callback<Response<Data>>() {
                    @Override
                    public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {
                        Log.d("Retrofit", response.toString());
                        if (response.code() == 200) {
                            Class = response.body().getData().getClassCount();
                            Log.d("", Class + "");
                            ClassPicker.setMinValue(1);
                            ClassPicker.setMaxValue(Class);
                        } else {
                            ClassPicker.setMinValue(1);
                            ClassPicker.setMaxValue(50);
                        }

                    }

                    @Override
                    public void onFailure(Call<Response<Data>> call, Throwable t) {
                        Log.e("Err", "네트워크 연결오류");
                    }
                });
            }
        });


        ClassPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                PickClass = newVal;
                DecideClass.setText(PickClass + "반");
                Log.d("Class", PickClass + "");
            }
        });
    }


    public void toGoNext(View view){
        Intent intent = new Intent(getApplicationContext(), NotifyActivity.class);
        intent.putExtra("Name",getIntent().getStringExtra("Name"));
        intent.putExtra("Id",getIntent().getStringExtra("Id"));
        intent.putExtra("Password",getIntent().getStringExtra("Password"));
        intent.putExtra("Email",getIntent().getStringExtra("Email"));
        intent.putExtra("PhoneNumber",getIntent().getStringExtra("PhoneNumber"));
        intent.putExtra("SchoolId", getIntent().getStringExtra("SchoolId"));
        intent.putExtra("SchoolName", getIntent().getStringExtra("School"));
        intent.putExtra("Grade",PickGrade+"");
        intent.putExtra("Class",PickClass+"");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void toGoBack (View view) {
        onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed () {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
