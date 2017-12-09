package com.fatec.labs.fatec_firebase.Common;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.net.URLEncoder;

public abstract class WhatsApp {
    public static void sendMessageToWhatsAppContact(Context context, String strNumber, String strMessage) {
        PackageManager packageManager = context.getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);
        try {
            String url = "https://api.whatsapp.com/send?phone=" + strNumber + "&text=" + URLEncoder.encode(strMessage, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                context.startActivity(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
