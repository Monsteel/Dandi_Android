package org.techtown.schooler.Channels;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.schooler.Model.ChannelInfo;
import org.techtown.schooler.Model.User;
import org.techtown.schooler.R;
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

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.ViewHolder>{

    private final List<User> mDataList;
    SharedPreferences Login;

    public MemberListAdapter(List<User> dataList){
        mDataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member,parent,false);
        //뷰홀더때문에 노가다했었음....
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User item = mDataList.get(position);

        Login = holder.Id.getContext().getSharedPreferences("Login", MODE_PRIVATE);//SharedPreferences 선언

        holder.Name.setText(item.getUser_name());
        holder.Id.setText(item.getUser_id());

        new DownloadImageFromInternet(holder.Profile)
                .execute(item.getPrifle_pic());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name;
        TextView Id;
        ImageView Profile;
        CardView MemberCardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            MemberCardView = itemView.findViewById(R.id.User_Info);
            Name = itemView.findViewById(R.id.Name_Info);
            Id = itemView.findViewById(R.id.ID_Info);
            Profile = itemView.findViewById(R.id.Profile_Info);
        }
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
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
