package com.perfresh.food_delivering_app.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.perfresh.food_delivering_app.BuildConfig;
import com.perfresh.food_delivering_app.R;


import com.perfresh.food_delivering_app.fragments.home_fragment;
import com.perfresh.food_delivering_app.fragments.orders_fragment;
import com.perfresh.food_delivering_app.fragments.profile_fragment;
import com.perfresh.food_delivering_app.fragments.search_fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity  {
    private TabLayout mTabLayout;
    private ViewPager mViewpager;
    int position;
    private static Dialog mDialog,mDialog3;
    private static Dialog mDialog1,mDialog15;
    public static int share=0;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;
     public static String Device;
    public Button retry,submit,update,cancel;
    public static int j=0;
    ImageView exit;
    public static EditText coupon;
    public static TextView wrongcode;
    Boolean hasinternet;
    public static String DeviceID;
    LinearLayout container;
    String availability="false";
    public static String  itemnottaken="";
    public static String appavailable="rohit";
    String app_version="rohit";
    String start="1";
    String start1="3";
    private int[] tabIcons = {
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_search_24,
            R.drawable.ic_baseline_add_shopping_cart_24,
            R.drawable.ic_baseline_emoji_people_24
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container=findViewById(R.id.container);
        DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel("MyNotifications","MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager= getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "successful";
                        if (!task.isSuccessful()) {
                            msg = "failed";
                        }
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


        if(!checkconnection()){
            createDialog();
        }

        getavailability();
        requestpermission();

}

    public void all(){
        mTabLayout=findViewById(R.id.tabLayout);
        mViewpager=findViewById(R.id.viewPager);

        setupViewPager(mViewpager);
        mTabLayout.setupWithViewPager(mViewpager);
        mTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#03DAC5"));
        setupTabIcons();

        Intent i = getIntent();
        String s = i.getStringExtra("vieworders");
        String s2 = i.getStringExtra("vieworderss");
        if(start.equals(s)){
            setCurrentItem(2,true);
        }
        if(start1.equals(s2)){
            setCurrentItem(3,true);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        KeyboardVisibilityEvent.setEventListener(
                MainActivity.this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {

                        if(isOpen){
                            mTabLayout.setVisibility(View.GONE);
                            getWindow().getDecorView().setSystemUiVisibility(
                                    View.SYSTEM_UI_FLAG_FULLSCREEN);
                        } else {
                            mTabLayout.setVisibility(View.VISIBLE);
                            getWindow().getDecorView().setSystemUiVisibility(
                                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                        }
                    }
                });

        SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
        boolean firstStart=prefs.getBoolean("firstStart",true);

        if(firstStart){
            showStartDialog();
        }

    }


    private void showStartDialog() {
        mDialog3 = new Dialog(this);
        mDialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog3.setContentView(R.layout.dialog_first_time);
        mDialog3.setCanceledOnTouchOutside(false);
        mDialog3.setCancelable(false);
        exit = mDialog3.findViewById(R.id.exit);
        coupon = mDialog3.findViewById(R.id.coupon);
        wrongcode= mDialog3.findViewById(R.id.wrongcode);
        submit = mDialog3.findViewById(R.id.submit);
        wrongcode.setVisibility(View.GONE);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog3.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                j=0;
               if(!coupon.getText().toString().isEmpty()){
                   customerdetail();
               }

            }
        });


        mDialog3.show();
        SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor=prefs.edit();
        editor.putBoolean("firstStart",false);
        editor.apply();
    }
    public void customerdetail() {
        DatabaseReference query = FirebaseDatabase.getInstance().getReference("customer detail");
        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && j==0){
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String mobile=postSnapshot.child("mobileno").getValue().toString();

                        if(mobile.equals(coupon.getText().toString())){
                            Device = postSnapshot.child("DeviceId").getValue().toString();
                            share = Integer.parseInt(postSnapshot.child("shares").getValue().toString());
                            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("customer detail").child(Device);

                            if(!Device.equals(DeviceID)) {
                                Map<String, Object> taskMap = new HashMap<String, Object>();
                                String share1 = String.valueOf(share + 1);
                                taskMap.put("shares", share1);
                                rootRef.updateChildren(taskMap);
                                if(mDialog3.isShowing()){
                                    mDialog3.dismiss();
                                }
                                Toast.makeText(MainActivity.this,"successfully earned coins",Toast.LENGTH_LONG).show();
                            }else{
                                wrongcode.setVisibility(View.VISIBLE);
                                j=1;
                            }
                            j=1;
                        }else{
                            wrongcode.setVisibility(View.VISIBLE);
                            j=1;
                    }
                }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ErrorTAG", "loadPost:onCancelled", databaseError.toException());
            }

        });


    }
    private void requestpermission() {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        int grant = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
        if (grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;

            ActivityCompat.requestPermissions(MainActivity.this, permission_list, 1);
        }
    }
    private void createDialog() {
        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.dialog_box);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(true);
        retry = (Button) mDialog.findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkconnection()){
                    mDialog.dismiss();
                    all();
                }else {
                    mDialog.dismiss();
                    createDialog();
                }
            }
        });
        mDialog.show();
    }
    private void availableDialog() {
        mDialog1 = new Dialog(this);
        mDialog1.setContentView(R.layout.dialog_box2);
        mDialog1.setCanceledOnTouchOutside(false);
        mDialog1.setCancelable(true);
        retry = (Button) mDialog1.findViewById(R.id.vieworders);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(MainActivity.this,OrderCompleted.class);
               startActivity(intent);
            }
        });
        retry = (Button) mDialog1.findViewById(R.id.retry);
        mDialog1.show();
    }

    public void getavailability(){
        DatabaseReference query2 = FirebaseDatabase.getInstance().getReference("whole");
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    availability = dataSnapshot.child("available").getValue().toString();
                    app_version = dataSnapshot.child("app_version").getValue().toString();
                    appavailable = dataSnapshot.child("appdontwork").getValue().toString();
                    itemnottaken = dataSnapshot.child("itemnottaken").getValue().toString();
                    String versionName = BuildConfig.VERSION_NAME;

                    if(!app_version.equals(versionName)){
                        updatedialog();
                    }

                    if(availability.equals("unavailable")){
                        availableDialog();
                        container.setVisibility(View.INVISIBLE);
                    }else{
                        container.setVisibility(View.VISIBLE);
                        container.setBackgroundColor(Color.WHITE);
                        if(checkconnection()){
                            all();
                        }else {
                            createDialog();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ErrorTAG", "loadPost:onCancelled", databaseError.toException());

            }
        });
    }

    private void updatedialog() {
        mDialog15 = new Dialog(this);
        mDialog15.setContentView(R.layout.updatedialog);
        mDialog15.setCanceledOnTouchOutside(false);
        mDialog15.setCancelable(true);
        update = mDialog15.findViewById(R.id.update);
        cancel= mDialog15.findViewById(R.id.cancel);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mDialog15.dismiss();
            }
        });
        mDialog15.show();
    }

    public boolean checkconnection(){
        ConnectivityManager manager=(ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo activenetwork=manager.getActiveNetworkInfo();
        if(null!=activenetwork){
            if(activenetwork.getType()==ConnectivityManager.TYPE_WIFI){
                return true;
            }
            else if(activenetwork.getType()==ConnectivityManager.TYPE_MOBILE){
                return true;
            }
        }else{
            hasinternet=false;
        }
        return false;
    }

    public void setCurrentItem (int item, boolean smoothScroll) {
        mViewpager.setCurrentItem(item, smoothScroll);
    }


    private void setupTabIcons() {
        mTabLayout.getTabAt(0).setIcon(tabIcons[0]);
        mTabLayout.getTabAt(1).setIcon(tabIcons[1]);
        mTabLayout.getTabAt(2).setIcon(tabIcons[2]);
        mTabLayout.getTabAt(3).setIcon(tabIcons[3]);

        mTabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#03DAC5"), PorterDuff.Mode.SRC_IN);
        mTabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#ADD8E6"), PorterDuff.Mode.SRC_IN);
        mTabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#ADD8E6"), PorterDuff.Mode.SRC_IN);
        mTabLayout.getTabAt(3).getIcon().setColorFilter(Color.parseColor("#ADD8E6"), PorterDuff.Mode.SRC_IN);

        mTabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewpager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                position= tab.getPosition();
                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.colorAccent);
                if(tab.getIcon()!=null){
                    tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.cyan);
                if(tab.getIcon()!=null){
                    tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                }

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }
        });

    }



    private void setupViewPager(ViewPager viewPager){
        viewPagerAdapter adapter = new viewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new home_fragment(),"");
        adapter.addFragment(new search_fragment(),"");
        adapter.addFragment(new orders_fragment(),"");
        adapter.addFragment(new profile_fragment(),"");
        viewPager.setAdapter(adapter);
    }


    public class viewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList = new ArrayList<>();
        private List<String> titleList = new ArrayList<>();

        public viewPagerAdapter(FragmentManager fm) {

            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }

        public void addFragment(Fragment fragment, String title)    {
            fragmentList.add(fragment);
            titleList.add(title);
        }
    }

    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else { Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
            if (hasFocus) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }

}