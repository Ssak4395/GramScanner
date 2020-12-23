package com.example.bar_code_scanner.utilities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Vibrator;

import androidx.appcompat.app.AlertDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {

    public static String dateToString(Date toConvert)
    {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY hh:mm");
        return dateFormat.format(toConvert);
    }

    public static void vibrateDevice(Context context)
    {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(100);
    }

    public static String dateToSQLString(Date toConvert)
    {
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        return dateFormat.format(toConvert);
    }

    public static void buildAlertDialog(Context context,String title, String Message, String buttonText)
    {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        mBuilder.setTitle(title);
        mBuilder.setMessage(Message);
        mBuilder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = mBuilder.create();
        Helper.vibrateDevice(context);
        alertDialog.show();
    }


}
