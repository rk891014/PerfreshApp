package com.example.food_delivering_app.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.food_delivering_app.R;
import com.example.food_delivering_app.activities.MainActivity;
import com.example.food_delivering_app.adapter.CategoryAdapter;
import com.example.food_delivering_app.model.Categorymodel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class search_fragment extends Fragment{

    RecyclerView recyclerView;
    DatabaseReference reference;
    StorageReference mStorageref;
    ArrayList<Categorymodel> categorylist = new ArrayList<>();
    ArrayList<Categorymodel> list;
    EditText et1;
    FloatingActionButton viewcart;


    public search_fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_search_fragment, container, false);
        recyclerView = view.findViewById(R.id.caterv);
        et1=view.findViewById(R.id.et1);
        viewcart=view.findViewById(R.id.viewcart);
        reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mStorageref = FirebaseStorage.getInstance().getReference();
        categorylist = new ArrayList<>();
        init();

        viewcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setCurrentItem (0, true);
                ((MainActivity)getActivity()).setCurrentItem (2, true);
            }
        });
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    init();
                }else{
                    searchItem(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }



    private void init() {
        clearAll();


        DatabaseReference query = FirebaseDatabase.getInstance().getReference("items");

        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                categorylist.clear();
                list=new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot postSnapshott : postSnapshot.getChildren()) {

                        Categorymodel model = postSnapshott.getValue(Categorymodel.class);
                        categorylist.add(model);
                        list.add(postSnapshott.getValue(Categorymodel.class));
                    }
                }
                CategoryAdapter Cadapter=new CategoryAdapter(categorylist,getContext(),getActivity());
                Collections.reverse(categorylist);
                recyclerView.setAdapter(Cadapter);
                Cadapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("ErrorTAG", "loadPost:onCancelled", databaseError.toException());

            }
        });
    }



    private void searchItem(final String toString) {

            ArrayList<Categorymodel> mylist=new ArrayList<>();
            for(Categorymodel ele : list){
                if(ele.getProductname().toLowerCase().contains(toString.toLowerCase())){
                    mylist.add(ele);
                }
            }
            CategoryAdapter Madapter=new CategoryAdapter(mylist,getContext(),getActivity());
            Collections.reverse(mylist);
            recyclerView.setAdapter(Madapter);
            Madapter.notifyDataSetChanged();
        }
    private void clearAll() {
        if(categorylist!=null){
            categorylist.clear();
        }
        categorylist = new ArrayList<>();
    }
}