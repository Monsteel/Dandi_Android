package org.techtown.schooler.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.techtown.schooler.R;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class PwFragment extends Fragment {

    EditText pw1; // 비밀번호 입력
    EditText pw2; // 비밀번호 확인

    ImageView okay_Image; // 성공 이미지
    ImageView no_Image; // 실패 이미지

    TextView textView; // 성공, 실패 안내 메세지

    Button check; // 비밀번호 확인 버튼

    InputMethodManager imm; // 가상 키패드 내리기

    LinearLayout layout; // 레이아웃

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_password, container, false);

        pw1 = rootView.findViewById(R.id.Input_Pw_1); // 비밀번호 입력
        pw2 = rootView.findViewById(R.id.Input_Pw_2); // 비밀번호 확인

        okay_Image = rootView.findViewById(R.id.okay); // 성공 이미지
        no_Image = rootView.findViewById(R.id.no); // 실패 이미지

        textView = rootView.findViewById(R.id.textView3); // 성공, 실패 안내 메세지

        check = rootView.findViewById(R.id.check); // 비밀번호 확인 버튼

        layout = rootView.findViewById(R.id.layout);


        imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);


        // 레이아웃 클릭 시 키패드 취소
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imm.hideSoftInputFromWindow(pw1.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(pw2.getWindowToken(), 0);
            }
        });


        // 비밀번호 확인 버튼 클릭 시 발생하는 클릭 이벤트
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // 버튼 클릭 시 키패드를 내린다.
                imm.hideSoftInputFromWindow(pw1.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(pw2.getWindowToken(), 0);


                // pw1 비밀번호 입력과 pw2 비밀번호 확인 둘 중 한개라도 8개 이하일 때 조건을 실행합니다.
                if(pw1.getText().toString().length() < 8 || pw2.getText().toString().length() < 8){


                    // 실패 이미지를 출력해준다.
                    okay_Image.setVisibility(View.INVISIBLE);
                    no_Image.setVisibility(View.VISIBLE);

                    textView.setText("비밀번호를 8자리 이상으로 구성해주세요.");
                    textView.setTextColor(Color.parseColor("#bc0000"));
                }

                // 만약 비밀번호 입력 pw1 과 비밀번호 확인 pw2 가 같을 시 성공 이미지를 출력해준다.
                else if(pw1.getText().toString().equals(pw2.getText().toString()) ){

                        // 성공 이미지를 출력해준다.
                        okay_Image.setVisibility(View.VISIBLE);
                        no_Image.setVisibility(View.INVISIBLE);

                        // 성공 메세지 또한 출력한다.
                        textView.setText("비밀번호가 같습니다.");
                        textView.setTextColor(Color.parseColor("#0ec600"));

                }

                // 만약 틀릴 경우
                else
                {
                        // 실패 이미지를 출력해준다.
                        okay_Image.setVisibility(View.INVISIBLE);
                        no_Image.setVisibility(View.VISIBLE);

                        // 실패 메시지를 또한 출력한다.
                        textView.setText("비밀번호가 다릅니다.");
                        textView.setTextColor(Color.parseColor("#bc0000"));

                        // 비밀번호 입력 pw1, 비밀번호 확인 pw2 입력 대화창을 모두 초기화한다.
                        pw1.setText("");
                        pw2.setText("");

                        // 비밀번호 입력 pw1 에 커서를 조정합니다.
                        pw1.requestFocus();
                }

            }
        });



        return rootView;
    }





}
