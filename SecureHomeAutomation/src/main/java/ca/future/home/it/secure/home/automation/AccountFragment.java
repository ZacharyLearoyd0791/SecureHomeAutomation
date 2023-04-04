/*
Authors/Std.#/Section:
Zachary Learoyd (LRDZ0002) - CENG-322-0NC
Akash Muhundhan (N01420118) - CENG-322-0NA
Harpreet Cheema (N01438638) - CENG-322-0NA
Krushang Parekh (N01415355) - CENG-322-0NC
*/
package ca.future.home.it.secure.home.automation;

import static android.content.Context.MODE_PRIVATE;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.animation.Animator;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AccountFragment extends Fragment {
    View view;

    //RecyclerView
    RecyclerView recyclerView;
    RecyclerView recyclerViewOthers;
    private List<AccountFragmentData> accountFragmentDataList;
    String userName;
    String userEmail;
    TextView userNameTV;
    String userPhone;
    private AccountFragmentRecyclerViewAdapter adapter;
    Button signoutButton;
    String key,localKey,personalKey,profileKey,sensorKey,windowsKey,userKey,userData, accountKey, emailKey,
            finalEmailKey, nameKey, finalNameKey;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    GoogleSignInClient mClient;
    DatabaseReference databaseReference;


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
        editor = sharedPreferences.edit();
        userNameTV = view.findViewById(R.id.account_person_name);

        //Setting up google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mClient = GoogleSignIn.getClient(getContext(),gso);
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getContext());


        //Getting user data from database
        //Getting Email address
        emailKey = getString(R.string.slash_email);
        nameKey = getString(R.string.name_info);

        accountKey = dbID();
        finalEmailKey = accountKey + emailKey;
        finalNameKey = accountKey + nameKey;

        databaseReference = FirebaseDatabase.getInstance().getReference().child((finalEmailKey));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    userEmail = snapshot.getValue().toString();
                }else{
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
                if(snapshot.exists()){
                    userName = snapshot.getValue().toString();
                    userNameTV.setText(userName);
                }else{
                    Toast.makeText(getContext(), "Cannot found Name!", Toast.LENGTH_SHORT).show();
                    userName = "No Name found";
                    userNameTV.setText(userName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        //Toast.makeText(getContext(), "Name DB: "+userName+"Email DB: "+userEmail, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();

        //RecyclerView
        recyclerView.setLayoutManager(linearLayoutManager);

        accountFragmentDataList = new ArrayList<>();
        accountFragmentDataList.add(new AccountFragmentData(R.drawable.person_icon_account,"Name",userName));
        accountFragmentDataList.add(new AccountFragmentData(R.drawable.email_icon_account,"Email",userEmail));
        accountFragmentDataList.add(new AccountFragmentData(R.drawable.phone_icon_account_4,"Phone","Null"));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        adapter = new AccountFragmentRecyclerViewAdapter(accountFragmentDataList);
        recyclerView.setAdapter(adapter);


        //SignOut User
        signoutButton =  view.findViewById(R.id.account_sign_out_button);
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                editor.putBoolean(getString(R.string.logged), false).apply();
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
                GoogleSignInClient googleSignInClient=GoogleSignIn.getClient(getApplicationContext(),gso);
                googleSignInClient.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
               }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    private String dbID(){
        userInfo.typeAccount();
        userKey = getApplicationContext().getString(R.string.userKey);
        userData = getString(R.string.user_info);

        localKey = userInfo.userId;
        personalKey = userInfo.idInfo;

        if(localKey!=null){
            key = localKey;
        }
        if(personalKey !=null){
            key = personalKey;
        }
        profileKey = key+userData;
        sensorKey = profileKey;

        return userKey+profileKey;
    }
    }