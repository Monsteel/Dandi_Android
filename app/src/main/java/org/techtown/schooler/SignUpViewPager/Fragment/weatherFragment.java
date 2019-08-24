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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linda.R;
import com.example.linda.Retrofit.Data;
import com.example.linda.Retrofit.NetRetrofit;
import com.example.linda.Retrofit.Response;

import org.techtown.schooler.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;

public class weatherFragment extends Fragment {

    TextView tem;
    TextView sky;
    TextView reh;
    TextView pop;
    TextView date;
    TextView pm10;
    TextView pm25;
    TextView Locate;
    TextView Ozone;
    ImageView imageView;

    SimpleDateFormat dateFormat;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일\nHH시 mm분 ss초", Locale.KOREA);

        View v = inflater.inflate(R.layout.fragment_weather, container, false);
        tem = v.findViewById(R.id.Text_tem);
        sky = v.findViewById(R.id.Text_sky);
        reh = v.findViewById(R.id.Text_reh);
        pop = v.findViewById(R.id.Text_pop);
        date = v.findViewById(R.id.Text_date);
        pm10 = v.findViewById(R.id.pm10);
        pm25 = v.findViewById(R.id.pm25);
        Locate = v.findViewById(R.id.SchoolLocation);
        Ozone = v.findViewById(R.id.Text_Ozone);
        imageView = v.findViewById(R.id.image_weather);

        setHasOptionsMenu(true);

