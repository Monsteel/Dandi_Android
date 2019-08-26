package org.techtown.schooler.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.schooler.Model.User;
import org.techtown.schooler.R;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.Email;
import org.techtown.schooler.network.IsOverlapped;
import org.techtown.schooler.network.LoginPostRequest;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import java.io.IOException;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static org.techtown.schooler.SigninUser.SignupActivity.user;


public class EmailFragment extends Fragment {

    EditText email;
    EditText inputAuthCode;
    Button EmailSend;
    Button authCodeCheck;
    TextView isEmailText;

    String AuthCode;
    Boolean isSendMail;
    LinearLayout AuthCodeLayout;

    LinearLayout layout;
    InputMethodManager imm; // 가상 키패드 내리기

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_email, container, false);

        email = rootView.findViewById(R.id.inputEmail);
        inputAuthCode = rootView.findViewById(R.id.inputAuthCode);
        isEmailText =rootView.findViewById(R.id.isEmailText);

        AuthCodeLayout = (LinearLayout)rootView.findViewById(R.id.AuthCodeLayout);

        EmailSend = rootView.findViewById(R.id.SendAuthCode);
        authCodeCheck = rootView.findViewById(R.id.CheckAuthCode);

        AuthCodeLayout.setVisibility(View.INVISIBLE);
        isEmailText.setVisibility(View.INVISIBLE);

        //E-mail전송버튼 클릭 시
        EmailSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // idCheck 매서드를 호출하면서 파라미터로 IsOverlapped 클래스의 파라미터인 editText 에 입력한 값을 전달한다.

                if(isEmail(email.getText().toString())){
                    SendMail(new Email(email.getText().toString()));
                }else{
                    //이메일 유효성 검사 불통과 시
                    isEmailText.setVisibility(View.VISIBLE);
                    isEmailText.setTextColor(Color.parseColor("#bc0000"));
                    isEmailText.setText("유효하지 않는 이메일 형식입니다");

                }

            }
        });
        //


        //코드체크 버튼 클릭 시
        authCodeCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String inputAuthCodeSaver = inputAuthCode.getText().toString()+"";
                if(AuthCode.equals(inputAuthCodeSaver)){


                    // 이메일 세터 저장
                    user.setUser_email(email.getText().toString());
                    Toast.makeText(getActivity(),"인증이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    Log.d("[CheckAuthCode]", "인증이 완료되었습니다.");

                    email.setInputType(InputType.TYPE_NULL);//Email 읽기모드 변경
                    EmailSend.setEnabled(false);
                    inputAuthCode.setInputType(InputType.TYPE_NULL);//authCode 읽기모드 변경
                }else{
                    Toast.makeText(getActivity(),"인증번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                    Log.e("[CheckAuthCode]", "인증번호가 올바르지 않습니다.");
                }
            }
        });

        layout = rootView.findViewById(R.id.layout);

        imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);


        // 레이아웃 클릭 시 키패드 취소
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imm.hideSoftInputFromWindow(email.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(inputAuthCode.getWindowToken(), 0);
            }
        });

        return rootView;
    }

    public final static boolean isValidEmail(String input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches();
        }
    }

    private void SendMail(Email email) {

        final Call<Response<Data>> res = NetRetrofit.getInstance().getSignup().eamilPost(email);
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {

                // Status == 200
                if(response.isSuccessful()){
                    SendEmailSuccess();
                    Integer Status = response.body().getStatus(); // Status 값
                    String Message = response.body().getMessage(); // Message 값
                    AuthCode = response.body().getData().getAuthCode(); // authCode(이메일 코드)
                    Toast.makeText(getActivity(), Status + ":" + Message, Toast.LENGTH_SHORT).show();
                    Log.d("[SandEmail] Status", Status + ":" + Message);

                }

                // Status != 200
                else{
                    try {
                        JSONObject errorBody = new JSONObject(response.errorBody().string());

                        Integer Error =errorBody.getInt("status");//error status value
                        if (Error == 500) {
                            Response response1 = new Response();
                            response1.setStatus(errorBody.getInt("status"));
                            response1.setMessage(errorBody.getString("message"));
                            Log.e("[SandEmail] Status", errorBody.getString("message"));
                            SendEmailFail();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {
                Log.e("Err", "네트워크 연결오류");
                SendEmailFail();
            }
        });

    }

    public static boolean isEmail(String email) {
        if (email == null) return false;
        boolean b = Pattern.matches(
                "[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+",
                email.trim());
        return b;
    }

    public void SendEmailSuccess() {
        AuthCodeLayout.setVisibility(View.VISIBLE);
        isEmailText.setTextColor(Color.parseColor("#0ec600"));
        isEmailText.setText("이메일 전송에 성공했습니다");
        isEmailText.setVisibility(View.VISIBLE);
    }

    public void SendEmailFail() {
        isEmailText.setVisibility(View.VISIBLE);
        isEmailText.setTextColor(Color.parseColor("#bc0000"));
        isEmailText.setText("이메일 전송에 실패했습니다");
    }
    //메일발송후, 일정시간동안만 인증가능하게하는 로직 추가하기
    //텍스트박스 필수입력



    /////////////////////이메일 유효성 검사하기
    /////////////////////인증코드 띄어쓰기 포함되면 제거해서 확인하는 로직 추가하기(서버에서)
    ////////////////////인증완료 후, EditText 둘 다 수정못하는 명령어 추가하기
    ////////////////////인증코드 받기 버튼을 누르면, 인증코드 입력하는 칸 + 버튼 보이게 하기


}
