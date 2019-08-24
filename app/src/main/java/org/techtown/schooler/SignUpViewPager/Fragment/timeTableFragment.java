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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linda.R;
import com.example.linda.Retrofit.Data;
import com.example.linda.Retrofit.NetRetrofit;
import com.example.linda.Retrofit.Response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;

public class timeTableFragment extends Fragment {

    ImageButton imageButton; // 앞으로 가기 버튼
    ImageButton imageButton2; // 뒤로 가기 버튼

    TextView className; // 몇 학년, 몇 반인지 알려주는 텍스트
    TextView day; // className 텍스트 및 무슨 요일인지 알려주는 텍스트


    // 1~7 교시
    TextView one;
    TextView two;
    TextView three;
    TextView four;
    TextView five;
    TextView six;
    TextView seven;


    // 자바 내장 시간을 받아온다.
    SimpleDateFormat dateFormat; // SimpleDateFormat 클래스를 사용하여 dateFormat 이라는 변수를 선언하고있다.


    // String[] 형으로 배열을 선언한다. days 라는 변수에 5칸의 크기를 가진 days 라는 변수를 생성한다.
    String[] days = new String[5];


    // 요일에 맞게 과목 정보를 올바르게 입력할 때 사용된다.
    int pageDay;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // 앞에서 선언한 자바 내장 시간을 받아오는 변수인 dateFormat 라는 변수를 인터페이스화 시키고있습니다.
        // 피라미터로 "E" 라는 의미는 요일을 의미하며 Locale 지역은 KOREA 로 설정합니다.
        dateFormat = new SimpleDateFormat("E", Locale.KOREA);

        // fragment_time_table 프레그먼트 레이아웃을 timeTableFragment 자바소스코드에서 이용할 수 있도록 인플레이션 하고있다.
        View v = inflater.inflate(R.layout.fragment_time_table, container, false);

        // fragment_time_table 프레그먼트 레이아웃에서 선언한 뷰들을 자바 소스코드에서 사용할 수 있도록 id 값으로 초기화 하고있다.
        // v. 을 붙이는 이유는 인플레이션 과정에서 View 클래스의 v 라는 변수에 저장하였기 때문이다.
        imageButton = v.findViewById(R.id.imageButton);
        imageButton2 = v.findViewById(R.id.imageButton2);

        // className 과 day 또한 초기화를 하고있다.
        className = v.findViewById(R.id.className);
        day = v.findViewById(R.id.day);

        // 1~7 교시 과목의 텍스트 뷰를 담을 변수 또한 초기화 하고있다.
        one = v.findViewById(R.id.one);
        two = v.findViewById(R.id.two);
        three = v.findViewById(R.id.three);
        four = v.findViewById(R.id.four);
        five = v.findViewById(R.id.five);
        six = v.findViewById(R.id.six);
        seven = v.findViewById(R.id.seven);


        // 앞에서 선언한 String[] 배열의 값을 입력하고있다. 각각의 인덱스에 월 ~ 금 요일의 값을 String 형으로 저장하고있다.
        days[0] = "월";
        days[1] = "화";
        days[2] = "수";
        days[3] = "목";
        days[4] = "금";




        // 또한 앞에서 선언한 자바 내장 시계역할을 하는 변수 dateFormat 라는 변수의 속성을 사용하고있다.
        // 속성중 equals 속성은 equals 파라미터로 있는 값과 같은지 판단한다.
        // 따라서 각각의 요일의 값을 숫자로 표현하고있다. ex) [ 월 = 0, 화 = 1, 수 = 2, 목 = 3, 금 = 4 ]
        if (dateFormat.format(new Date()).equals("월")) { pageDay = 0; }
        else if (dateFormat.format(new Date()).equals("화")) { pageDay = 1; }
        else if (dateFormat.format(new Date()).equals("수")) { pageDay = 2; }
        else if (dateFormat.format(new Date()).equals("목")) { pageDay = 3; }
        else if (dateFormat.format(new Date()).equals("금")) { pageDay = 4; }



        setHasOptionsMenu(true);


        // onSearch() 매서드를 호출하고있으며 이제 본격적인 기능구현이 시작된다.
        onSearch();


