package org.techtown.schooler.NavigationFragment;

import android.content.SharedPreferences;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.techtown.schooler.Model.ChannelEvents;
import org.techtown.schooler.RecyclerView_main.ScheduleAdapter;
import org.techtown.schooler.R;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;

public class MainFragment extends Fragment  {

    // 캘린더
    MaterialCalendarView materialCalendarView;

    // 캘린더에서 선택하는 Year, Month, Day 입니다.
    String selectedYear;
    String selectedDay;
    int selectedMonth;

    // selectedMonth 의 오류를 수정하기 위해 String 형 month 를 생성하였습니다.
    String month;

    RecyclerView recyclerView; // 리사이클러뷰

    // RecyclerView 속성인 LayoutManager 클래스를 사용해서 layoutManager 인스턴스를 생성하였습니다.
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

    // ArrayList 배열인 scheduleInfoArrayList 배열
    ArrayList<ChannelEvents> channelEventsArrayList = new ArrayList<>();

    SharedPreferences Login = getActivity().getSharedPreferences("Login", MODE_PRIVATE); //SharedPreferences 선언

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_main, container, false);

        materialCalendarView = rootView.findViewById(R.id.materialCalendarView); // 캘린더
        recyclerView = rootView.findViewById(R.id.recyclerView); // 리사이클러뷰

        // 캘린더 클릭 시 발생하는 이벤트를 수행합니다.
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);


                // 각각의 변수에 캘린더의 값을 저장하고있습니다.
                selectedYear = String.valueOf(date.getYear());
                month = String.valueOf(date.getMonth());
                selectedDay = String.valueOf(date.getDay());

                // selectedMonth 변수에 month 변수에 담겨있는 문자열의 값을 정수로 변환하고있습니다.
                selectedMonth = Integer.parseInt(month);

                // selectedMonth 값을 1 증가시킵니다.
                // 이유는 라이브러리 상 문제로 월의 단위가 1 씩 앞으로 당겨졌기 때문입니다.
                selectedMonth += 1;

                onChannelEvent();

            }
        });

        return rootView;
    }

    public void onChannelEvent(){

        Call<Response<Data>> res = NetRetrofit.getInstance().getChannelEvent().GetChannelEvent(Login.getString("token",""),"3");
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {

                Toast.makeText(getActivity(), response.body().getData().getEvents().get(0).getTitle(), Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {

                Toast.makeText(getActivity(), "hello", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
