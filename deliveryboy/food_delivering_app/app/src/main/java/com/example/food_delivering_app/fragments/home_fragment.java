package com.example.food_delivering_app.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.food_delivering_app.BuildConfig;
import com.example.food_delivering_app.activities.MainActivity;
import com.example.food_delivering_app.adapter.MyAdapter;
import com.example.food_delivering_app.adapter.MyAdapter2;
import com.example.food_delivering_app.R;
import com.example.food_delivering_app.model.Model;
import com.example.food_delivering_app.model.Model2;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

import static android.content.ContentValues.TAG;


public class home_fragment extends Fragment {
    TextView category,popular;
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    DatabaseReference reference;
    StorageReference mStorageref;
    ArrayList<Model> imageList = new ArrayList<>();
    ArrayList<Model2> popularlist = new ArrayList<>();
    ImageView safetyimage,showpopup;
    LinearLayout card1;
    PopupMenu popup;
    CardView card2;
    String DeviceID;
    TextView homeaddres,buddy;

    public home_fragment() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home_fragment, container, false);

        recyclerView = view.findViewById(R.id.rv);
        recyclerView2 = view.findViewById(R.id.rv2);
        safetyimage = view.findViewById(R.id.safety_image);
        category = view.findViewById(R.id.categories);
        popular = view.findViewById(R.id.popular);
        card1=view.findViewById(R.id.card1);
        showpopup=view.findViewById(R.id.showPopup);
        card2=view.findViewById(R.id.card2);
        homeaddres=view.findViewById(R.id.homeaddress);
        buddy=view.findViewById(R.id.buddy);
        DeviceID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        recyclerView.setHasFixedSize(true);
        recyclerView2.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mStorageref = FirebaseStorage.getInstance().getReference();
        imageList = new ArrayList<>();
        popularlist=new ArrayList<>();
        init();
        initprofile();
        initpopular();
        safetyimagequery();

        showpopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup = new PopupMenu(getContext(), v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu, popup.getMenu());
                showMenu(v);
            }

        });

       card1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ((MainActivity)getActivity()).setCurrentItem (3, true);
           }
       });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setCurrentItem (1, true);
            }
        });

        return view;
    }

    public void showMenu(View v) {


        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.title:
                        Toast.makeText(getContext(),"bhjchvjvj",Toast.LENGTH_LONG).show();
                        return true;
                    case R.id.call:
                        dialContactPhone("9651804942");
                        return true;
                    case R.id.share:
                        try {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                            String message="We are Purfresh, delivering veggies, groceries, dairy products, ice cream, chocolate, cake, " +
                                    "with in 30 mins to your home. Hope this helps.\n Stay in. Stay safe.\n\n";
                            String shareMessage=message+ "\nLet me recommend you this application\n";
                            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            startActivity(Intent.createChooser(shareIntent, "choose one"));
                        } catch(Exception e) {
                            //e.toString();
                        }
                        return true;
                    case R.id.rate:
                        try{
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+BuildConfig.APPLICATION_ID)));
                        }
                        catch (ActivityNotFoundException e){
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+BuildConfig.APPLICATION_ID)));
                        }
                        return true;
//                    case R.id.title:
//                        Toast.makeText(getContext(),"bhjchvjvj",Toast.LENGTH_LONG).show();
//                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }


    private void safetyimagequery(){
        DatabaseReference query2 = FirebaseDatabase.getInstance().getReference("whole");
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url=dataSnapshot.child("safetyimageurl").getValue().toString();
                Glide.with(getContext()).load(url).into(safetyimage);
                String categorii=dataSnapshot.child("category").getValue().toString();
                        category.setText(categorii);
                String popu=dataSnapshot.child("popular").getValue().toString();
                popular.setText(popu);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ErrorTAG", "loadPost:onCancelled", databaseError.toException());

            }
        });



    }











    private void initprofile() {
        clearAll();

        DatabaseReference query3 = FirebaseDatabase.getInstance().getReference("customer detail").child(DeviceID);

        query3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String addres = dataSnapshot.child("address").getValue().toString();
                    String mobileno = dataSnapshot.child("mobileno").getValue().toString();
                    homeaddres.setText(addres + " - " + mobileno);


                    String names = dataSnapshot.child("name").getValue().toString();
                    buddy.setText(names);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ErrorTAG", "loadPost:onCancelled", databaseError.toException());

            }
        });

    }




    private void init() {
        clearAll();


        DatabaseReference query = FirebaseDatabase.getInstance().getReference("profiles");

        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                imageList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Model model = postSnapshot.getValue(Model.class);
                    imageList.add(model);
                }

                MyAdapter  adapter=new MyAdapter(imageList,getContext());
                Collections.reverse(imageList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("ErrorTAG", "loadPost:onCancelled", databaseError.toException());

            }
        });
    }

    private void clearAll() {
        if(imageList!=null){
            imageList.clear();
        }
        imageList = new ArrayList<>();
    }
    private void initpopular() {

        DatabaseReference query = FirebaseDatabase.getInstance().getReference("popular");

        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                popularlist.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Model2 model2 = postSnapshot.getValue(Model2.class);
                    popularlist.add(model2);
                }
                MyAdapter2  adapter2=new MyAdapter2(popularlist,getContext());
                Collections.reverse(popularlist);
                recyclerView2.setAdapter(adapter2);
                adapter2.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("ErrorTAG", "loadPost:onCancelled", databaseError.toException());

            }
        });

    }

}