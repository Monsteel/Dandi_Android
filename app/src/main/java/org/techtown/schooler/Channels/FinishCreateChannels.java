package org.techtown.schooler.Channels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import org.techtown.schooler.MainActivity;
import org.techtown.schooler.R;

public class FinishCreateChannels extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_create_channels);
    }

    @Override
    public void onBackPressed () {
        startActivity(new Intent(FinishCreateChannels.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void goToMain(View view){
        startActivity(new Intent(FinishCreateChannels.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void goToChangeImage(View view){
        Intent intent = new Intent(FinishCreateChannels.this, UploadChannelsThumbnail.class);

        intent.putExtra("channel_id",getIntent().getStringExtra("channel_id"));

        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
