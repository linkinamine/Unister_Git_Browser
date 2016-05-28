package com.unister.gitquery.Network;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;

/**
 * Created by Mohamed El Amine on 28/05/2016.
 */


/**
 * display Check connection  dialog
 */
public class ConnectionCheck {
    public static void showNotConnected(String message, @Nullable final Runnable onRetry, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Check your internet ! ")
                .setMessage(message)
                .setNegativeButton("Oh well", null);

        if (onRetry != null) {
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onRetry.run();
                }
            });
        }

        builder.show();
    }

    public static void showYouBrokeItDialog(String message, Context context) {
        showNotConnected(message, null, context);
    }


}
