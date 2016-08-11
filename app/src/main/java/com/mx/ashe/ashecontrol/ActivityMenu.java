package com.mx.ashe.ashecontrol;


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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;

import com.mx.ashe.ashecontrol.Fragments.FragmentAcercaDe;
import com.mx.ashe.ashecontrol.Fragments.FragmentHistorialVisitas;
import com.mx.ashe.ashecontrol.Fragments.FragmentRegAsistencia;
import com.mx.ashe.ashecontrol.Fragments.HomeFragment;
import com.mx.ashe.ashecontrol.UI.CustomTypefaceSpan;
import com.mx.ashe.ashecontrol.app.MyApplication;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.mobiwise.materialintro.animation.MaterialIntroListener;
import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.view.MaterialIntroView;


public class ActivityMenu extends AppCompatActivity implements MaterialIntroListener {

    @InjectView(R.id.navigation_drawer_layout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    ActionBar actionBar;
    private String TAG = ActivityMenu.class.getSimpleName();

    private static final String INTRO_CARD = "material_intro";

    TextView tvuser;



    @InjectView(R.id.navigation_view)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (MyApplication.getInstance().getPrefManager().getUser() == null) {
            launchLoginActivity();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        //  actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        TextView tv = new TextView(getApplicationContext());
        tv.setText("ASHE Monitor");
        tv.setTextSize(25);
        tv.setTypeface(Typeface.createFromAsset(getAssets(), "CaviarDreams.ttf"));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        // Finally, set the newly created TextView as ActionBar custom view
        actionBar.setCustomView(tv);
        // Update the action bar title with the TypefaceSpan instance
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);



       // tvuser.setText(MyApplication.getInstance().getPrefManager().getUser().getNombre());

        Menu m = navigationView.getMenu();

        //new PreferencesManager(getApplicationContext()).resetAll();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);
            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
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
        // txtusuario.setText(MyApplication.getInstance().getPrefManager().getUser().getNombre());
        setFragment(0);
        showIntro(toolbar, INTRO_CARD, "Accede al menu de la aplicación tocando\n el icono en la esquina superior izquierda" +
                "\no deslizando la pantalla de izquierda a derecha!");

    }

    private void showIntro(View view, String usageId, String text) {
        new MaterialIntroView.Builder(ActivityMenu.this)
                .enableDotAnimation(true)
                //.enableIcon(false)
                .setFocusGravity(FocusGravity.LEFT)
                .setFocusType(Focus.MINIMUM)
                .setDelayMillis(200)
                .enableFadeAnimation(true)
                .performClick(true)
                .setInfoText(text)
                .setTarget(view)
                .setUsageId(usageId) //THIS SHOULD BE UNIQUE ID
                .show();
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "CaviarDreams.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
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
                                setFragment(3);
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


                Log.d(TAG, String.valueOf(MyApplication.getInstance().getPrefManager().getUltimoCliente().isFinalizado()));
                if (MyApplication.getInstance().getPrefManager().getUltimoCliente().isFinalizado()) {
                    startActivity(new Intent(ActivityMenu.this, FragmentRegAsistencia.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    startActivity(new Intent(ActivityMenu.this, Activity_ConcluirVisita.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }


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
                FragmentAcercaDe ub = new FragmentAcercaDe();
                fragmentTransaction.replace(R.id.fragment, ub);
                fragmentTransaction.commit();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(ActivityMenu.this, ActivityLogin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    public void onUserClicked(String s) {

    }
}