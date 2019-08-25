package org.techtown.schooler.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.schooler.R;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.IsOverlapped;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class IdFragment extends Fragment {

    EditText Input_Id; // 아이디 입력
    Button IsOverLapped_button; // 중복 확인
    TextView check_Text;


    InputMethodManager imm; // 가상 키패드 내리기

    LinearLayout layout; // 레이아웃

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_id, container, false);


        Input_Id = rootView.findViewById(R.id.Input_Id); // 아이디 입력
        IsOverLapped_button = rootView.findViewById(R.id.IsOverLapped_button); // 중복 확인
        check_Text = rootView.findViewById(R.id.check_Text);

        // button 을 클릭 시 중복 체크를 합니다.
        IsOverLapped_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // idCheck 매서드를 호출하면서 파라미터로 IsOverlapped 클래스의 파라미터인 editText 에 입력한 값을 전달한다.
                idCheck(new IsOverlapped(Input_Id.getText().toString()));
            }
        });

        layout = rootView.findViewById(R.id.layout);


        imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);


        // 레이아웃 클릭 시 키패드 취소
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imm.hideSoftInputFromWindow(Input_Id.getWindowToken(), 0);
            }
        });



        return rootView;
    }

    // login 매서드
    private void idCheck(IsOverlapped isOverlapped) {

        final Call<Response<Data>> res = NetRetrofit.getInstance().getSignup().isoverlapped(isOverlapped);
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {

                imm.hideSoftInputFromWindow(Input_Id.getWindowToken(), 0);

                // 아이디를 입력하지 않은 경우
                if(Input_Id.getText().toString().length() == 0) {

                    check_Text.setText("아이디를 입력해주세요.");
                    check_Text.setTextColor(Color.parseColor("#bc0000"));
                }
                // 중복이 아닐 경우
                else if(response.isSuccessful()){

                    Integer Status = response.body().getStatus(); // Status 값
                    String Message = response.body().getMessage(); // Message 값
                    Boolean data = response.body().getData().getOverlapped(); // true or false 값
                    Log.d("[isOverlapped] Status", Status + ":" + Message);

                    check_Text.setText("사용 가능한 아이디 입니다.");
                    check_Text.setTextColor(Color.parseColor("#0ec600"));

                }

                // 중복일 경우
                else{

                   try{
                       JSONObject errorBody = new JSONObject(response.errorBody().string());
                       Integer Error =errorBody.getInt("status");//error status value

                       if(Error == 400 || Error == 500){

                           Response response1 = new Response();
                           response1.setStatus(errorBody.getInt("status"));
                           response1.setMessage(errorBody.getString("message"));
                           Log.e("[Login] Status", errorBody.getString("message"));
                       }

                   } catch (JSONException e) {
                       e.printStackTrace();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }

                    check_Text.setText("중복한 아이디가 존재합니다.");
                    check_Text.setTextColor(Color.parseColor("#bc0000"));
                }
            }

            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {
                Log.e("Err", "네트워크 연결오류");

            }
        });

    }




}

/**
 * 앞으로 해야할 것
 * - 앞으로 가기 버튼
  */
