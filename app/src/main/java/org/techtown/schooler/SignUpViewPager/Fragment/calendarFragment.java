package org.techtown.schooler.SignUpViewPager.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linda.Activity.EditSchoolActivity;
import com.example.linda.Activity.ScheduleActivity;
import com.example.linda.R;
import com.example.linda.Retrofit.Data;
import com.example.linda.Retrofit.NetRetrofit;
import com.example.linda.Retrofit.Response;
import com.example.linda.Utillity.DateToDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;

public class calendarFragment extends Fragment {

    String Date;//캘린더에서 선택 한 날짜
    SimpleDateFormat dateFormat;
    TextView Today_School;
    TextView Today_Class;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        Date = "";

        Today_Class = v.findViewById(R.id.Today_ClassSchedule);
        Today_School = v.findViewById(R.id.Today_SchoolSchedule);


        CalendarView calendarView;

        calendarView = (CalendarView) v.findViewById(R.id.calendar);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date;
                String month_;
                String DayOfMonth_;

                if ((month + 1) < 10) {
                    month_ = "" + 0 + (month + 1);
                } else {
                    month_ = "" + (month + 1);
                }

                if ((dayOfMonth) < 10) {
                    DayOfMonth_ = "" + 0 + (dayOfMonth);
                } else {
                    DayOfMonth_ = "" + (dayOfMonth);
                }
                date = (String) (year + "-" + month_ + "-" + DayOfMonth_);

                Date = date;
                Log.d("Schedule", "onSelectedDayChange:date" + date);

