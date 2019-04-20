package com.UV.rokonalzorobyan.UV;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.UV.rokonalzorobyan.UV.Api.UserClient;
import com.UV.rokonalzorobyan.UV.Api.apiRequist;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUs extends Fragment {
    EditText name,phone,email,massage;
    Button btnSend;
    ProgressDialog progressdialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_contact_us, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        name=getActivity().findViewById(R.id.name);

        getActivity().findViewById(R.id.scr).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event){
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                return false;
            }
        });
        phone=getActivity().findViewById(R.id.phon);
        email=getActivity().findViewById(R.id.email);
        massage=getActivity().findViewById(R.id.massage);
        btnSend=getActivity().findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();
                if (phone.getText().length()>2&&phone.getText().toString().substring(0,2).trim().equals("05")){
                    sendMassage();
                }else {
                    progressdialog.cancel();
                    Toast.makeText(getActivity(), "برجاء إكمال الحقول واستخدام رقم هاتف يبدأ ب 05", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void sendMassage(){
            UserClient userClient = apiRequist.sendRequist();
            Call<ResponseBody> call = userClient.contactUs(MainActivity.tokken,name.getText().toString(),email.getText().toString(),phone.getText().toString(),massage.getText().toString());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()){
                        progressdialog.cancel();
                        Toast.makeText(getActivity(), "تم إرسال رسالتكم... شكراً لتواصلكم معنا", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_lay,
                                new HomePage()).commit();
                    }else {
                        progressdialog.cancel();
                        Toast.makeText(getActivity(), "يرجي كتابة البيانات صحيحة", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressdialog.cancel();
                }
            });
        }

    public void showdialog(){
        progressdialog = new ProgressDialog(getActivity());
        progressdialog.setMessage("الرجاء الإنتظار ....");
        progressdialog.setCancelable(false);
        progressdialog.show();
    }
    };

