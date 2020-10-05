package com.world_tech_point.lambebrowser.serviceFragment;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.world_tech_point.lambebrowser.HistoryActivity;
import com.world_tech_point.lambebrowser.ProfileActivity;
import com.world_tech_point.lambebrowser.R;
import com.world_tech_point.lambebrowser.SettingActivity;
import com.world_tech_point.lambebrowser.mp3Folder.MP3_PlayActivity;
import com.world_tech_point.lambebrowser.videoShowFolder.VideoShowActivity;
import com.world_tech_point.lambebrowser.wallet.WalletActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class MeFragment extends Fragment {


    TextView userSignInStatus,userName;
    LinearLayout signInTwo;
    LinearLayout history, exits, wallet, setting,file,videoList, mp3List, fb,share,help,profile;
    de.hdodenhof.circleimageview.CircleImageView userProfile;
    private static final int RC_SIGN_IN = 100;
    private static final int FILE_PICKER_CODE = 101;

    AlertDialog.Builder builder;
    AlertDialog dialog;
    String value =  null;
    RadioButton Membership;
    MembershipSave membershipSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_me, container, false);

        userSignInStatus = root.findViewById(R.id.showUserStatus_id);
        userName = root.findViewById(R.id.showUserName_id);
        userProfile = root.findViewById(R.id.userProfile_id);
        signInTwo = root.findViewById(R.id.signInTwo_id);
        history = root.findViewById(R.id.meHistory_id);
        profile = root.findViewById(R.id.meProfile_id);
        exits = root.findViewById(R.id.meExits_id);
        wallet = root.findViewById(R.id.meWallet_id);
        setting = root.findViewById(R.id.meSetting_id);
        file = root.findViewById(R.id.meFile_id);
        videoList = root.findViewById(R.id.meVideo_id);
        mp3List = root.findViewById(R.id.meMp3Song_id);

        membershipSave= new MembershipSave(getContext());

        fb = root.findViewById(R.id.meFacebook_id);
        help = root.findViewById(R.id.meHelp_id);
        share = root.findViewById(R.id.meShare_id);
        getUserProfile(membershipSave.getNumber(),membershipSave.getEmail());

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com")));
                } catch (ActivityNotFoundException e) {

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com")));

                }
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com")));
                } catch (ActivityNotFoundException e) {

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com")));

                }
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
                startActivity(new Intent(getContext(), HistoryActivity.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ProfileActivity.class));
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
                startActivity(new Intent(getContext(), SettingActivity.class));
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
                startActivity(new Intent(getContext(), VideoShowActivity.class));

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
                    Intent intent = new Intent(getContext(), SubmitMemberInfoActivity.class);
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
                        membershipSave.delete();
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

            MembershipSave membershipSave = new MembershipSave(getContext());
            if (membershipSave.getUserName().equals("")){
                choosePlaneAlert();
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
                membershipSave= new MembershipSave(getContext());

                String  phoneNumber = user.getPhoneNumber();
                String email = user.getEmail();

                if (phoneNumber != null || email != null) {
                    if (phoneNumber != null) {
                        phoneNumber = phoneNumber.replaceAll("[^a-zA-Z0-9]", "");
                    }
                    save(phoneNumber,email);
                }

            } else {

                Toast.makeText(getContext(), "Please try again", Toast.LENGTH_SHORT).show();

            }
        }else if (requestCode == FILE_PICKER_CODE){

            if (resultCode ==RESULT_OK){
                Toast.makeText(getContext(), ""+data.getData(), Toast.LENGTH_SHORT).show();
             /*  Intent intent = new Intent(getContext(), VideoPlayActivity.class);
                intent.putExtra("video_url",data.getData());
                startActivity(intent);*/
            }

        }
    }

    private void save(String phoneNumber, String email) {

        membershipSave.setEmailNumber(email,phoneNumber);
        if (membershipSave.getNumber() != null){

            Toast.makeText(getContext(), "ok", Toast.LENGTH_SHORT).show();

        }else {
            AuthUI.getInstance()
                    .signOut(getContext())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            netAlert();
                        }
                    });


        }
    }

    private void netAlert() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Alert!")
                .setMessage("Please check your net Connection")
                .setCancelable(false)
                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void getUserProfile(final String number , final String emailAddress) {

        String url = getString(R.string.BASS_URL) + "getUserProfile";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("success")) {
                        String res = obj.getString("user");
                        JSONArray jsonArray = new JSONArray(res);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject dataobj = jsonArray.getJSONObject(i);

                            String userId = dataobj.getString("id");
                            String userName = dataobj.getString("userName");
                            String emailAddress = dataobj.getString("emailAddress");
                            String number = dataobj.getString("number");
                            String user_type = dataobj.getString("user_type");
                            String bankName = dataobj.getString("bankName");
                            String bankAccountNo = dataobj.getString("bankAccountNo");
                            String facebookID = dataobj.getString("facebookID");
                            String instragramID = dataobj.getString("instragramID");
                            String referCode = dataobj.getString("referCode");

                            if (userName.equals("")){

                                Toast.makeText(getContext(), "Empty", Toast.LENGTH_SHORT).show();


                            }else {

                                membershipSave.saveUserInfo(Integer.parseInt(userId),userName,emailAddress,number,user_type,bankName,bankAccountNo,
                                        facebookID,instragramID,referCode);

                            }

                        }

                    } else {

                        Toast.makeText(getContext(), "Problem", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //netAlert();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //netAlert();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> Params = new HashMap<>();
                Params.put("number", number);
                Params.put("emailAddress", emailAddress);
                return Params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);



    }


}