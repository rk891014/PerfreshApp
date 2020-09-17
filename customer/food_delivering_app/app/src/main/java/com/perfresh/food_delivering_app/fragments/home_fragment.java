package com.perfresh.food_delivering_app.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;


import com.perfresh.food_delivering_app.BuildConfig;
import com.perfresh.food_delivering_app.R;
import com.Perfresh.food_delivering_app.adapter.MyAdapter2;

import com.perfresh.food_delivering_app.activities.AboutUsActivity;
import com.perfresh.food_delivering_app.activities.AppReviewActivity;
import com.perfresh.food_delivering_app.activities.MainActivity;
import com.perfresh.food_delivering_app.adapter.MyAdapter;
import com.perfresh.food_delivering_app.adapter.SliderAdapterperfresh;
import com.perfresh.food_delivering_app.model.Model;
import com.perfresh.food_delivering_app.model.Model2;
import com.perfresh.food_delivering_app.model.SliderItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.Collections;


public class home_fragment extends Fragment {
    TextView category,popular;
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    DatabaseReference reference;
    CardView cardro;
    StorageReference mStorageref;
    ArrayList<Model> imageList = new ArrayList<>();
    ArrayList<Model2> popularlist = new ArrayList<>();
    ArrayList<SliderItem> centerImagelist = new ArrayList<>();
    ImageView showpopup;
    LinearLayout card1;
    PopupMenu popup;
    CardView card2;
    TextView contactno2,itemnottaken;
    SliderView sliderView;
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
        category = view.findViewById(R.id.categories);
        popular = view.findViewById(R.id.popular);
        card1=view.findViewById(R.id.card1);
        showpopup=view.findViewById(R.id.showPopup);
        cardro=view.findViewById(R.id.cardro);
        cardro.setVisibility(View.GONE);
        card2=view.findViewById(R.id.card2);
        homeaddres=view.findViewById(R.id.homeaddress);
        buddy=view.findViewById(R.id.buddy);
        itemnottaken=view.findViewById(R.id.itemnottaken);
        sliderView = view.findViewById(R.id.imageSlider);
        contactno2=view.findViewById(R.id.contactno2);
        DeviceID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        recyclerView.setHasFixedSize(true);
        recyclerView2.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mStorageref = FirebaseStorage.getInstance().getReference();
        imageList = new ArrayList<>();
        popularlist=new ArrayList<>();
        centerImagelist=new ArrayList<>();
        init();
        init2();
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

            if(!MainActivity.itemnottaken.equals("")){
                itemnottaken.setText(MainActivity.itemnottaken);
                cardro.setVisibility(View.VISIBLE);
            }


        return view;
    }



    public void showMenu(View v) {


        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.appreviews:
                        Intent i=new Intent(getContext(), AppReviewActivity.class);
                        startActivity(i);
                        return true;
                    case R.id.call:
                        dialContactPhone(contactno2.getText().toString());
                        return true;
                    case R.id.share:
                        try {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            String s=homeaddres.getText().toString();
                            String[] parts1 = s.split("-");
                            String part1 = parts1[1];
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                            String message="We are Purfresh, delivering veggies, groceries, dairy products, ice cream, chocolate, cake, " +
                                    "with in 30 mins to your home. Hope this helps.\n Stay in. Stay safe.\n\n";
                            String shareMessage=message+ "\nLet me recommend you this application\n";
                            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n"
                            +"Coupon Code : "+part1+"\n\n"+"   copy this code to get coins"+"\n\n";
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            startActivity(Intent.createChooser(shareIntent, "choose one"));
                        } catch(Exception e) {
                            ((MainActivity)getActivity()).setCurrentItem (3, true);
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
                    case R.id.aboutus:
                        Intent intent=new Intent(getContext(), AboutUsActivity.class);
                        startActivity(intent);
                        return true;
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
                String categorii=dataSnapshot.child("category").getValue().toString();
                        category.setText(categorii);
                String popu=dataSnapshot.child("popular").getValue().toString();
                popular.setText(popu);
                String contactnoo=dataSnapshot.child("contactno").getValue().toString();
                contactno2.setText(contactnoo);
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

    private void init2() {
        clearAll();


        DatabaseReference query = FirebaseDatabase.getInstance().getReference("centerimage");

        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                centerImagelist.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    SliderItem modelll = postSnapshot.getValue(SliderItem.class);
                    centerImagelist.add(modelll);
                }

                SliderViewAdapter adapter=new SliderAdapterperfresh(getContext(),centerImagelist,getActivity());
                sliderView.setSliderAdapter(adapter);
                sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                sliderView.setIndicatorSelectedColor(Color.WHITE);
                sliderView.setIndicatorUnselectedColor(Color.GRAY);
                sliderView.setScrollTimeInSec(3);
                sliderView.setAutoCycle(true);
                sliderView.startAutoCycle();


                sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
                    @Override
                    public void onIndicatorClicked(int position) {
                        Log.i("GGG", "onIndicatorClicked: " + sliderView.getCurrentPagePosition());
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
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

                MyAdapter adapter=new MyAdapter(imageList,getContext());
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