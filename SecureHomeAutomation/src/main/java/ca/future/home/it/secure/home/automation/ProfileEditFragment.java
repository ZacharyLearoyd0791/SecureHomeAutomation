/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class
ProfileEditFragment extends Fragment {

    //Declarations
    private CircleImageView profileImage;
    //private FloatingActionButton changeProfileImageButton;
    private Button saveChanges;
    private Button changePassword;
    private EditText eName;
    final Handler handler = new Handler();
    static AccountFragment accountFragment = new AccountFragment();

    String key,localKey,personalKey,windowsKey,sensorKey,userKey,userData;
    private EditText eEmail;
    private EditText ePhoneNumber;
    Uri imageUri;
    View view;
    DatabaseReference reference;
    UserInfo userInfo=new UserInfo();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String DBName,DBEmail,DBPhone,DBImage;



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public ProfileEditFragment() {
        // Required empty public constructor
    }

    public static ProfileEditFragment newInstance(String param1, String param2) {
        ProfileEditFragment fragment = new ProfileEditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile_edit, container, false);
        profileImage = view.findViewById(R.id.profile_image);
        //changeProfileImageButton = view.findViewById(R.id.editProfileImageIcon);
        changePassword = view.findViewById(R.id.editProfileChangePasswordButton);
        saveChanges = view.findViewById(R.id.editProfileSaveChanges);
        eName = view.findViewById(R.id.editProfilePersonName);
        eEmail = view.findViewById(R.id.editProfilePersonEmail);
        ePhoneNumber = view.findViewById(R.id.editProfilePersonPhone);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.user_new_data), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String neweditedEmail = sharedPreferences.getString("NewUserEmail","No Email found");
        String neweditedName =  sharedPreferences.getString("NewUserName","No Name found");
        String neweditedPhone =  sharedPreferences.getString("NewUsePhone","No Phone found");
        String neweditedImage = sharedPreferences.getString("ImageURL",null);

        if(neweditedName!=null) {
            eName.setText(neweditedName);
        }
        if(neweditedEmail!=null) {
            eEmail.setText(neweditedEmail);
        }
        if(neweditedPhone!=null) {
            ePhoneNumber.setText(neweditedPhone);
        }
        if(neweditedImage!=null) {
            Uri uri = Uri.parse(neweditedImage);
            profileImage.setImageURI(uri);
        }

        //userinfo();
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),ForgotPasswordActivity.class));
            }
        });
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean(getString(R.string.profiile_edited), true);
                editor.putString(getString(R.string.user_new_name), eName.getText().toString());
                editor.putString(getString(R.string.user_new_email), eEmail.getText().toString());
                editor.putString(getString(R.string.user_new_phone), ePhoneNumber.getText().toString());
                editor.apply();
                editor.commit();
                handler.postDelayed(() -> getParentFragmentManager().beginTransaction().replace(R.id.flFragment, ProfileEditFragment.accountFragment).commit(), 300);
              /*  if (eName != null) {
                    reference.child(dbID()).child(getString(R.string.user_name)).setValue(eName);
                }
                if (eEmail != null){
                    reference.child(dbID()).child(getString(R.string.user_email)).setValue(eEmail);
                }
                if(ePhoneNumber!=null){
                    reference.child(dbID()).child(getString(R.string.user_phone)).setValue(ePhoneNumber);

                }
                reference.child(dbID()).child(getString(R.string.editted)).setValue(true);
                Toast.makeText(getContext(), R.string.changesSaved, Toast.LENGTH_SHORT).show();*/
            }
        });





        return view;
    }
    private String dbID(){
        userInfo.typeAccount();
        userKey=getApplicationContext().getString(R.string.userKey);
        userData=getApplicationContext().getString(R.string.userData);


        localKey=userInfo.userId;
        personalKey=userInfo.idInfo;

        if(localKey!=null){
            key=localKey;
        }
        if(personalKey!=null) {
            key= personalKey;
        }
        windowsKey=key+userData+getString(R.string.user_details_path);
        sensorKey=windowsKey;

        return userKey+windowsKey  ;
    }
    private void userinfo() {

        userInfo.typeAccount();

        if (userInfo.localEmail!=null){
            eEmail.setText(userInfo.localEmail);
        }
        if (userInfo.personEmail!=null){
            eEmail.setText(userInfo.emailInfo);
        }
        else
            eEmail.setText(R.string.noInfo);

        if (userInfo.localName!=null){
            eName.setText(userInfo.localName);
        }
        if (userInfo.nameInfo!=null){
            eName.setText(userInfo.nameInfo);
            if(userInfo.personPhoto!=null) {
                imageUri=userInfo.personPhoto;
            }
            else {
                imageUri=null;
            }
        }
        else{
            eName.setText(R.string.noInfo);
        }
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 200){
//            if(resultCode == Activity.RESULT_OK){
//                imageUri = data.getData();
//                profileImage.setImageURI(imageUri);
//            }
//        }
//    }
}