        onSearch();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_weather, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_weather) {
            onSearch();
        }
        return true;
    }

    public void onSearch() {

        SharedPreferences FirstData = this.getActivity().getSharedPreferences("shared", MODE_PRIVATE);

        String SchoolLocal = FirstData.getString("SchoolLocal","");

        String[] Local = SchoolLocal.split("\\s");

        String SchoolLocalData = Local[0] +" "+ Local[1];
        Locate.setText(SchoolLocalData);

    Call<Response<Data>> res = NetRetrofit.getInstance().getService().getWeather(SchoolLocalData);
    res.enqueue(new Callback<Response<Data>>() {
        @Override
        public void onResponse(Call<com.example.linda.Retrofit.Response<Data>> call, retrofit2.Response<Response<Data>> response) {
            Log.d("Retrofit", response.toString());
            if (response.body().getData() != null && response.body().getStatus() == 200) {
                // 밑에부터 이제 데이터 받는 코드인데
                // getCalendar().get(0) <- 0은 배열에서 0번째 값들을 말하고
                // 그 0번째 값들중에서 getId나 getDate 등등 하면 된다
                // 밑에는 값이 만약 Integer 일때 String으로 변환해 주는 코드이니깐
                // Date나 다른 String 값을 받을 때면 굳이 이렇게 안해줘도 돼
                // 그럼 열심히 해^^
                String tem_data = Integer.toString(response.body().getData().getWeather().get(0).getTemData());
                tem.setText(tem_data+"°C");

                String reh_data = Integer.toString(response.body().getData().getWeather().get(0).getRehData());
                reh.setText(reh_data+"%");

                String pop_data = Integer.toString(response.body().getData().getWeather().get(0).getPopData());
                pop.setText(pop_data+"%");

                String sky_data = (response.body().getData().getWeather().get(0).getSkyData());
                sky.setText(sky_data);

                Integer dust_pm10_data = (response.body().getData().getWeather().get(0).getPm10Data());
                pm10.setText("미세먼지 : " + dust_pm10_data);

                Integer dust_pm25_data = (response.body().getData().getWeather().get(0).getPm10Data());
                pm25.setText("초미세먼지 : " + dust_pm25_data);

                float Ozone_Data = (response.body().getData().getWeather().get(0).getOzonData());
                Ozone.setText(""+Ozone_Data);

                Integer Image_data= (response.body().getData().getWeather().get(0).getPicturNumData());

                if(Image_data == 1){
                    imageView.setImageResource(R.drawable.weather_1);
                }else if(Image_data == 2){
                    imageView.setImageResource(R.drawable.weather_2);
                }else if(Image_data == 3){
                    imageView.setImageResource(R.drawable.weather_3);
                }else if(Image_data == 4){
                    imageView.setImageResource(R.drawable.weather_4);
                }else if(Image_data == 5){
                    imageView.setImageResource(R.drawable.weather_5);
                }else if(Image_data == 6){
                    imageView.setImageResource(R.drawable.weather_6);
                }else if(Image_data == 7){
                    imageView.setImageResource(R.drawable.weather_7);
                }else if(Image_data == 8){
                    imageView.setImageResource(R.drawable.weather_8);
                }else if(Image_data == 9){
                    imageView.setImageResource(R.drawable.weather_9);
                }else if(Image_data == 10){
                    imageView.setImageResource(R.drawable.weather_10);
                } else if(Image_data == 11){
                    imageView.setImageResource(R.drawable.weather_11);
                }else if(Image_data == 12){
                    imageView.setImageResource(R.drawable.weather_12);
                }else if(Image_data == 13){
                    imageView.setImageResource(R.drawable.weather_13);
                }else if(Image_data == 14){
                    imageView.setImageResource(R.drawable.weather_14);
                }else if(Image_data == 15){
                    imageView.setImageResource(R.drawable.weather_15);
                }else if(Image_data == 16){
                    imageView.setImageResource(R.drawable.weather_16);
                }else if(Image_data == 17){
                    imageView.setImageResource(R.drawable.weather_17);
                }else if(Image_data == 18){
                    imageView.setImageResource(R.drawable.weather_18);
                }else if(Image_data == 19){
                    imageView.setImageResource(R.drawable.weather_19);
                }else if(Image_data == 20){
                    imageView.setImageResource(R.drawable.weather_20);
                }else if(Image_data == 21){
                    imageView.setImageResource(R.drawable.weather_21);
                }else if(Image_data == 22){
                    imageView.setImageResource(R.drawable.weather_22);
                }else if(Image_data == 23){
                    imageView.setImageResource(R.drawable.weather_23);
                }else if(Image_data == 24){
                    imageView.setImageResource(R.drawable.weather_24);
                }else if(Image_data == 25){
                    imageView.setImageResource(R.drawable.weather_25);
                }else if(Image_data == 26){
                    imageView.setImageResource(R.drawable.weather_26);
                }else if(Image_data == 27){
                    imageView.setImageResource(R.drawable.weather_27);
                }else if(Image_data == 28){
                    imageView.setImageResource(R.drawable.weather_28);
                }else if(Image_data == 29){
                    imageView.setImageResource(R.drawable.weather_29);
                }else if(Image_data == 30){
                    imageView.setImageResource(R.drawable.weather_30);
                }else{
                    sky.setText("[Error] code"+sky_data);//에러표시
                }


                date.setText((dateFormat.format(new Date()))+"\n날씨 정보 조회");

                Toast.makeText(getActivity(), "날씨정보를 성공적으로 조회했습니다.", Toast.LENGTH_SHORT)
                        .show();

            }else if(response.body().getStatus() == 200){
                tem.setText("날씨정보가 없습니다");
                sky.setText("날씨정보가 없습니다");
                reh.setText("날씨정보가 없습니다");
                pop.setText("날씨정보가 없습니다");


                Toast.makeText(getActivity(), "날씨정보가 없습니다.", Toast.LENGTH_SHORT)
                        .show();

            }else{
                Toast.makeText(getActivity(), "날씨조회에 실패하였습니다.\nError Code : " + response.body().getStatus(), Toast.LENGTH_SHORT)
                        .show();
            }

        }

        @Override
        public void onFailure(Call<com.example.linda.Retrofit.Response<Data>> call, Throwable t) {
            Log.e("Err", t.getMessage());
        }
    });
}
}