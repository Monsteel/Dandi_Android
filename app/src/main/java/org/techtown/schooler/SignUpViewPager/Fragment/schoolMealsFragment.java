package org.techtown.schooler.SignUpViewPager.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linda.R;
import com.example.linda.Retrofit.Data;
import com.example.linda.Retrofit.NetRetrofit;
import com.example.linda.Retrofit.Response;
import com.example.linda.Utillity.DateToDate;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;

public class schoolMealsFragment extends Fragment {

    TextView breakfast;
    TextView lunch;
    TextView dinner;
    TextView date;
    TextView TextSchoolName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_school_meals, container, false);
        breakfast = v.findViewById(R.id.text_b);
        lunch = v.findViewById(R.id.text_l);
        dinner = v.findViewById(R.id.text_d);
        date  = v.findViewById(R.id.Date);
        TextSchoolName = v.findViewById(R.id.Text_School_Name);
        setHasOptionsMenu(true);

        onSearch();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_school_meals, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_school_meals) {
            onSearch();
        }
        return true;
    }

    public void onSearch() {
        SharedPreferences FirstData = this.getActivity().getSharedPreferences("shared", MODE_PRIVATE);
        String SchoolCode = FirstData.getString("SchoolCode","");
        String OfficeCode = FirstData.getString("OfficeCode","");
        String SchoolName = FirstData.getString("SchoolName","");
        TextSchoolName.setText(SchoolName);

        Call<Response<Data>> res = NetRetrofit.getInstance().getService().getSchoolMeals(SchoolCode, OfficeCode);
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<com.example.linda.Retrofit.Response<Data>> call, retrofit2.Response<Response<Data>> response) {
                Log.d("Retrofit", response.toString());
                if (response.body().getData() != null) {
                    String breakfast_data = (response.body().getData().getMeal().get(0).getBreakfast());
                    String lunch_data = (response.body().getData().getMeal().get(0).getLaunch());
                    String dinner_data = (response.body().getData().getMeal().get(0).getDinner());
                    breakfast.setText(breakfast_data);
                    lunch.setText(lunch_data);
                    dinner.setText(dinner_data);
                    Toast.makeText(getActivity(), "급식정보를 성공적으로 조회했습니다.", Toast.LENGTH_SHORT)
                            .show();
                }else if (response.body().getStatus() == 200) {
                    String massage = (response.body().getMessage());
                    breakfast.setText(massage);
                    lunch.setText(massage);
                    dinner.setText(massage);
                    Toast.makeText(getActivity(), "등록된 급식이 없습니다.", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(getActivity(), "급식조회에 실패하였습니다.\nError Code : " + response.body().getStatus(), Toast.LENGTH_SHORT)
                            .show();
                }
            }
            @Override
            public void onFailure(Call<com.example.linda.Retrofit.Response<Data>> call, Throwable t) {
                Log.e("Err", t.getMessage());
            }
        });

        Call<Response<Data>> res2 = NetRetrofit.getInstance().getService().getDate();
        res2.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<com.example.linda.Retrofit.Response<Data>> call, retrofit2.Response<Response<Data>> response) {
                Log.d("Retrofit", response.toString());
                if (response.body().getData().getDate().size() != 0) {
                    DateToDate dateToDate = new DateToDate();
                    String date_data = (response.body().getData().getDate().get(0).getDate());
                    String Date = (dateToDate.getYear(date_data)+"년 "+dateToDate.getMonth(date_data)+"월 "+dateToDate.getDay(date_data)+"일 ");
                    date.setText(Date+"식단표");
                }else{
                    date.setText("YYYY년 MM월 DD일 "+"식단표");
                }
            }
            @Override
            public void onFailure(Call<com.example.linda.Retrofit.Response<Data>> call, Throwable t) {
                Log.e("Err", t.getMessage());
            }
        });
    }
}