package org.techtown.schooler.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.schooler.R;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.Email;
import org.techtown.schooler.network.IsOverlapped;
import org.techtown.schooler.network.LoginPostRequest;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;


public class EmailFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        idCheck(new Email("eun36739@"));

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_email, container, false);
        return rootView;
    }

    public final static boolean isValidEmail(String input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches();
        }
    }

    private void idCheck(Email email) {

        final Call<Response<Data>> res = NetRetrofit.getInstance().getSignup().eamilPost(email);
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {

                // Status == 200
                if(response.isSuccessful()){

                    Integer Status = response.body().getStatus(); // Status 값
                    String Message = response.body().getMessage(); // Message 값
                    String authCode = response.body().getData().getAuthCode(); // authCode(이메일 코드)
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
                        }
                    } catch (Exception e) {
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
