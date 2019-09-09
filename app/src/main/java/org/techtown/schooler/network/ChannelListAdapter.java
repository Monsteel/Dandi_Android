package org.techtown.schooler.network;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.schooler.Model.ChannelInfo;
import org.techtown.schooler.R;

import java.util.List;

public class ChannelListAdapter extends RecyclerView.Adapter<ChannelListAdapter.ViewHolder>{

    private final List<ChannelInfo> mDataList;

    public ChannelListAdapter(List<ChannelInfo> dataList){
        mDataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_channel,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChannelInfo item = mDataList.get(position);
        holder.title.setText(item.get_Name());
        holder.content.setText(item.getExplain());
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.Title_TextView);
            content = itemView.findViewById(R.id.Content_TextView);
            BackGroundImage = itemView.findViewById(R.id.Background_ImageView);
            isPublicImage = itemView.findViewById(R.id.isPublic_ImageView);
        }
    }
}
