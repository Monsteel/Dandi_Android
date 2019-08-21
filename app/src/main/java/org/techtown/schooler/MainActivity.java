package org.techtown.schooler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    int value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 스레드 객체를 참고해서 thread 인스턴스를 생성하였습니다.
                BackgroundThread thread = new BackgroundThread();

                // thread 인스턴스의 속성인 start() 매서드를 사용하여 시작합니다.
                thread.start();
            }
        });
    }
}


