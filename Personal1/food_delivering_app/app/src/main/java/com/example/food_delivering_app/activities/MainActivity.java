package com.example.food_delivering_app.activities;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_delivering_app.R;
import com.example.food_delivering_app.fragments.home_fragment;
import com.example.food_delivering_app.fragments.orders_fragment;
import com.example.food_delivering_app.fragments.profile_fragment;
import com.example.food_delivering_app.fragments.search_fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewpager;
    int position;
    private static Dialog mDialog;
    private static Dialog mDialog1;
    public Button retry;
    Boolean hasinternet;
    LinearLayout container;
    String availability="false";
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


        all();
        container.setBackgroundColor(Color.WHITE);
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
//        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog1.setContentView(R.layout.dialog_box2);
        mDialog1.setCanceledOnTouchOutside(false);
        mDialog1.setCancelable(true);
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
                    Toast.makeText(getApplicationContext(),"hjhggv",Toast.LENGTH_LONG).show();
                        container.setVisibility(View.VISIBLE);
                        container.setBackgroundColor(Color.WHITE);

                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ErrorTAG", "loadPost:onCancelled", databaseError.toException());

            }
        });
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