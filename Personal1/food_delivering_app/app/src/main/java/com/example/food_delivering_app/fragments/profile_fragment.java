package com.example.food_delivering_app.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_delivering_app.R;
import com.example.food_delivering_app.activities.MainActivity;
import com.example.food_delivering_app.model.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class profile_fragment extends Fragment {
    private static final String TAG = "register";
    EditText address,username,mobileno;
    Button button;
    TextView name,address1,mobileno1;
    ImageView pback;

    public profile_fragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile_fragment, container, false);
        address=view.findViewById(R.id.address);
        mobileno=view.findViewById(R.id.contactno);
        username=view.findViewById(R.id.username);
        pback=view.findViewById(R.id.pback);
        name=view.findViewById(R.id.name);
        address1=view.findViewById(R.id.address1);
        mobileno1=view.findViewById(R.id.contactno1);

        button=view.findViewById(R.id.btnUpload);
        pback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!address.getText().toString().isEmpty() && !username.getText().toString().isEmpty() && !mobileno.getText().toString().isEmpty())
                {
                    addcomment();
                }else {
                    Toast.makeText(getContext(),"All fields Required",Toast.LENGTH_LONG).show();
                }
            }
        });
        String DeviceID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("customer detail").child(DeviceID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    user u=dataSnapshot.getValue(user.class);
                    Log.d(TAG, "onDataChange: data : "+dataSnapshot.getValue().toString());
                    username.setText(u.getName());
                    address.setText(u.getAddress());
                    mobileno.setText(u.getMobileno());
                    name.setText(u.getName());
                    address1.setText(u.getAddress());
                    mobileno1.setText(u.getMobileno());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: called");
            }
        });

        return view;
    }
    private void addcomment() {
        String DeviceID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("customer detail");
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("name",username.getText().toString());
        hashMap.put("address",address.getText().toString());
        hashMap.put("mobileno",mobileno.getText().toString());
        reference.child(DeviceID).setValue(hashMap);
        address.setText("");
        username.setText("");
        mobileno.setText("");
        Toast.makeText(getContext(),"Uploaded",Toast.LENGTH_LONG).show();

    }
}