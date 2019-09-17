package org.techtown.schooler.NavigationFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_main, container, false);

        materialCalendarView = rootView.findViewById(R.id.materialCalendarView); // 캘린더
        recyclerView = rootView.findViewById(R.id.recyclerView); // 리사이클러뷰

        //onChannelEventSearch();

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

            }
        });

//        scheduleInfoArrayList.add(new ScheduleInfo("오늘은 기말고사"));
//        scheduleInfoArrayList.add(new ScheduleInfo("내일은 ICT 융합"));

        // ScheduleAdapter 클래스를 참조해서 myAdapter 인스턴스를 생성하였으며 scheduleInfoArrayList 배열을 전달하고있습니다.
//        ScheduleAdapter myAdapter = new ScheduleAdapter(scheduleInfoArrayList);
//        recyclerView.setAdapter(myAdapter);

        return rootView;
    }


    // 서버 통신
    // 채널 이벤트에서 날짜를 받아오는 형식은 20190912********* 뒤에 많다.
    public void onChannelEventSearch() {

        Call<Response<Data>> res = NetRetrofit.getInstance().getChannelEvent().SearchChannelEvent("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoianV5ZW9wIiwiaWF0IjoxNTY4NzA3NTUxLCJleHAiOjE1Njg3NTA3NTEsImlzcyI6ImRhbmRpIiwic3ViIjoidG9rZW4ifQ._qhERNku8aAqyZuJGgjIZQNCI_wBXc5YM8SLFYv9XdU","7","안드로이드");
        res.enqueue(new Callback<Response<Data>>() {

            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {

                // String 형 channelEventDateStart 라는 변수에 각 채널의 날짜를 저장합니다.
                String channelEventDateStart = response.body().getData().getEvents().get(0).getStart_date();

                // channelEventDateStart 에서 저장한 값을 년도, 월, 일 각각에 맞게 따로따로 저장합니다.
                String channelEventYear = channelEventDateStart.substring(0, 4);
                String channelEventMonth = channelEventDateStart.substring(5,7);
                String channelEventDay = channelEventDateStart.substring(8,10);


                // 캘린더에서 클릭 한 값과 실제로 데이터에 존재하는 값이 동일한지 확인하는 코드입니다.
                if(Integer.parseInt(selectedYear) == Integer.parseInt(channelEventYear) && selectedMonth == Integer.parseInt(channelEventMonth) && Integer.parseInt(selectedDay) == Integer.parseInt(channelEventDay)){


                } else{

                }

                ScheduleAdapter myAdapter = new ScheduleAdapter(channelEventsArrayList);
                recyclerView.setAdapter(myAdapter);

            }

            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {

            }
        });
    }

    // 서버 통신
    // 학교 이벤트에서 날짜가 받아오는 형식은 20190912
    public void onSchoolEventSearch() {

        Call<Response<Data>> res = NetRetrofit.getInstance().getSchoolEvent().getSchoolEvent("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoianV5ZW9wIiwiaWF0IjoxNTY4NjMzMzg5LCJleHAiOjE1Njg2NzY1ODksImlzcyI6ImRhbmRpIiwic3ViIjoidG9rZW4ifQ.OFn-kOimP1S6foKN9TyRgyQWtVYzeg55_nWw0rRrvtI","2019","9");
        res.enqueue(new Callback<Response<Data>>() {

            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {

                // String 형 eventDate 라는 변수에 각 채널의 날짜를 저장합니다.
                String schoolEventDateStart = response.body().getData().getEvents().get(0).getStart_date();

                // eventDateStart 에서 저장한 값을 년도, 월, 일 각각에 맞게 따로따로 저장합니다.
                String schoolEventYear = schoolEventDateStart.substring(0, 4);
                String schoolEventMonth = schoolEventDateStart.substring(4,6);
                String schoolEventDay = schoolEventDateStart.substring(6,8);


                // 캘린더에서 클릭 한 값과 실제로 데이터에 존재하는 값이 동일한지 확인하는 코드입니다.
                if(Integer.parseInt(selectedYear) == Integer.parseInt(schoolEventYear) && selectedMonth == Integer.parseInt(schoolEventMonth) && Integer.parseInt(selectedDay) == Integer.parseInt(schoolEventDay)){


                } else{


                }

            }

            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {

            }
        });
    }

}