        // 인플레이션에서 진행한 View 클래스의 변수 v 를 반환하고있다.
        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_time_table, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_time_table) {
            Toast.makeText(getActivity(), "Clicked on " + item.getTitle(), Toast.LENGTH_SHORT)
                    .show();
        }

        return true;
    }



    public void onSearch() {


        // SharedPreferences 클래스는 값의 저장을 담당하는 클래스이다. 앱 종료 후 에도 값을 복원할 수 있는 힘을 가진 역할을 한다.
        final SharedPreferences FirstData = this.getActivity().getSharedPreferences("shared", MODE_PRIVATE);

        // 각각의 String 변수에 전달한 이름으로 된 값을 다시 복원하고있다.
        String SchoolCode = FirstData.getString("SchoolCode", "");
        String OfficeCode = FirstData.getString("OfficeCode", "");
        String Grade = FirstData.getString("Grade", "");
        String Class = FirstData.getString("Class", "");
        String SchoolKind = FirstData.getString("SchoolKind", "");


        // 앞에서 복원한 값을 저장한 Grade 와 Class 변수를 fragment_time_table 프레그먼트 xml 파일에 텍스트 뷰로 설정합니다.
        // 이것의 역할은 몇학년 몇반의 시간표인지 확인할 수 있도록 도와주는 역할을 합니다.
        // 앞에서 선언한 className 텍스트에 학년, 반을 텍스트로 설정합니다.
        className.setText(Grade + "학년 " + Class + "반 시간표");


        // ---- onResponse 매서드를 위해 사용하는 값인 것 같습니다. ---- //
        Call<Response<Data>> res = NetRetrofit.getInstance().getService().getTimeTable(SchoolCode, OfficeCode, Grade, Class, SchoolKind);
        res.enqueue(new Callback<Response<Data>>() {


            @Override
            public void onResponse(Call<com.example.linda.Retrofit.Response<Data>> call, retrofit2.Response<Response<Data>> response) {
                Log.d("Retrofit", response.toString());


                // 서버에서 받아오는 body 의 데이터 값이 null 이 아닐 때 조건문을 실행합니다.
                if (response.body().getData() != null) {


                    // int 형의 자료형을 가진 dayString 이라는 변수에 앞서 만든 pageDay 변수의 값을 대입합니다.
                    int dayString = pageDay;


                    // pageDay 의 값에 따라 앞서 만든 day 변수의 텍스트 뷰의 값을 지정합니다.
                    if (pageDay == 0) {
                        day.setText("월요일");
                    } else if (pageDay == 1) {
                        day.setText("화요일");
                    } else if (pageDay == 2) {
                        day.setText("수요일");
                    } else if (pageDay == 3) {
                        day.setText("목요일");
                    } else if (pageDay == 4) {
                        day.setText("금요일");
                    }


                    // String 자료형을 가진 Mon 이라는 변수에 서버로 부터 전달받은 월요일의 과목을 모두 저장합니다.
                    // getMon() 을 사용하여 월요일에 존재하는 과목의 값을 받아옵니다.
                    String Mon = response.body().getData().getTimeTable().get(0).getMon();

                    // String 배열을 사용해 MonData 라는 배열을 만들었습니다.
                    // MonData[] 배열의 인덱스에 Mon 변수에 존재하는 많은 값들은 서버로 받은 !로 구분되는 것을 !로 나누어 값을 분활합니다.
                    String[] MonData = Mon.split("!");


                    // 만약 pageDay 가 0이라면 월요일을 의미합니다.
                    // 또한 1교시 ~ 7교시의 값에 MonData 배열 인덱스 안에 존재하는 값들을 저장합니다.
                    if (pageDay==0) {
                        try {
                            one.setText(MonData[1]);
                            two.setText(MonData[2]);
                            three.setText(MonData[3]);
                            four.setText(MonData[4]);
                            five.setText(MonData[5]);
                            six.setText(MonData[6]);
                            seven.setText(MonData[7]);
                        } catch (Exception e) {

                        }

                    }

                    // 화요일
                    else if (pageDay==1) {
                        String Tue = response.body().getData().getTimeTable().get(0).getTue();
                        String[] TueData = Tue.split("!");

                        try {
                            one.setText(TueData[1]);
                            two.setText(TueData[2]);
                            three.setText(TueData[3]);
                            four.setText(TueData[4]);
                            five.setText(TueData[5]);
                            six.setText(TueData[6]);
                            seven.setText(TueData[7]);
                        } catch (Exception e) {

                        }

                    }

                    // 수요일
                    else if (pageDay==2) {
                        String Wed = response.body().getData().getTimeTable().get(0).getWed();
                        String[] WedData = Wed.split("!");

                        try {
                            one.setText(WedData[1]);
                            two.setText(WedData[2]);
                            three.setText(WedData[3]);
                            four.setText(WedData[4]);
                            five.setText(WedData[5]);
                            six.setText(WedData[6]);
                            seven.setText(WedData[7]);
                        } catch (Exception e) {

                        }

                    }

                    // 목요일
                    else if (pageDay==3) {
                        String Thu = response.body().getData().getTimeTable().get(0).getThu();
                        String[] ThuData = Thu.split("!");

                        try {
                            one.setText(ThuData[1]);
                            two.setText(ThuData[2]);
                            three.setText(ThuData[3]);
                            four.setText(ThuData[4]);
                            five.setText(ThuData[5]);
                            six.setText(ThuData[6]);
                            seven.setText(ThuData[7]);
                        } catch (Exception e) {

                        }
                    }

                    // 금요일
                    else if (pageDay==4) {
                        String Fri = response.body().getData().getTimeTable().get(0).getFri();
                        String[] FriData = Fri.split("!");

                        try {
                            one.setText(FriData[1]);
                            two.setText(FriData[2]);
                            three.setText(FriData[3]);
                            four.setText(FriData[4]);
                            five.setText(FriData[5]);
                            six.setText(FriData[6]);
                            seven.setText(FriData[7]);
                        } catch (Exception e) {

                        }

                    }

                }


                // 앞으로가기 버튼을 클릭시 기능들을 구현하는 클릭 이벤트 처리하는 매서드입니다.
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // 만약 pageDay 의 값이 4 이하 일 때 = ( pageDay 의 값이 금요일 이하 일 때) 의미입니다.
                        if(pageDay<4){

                            // 기본으로 설정된 기본 요일을 증가시킵시다.
                            pageDay++;

                            // onSearch 매서드를 호출합니다. 그러나 파라미터로 pageDay 값을 전달하지 않아도 되는 이유는
                            // 클래스 상단에 pageDay 를 선언해주었고, onCreateView 에서 초기화를 해주었기 때문에 다른 매서드에서 또한 사용이 자유롭기 때문입니다.
                            onSearch();

                        }

                        // 만약 pageDay 의 값이 4 라면 == (pageDay 의 값이 금요일 일 때) 의미입니다.
                        else if(pageDay == 4) {

                            // pageDay 의 값을 0으로 초기화합니다. == (pageDay 의 값을 월요일로 초기화) 한다는 의미입니다.
                            pageDay = 0;

                            // onSearch 매서드를 또한 호출합니다.
                            onSearch();
                        }

                    }
                });


                // 뒤로가기 버튼을 클릭시 기능들을 구현하는 클릭 이벤트를 처리하는 매서드입니다.
                imageButton2.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view){

                        // 만약 pageDay 의 값이 0 이상일 때 = (pageDay 의 값이 월요일 이상일 때) 와 같습니다.
                        if(pageDay>0){

                            // pageDay 의 값을 감소시킵니다. = (pageDay 의 값이 화요일 이상일 때 뒤로 가기 기능을 사용할 수 있습니다.)
                            pageDay--;

                            onSearch();

                        }

                        // 만약 pageDay 의 값이 0일 때 (pageDay 의 값이 월요일 일 때) 와 같습니다.
                        else if(pageDay == 0){

                            // pageDay 를 4로 설정합니다. (pageDay 의 값을 금요일로 설정합니다.)
                            pageDay = 4;

                            onSearch();
                        }
                    }
                });



            }


            @Override
            public void onFailure(Call<com.example.linda.Retrofit.Response<Data>> call, Throwable t) {
                Log.e("Err", t.getMessage());
            }
        });
    }

}