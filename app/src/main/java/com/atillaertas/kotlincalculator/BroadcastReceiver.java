package com.atillaertas.kotlincalculator;

import android.content.Context;
import android.content.Intent;

public class BroadcastReceiver extends android.content.BroadcastReceiver {
    static final String action_boot = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(action_boot)){
            Intent ootStartIntent = new Intent(context,real_time_graph.class);
            ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(ootStartIntent);
        }
    }
}
