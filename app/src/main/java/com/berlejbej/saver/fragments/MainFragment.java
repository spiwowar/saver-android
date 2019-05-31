package com.berlejbej.saver.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.berlejbej.saver.R;
import com.berlejbej.saver.blocks.BlocksManager;
import com.berlejbej.saver.notifiers.ChangeRecipient;
import com.berlejbej.saver.notifiers.NotifierManager;

/**
 * Created by Szymon on 2016-11-06.
 */
public class MainFragment extends Fragment implements ChangeRecipient {

    private View view;
    private BlocksManager blocksManager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragments_main, container, false);

        init();
        return view;
    }

    private void init(){

        NotifierManager.addRecipient(this);
        setupBlocks();
        updateMainScreen();
    }

    private void setupBlocks(){
        // TODO: It should be probably added also to setup block on resume activity
        // (after get back from another fragment if something has been changed)
        // after analysis it appears that in MainActivity every Fragment is created every time
        // so right now no need for onResume I guess, but it should be changed imho

        LinearLayout blocksRootView = (LinearLayout) view.findViewById(R.id.main_layout_id);
        blocksRootView.removeAllViews();

        blocksManager = new BlocksManager(getContext());
        blocksManager.addBlocksToLayout(blocksRootView);
    }

    public void updateMainScreen(){
        blocksManager.updateBlocks();
    }

    public void onPause(){
        super.onPause();
    }

    public void onResume(){
        super.onResume();
    }

    @Override
    public void changePerformed() {
        if (isAdded()) {
            updateMainScreen();
        }
    }
}
