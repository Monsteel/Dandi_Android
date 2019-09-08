package org.techtown.schooler.NavigationFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import org.techtown.schooler.MainActivity;
import org.techtown.schooler.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainFragment extends Fragment  {

    Button button;
    TextView textView;
    TextView textView2;

    long mNow;  // 현재 mNow
    Date mDate;  // 현재 mDate

    // Date 클래스 today 인스턴스
    Date today = new Date();

    // SimpleDateFormat 클래스 date 날짜
    SimpleDateFormat date = new SimpleDateFormat("yyyy년 MM월 dd일");

    // SimpleDateFormat 클래스 time 시간
    SimpleDateFormat time = new SimpleDateFormat("hh시 mm분 ss초");

    // CountDownTimer 클래스 _timer 변수
    private CountDownTimer _timer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_main, container, false);

        textView = rootView.findViewById(R.id.textView);
        textView2 = rootView.findViewById(R.id.textView2);

        // 실시간 날짜를 설정하는 코드
        textView.setText(date.format(today));

        // 실시간 시계 설정하는 코드
        _timer = new CountDownTimer(10 * 1000, 1000) {   //_timer 객체에 10*1000 밀리초(10초) 가 1000밀리초마다 1씩달게한다.

            public void onTick(long millisUntilFinished) {

                // 시간 텍스트를 설정한다.
                textView2.setText(getTime());


            }

            public void onFinish() {

                _timer.cancel();
                _timer.start(); // 끝났을때 재설정을 통한 무한 반복문 실행

            }

        };
        _timer.start();

        return rootView;
    }



    // 실시간 시계를 담당하는 매서드
    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return time.format(mDate);
    }



}
