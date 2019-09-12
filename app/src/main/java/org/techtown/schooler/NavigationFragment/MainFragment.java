package org.techtown.schooler.NavigationFragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.techtown.schooler.Model.SchoolList;
import org.techtown.schooler.R;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;



import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;

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

    // 캘린더
    MaterialCalendarView materialCalendarView;

    String year;
    String month;
    String day;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_main, container, false);

        textView = rootView.findViewById(R.id.textView); // 날짜
        textView2 = rootView.findViewById(R.id.textView2); // 시간
        materialCalendarView = rootView.findViewById(R.id.materialCalendarView); // 캘린더

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


        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                year = String.valueOf(date.getYear());
                month = String.valueOf(date.getMonth());
                day = String.valueOf(date.getDay());

                onChannelEventSearch();

            }
        });

        return rootView;
    }


    // 실시간 시계를 담당하는 매서드
    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return time.format(mDate);
    }

    // 서버 통신
    public void onChannelEventSearch() {

        Call<Response<Data>> res = NetRetrofit.getInstance().getChannelEvent().SearchChannelEvent("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoianV5ZW9wIiwiaWF0IjoxNTY4MjY0NTg0LCJleHAiOjE1NjgzMDc3ODQsImlzcyI6ImRhbmRpIiwic3ViIjoidG9rZW4ifQ.m76OfUadbL0KMYGFYQZo0Sh1UOrtezzoN5ZOnfx3hgM","1","정모");
        res.enqueue(new Callback<Response<Data>>() {

            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {

                String EventDay = response.body().getData().getEvents().get(0).getStart_date();

            }

            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {

            }
        });
    }

    // 서버 통신
    public void onSchoolEventSearch(CalendarDay date) {

        Call<Response<Data>> res = NetRetrofit.getInstance().getSchoolEvent().getSchoolEvent("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoianV5ZW9wIiwiaWF0IjoxNTY4MjY0NTg0LCJleHAiOjE1NjgzMDc3ODQsImlzcyI6ImRhbmRpIiwic3ViIjoidG9rZW4ifQ.m76OfUadbL0KMYGFYQZo0Sh1UOrtezzoN5ZOnfx3hgM","2019","9");
        res.enqueue(new Callback<Response<Data>>() {

            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {


            }

            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {

            }
        });
    }

}
