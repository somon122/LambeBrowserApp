package com.world_tech_point.lambebrowser.serviceFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.world_tech_point.lambebrowser.R;
import com.world_tech_point.lambebrowser.VideoPlayActivity;
import com.world_tech_point.lambebrowser.mp3Folder.MP3_PlayActivity;
import com.world_tech_point.lambebrowser.videoShowFolder.FileShowActivity;
import com.world_tech_point.lambebrowser.wallet.WalletActivity;

import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MeFragment extends Fragment {


    TextView userSignInStatus,userName;
    LinearLayout signInTwo;
    LinearLayout history, exits, wallet, setting,file,videoList, mp3List, fb,share,help;
    de.hdodenhof.circleimageview.CircleImageView userProfile;
    private static final int RC_SIGN_IN = 100;
    private static final int FILE_PICKER_CODE = 101;

    AlertDialog.Builder builder;
    AlertDialog dialog;
    String value =  null;
    RadioButton Membership;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_me, container, false);

        userSignInStatus = root.findViewById(R.id.showUserStatus_id);
        userName = root.findViewById(R.id.showUserName_id);
        userProfile = root.findViewById(R.id.userProfile_id);
        signInTwo = root.findViewById(R.id.signInTwo_id);
        history = root.findViewById(R.id.meHistory_id);
        exits = root.findViewById(R.id.meExits_id);
        wallet = root.findViewById(R.id.meWallet_id);
        setting = root.findViewById(R.id.meSetting_id);
        file = root.findViewById(R.id.meFile_id);
        videoList = root.findViewById(R.id.meVideo_id);
        mp3List = root.findViewById(R.id.meMp3Song_id);

        fb = root.findViewById(R.id.meFacebook_id);
        help = root.findViewById(R.id.meHelp_id);
        share = root.findViewById(R.id.meShare_id);

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "FaceBook", Toast.LENGTH_SHORT).show();
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "help", Toast.LENGTH_SHORT).show();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = "Site link: https://play.google.com/store/apps/details?id="+getActivity().getPackageName();
                String shareSub = "App Link";
                intent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                intent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(intent,"Lambe Browser"));

            }
        });


        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "history", Toast.LENGTH_SHORT).show();
            }
        });

        exits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitsUser();
            }
        });
        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               startActivity(new Intent(getContext(), WalletActivity.class));
            }
        });
        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getContext(), FileShowActivity.class));
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent,FILE_PICKER_CODE);

            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "setting", Toast.LENGTH_SHORT).show();
            }
        });
        mp3List.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MP3_PlayActivity.class));
            }
        });
        videoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), FileShowActivity.class));

            }
        });

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

                   logoutUser();

                }else {

                    login();
                }


            }
        });

        return root;
    }

    private void choosePlaneAlert() {

        builder = new AlertDialog.Builder(getContext());
        final View view1 = getLayoutInflater().inflate(R.layout.select_membership,null);
        builder.setView(view1);
        final RadioGroup radioGroup = view1.findViewById(R.id.selectRadioGroup);
        final RadioButton freeMembership = view1.findViewById(R.id.selectFreeMembership);
        final RadioButton proMembership = view1.findViewById(R.id.selectProMembership);
        Button button = view1.findViewById(R.id.selectMembershipNext);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Membership = view1.findViewById(checkedId);
                value =Membership.getText().toString();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (value !=null){
                    Intent intent = new Intent(getContext(),ChooseMemberActivity.class);
                    intent.putExtra("userStatus",value);
                    startActivity(intent);
                }else {
                    Toast.makeText(getContext(), "Please Choose Option", Toast.LENGTH_SHORT).show();
                }
            }
        });


        dialog = builder.create();
        dialog.show();
    }




    private void exitsUser() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Exits Alert");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().finishAffinity();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void logoutUser() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Logout alert");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Logout();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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

            choosePlaneAlert();


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
        }else if (requestCode == FILE_PICKER_CODE){

            if (resultCode ==RESULT_OK){
                Toast.makeText(getContext(), ""+data.getData(), Toast.LENGTH_SHORT).show();
               Intent intent = new Intent(getContext(), VideoPlayActivity.class);
                intent.putExtra("video_url",data.getData());
                startActivity(intent);
            }

        }
    }

}