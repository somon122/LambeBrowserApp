package com.world_tech_point.lambebrowser.serviceFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.world_tech_point.lambebrowser.R;

public class ChooseMemberActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


    String value;
    TextInputEditText userName,email, number, bankName, bankNumber, referCode,fbId, instragramId;
    Button submitBtn;
    TextInputLayout bankLayoutName,bankLayoutNumber,fbLayoutId, instragramLayoutId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_member);

        Toolbar toolbar = findViewById(R.id.submitToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        userName = findViewById(R.id.submitUserName);
        email = findViewById(R.id.submitEmail);
        number = findViewById(R.id.submitNumber);
        bankName = findViewById(R.id.submitBankName);
        bankNumber = findViewById(R.id.submitBankNumber);
        referCode = findViewById(R.id.submitReferCode);
        fbId = findViewById(R.id.submitFbId);
        instragramId = findViewById(R.id.submitInstagramId);
        submitBtn = findViewById(R.id.submitButton);

        bankLayoutName = findViewById(R.id.submitBNameTextInputLayout);
        bankLayoutNumber = findViewById(R.id.submitAccountNumberTextInputLayout);
        fbLayoutId = findViewById(R.id.submitFbTextInputLayout);
        instragramLayoutId = findViewById(R.id.submitInstagramTextInputLayout);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            value = bundle.getString("userStatus");
            setTitle(value);
            if (value.equals("Free membership")){
                bankLayoutName.setVisibility(View.GONE);
                bankLayoutNumber.setVisibility(View.GONE);
                fbLayoutId.setVisibility(View.GONE);
                instragramLayoutId.setVisibility(View.GONE);
            }
        }
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (value.equals("Free membership")){
                   freeSubmit();
                }else {
                    proSubmit();
                }
            }
        });
    }



    private void proSubmit() {
        String name = userName.getText().toString();
        String emailAdd = email.getText().toString();
        String phone = number.getText().toString();
        String refer = referCode.getText().toString();
        String fb = fbId.getText().toString();
        String instraGram = instragramId.getText().toString();
        String bName = bankName.getText().toString();
        String bNumber = bankNumber.getText().toString();

        if (name.isEmpty()){
            userName.setError("Please Enter Name");
        }else if (emailAdd.isEmpty()){
            email.setError("Please Enter Email Address");
        }else if (phone.isEmpty()){
            number.setError("Please Enter Number");
        }else if (refer.isEmpty()){
            referCode.setError("Please Enter referCode");
        }else if (fb.isEmpty()){
            fbId.setError("Please Enter Facebook id");
        }else if (instraGram.isEmpty()){
            instragramId.setError("Please Enter instragram Id");
        }else if (bName.isEmpty()){
            bankName.setError("Please Enter bank Name");
        }else if (bNumber.isEmpty()){
            bankNumber.setError("Please Enter bank Number");
        }else {
            Toast.makeText(this, "Pro Ok", Toast.LENGTH_SHORT).show();
        }
    }

    private void freeSubmit() {

        String name = userName.getText().toString();
        String emailAdd = email.getText().toString();
        String phone = number.getText().toString();
        String refer = referCode.getText().toString();

        if (name.isEmpty()){
            userName.setError("Please Enter Name");
        }else if (emailAdd.isEmpty()){
            email.setError("Please Enter Email Address");
        }else if (phone.isEmpty()){
            number.setError("Please Enter Number");
        }else if (refer.isEmpty()){
            referCode.setError("Please Enter referCode");
        }else {
            Toast.makeText(this, "free Ok", Toast.LENGTH_SHORT).show();

        }


    }
}