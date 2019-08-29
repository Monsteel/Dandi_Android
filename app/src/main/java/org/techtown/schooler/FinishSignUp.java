package org.techtown.schooler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.techtown.schooler.SigninUser.SignupActivity;
import org.techtown.schooler.StartMemberActivity.LoginActivity;
import org.techtown.schooler.network.LoginPostRequest;

public class FinishSignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_sign_up);
        Button GotoMain = findViewById(R.id.goToMain);


        GotoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Intent 클래스를 참조해서 화면을 전환하고있습니다.
                Intent gotomain = new Intent(FinishSignUp.this, MainActivity.class);
                startActivity(gotomain);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent gotomain = new Intent(FinishSignUp.this, MainActivity.class);
        startActivity(gotomain);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
