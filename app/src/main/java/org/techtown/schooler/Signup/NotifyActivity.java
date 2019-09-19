package org.techtown.schooler.Signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.techtown.schooler.Model.User;
import org.techtown.schooler.R;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import retrofit2.Call;
import retrofit2.Callback;

public class NotifyActivity extends AppCompatActivity {

    String Name;
    String Id;
    String Pw;
    String Email;
    String Phone;
    String SchoolId;
    String Grade;
    String Class;

    TextView ClearName;
    TextView ClearId;
    TextView ClearEmail;
    TextView ClearPhone;
    TextView ClearSchool;
    TextView ClearGrade;
    TextView ClearClass;
    TextView Finish;

    Switch isPublic;
    Switch isNotify;



    public static User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        ClearName =(TextView)findViewById(R.id.ClearName);
        ClearId =(TextView)findViewById(R.id.ClearId);
        ClearEmail =(TextView)findViewById(R.id.ClearEmail);
        ClearPhone = (TextView)findViewById(R.id.ClearPhoneNumber);
        ClearSchool = (TextView)findViewById(R.id.ClearSchool);
        ClearGrade = (TextView)findViewById(R.id.ClearGrade);
        ClearClass = (TextView)findViewById(R.id.ClearClass);
        isPublic = (Switch)findViewById(R.id.isPublic);
        isNotify = (Switch)findViewById(R.id.isNotify);
        Finish = (TextView)findViewById(R.id.Finish);

        user.setPushNotify("false");
        isPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    user.setPushNotify("true");
                }else{
                    user.setPushNotify("false");
                }
            }
        });

        user.setIsPublic("false");
        isNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    user.setIsPublic("true");
                }else{
                    user.setIsPublic("false");
                }
            }
        });

        Name = getIntent().getStringExtra("Name");
        Id = getIntent().getStringExtra("Id");
        Pw = getIntent().getStringExtra("Password");
        Email = getIntent().getStringExtra("Email");
        Phone = getIntent().getStringExtra("PhoneNumber");
        SchoolId = getIntent().getStringExtra("SchoolId");
        Grade = getIntent().getStringExtra("Grade");
        Class = getIntent().getStringExtra("Class");

        user.setUser_name(Name);
        user.setUser_id(Id);
        user.setUser_pw(Pw);
        user.setUser_email(Email);
        user.setUser_phone(Phone+"");
        user.setSchool(SchoolId);
        user.setSchool_grade(Grade);
        user.setSchool_class(Class);

        ClearName.setText(Name);
        ClearId.setText(Id);
        ClearEmail.setText(Email);
        ClearPhone.setText(Phone);
        ClearSchool.setText(getIntent().getStringExtra("SchoolName"));
        ClearGrade.setText(Grade);
        ClearClass.setText(Class);

        Finish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                signup(user);
            }
        });

    }

    public void signup(User user) {
        final Call<Response<Data>> res = NetRetrofit.getInstance().getSignup().signupPost(user);
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {
                if (response.isSuccessful()) {
                    Integer Status = response.body().getStatus();
                    String Message = response.body().getMessage();
                    Log.d("[SignUp] Status", Status + ":" + Message);

                    Intent intent = new Intent(NotifyActivity.this, FinishSignup.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                }else{
                    try {
                        JSONObject errorBody = new JSONObject(response.errorBody().string());
                        Integer Error =errorBody.getInt("status");

                        if (Error == 401 ||Error == 405) {
                            Response response1 = new Response();
                            response1.setStatus(errorBody.getInt("status"));
                            response1.setMessage(errorBody.getString("message"));
                            Log.e("[SignUp] Status", errorBody.getString("message"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {
                Log.e("Err", "네트워크 연결오류");
                Toast.makeText(NotifyActivity.this, "네트워크에 연결되지 않았습니다.\nError:200", Toast.LENGTH_SHORT).show();

            }
        });

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
