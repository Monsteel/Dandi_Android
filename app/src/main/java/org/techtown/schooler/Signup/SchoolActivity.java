package org.techtown.schooler.Signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.techtown.schooler.network.SchoolListAdapter;
import org.techtown.schooler.Model.SchoolList;
import org.techtown.schooler.R;
import org.techtown.schooler.network.Data;
import org.techtown.schooler.network.NetRetrofit;
import org.techtown.schooler.network.response.Response;

import retrofit2.Call;
import retrofit2.Callback;

public class SchoolActivity extends AppCompatActivity {

    private SchoolListAdapter adapter;
    TextView Search;
    EditText SearchSchoolName;
    private ListView listView;
    TextView DecideSchoolName;
    ImageView GotoClass;
    String School;
    String SchoolId;
    String OfficeId;
    String SchoolKind;
    FrameLayout list_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new SchoolListAdapter();
        setContentView(R.layout.activity_school);
        Search = (TextView)findViewById(R.id.Search);
        listView = (ListView) findViewById(R.id.School_ListView);
        SearchSchoolName = (EditText)findViewById(R.id.InputSchoolName);
        listView.setAdapter(adapter);
        DecideSchoolName = (TextView)findViewById(R.id.decideSchoolName);
        DecideSchoolName.setVisibility(View.INVISIBLE);
        GotoClass = (ImageView)findViewById(R.id.next_class);
        list_layout = (FrameLayout)findViewById(R.id.list_layout);
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        GotoClass.setEnabled(false);
        GotoClass.setImageResource(R.drawable.ic_chevron_right_black_24dp);

        list_layout.setVisibility(View.INVISIBLE);




        SearchSchoolName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list_layout.setVisibility(View.INVISIBLE);
            }
        });

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearch();
                list_layout.setVisibility(View.VISIBLE);
                imm.hideSoftInputFromWindow(list_layout.getWindowToken(), 0);
            }
        });
    }

    private void onSearch() {
        String SearchSchool = (SearchSchoolName.getText().toString());
        final Call<Response<Data>> res = NetRetrofit.getInstance().getSignup().SearchSchoolGet(SearchSchool);
        res.enqueue(new Callback<Response<Data>>() {
            @Override
            public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {
                Log.d("Retrofit", response.toString());
                if (response.body() != null) {
                    String[] contents = new String[response.body().getData().getSchoolInfo().size()];
                    String[] titles = new String[response.body().getData().getSchoolInfo().size()];

                    String SchoolName = "";
                    String SchoolLocal = "";

                    if (response.body().getData() != null) {
                        adapter.clear();
                        for (int A = 0; A < response.body().getData().getSchoolInfo().size(); A++) {
                            SchoolList dto = new SchoolList();
                            SchoolName = response.body().getData().getSchoolInfo().get(A).getSchool_name();
                            SchoolLocal = response.body().getData().getSchoolInfo().get(A).getSchool_locate();

                            contents[A] = SchoolLocal;
                            titles[A] = SchoolName;

                            dto.setTitle(titles[A]);
                            dto.setContent(contents[A]);
                            adapter.addItem(dto);
                        }
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<Response<Data>> call, Throwable t) {
                Log.e("Err", "네트워크 연결오류");
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick (AdapterView< ? > parent, View view, final int position, long id) {
                String SearchSchool = (SearchSchoolName.getText().toString());
                final Call<Response<Data>> res = NetRetrofit.getInstance().getSignup().SearchSchoolGet(SearchSchool);
                res.enqueue(new Callback<Response<Data>>() {
                    @Override
                    public void onResponse(Call<Response<Data>> call, retrofit2.Response<Response<Data>> response) {
                        Log.d("Retrofit", response.toString());
                        School = response.body().getData().getSchoolInfo().get(position).getSchool_name();
                        SchoolId = response.body().getData().getSchoolInfo().get(position).getSchool_code();
                        OfficeId = response.body().getData().getSchoolInfo().get(position).getOffice_code();
                        SchoolKind = response.body().getData().getSchoolInfo().get(position).getSchool_type();
                        DecideSchoolName.setText(School);
                        DecideSchoolName.setVisibility(View.VISIBLE);

                        GotoClass.setEnabled(true);
                        GotoClass.setImageResource(R.drawable.ic_chevron_right_yellow_24dp);
                    }

                    @Override
                    public void onFailure(Call<Response<Data>> call, Throwable t) {
                        Log.e("Err", "네트워크 연결오류");
                    }
                });

            }

        });

    }

    public void toGoBack (View view) {
        onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void toGoNext(View view){
        Intent intent = new Intent(getApplicationContext(), ClassActivity.class);
        intent.putExtra("Name",getIntent().getStringExtra("Name"));
        intent.putExtra("Id",getIntent().getStringExtra("Id"));
        intent.putExtra("Password",getIntent().getStringExtra("Password"));
        intent.putExtra("Email",getIntent().getStringExtra("Email"));
        intent.putExtra("PhoneNumber",getIntent().getStringExtra("PhoneNumber"));
        intent.putExtra("School", School);
        intent.putExtra("SchoolId", SchoolId);
        intent.putExtra("OfficeId", OfficeId);
        intent.putExtra("SchoolKind", SchoolKind);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onBackPressed () {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