                onSearch();
            }
        });
        setHasOptionsMenu(true);
        toDay();
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_calendar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_calendar) {
            Toast.makeText(getActivity(), "학교정보 변경탭으로 넘어갑니다.", Toast.LENGTH_SHORT)
                    .show();

            Intent intent = new Intent(getActivity(), EditSchoolActivity.class);
            startActivity(intent);
        }
        return true;
    }

    public void toDay() {
        SharedPreferences FirstData = this.getActivity().getSharedPreferences("shared", MODE_PRIVATE);
        String SchoolName = FirstData.getString("SchoolName","");
        String SchoolCode = FirstData.getString("SchoolCode","");
        String OfficeCode = FirstData.getString("OfficeCode","");
        String Grade = FirstData.getString("Grade","");
        String Class = FirstData.getString("Class","");

        DateToDate dateToDate = new DateToDate();
        final String Today = dateFormat.format(new Date());
        String year = dateToDate.getYear(Today);
        String month = dateToDate.getMonth(Today);

        System.out.println("");

        Call<Response<Data>> res = NetRetrofit.getInstance().getService().getCalendar(SchoolName, Grade, Class, SchoolCode, OfficeCode, year, month);
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<com.example.linda.Retrofit.Response<Data>> call, retrofit2.Response<Response<Data>> response) {

                Log.d("Retrofit", response.toString());
                List<String> output_Class = new ArrayList<>();
                List<String> output_School = new ArrayList<>();

                Intent intent = new Intent(getActivity(), ScheduleActivity.class);

                //학급일정
                int Status = response.body().getStatus();
                if (Status == 200) {
                    int B = 0;
                    for (int A = 0; A < response.body().getData().getCalendarSchedule().size(); A++) {
                        Log.d("Schedule", "onSelectedDayChange:date" + Today);
                        String AA = response.body().getData().getCalendarSchedule().get(A).getDate();//서버에서 넘어오는 일정의 날짜값

                        if (AA.equals(Today)) {
                            B++;
                            output_Class.add(B + " . " + response.body().getData().getCalendarSchedule().get(A).getContent() + " / 일정No." + response.body().getData().getCalendarSchedule().get(A).getId() + "\n");
                            String[] AAA = output_Class.toArray(new String[output_Class.size()]);

                            String str = Arrays.toString(AAA);
                            String str2 = str.substring(1, Arrays.toString(AAA).length() - 2);
                            String ClassR = str2.replace(", ", "");
                            Today_Class.setText(ClassR);
                        } else if(output_Class.size() == 0){
                            intent.putExtra("Text_Class", "학급일정이 없습니다");
                        }

                    }
                } else {
                    Toast.makeText(getActivity(), "일정조회에 실패했습니다.\nError Code : " + response.body().getStatus(), Toast.LENGTH_SHORT)
                            .show();
                }

                //학사일정
                if (response.body().getData().getmSchoolSchedule().size() != 0 && Status == 200) {
                    int B = 0;
                    for (int A = 0; A < response.body().getData().getmSchoolSchedule().size(); A++) {
                        Log.d("Schedule", "onSelectedDayChange:date" + Today);
                        String AA = response.body().getData().getmSchoolSchedule().get(A).getDate();
                        if (AA.equals(Today)) {
                            B++;
                            output_School.add(B + " . " + response.body().getData().getmSchoolSchedule().get(A).getContent() + "\n");
                            String[] AAA = output_School.toArray(new String[output_School.size()]);
                            String str = Arrays.toString(AAA).substring(1, Arrays.toString(AAA).length() - 2);
                            String School = str.replaceAll(", ", "");
                            Today_School.setText(School);
                        } else if(output_School.size() == 0){
                            intent.putExtra("Text_School", "학사일정이 없습니다");
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "일정조회에 실패했습니다.\nError Code : " + response.body().getStatus(), Toast.LENGTH_SHORT)
                            .show();
                }
                intent.putExtra("Date", Date);
            }

            @Override
            public void onFailure(Call<com.example.linda.Retrofit.Response<Data>> call, Throwable t) {
                Log.e("Err", t.getMessage());
            }
        });
    }

    public void onSearch() {
        SharedPreferences FirstData = this.getActivity().getSharedPreferences("shared", MODE_PRIVATE);
        String SchoolName = FirstData.getString("SchoolName","");
        String SchoolCode = FirstData.getString("SchoolCode","");
        String OfficeCode = FirstData.getString("OfficeCode","");
        String Grade = FirstData.getString("Grade","");
        String Class = FirstData.getString("Class","");


        DateToDate dateToDate = new DateToDate();
        String year = dateToDate.getYear(Date);
        String month =dateToDate.getMonth(Date);
        System.out.println("");

        Call<Response<Data>> res = NetRetrofit.getInstance().getService().getCalendar(SchoolName, Grade, Class, SchoolCode, OfficeCode, year, month);
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<com.example.linda.Retrofit.Response<Data>> call, retrofit2.Response<Response<Data>> response) {

                Log.d("Retrofit", response.toString());
                List<String> output_Class = new ArrayList<>();
                List<String> output_School = new ArrayList<>();

                Intent intent = new Intent(getActivity(), ScheduleActivity.class);

                //학급일정
                int Status = response.body().getStatus();
                if (Status == 200) {
                    int B = 0;
                    for (int A = 0; A < response.body().getData().getCalendarSchedule().size(); A++) {
                        Log.d("Schedule", "onSelectedDayChange:date" + Date);
                        String AA = response.body().getData().getCalendarSchedule().get(A).getDate();//서버에서 넘어오는 일정의 날짜값

                        if (AA.equals(Date)) {
                            B++;
                            output_Class.add(B + " . " + response.body().getData().getCalendarSchedule().get(A).getContent() + " / 일정No." + response.body().getData().getCalendarSchedule().get(A).getId() + "\n");
                            String[] AAA = output_Class.toArray(new String[output_Class.size()]);

                            String str = Arrays.toString(AAA);
                            String str2 = str.substring(1, Arrays.toString(AAA).length() - 2);
                            String ClassR = str2.replace(", ", "");
                            intent.putExtra("Text_Class", ClassR);
                        } else if(output_Class.size() == 0){
                            intent.putExtra("Text_Class", "학급일정이 없습니다");
                        }

                    }
                } else {
                    Toast.makeText(getActivity(), "일정조회에 실패했습니다.\nError Code : " + response.body().getStatus(), Toast.LENGTH_SHORT)
                            .show();
                }

                //학사일정
                if (response.body().getData().getmSchoolSchedule().size() != 0 && Status == 200) {
                    int B = 0;
                    for (int A = 0; A < response.body().getData().getmSchoolSchedule().size(); A++) {
                        Log.d("Schedule", "onSelectedDayChange:date" + Date);
                        String AA = response.body().getData().getmSchoolSchedule().get(A).getDate();
                        if (AA.equals(Date)) {
                            B++;
                            output_School.add(B + " . " + response.body().getData().getmSchoolSchedule().get(A).getContent() + "\n");
                            String[] AAA = output_School.toArray(new String[output_School.size()]);
                            String str = Arrays.toString(AAA).substring(1, Arrays.toString(AAA).length() - 2);
                            String School = str.replaceAll("[, ]", "");
                            intent.putExtra("Text_School", School);
                        } else if(output_School.size() == 0){
                            intent.putExtra("Text_School", "학사일정이 없습니다");
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "일정조회에 실패했습니다.\nError Code : " + response.body().getStatus(), Toast.LENGTH_SHORT)
                            .show();
                }
                intent.putExtra("Date", Date);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<com.example.linda.Retrofit.Response<Data>> call, Throwable t) {
                Log.e("Err", t.getMessage());
            }
        });
    }
}