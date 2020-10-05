package com.world_tech_point.lambebrowser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.world_tech_point.lambebrowser.R;
import com.world_tech_point.lambebrowser.serviceFragment.MembershipSave;

public class ProfileActivity extends AppCompatActivity {
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private TextView name, email, number,referCode, bankName, bankAcNo;
    MembershipSave membershipSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.profileTooBar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Profile");
        membershipSave = new MembershipSave(this);

        name = findViewById(R.id.profileUserName);
        email = findViewById(R.id.profileEmail);
        number = findViewById(R.id.profileNumber);
        referCode = findViewById(R.id.profileReferCode);
        bankName = findViewById(R.id.profileBankName);
        bankAcNo = findViewById(R.id.profileBankNo);


        name.setText(membershipSave.getUserName());
        email.setText(membershipSave.getEmail());
        number.setText(membershipSave.getNumber());
        referCode.setText(membershipSave.getReferCode());
        bankName.setText(membershipSave.getBankName());
        bankAcNo.setText(membershipSave.getBankAccountNo());


    }
}