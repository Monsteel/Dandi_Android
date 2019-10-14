package org.techtown.schooler.Channels;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.techtown.schooler.Model.User;
import org.techtown.schooler.Model.UserInfo;
import org.techtown.schooler.R;
import org.techtown.schooler.StartMemberActivity.LoginActivity;
import org.techtown.schooler.network.ChannelListAdapter;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;

public class AwaitUserAdapter extends RecyclerView.Adapter<AwaitUserAdapter.ViewHolder>{

    private final List<UserInfo> mDataList;
    private final List<UserInfo> arrayList;
    SharedPreferences Login;
    String channel_id;

    public AwaitUserAdapter(List<UserInfo> dataList){
        mDataList = dataList;
        arrayList = new ArrayList<UserInfo>();
        arrayList.addAll(mDataList);
    }

    public void CatchChannelId(String channel_id){
        this.channel_id = channel_id;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mDataList.clear();
        if (charText.length() == 0) {
            mDataList.addAll(arrayList);
        } else {
            for (UserInfo userinfo : arrayList) {
                String name = userinfo.getUser_name();
                if (name.toLowerCase().contains(charText)) {
                    mDataList.add(userinfo);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_await_user_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Activity activity = (Activity) holder.school.getContext();
        Login = activity.getSharedPreferences("Login", MODE_PRIVATE);//SharedPreferences 선언
        UserInfo item = mDataList.get(position);

        holder.school.setText(item.getSchool().getSchool_name());
        holder.gradeClass.setText(item.getSchool_grade()+"학년"+item.getSchool_class()+"학년");
        holder.name.setText(item.getUser_name());

        Glide.with(holder.gradeClass.getContext()).load(item.getProfile_pic()).into(holder.profile);



        holder.allowbtn.setOnClickListener(v -> {
            Log.e("승인버튼", "클릭됐어요");
            Allow(holder, item);
        });


        holder.rejectbtn.setOnClickListener(v -> {
            Log.e("거절버튼", "클릭됐어요");
            Reject(holder, item);
        });

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView school;
        TextView gradeClass;
        TextView name;
        ImageView profile;

        TextView allowbtn;
        TextView rejectbtn;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            school = (TextView)itemView.findViewById(R.id.school);
            gradeClass = (TextView)itemView.findViewById(R.id.gradeClass);
            name = (TextView)itemView.findViewById(R.id.name);
            profile = (ImageView)itemView.findViewById(R.id.profile);

            allowbtn= (TextView)itemView.findViewById(R.id.Allow);
            rejectbtn = (TextView)itemView.findViewById(R.id.Reject);
        }
    }

    public void Allow(ViewHolder holder,UserInfo item){
        final Call<Response<Data>> res = NetRetrofit.getInstance().getChannel().AllowUser(Login.getString("token",""),channel_id,item.getUser_id());//token불러오기
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {
                if(response.code() == 200){
                    Toast.makeText(holder.profile.getContext(), "승인되었습니다", Toast.LENGTH_SHORT).show();
                    mDataList.remove(item);
                    notifyDataSetChanged();
                }else if(response.code() == 400){

                }else if(response.code() == 403){

                }else if(response.code() == 410){
                    SharedPreferences.Editor editor = Login.edit();
                    editor.putString("token",null);
                    editor.putString("id",null);
                    editor.commit();
                    holder.profile.getContext().startActivity(new Intent(holder.profile.getContext(), LoginActivity.class));
                    Log.e("","토큰 만료");
                    Toast.makeText(holder.profile.getContext(), "토큰이 만료되었습니다\n다시 로그인 해 주세요", Toast.LENGTH_SHORT).show();
                }else{
                    Log.e("","오류 발생");
                    Toast.makeText(holder.profile.getContext(), "서버에서 오류가 발생했습니다.\n문제가 지속되면 관리자에게 문의하세요", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {
                Log.e("","네트워크 오류");
                Toast.makeText(holder.profile.getContext(), "네크워크 상태가 원할하지 않습니다.\n잠시 후 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void Reject(ViewHolder holder,UserInfo item){
        final Call<Response<Data>> res = NetRetrofit.getInstance().getChannel().RejectUser(Login.getString("token",""),channel_id,item.getUser_id());//token불러오기
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {
                if(response.code() == 200){
                    Toast.makeText(holder.profile.getContext(), "거절되었습니다.", Toast.LENGTH_SHORT).show();
                    mDataList.remove(item);
                    notifyDataSetChanged();
                }else if(response.code() == 400){

                }else if(response.code() == 403){

                }else if(response.code() == 410){
                    SharedPreferences.Editor editor = Login.edit();
                    editor.putString("token",null);
                    editor.putString("id",null);
                    editor.commit();
                    holder.profile.getContext().startActivity(new Intent(holder.profile.getContext(), LoginActivity.class));
                    Log.e("","토큰 만료");
                    Toast.makeText(holder.profile.getContext(), "토큰이 만료되었습니다\n다시 로그인 해 주세요", Toast.LENGTH_SHORT).show();
                }else{
                    Log.e("","오류 발생");
                    Toast.makeText(holder.profile.getContext(), "서버에서 오류가 발생했습니다.\n문제가 지속되면 관리자에게 문의하세요", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {
                Log.e("","네트워크 오류");
                Toast.makeText(holder.profile.getContext(), "네크워크 상태가 원할하지 않습니다.\n잠시 후 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
