package org.techtown.schooler.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;
import org.techtown.schooler.FinishSignUp;
import org.techtown.schooler.MainActivity;
import org.techtown.schooler.Model.User;
import org.techtown.schooler.R;
import org.techtown.schooler.StartMemberActivity.LoginActivity;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.LoginPostRequest;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import retrofit2.Call;
import retrofit2.Callback;

import static org.techtown.schooler.SigninUser.SignupActivity.user;


public class UserFragment extends Fragment {

    String isPublicChecked = "0";
    String isPushNotifyChecked = "0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_user, container, false);
        final EditText FirstName = rootView.findViewById(R.id.FirstName);
        final EditText LastName = rootView.findViewById(R.id.LastName);
        final Switch isPublic = rootView.findViewById(R.id.isPublic);
        Switch PushNotify = rootView.findViewById(R.id.PushNotify);
        Button Finish = rootView.findViewById(R.id.Finish);
        TextView Error = rootView.findViewById(R.id.Error);
        Error.setVisibility(View.INVISIBLE);


        isPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true) {
                    isPublicChecked = "1";
                }else if(isChecked = false){
                    isPublicChecked = "0";
                }
            }
        });

        isPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true) {
                    isPushNotifyChecked = "1";
                }else if(isChecked = false){
                    isPushNotifyChecked = "0";
                }
            }
        });


        Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String First_Name = FirstName.getText().toString();
                String Last_Name  = LastName.getText().toString();
                String Name = First_Name + Last_Name;

                user.setUser_name(Name);
                user.setIsPublic(isPublicChecked);
                user.setPushNotify(isPushNotifyChecked);
                Error.setVisibility(View.INVISIBLE);
                Error.setText("[회원가입을 진행 할 수 없습니다]\n");

                if(user.getUser_id() == null){
                    Error.setVisibility(View.VISIBLE);
                    Error.append("\n아이디가 입력되지 않았습니다\n");
                    Log.d("","Id");
                }
                if(user.getUser_pw() == null){
                    Error.setVisibility(View.VISIBLE);
                    Error.append("\n비밀번호가 입력되지 않았습니다\n");

                }if(user.getUser_email() == null){
                    Error.setVisibility(View.VISIBLE);
                    Error.append("\n이메일이  입력되지 않았습니다\n");
                }if(user.getSchool() == null || user.getSchool_class() == null || user.getSchool_grade() == null){
                    Error.setVisibility(View.VISIBLE);
                    Error.append("\n학교정보가 입력되지 않았습니다\n");
                }if(Name.length() == 0) {
                    Error.setVisibility(View.VISIBLE);
                    Error.append("\n이름이 입력되지 않았습니다\n");
                }
                if(user.getUser_id() != null && user.getUser_pw() != null && user.getUser_email() != null && (user.getSchool() != null || user.getSchool_class() != null || user.getSchool_grade() != null)&& user.getUser_name() != null)
                signup(user);
            }

        });

        return rootView;
    }


    private void signup(User usr) {

        final Call<Response<Data>> res = NetRetrofit.getInstance().getSignup().signupPost(usr);
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {
                startActivity(new Intent(getActivity(), FinishSignUp.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }

            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {
                Toast.makeText(getActivity(), "네트워크 연결 오류", Toast.LENGTH_SHORT).show();
                Log.e("Err", "네트워크 연결오류");
            }
        });
    }



}
