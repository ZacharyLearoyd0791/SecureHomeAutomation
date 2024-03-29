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

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

import java.net.URL;
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
    String phoneNumber;
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
    Button changePassword,editAccount;
   public static ProfileEditFragment profileEditFragment = new ProfileEditFragment();
    final Handler handler = new Handler();
    String userNameDb;
    SharedPreferences profileDataSP;
    SharedPreferences.Editor profilDataEditor;
    String editedName;
    String editedEmail;
    String editedPhone;
    Uri imageUriNew;
    String editedImage;
    String personNameG;
    String personFamilyNameG;
    String personEmailG;
    Uri personPhotoG;


    UserInfo userInfo = new UserInfo();
    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account_new, container, false);

        //recyclerViewOthers = (RecyclerView) view.findViewById(R.id.account_recyclerview_other);
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
        changePassword = view.findViewById(R.id.account_change_password);
        editAccount = view.findViewById(R.id.account_edit_profile_details);
        profileDataSP =  getActivity().getSharedPreferences(getString(R.string.user_new_data), Context.MODE_PRIVATE);

        editedEmail = profileDataSP.getString("NewUserEmail","No Email found");
        editedName =  profileDataSP.getString("NewUserName","No Name found");
        editedPhone =  profileDataSP.getString("NewUsePhone","No Phone found");
        editedImage = profileDataSP.getString("ImageURL",null);


        //Change Password
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                Intent intent = new Intent(getActivity(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        //Opening profile Edit
        editAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.postDelayed(() -> getParentFragmentManager().beginTransaction().replace(R.id.flFragment, AccountFragment.profileEditFragment).commit(), 300);
            }
        });
        //Setting up google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mClient = GoogleSignIn.getClient(getContext(),gso);
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        //Getting user data from database if not logged in with google
        //Getting Email address
        emailKey = getString(R.string.slash_email);
        nameKey = getString(R.string.name_info);
        phoneKey = "Phone";
        dbID();
        accountKey=userKey+key;


        if(account!= null){
             personNameG = account.getDisplayName();
             personFamilyNameG = account.getFamilyName();
             personEmailG = account.getEmail();
             personPhotoG = account.getPhotoUrl();
        }else{
            personNameG = null;
            personEmailG = null;
            personPhotoG = null;
            personFamilyNameG =null;
        }




        recyclerView.setLayoutManager(linearLayoutManager);
        accountFragmentDataList = new ArrayList<>();
        //dbID();

        //Setting person name
        if(personNameG!=null){
            accountFragmentDataList.add(new AccountFragmentData(R.drawable.person_icon_account, "Name", personNameG));
            userNameTV.setText(personNameG);
            editAccount.setClickable(false);
        }else if(editedName!=null) {
            accountFragmentDataList.add(new AccountFragmentData(R.drawable.person_icon_account, "Name", editedName));
            userNameTV.setText(editedName);
            editAccount.setClickable(true);
        }

        //Setting person email
        if(personEmailG!=null){
            accountFragmentDataList.add(new AccountFragmentData(R.drawable.email_icon_account, "Email", personEmailG));
            editAccount.setClickable(false);
        }else if(editedEmail!=null) {
            accountFragmentDataList.add(new AccountFragmentData(R.drawable.email_icon_account, "Email", editedEmail));
            editAccount.setClickable(true);

        }
        //Setting person phone numbe
        if(editedPhone!=null) {
            accountFragmentDataList.add(new AccountFragmentData(R.drawable.phone_icon_account_4, "Phone", editedPhone));
        }

        //Setting person profile photo
        if(personPhotoG!=null){
            //imageUriNew = Uri.parse(personPhotoG);
            profileImage.setImageURI(personPhotoG);
            editAccount.setClickable(false);
            editProfileImage.setClickable(false);
        }else if(editedImage !=null){
            imageUriNew = Uri.parse(editedImage);
            profileImage.setImageURI(imageUriNew);
            editAccount.setClickable(true);
            editProfileImage.setClickable(true);
        }
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
    public void Database(){


        finalEmailKey = accountKey +"/userInfo/"+ emailKey;
        finalNameKey = accountKey +"/userInfo/"+ nameKey;
        finalPhoneKey = accountKey+"/userInfo/"+phoneKey;
        Log.d(TAG,finalEmailKey+"\n"+finalNameKey+"\n"+finalPhoneKey);
        Toast.makeText(getContext(), "Login Type"+loggedWithGoogle, Toast.LENGTH_SHORT).show();


        if(!loggedWithGoogle) {
            editable = true;
            databaseReference = FirebaseDatabase.getInstance().getReference().child(finalEmailKey);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        userEmail = Objects.requireNonNull(snapshot.getValue().toString());
                        Toast.makeText(getContext(), userEmail, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Cannot find email!", Toast.LENGTH_SHORT).show();
                        userEmail = "No Email Found";
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            //Getting user name
            databaseReference = FirebaseDatabase.getInstance().getReference().child(finalNameKey);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        userName = Objects.requireNonNull(snapshot.getValue().toString());

                    } else {
                        Toast.makeText(getContext(), "Cannot found Name!", Toast.LENGTH_SHORT).show();
                        userName = "No Name found";

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            databaseReference = FirebaseDatabase.getInstance().getReference().child(finalPhoneKey);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        userPhone = Objects.requireNonNull(snapshot.getValue().toString());
                    }else{
                        userPhone=phoneNumber;
                        databaseReference.setValue(userPhone);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (loggedWithGoogle) {
            editable = false;


        }
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
                    //profileImage.setImageURI(userInfo.photOut);
                    profileImage.setImageURI(selectedImageUri);
                    profilDataEditor.putString("ImageURL",selectedImageUri.toString());
                    profilDataEditor.apply();
                    profilDataEditor.commit();

                }
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbID();

    }
    @Override
    public void onResume() {
        super.onResume();
        dbID();
        userNameTV.setText(userName);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        //Setting person name
        if(personNameG!=null){
            accountFragmentDataList.add(new AccountFragmentData(R.drawable.person_icon_account, "Name", personNameG));
            userNameTV.setText(personNameG);
            editAccount.setClickable(false);
        }else if(editedName!=null) {
            accountFragmentDataList.add(new AccountFragmentData(R.drawable.person_icon_account, "Name", editedName));
            userNameTV.setText(editedName);
            editAccount.setClickable(true);
        }

        //Setting person email
        if(personEmailG!=null){
            accountFragmentDataList.add(new AccountFragmentData(R.drawable.email_icon_account, "Email", personEmailG));
            editAccount.setClickable(false);
        }else if(editedEmail!=null) {
            accountFragmentDataList.add(new AccountFragmentData(R.drawable.email_icon_account, "Email", editedEmail));
            editAccount.setClickable(true);
        }
        //Setting person phone numbe
        if(editedPhone!=null) {
            accountFragmentDataList.add(new AccountFragmentData(R.drawable.phone_icon_account_4, "Phone", editedPhone));
        }

        //Setting person profile photo
        if(personPhotoG!=null){
            //imageUriNew = Uri.parse(personEmailG);
            profileImage.setImageURI(personPhotoG);
            editAccount.setClickable(false);
            editProfileImage.setClickable(false);
        }else if(editedImage !=null){
            imageUriNew = Uri.parse(editedImage);
            profileImage.setImageURI(imageUriNew);
            editAccount.setClickable(true);
            editProfileImage.setClickable(true);
        }
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
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

        if(localKey!=null) {
            key = localKey;
        }

        if(personalKey !=null) {
            key = personalKey;
        }


    }


    @Override
    public void onItemClick(int position) {
        if(position ==0){
            Toast.makeText(getContext(), "You can edit profile Details by clicking on edit profile button!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(), "You can edit profile Details by clicking on edit profile button!", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendEditDataToDB(int pos, String userEdit){
        if(pos == 0){
            databaseReference = FirebaseDatabase.getInstance().getReference().child("userInfo").child(finalNameKey);
            databaseReference.setValue(userEdit);
        }else if(pos == 1){
            databaseReference = FirebaseDatabase.getInstance().getReference().child(finalEmailKey);
            databaseReference.setValue(userEdit);
        }else if(pos == 2){
            dbref = FirebaseDatabase.getInstance().getReference().child("userInfo").child((finalPhoneKey));
            dbref.setValue(userEdit);
        }
    }
}