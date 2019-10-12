package org.techtown.schooler.NavigationFragment;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.techtown.schooler.Account.JoinedChannel;
import org.techtown.schooler.MainActivity;
import org.techtown.schooler.Model.UserInfo;
import org.techtown.schooler.R;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import java.io.InputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;

public class AccountFragment extends Fragment {

    SharedPreferences Login;
    UserInfo userData;

    // View
    ImageView profile;
    Button edit_profile;
    Button check_channel;
    TextView user_name;
    TextView user_id;
    TextView user_phone;
    TextView user_email;
    TextView user_school;
    TextView user_class;

    private Boolean isPermission = true;
    private static final int PICK_FROM_ALBUM = 1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_account, container, false);
        Login = getActivity().getSharedPreferences("Login", MODE_PRIVATE);//SharedPreferences 선언

        userProfile();

        // View
        profile = rootView.findViewById(R.id.profile);
        edit_profile = rootView.findViewById(R.id.edit_profile); // Button
        check_channel = rootView.findViewById(R.id.check_channel); // Button
        user_name = rootView.findViewById(R.id.user_name);
        user_id = rootView.findViewById(R.id.user_id);
        user_phone = rootView.findViewById(R.id.user_phone);
        user_email = rootView.findViewById(R.id.user_email);
        user_school = rootView.findViewById(R.id.user_school);
        user_class = rootView.findViewById(R.id.user_class);

        // profile 즉 프로필 사진을 둥글게 만들어줍니다.
        profile.setBackground(new ShapeDrawable(new OvalShape()));
        profile.setClipToOutline(true);

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tedPermission();
                
                if(isPermission){

                    goToAlbum();
                } else {

                    Toast.makeText(rootView.getContext(), "", Toast.LENGTH_SHORT).show();
                }
                
            }
        });

        check_channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), JoinedChannel.class);
                startActivity(intent);

                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        return rootView;
    }

    public void userProfile(){

        Call<Response<Data>> res = NetRetrofit.getInstance().getProfile().GetProfile(Login.getString("token",""),"");
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {

                if(response.code() == 200){

                    userData = response.body().getData().getUserInfo();

                    // View 텍스트 삽입
                    user_name.setText(userData.getUser_name());
                    user_id.setText("(" + userData.getUser_id() + ")");
                    user_phone.setText(userData.getUser_phone());
                    user_email.setText(userData.getUser_email());
                    user_school.setText(userData.getSchool().getSchool_name());
                    user_class.setText(userData.getSchool_grade() + "학년 " + userData.getSchool_class() + "반");

                    new MainActivity.DownloadImageFromInternet(profile)
                            .execute(userData.getProfile_pic());

                    Toast.makeText(getActivity(), "정상적으로 프로필을 조회하였습니다.", Toast.LENGTH_SHORT).show();
                    Log.e("[status 200]",response.message());

                } else if(response.code() == 403){
                    Log.e("[status 403]",response.message());
                } else if(response.code() == 404){

                    Log.e("[status 404]",response.message());
                } else if(response.code() == 500){

                    Log.e("[status 500]",response.message());
                }

            }

            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {

                Toast.makeText(getActivity(), "서버 통신 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e("[서버 통신 X]","서버 통신 오류");
            }
        });

    }

    public void tedPermission(){

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {

            }
        };

        TedPermission.with(getContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    public void goToAlbum(){

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    public static class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("[ImageDownLoad][Error]", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

}
