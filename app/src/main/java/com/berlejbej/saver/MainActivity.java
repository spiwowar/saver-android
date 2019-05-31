package com.berlejbej.saver;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.berlejbej.saver.fragments.ConstantCostsFragment;
import com.berlejbej.saver.fragments.IncomeFragment;
import com.berlejbej.saver.fragments.MainFragment;
import com.berlejbej.saver.fragments.AdditionalIncomeFragment;

public class MainActivity extends AppCompatActivity{

    private int currentlyVisibleFragment = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activities_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setupMenu();

        //My implementation
        showMainScreen();
    }

    private void setupMenu(){
        LinearLayout main = (LinearLayout) findViewById(R.id.settings_block_main);
        LinearLayout income = (LinearLayout) findViewById(R.id.settings_block_income);
        LinearLayout additionalIncome = (LinearLayout) findViewById(R.id.settings_block_additional_income);
        LinearLayout constantCosts = (LinearLayout) findViewById(R.id.settings_block_constant_costs);
        LinearLayout predictions = (LinearLayout) findViewById(R.id.settings_block_predictions);
        LinearLayout statistics = (LinearLayout) findViewById(R.id.settings_block_statistics);

        main.setOnClickListener(blockClickListener(R.id.settings_block_main));
        income.setOnClickListener(blockClickListener(R.id.settings_block_income));
        additionalIncome.setOnClickListener(blockClickListener(R.id.settings_block_additional_income));
        constantCosts.setOnClickListener(blockClickListener(R.id.settings_block_constant_costs));
        predictions.setOnClickListener(blockClickListener(R.id.settings_block_predictions));
        statistics.setOnClickListener(blockClickListener(R.id.settings_block_statistics));
    }

    private View.OnClickListener blockClickListener(final int blockType){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentlyVisibleFragment != blockType) {
                    replaceFragment(blockType);
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        };
    }

    private void showMainScreen(){
        replaceFragment(R.id.settings_block_main);
        currentlyVisibleFragment = R.id.settings_block_main;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void replaceFragment(int itemId){
        Fragment fragment = null;

        Log.d("MAINA!", "Clicked_item: " + itemId);

        switch (itemId){
            case R.id.settings_block_main:
                fragment = new MainFragment();
                break;
            case R.id.settings_block_income:
                fragment = new IncomeFragment();
                break;
            case R.id.settings_block_constant_costs:
                fragment = new ConstantCostsFragment();
                break;
            case R.id.settings_block_additional_income:
                fragment = new AdditionalIncomeFragment();
                break;
            case R.id.settings_block_predictions:
                break;
            case R.id.settings_block_statistics:
                break;
            // No default needed and even it would be worse
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.replace(R.id.content_frame, fragment);
            ft.commit();

            setCurrentlyVisibleFragment(itemId);
        }
    }

    private void setCurrentlyVisibleFragment(int blockType){

        if (currentlyVisibleFragment != -1) {
            LinearLayout previousBlock = (LinearLayout) findViewById(currentlyVisibleFragment);
            previousBlock.setBackgroundColor(0);
        }

        currentlyVisibleFragment = blockType;
        LinearLayout block = (LinearLayout)findViewById(blockType);
        block.setBackgroundColor(Color.parseColor("#987987"));
    }

}
