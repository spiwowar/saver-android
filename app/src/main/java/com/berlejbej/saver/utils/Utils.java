package com.berlejbej.saver.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.CalendarView;

import com.berlejbej.saver.R;
import com.berlejbej.saver.database.DBHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Szymon on 2016-10-27.
 */
public class Utils {

    private static final String TAG = "Utils";

    public static void createRemoveAlertDialog(Context context, DialogInterface.OnClickListener okRemoveButtonListener,
                                           DialogInterface.OnClickListener cancelRemoveButtonListener){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        String yes = context.getResources().getString(R.string.yes);
        String no = context.getResources().getString(R.string.no);
        alertDialogBuilder.setPositiveButton(yes, okRemoveButtonListener);
        alertDialogBuilder.setNegativeButton(no, cancelRemoveButtonListener);

        alertDialogBuilder.setTitle(context.getResources().getString(R.string.removing_item_title));
        alertDialogBuilder.setMessage(context.getResources().getString(R.string.removing_item_message));
        alertDialogBuilder.show();
    }
}
