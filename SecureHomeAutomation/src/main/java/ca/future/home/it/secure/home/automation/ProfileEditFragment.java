/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/

package ca.future.home.it.secure.home.automation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileEditFragment extends Fragment {

    //Declarations
    private CircleImageView profileImage;
    private FloatingActionButton changeProfileImageButton;
    private Button saveChanges;
    private Button changePassword;
    private EditText eName;
    private EditText eEmail;
    private EditText ePhoneNumber;
    Uri imageUri;
    View view;
    UserInfo userInfo=new UserInfo();
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
        changeProfileImageButton = view.findViewById(R.id.editProfileImageIcon);
        changePassword = view.findViewById(R.id.editProfileChangePasswordButton);
        saveChanges = view.findViewById(R.id.editProfileSaveChanges);
        eName = view.findViewById(R.id.editProfilePersonName);
        eEmail = view.findViewById(R.id.editProfilePersonEmail);
        ePhoneNumber = view.findViewById(R.id.editProfilePersonPhone);
        userinfo();
        changeProfileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Opening Gallary
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery,200);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),ForgotPasswordActivity.class));
            }
        });
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), R.string.changesSaved, Toast.LENGTH_SHORT).show();
            }
        });





        return view;
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200){
            if(resultCode == Activity.RESULT_OK){
                imageUri = data.getData();
                profileImage.setImageURI(imageUri);
            }
        }
    }
}