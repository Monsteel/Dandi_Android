package org.techtown.schooler.network;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.schooler.MainActivity;
import org.techtown.schooler.Model.ChannelInfo;
import org.techtown.schooler.MoreChannelInfoActivity;
import org.techtown.schooler.NavigationFragment.ChannelFragment;
import org.techtown.schooler.R;
import org.techtown.schooler.StartMemberActivity.LoginActivity;

import java.io.InputStream;
import java.util.List;

public class ChannelListAdapter extends RecyclerView.Adapter<ChannelListAdapter.ViewHolder>{

    private final List<ChannelInfo> mDataList;

    public ChannelListAdapter(List<ChannelInfo> dataList){
        mDataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_channel,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChannelInfo item = mDataList.get(position);
        holder.title.setText(item.getName());
        holder.content.setText(item.getExplain());
        holder.create_User.setText("| Master : " + item.getCreate_user());

        new DownloadImageFromInternet(holder.BackGroundImage)
                .execute("http://10.80.162.124:5000/static/image/thumbnail_basic.jpg");

        if(item.getIsPublic().equals("0")) {
            holder.isPublicImage.setImageResource(R.drawable.locked);
        }else{
            holder.isPublicImage.setImageResource(R.drawable.un_locked);
        }

        holder.channelCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("리스트뷰","클릭됐어요");
                Intent intent = new Intent(holder.joinButton.getContext(), MoreChannelInfoActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        holder.joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("가입버튼","클릭됐어요");

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.joinButton.getContext());
                builder.setTitle("채널가입");
                builder.setMessage("채널에 가입하시겠습니까?\n\n채널이름 : "+item.getName()+"\n개설자 : "+item.getCreate_user());
                builder.setIcon(Integer.parseInt(String.valueOf(R.drawable.ic_exit_to_app_black_24dp)));

                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e("예 버튼","클릭됐어요");
                    }
                });

                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.e("아니오 버튼","클릭됐어요");
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView content;
        ImageView BackGroundImage;
        ImageView isPublicImage;
        TextView create_User;
        CardView channelCardView;
        Button joinButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.Title_TextView);
            title.setSingleLine(true);
            title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            title.setSelected(true);
            create_User = itemView.findViewById(R.id.MakeUser_TextView);
            content = itemView.findViewById(R.id.Content_TextView);
            BackGroundImage = itemView.findViewById(R.id.Background_ImageView);
            isPublicImage = itemView.findViewById(R.id.isPublic_ImageView);
            channelCardView = itemView.findViewById(R.id.ChannelCardView);
            joinButton = itemView.findViewById(R.id.JoinChannel_Button);
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
