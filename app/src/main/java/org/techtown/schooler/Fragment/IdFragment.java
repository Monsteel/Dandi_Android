package org.techtown.schooler.Fragment;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.schooler.R;
import org.techtown.schooler.StartMemberActivity.LoginActivity;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.IsOverlapped;
import org.techtown.schooler.network.LoginPostRequest;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;


public class IdFragment extends Fragment {

    EditText editText; // 아이디 입력
    Button button; // 다음 버튼

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_id, container, false);

        editText = rootView.findViewById(R.id.editText); // 아이디 입력
        button = rootView.findViewById(R.id.button); // 다음 버튼


        // button 을 클릭 시 중복 체크를 합니다.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // idCheck 매서드를 호출하면서 파라미터로 IsOverlapped 클래스의 파라미터인 editText 에 입력한 값을 전달한다.
                idCheck(new IsOverlapped(editText.getText().toString()));
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

                // 중복이 아닐 경우
                if(response.isSuccessful()){

                    Integer Status = response.body().getStatus(); // Status 값
                    String Message = response.body().getMessage(); // Message 값
                    Boolean data = response.body().getData().getOverlapped(); // true or false 값
                    Toast.makeText(getActivity(), Status + ":" + Message, Toast.LENGTH_SHORT).show();
                    Log.d("[isOverlapped] Status", Status + ":" + Message);
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
                }
            }

            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {
                Log.e("Err", "네트워크 연결오류");
            }
        });

    }





}
