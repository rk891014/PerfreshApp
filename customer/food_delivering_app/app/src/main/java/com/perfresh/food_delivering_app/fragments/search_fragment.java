package com.perfresh.food_delivering_app.fragments;

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
import android.widget.EditText;
import android.widget.LinearLayout;

import com.perfresh.food_delivering_app.R;
import com.perfresh.food_delivering_app.activities.MainActivity;
import com.perfresh.food_delivering_app.adapter.CategoryAdapter;
import com.perfresh.food_delivering_app.model.Categorymodel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class search_fragment extends Fragment{

    RecyclerView recyclerView;
    DatabaseReference reference;
    StorageReference mStorageref;
    ArrayList<Categorymodel> categorylist = new ArrayList<>();
    ArrayList<Categorymodel> list;
    EditText et1;
    LinearLayout linearfind;
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
        linearfind=view.findViewById(R.id.linearfind);
        linearfind.setVisibility(View.GONE);
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


        DatabaseReference query = FirebaseDatabase.getInstance().getReference("CategoryItems");

        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    categorylist.clear();
                    int i=0;
                    Random rand = new Random();
                    int rand_int = rand.nextInt(200);
                    list = new ArrayList<>();
                    String availability;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot postSnapshott : postSnapshot.getChildren()) {
                            availability = postSnapshott.child("availability").getValue().toString();
                            if (availability.equals("yes")){
                                Categorymodel model = postSnapshott.getValue(Categorymodel.class);
                                if(i>rand_int && i<rand_int+10){
                                    categorylist.add(model);
                                }
                                i++;
                                list.add(postSnapshott.getValue(Categorymodel.class));
                            }
                        }
                    }
                    CategoryAdapter Cadapter = new CategoryAdapter(categorylist, getContext(), getActivity());
                    Collections.reverse(categorylist);
                    recyclerView.setAdapter(Cadapter);
                    Cadapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("ErrorTAG", "loadPost:onCancelled", databaseError.toException());

            }
        });
    }



    private void searchItem(final String toString) {
            int j=0;
            ArrayList<Categorymodel> mylist=new ArrayList<>();
            for(Categorymodel ele : list ){
                if(ele.getProductname().toLowerCase().contains(toString.toLowerCase()) && j<10){
                    mylist.add(ele);
                    j++;
                }
            }
            if(mylist.isEmpty()){
                linearfind.setVisibility(View.VISIBLE);
            }else{
                linearfind.setVisibility(View.GONE);
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