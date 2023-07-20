package com.atillaertas.kotlincalculator.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.atillaertas.kotlincalculator.R;

import java.util.ArrayList;

public class PermissionHelper {

    private static final String TAG = PermissionHelper.class.getSimpleName();

    /** Check WRITE_EXTERNAL_STORAGE/READ_EXTERNAL_STORAGE/RECORD_AUDIO
     * permissions for gallery activity.
     * If all permissions are granted, return true.
     * If one of them is denied, request permissions and return false.
     * @param activity GalleryActivity
     * @return If all permissions are granted, return true.
     *         If one of them is denied, request permissions and return false.
     */
    public static boolean checkAndRequestForGallery(Activity activity) {
        // get permissions needed in current scenario
        ArrayList<String> permissionsNeeded = new ArrayList<String>();
        permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        // check status of permissions, get which permissions need to request
        ArrayList<String> permissionsNeedRequest = new ArrayList<String>();
        for (String permission : permissionsNeeded) {
            if (ContextCompat.checkSelfPermission(activity, permission)
                    == PackageManager.PERMISSION_GRANTED) {
                continue;
            }
            permissionsNeedRequest.add(permission);
        }
        // request permissions
        if (permissionsNeedRequest.size() == 0) {
            Log.i(TAG, "<checkAndRequestForGallery> all permissions are granted");
            return true;
        } else {
            Log.i(TAG, "<checkAndRequestForGallery> not all permissions are granted, reuqest");
            String[] permissions = new String[permissionsNeedRequest.size()];
            permissions = permissionsNeedRequest.toArray(permissions);
            ActivityCompat.requestPermissions(activity, permissions, 0);
            return false;
        }
    }

    /** Check if all permissions in String[] are granted.
     * @param permissions A group of permissions
     * @param grantResults The granted status of permissions
     * @return If all permissions are granted, return true, or else return false.
     */
    public static boolean isAllPermissionsGranted(String[] permissions, int[] grantResults) {
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Show toast after permission is denied.
     * @param context Current application environment
     */
    public static void showDeniedPrompt(Context context) {
        Toast.makeText(context, "denied",
                Toast.LENGTH_SHORT).show();
    }
}
