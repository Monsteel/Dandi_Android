package org.techtown.schooler.StartMemberActivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.techtown.schooler.MainActivity;
import org.techtown.schooler.R;
import org.techtown.schooler.network.SHA512;
import org.techtown.schooler.Signup.SignupOneActivity;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.Model.LoginPostRequest;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity{


    EditText Id_EditText; // Id 입력 창
    EditText Pw_EditText; // Pw 입력 창

    Button button; // Login 버튼

    TextView textView; // 회원가입 버튼

    InputMethodManager imm; // 가상 키패드 내리기

    LinearLayout layout; // 레이아웃

    CheckBox checkBox; // Id 저장 체크박스

    VideoView videoView; // VideoView

    public static int number = 0;

    SharedPreferences Login;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // 각각의 뷰들을 설정하고있습니다.
        Id_EditText = findViewById(R.id.Id_EditText); // ID
        Pw_EditText = findViewById(R.id.Pw_EditText); // PW
        button = findViewById(R.id.Login_Button); // Login 버튼
        textView = findViewById(R.id.join_TextView); // 회원가입 텍스트
        layout = findViewById(R.id.layout); // 레이아웃
        checkBox = findViewById(R.id.check_Id); // Id 저장 체크박스
        videoView = findViewById(R.id.videoView); // VideoView

        // SharedPreferences 클래스를 참조해서 sf 라는 인스턴스를 생성하였습니다.
        SharedPreferences sf = getSharedPreferences("sFile", MODE_PRIVATE);

        // Number 변수에 앞에서 저장한 Number 이름으로 설정한 값을 저장합니다.
        // 만약 값이 없을 경우 0을 저장한다.
        int Number = sf.getInt("Number", 0);

        // Id 라는 변수에 Id 이름으로 설정한 값을 저장합니다.
        String Id = sf.getString("Id","");

        // 저장한 Number 변수의 값이 1이라면 저장한 텍스트 값을 다시 복구하고 checkBox 값도 복구한다.
        if(Number == 1){

            Id_EditText.setText(Id);

            checkBox.setChecked(true);

        }


        // 가상 키패드를 내리기 위한 코드이다.
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        // 레이아웃을 클릭 시 가상 키패드를 내린다.
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(Id_EditText.getWindowToken(), 0);
            }
        });

        // Login 버튼을 클릭 시 login() 매서드를 호출합니다.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // login() 매서드의 파라미터로 사용자가 입력한 Id, Pw 를 전달하고있습니다.
                login(new LoginPostRequest(Id_EditText.getText().toString(), SHA512.getSHA512(Pw_EditText.getText().toString())));
            }
        });

        // 회원가입 버튼을 클릭 시 SignupActivity 즉 회원가입 페이지로 화면을 전환합니다.
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Intent 클래스를 참조해서 화면을 전환하고있습니다.
                Intent StartSignup = new Intent(LoginActivity.this, SignupOneActivity.class);
                startActivity(StartSignup);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        // 아이디 저장 체크박스를 클릭 시 사용자가 입력한 아이디를 저장하는 이벤트 입니다.
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {

                // Id 저장 클릭 (O)
                if(check == true){

                    // SharedPreferences 클래스를 참조해서 sharedPreferences 인스턴스를 생성합니다.
                    // 파라미터로 sFile, MODE_PRIVATE 기본으로 설정을합니다.
                    SharedPreferences sharedPreferences = getSharedPreferences("sFile", MODE_PRIVATE);

                    // SharedPreferences 클래스를 참조해서 Editor 속성을 사용하여 editor 라는 변수를 생성하였습니다.
                    // 앞에서 만든 sharedPreferences 인스턴스의 속성인 edit() 속성을 저장합니다.
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    // Id 변수를 생성해 Id_EditText 에서 입력한 아이디를 저장합니다.
                    String Id = Id_EditText.getText().toString();

                    // editor 변수의 속성인 putString 속성을 사용합니다.
                    // 파라미터로 Id 라는 이름으로 Id 변수를 전달합니다.
                    editor.putString("Id", Id);

                    editor.putInt("Number", 1);

                    // editor 을 최종적으로 커밋합니다.
                    editor.commit();

                }

                // Id 저장 클릭 (X)
                else if(check == false){


                    // SharedPreferences 클래스를 참조해서 sharedPreferences 인스턴스를 생성합니다.
                    // 파라미터로 sFile, MODE_PRIVATE 기본으로 설정을합니다.
                    SharedPreferences sharedPreferences = getSharedPreferences("sFile", MODE_PRIVATE);

                    // SharedPreferences 클래스를 참조해서 Editor 속성을 사용하여 editor 라는 변수를 생성하였습니다.
                    // 앞에서 만든 sharedPreferences 인스턴스의 속성인 edit() 속성을 저장합니다.
                    SharedPreferences.Editor editor = sharedPreferences.edit();


                    // check 가 해제된다면 editor 변수에 있는 값들을 clear() 합니다.
                    editor.clear();
                    editor.commit();
                }
            }
        });

    }


    // login 매서드
    private void login(LoginPostRequest loginPostRequest) {

        final Call<Response<Data>> res = NetRetrofit.getInstance().getLogin().loginPost(loginPostRequest);
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {

                if(Id_EditText.getText().toString().length() == 0 || Pw_EditText.getText().toString().length() == 0){
                    Toast.makeText(LoginActivity.this, "아이디 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    Log.d("[Login]","아이디 비밀번호를 입력해주세요");
                } else if (response.isSuccessful()) {
                    Integer Status = response.body().getStatus();
                    String Message = response.body().getMessage();
                    Toast.makeText(LoginActivity.this, Status + ":" + Message, Toast.LENGTH_SHORT).show();

                    Login = getSharedPreferences("Login", MODE_PRIVATE);
                    SharedPreferences.Editor editor = Login.edit();
                    editor.putString("token",response.body().getData().getToken());
                    editor.commit();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                    overridePendingTransition(R.anim.loadfadein, R.anim.loadfadeout);
                    Log.d("[Login] Status", Status + ":" + Message);
                }else if(response.code()==401){
                    Toast.makeText(LoginActivity.this, "승인 대기중인 유저입니다.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginActivity.this, "일치하는 회원정보가 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {
                Log.e("Err", "네트워크 연결오류");
            }
        });
    }

    // onBackPresses() 매서드를 사용하여 뒤로가기 버튼을 방지합니다.
    @Override
    public void onBackPressed(){
        // 뒤로가기 버튼을 클릭 시 앱이 종료된다.
        ActivityCompat.finishAffinity(this);
    }


    // 레이아웃 선택 시 키패드가 종료되도록 설정하는 매서드입니다.
    public void touchLayout(){
        imm.hideSoftInputFromWindow(Id_EditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(Pw_EditText.getWindowToken(), 0);
    }


    // 화면이 종료되고 다시 시작이 되어도 영상이 시작되게합니다.
    @Override
    protected void onStop() {
        super.onStop();
        ActivityCompat.finishAffinity(this);
    }
}

