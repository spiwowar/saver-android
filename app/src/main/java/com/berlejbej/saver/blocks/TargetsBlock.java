package com.berlejbej.saver.blocks;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.berlejbej.saver.R;
import com.berlejbej.saver.TargetDetailsActivity;
import com.berlejbej.saver.blocks.utils.dialogs.DialogAddTarget;
import com.berlejbej.saver.database.DBHandler;
import com.berlejbej.saver.notifiers.NotifierManager;
import com.berlejbej.saver.objects.Target;
import com.berlejbej.saver.blocks.utils.targets.TargetAdapter;
import com.berlejbej.saver.utils.ClickableFrameLayout;
import com.berlejbej.saver.utils.Utils;

import java.util.List;

/**
 * Created by Szymon on 2016-10-07.
 */
public class TargetsBlock extends Block {

    private Context context;
    private View block;

    private TextView lackOfTargets;
    private ListView listView;
    private ClickableFrameLayout targetsClickSpace;
    private TargetAdapter targetAdapter;

    private AdapterView.OnItemClickListener targetItemClickListener = targetItemClickListener();
    private AdapterView.OnItemLongClickListener targetItemLongClickListener = targetItemLongClickListener();
    private DialogInterface.OnClickListener okRemoveTargetButtonListener = okRemoveTargetButtonListener();
    private DialogInterface.OnClickListener cancelRemoveTargetButtonListener = cancelRemoveTargetButtonListener();
    private View.OnClickListener targetsAddButtonClicked = targetsAddButtonClicked();
    private View targetToRemove;

    public TargetsBlock(Context context) {
        super(context);
        this.context = context;
    }

    public TargetsBlock(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public TargetsBlock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public TargetsBlock(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    @Override
    public void create(ViewGroup viewGroup){
        block = ((Activity) context).getLayoutInflater().inflate(R.layout.block_targets, viewGroup);

        lackOfTargets = (TextView) block.findViewById(R.id.main_lack_of_targets_text_view_id);
        targetsClickSpace = (ClickableFrameLayout) block.findViewById(R.id.main_target_add_button_space_id);
        listView = (ListView) block.findViewById(R.id.main_target_list_view_id);
    }

    @Override
    public void update() {
        DBHandler dbHandler = DBHandler.getInstance(getContext());
        List<Target> data = dbHandler.getAllTargets();

        if (data.isEmpty()){
            lackOfTargets.setVisibility(View.VISIBLE);
        }
        else {
            lackOfTargets.setVisibility(View.GONE);
        }

        targetAdapter = new TargetAdapter(getContext(), R.layout.targets_list_view, data);
        listView.setAdapter(targetAdapter);
        listView.setOnItemClickListener(targetItemClickListener);
        listView.setOnItemLongClickListener(targetItemLongClickListener);

        targetsClickSpace.setOnClickListener(targetsAddButtonClicked);
    }

    public AdapterView.OnItemClickListener targetItemClickListener(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), TargetDetailsActivity.class);
                intent.putExtra("item_id", (int) view.getTag(R.string.item_id));
                getContext().startActivity(intent);
            }
        };
    }
    public AdapterView.OnItemLongClickListener targetItemLongClickListener(){
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                targetToRemove = view;
                Utils.createRemoveAlertDialog(getContext(),
                        okRemoveTargetButtonListener, cancelRemoveTargetButtonListener);

                return true;
            }
        };
    }

    private DialogInterface.OnClickListener okRemoveTargetButtonListener(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (targetToRemove != null) {
                    DBHandler dbHandler = DBHandler.getInstance(getContext());
                    Target target = new Target();
                    target.setID((Integer) targetToRemove.getTag(R.string.item_id));
                    if (dbHandler.removeTarget(target)) {
                        NotifierManager.notifyChange();
                    }
                    else {
                        Toast.makeText(getContext(), "Could not delete the item", Toast.LENGTH_SHORT).show();
                    }
                }
                dialog.cancel();
            }
        };
    }

    private DialogInterface.OnClickListener cancelRemoveTargetButtonListener(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                targetToRemove = null;
                dialog.cancel();
            }
        };
    }


    public View.OnClickListener targetsAddButtonClicked(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAddTarget dialogAddTarget = new DialogAddTarget(context);
                dialogAddTarget.show();
            }
        };
    }
}

