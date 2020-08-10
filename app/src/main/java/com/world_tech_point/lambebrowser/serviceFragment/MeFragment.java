package com.world_tech_point.lambebrowser.serviceFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.world_tech_point.lambebrowser.MainActivity;
import com.world_tech_point.lambebrowser.R;

import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MeFragment extends Fragment {


    TextView userSignInStatus,userName;
    LinearLayout signInTwo;
    de.hdodenhof.circleimageview.CircleImageView userProfile;
    private static final int RC_SIGN_IN = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_me, container, false);

        userSignInStatus = root.findViewById(R.id.showUserStatus_id);
        userName = root.findViewById(R.id.showUserName_id);
        userProfile = root.findViewById(R.id.userProfile_id);
        signInTwo = root.findViewById(R.id.signInTwo_id);




        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Coming soon...", Toast.LENGTH_SHORT).show();
            }
        });


        signInTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user!=null){

                    Logout();

                }else {

                    login();
                }


            }
        });


        return root;
    }

    private void Logout() {

        AuthUI.getInstance()
                .signOut(getContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "Logout success", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void login() {

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);


    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            if (user.getPhoneNumber()==""){
                userName.setText(user.getDisplayName());
                userSignInStatus.setText("Signed in with g_mail");
                Picasso.get().load(user.getPhotoUrl()).into(userProfile);

            }else {
                userSignInStatus.setText("Signed in with Number");
            }


        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                userSignInStatus.setText(user.getDisplayName()+"\nSigned in");
                Picasso.get().load(user.getPhotoUrl()).into(userProfile);

            } else {

                Toast.makeText(getContext(), "Please try again", Toast.LENGTH_SHORT).show();

            }
        }
    }
}