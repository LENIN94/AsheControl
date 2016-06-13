package com.mx.ashe.ashecontrol;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.mx.ashe.ashecontrol.Fragments.FragmentHistorialVisitas;
import com.mx.ashe.ashecontrol.Fragments.FragmentRegAsistencia;
import com.mx.ashe.ashecontrol.Fragments.HomeFragment;
import com.mx.ashe.ashecontrol.UI.CustomTypefaceSpan;
import com.mx.ashe.ashecontrol.app.MyApplication;


public class ActivityMenu extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBar actionBar;
    public TextView txtusuario;
    ImageView img;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String TAG = ActivityMenu.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (MyApplication.getInstance().getPrefManager().getUser() == null) {
            launchLoginActivity();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView tv = new TextView(getApplicationContext());
        tv.setText("Visitas");
        tv.setTextSize(25);
        tv.setTypeface(Typeface.createFromAsset(getAssets(),"CaviarDreams.ttf"));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        // Finally, set the newly created TextView as ActionBar custom view
        actionBar.setCustomView(tv);
        // Update the action bar title with the TypefaceSpan instance
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            //the method we have create in activity
            applyFontToMenuItem(mi);
        }
        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);

        }
        setupNavigationDrawerContent(navigationView);
        txtusuario = (TextView) findViewById(R.id.tvNombre);
        // txtusuario.setText(MyApplication.getInstance().getPrefManager().getUser().getNombre());
        Log.e(getPackageName().toString(), MyApplication.getInstance().getPrefManager().getUser().getNombre());
        img = (ImageView) findViewById(R.id.img_user);
        //First start (Inbox Fragment)
        if (checkPlayServices()) {

            // fetchChatRooms();
        }
        setFragment(0);
    }
    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "CaviarDreams.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported. Google Play Services not installed!");
                Toast.makeText(getApplicationContext(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.item_principal:
                                menuItem.setChecked(true);
                                setFragment(0);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;

                            case R.id.item_regvisita:
                                menuItem.setChecked(true);
                                setFragment(1);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;

                            case R.id.item_visitas:
                                menuItem.setChecked(true);
                                setFragment(2);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;


                            case R.id.item_about:
                                menuItem.setChecked(true);
                                Toast.makeText(ActivityMenu.this, menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;

                            case R.id.item_exit:
                                MyApplication.getInstance().logout();
                                return true;
                        }
                        return true;
                    }
                });
    }

    public void setFragment(int position) {
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        switch (position) {
            case 0:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                HomeFragment inboxFragment = new HomeFragment();
                fragmentTransaction.replace(R.id.fragment, inboxFragment);
                fragmentTransaction.commit();
                break;

            case 1:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                //  FragmentRegAsistencia rpt = new FragmentRegAsistencia();
                startActivity(new Intent(ActivityMenu.this, FragmentRegAsistencia.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                // fragmentTransaction.replace(R.id.fragment, rpt);
                fragmentTransaction.commit();
                break;

            case 2:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                FragmentHistorialVisitas tec = new FragmentHistorialVisitas();
                fragmentTransaction.replace(R.id.fragment, tec);
                fragmentTransaction.commit();
                break;

            case 3:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                //  MiPosicionFragment ub = new MiPosicionFragment();
                //  fragmentTransaction.replace(R.id.fragment, ub);
                fragmentTransaction.commit();
                break;

        }
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(ActivityMenu.this, ActivityLogin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


}