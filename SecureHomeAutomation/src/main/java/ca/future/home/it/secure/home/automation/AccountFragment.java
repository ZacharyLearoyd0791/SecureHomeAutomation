/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/
package ca.future.home.it.secure.home.automation;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AccountFragment extends Fragment implements AccountRecyclerViewInterface {
    View view;


    //RecyclerView
    RecyclerView recyclerView;
    String userChangedName;
    RecyclerView recyclerViewOthers;
    private List<AccountFragmentData> accountFragmentDataList;
    String userName;
    String userEmail;
    TextView userNameTV;
    String userPhone;
    private AccountFragmentRecyclerViewAdapter adapter;
    Button signoutButton;
    SharedPreferences loginTypeSP;
    SharedPreferences.Editor loginTypeSPEditor;
    String key,localKey,personalKey,profileKey,sensorKey,phoneKey,userKey,userData, accountKey, emailKey,
            finalEmailKey, nameKey, finalNameKey, finalPhoneKey;
    SharedPreferences sharedPreferences;
    SharedPreferences userSharedPref;
    SharedPreferences.Editor userSharedPrefEditor;
    SharedPreferences.Editor editor;
    GoogleSignInClient mClient;
    DatabaseReference databaseReference;
    boolean loggedWithGoogle;
    boolean editable;
    ImageView editProfileImage;
    ImageView profileImage;
    DatabaseReference dbref;
    String userNameDb;



    UserInfo userInfo = new UserInfo();
    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account_new, container, false);

        recyclerViewOthers = (RecyclerView) view.findViewById(R.id.account_recyclerview_other);
        recyclerView = (RecyclerView) view.findViewById(R.id.account_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        sharedPreferences = getActivity().getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE);
        userSharedPref = getActivity().getSharedPreferences("Users Data", MODE_PRIVATE);
        userSharedPrefEditor = userSharedPref.edit();
        loginTypeSP = getActivity().getSharedPreferences("Login User Type",MODE_PRIVATE);
        loginTypeSPEditor = loginTypeSP.edit();
        editor = sharedPreferences.edit();
        userNameTV = view.findViewById(R.id.account_person_name);
        loggedWithGoogle = loginTypeSP.getBoolean("Google SignIn",false);
        editProfileImage = view.findViewById(R.id.account_profile_photo_edit_icon);
        profileImage = view.findViewById(R.id.account_profile_image);
        //Setting up google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mClient = GoogleSignIn.getClient(getContext(),gso);
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getContext());


        //Getting user data from database if not logged in with google
        //Getting Email address
        emailKey = getString(R.string.slash_email);
        nameKey = getString(R.string.name_info);
        phoneKey = "Phone";
        dbID();



        finalEmailKey = accountKey + emailKey;
        finalNameKey = accountKey + nameKey;
        finalPhoneKey = accountKey+ phoneKey;
        Toast.makeText(getContext(), "Login Type"+loggedWithGoogle, Toast.LENGTH_SHORT).show();



        //RecyclerView
        recyclerView.setLayoutManager(linearLayoutManager);

        accountFragmentDataList = new ArrayList<>();
        accountFragmentDataList.add(new AccountFragmentData(R.drawable.person_icon_account,"Name",userName));
        accountFragmentDataList.add(new AccountFragmentData(R.drawable.email_icon_account,"Email",userEmail));
        accountFragmentDataList.add(new AccountFragmentData(R.drawable.phone_icon_account_4,"Phone",userPhone));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        adapter = new AccountFragmentRecyclerViewAdapter(accountFragmentDataList,this);
        recyclerView.setAdapter(adapter);


        //SignOut User
        signoutButton =  view.findViewById(R.id.account_sign_out_button);
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!loggedWithGoogle) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                }else {
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
                    GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
                    googleSignInClient.signOut();
                }
                Toast.makeText(getContext(), "Logged Out!", Toast.LENGTH_SHORT).show();
                editor.putBoolean(getString(R.string.logged), false).apply();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
               }
        });


        //Selecting image form gallery
        editProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), 200);
            }
        });


        return view;
    }
    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == 200) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    profileImage.setImageURI(userInfo.photOut);
                }
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
    @Override
    public void onResume() {
        super.onResume();
        userNameTV.setText(userName);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        accountFragmentDataList = new ArrayList<>();
        accountFragmentDataList.add(new AccountFragmentData(R.drawable.person_icon_account,"Name",userName));
        accountFragmentDataList.add(new AccountFragmentData(R.drawable.email_icon_account,"Email",userEmail));
        accountFragmentDataList.add(new AccountFragmentData(R.drawable.phone_icon_account_4,"Phone",userPhone));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        adapter = new AccountFragmentRecyclerViewAdapter(accountFragmentDataList,this);
        recyclerView.setAdapter(adapter);

    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    private void dbID(){
        userInfo.typeAccount();
        userKey = getApplicationContext().getString(R.string.userKey);
        userData = getString(R.string.user_info);

        localKey = userInfo.userId;
        personalKey = userInfo.idInfo;

        if(localKey!=null){
            key = localKey;
            userInfo.LocalUsers();

            userName= userInfo.returnLocalName();
            Log.d(TAG,"User Name is"+userName);
            Log.d(TAG,"User Nam is  key "+(userKey + key + "/userInfo/Name:"));
            if(userName==null) {
                databaseReference = FirebaseDatabase.getInstance().getReference().child(userKey + key + "/userInfo/Name:");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            userNameDb = Objects.requireNonNull(snapshot.getValue().toString());

                        }
                        else{
                            userName=getString(R.string.nameNotFound);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            userName=userNameDb;
            userEmail=userInfo.returnLocalEmail();
            Log.d(TAG,userKey+key+"/userInfo/Phone");
            databaseReference=FirebaseDatabase.getInstance().getReference().child(userKey+key+"/userInfo/Phone");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        userPhone=Objects.requireNonNull(snapshot.getValue().toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            if(userPhone==null){
                userPhone=getString(R.string.noPhone);
            }
        }
        if(personalKey !=null){
            key = personalKey;
            userInfo.googleLoginUsers();
            userName= userInfo.returnName();
            userEmail=userInfo.returnEmail();
            userPhone=getString(R.string.noPhone);



        }
    }


    @Override
    public void onItemClick(int position) {
        if(editable) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            final EditText alertEditText = new EditText(getApplicationContext());
            if (position == 0) {
                alertEditText.setText(userName);
                alert.setMessage(R.string.EditName);
                alert.setTitle(R.string.ChangeName);
                alert.setPositiveButton(R.string.change, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        userName = alertEditText.getText().toString();
                        userNameTV.setText(userName);
                        sendEditDataToDB(position, userName);
                    }
                });
                alert.setView(alertEditText);
                alert.setNegativeButton(getString(R.string.no), (DialogInterface.OnClickListener) (dialog, which) -> {
                    // If user click no then dialog box is canceled.
                    dialog.cancel();
                });
            }
            else if (position == 2) {

                alertEditText.setText(userPhone);
                alert.setMessage("Edit your phone number");
                alert.setTitle("Change Name");
                alert.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        userPhone = alertEditText.getText().toString();
                        sendEditDataToDB(position, userPhone);
                    }
                });
                alert.setView(alertEditText);
                alert.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    // If user click no then dialog box is canceled.
                    dialog.cancel();
                });
            }else{
                Toast.makeText(getApplicationContext(), R.string.cannotchangeemail, Toast.LENGTH_LONG).show();
            }
            //sendEditDataToDB(userName,userEmail,userPhone);

            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        }else{
            Toast.makeText(getContext(), R.string.googleProfileChange, Toast.LENGTH_LONG).show();

        }
    }
    public void sendEditDataToDB(int pos, String userEdit){
        if(pos == 0){
            //databaseReference = FirebaseDatabase.getInstance().getReference().child(finalNameKey);
            databaseReference.setValue(userEdit);
        }else if(pos == 1){
            databaseReference = FirebaseDatabase.getInstance().getReference().child(finalEmailKey);
            databaseReference.setValue(userEdit);
        }else if(pos == 2){
            dbref = FirebaseDatabase.getInstance().getReference().child((finalPhoneKey));
            dbref.setValue(userEdit);
        }




    }
}