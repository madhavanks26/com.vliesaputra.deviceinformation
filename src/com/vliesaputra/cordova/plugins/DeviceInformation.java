package com.vliesaputra.cordova.plugins;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import android.app.AlertDialog;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.util.Log;
public class DeviceInformation extends CordovaPlugin {
private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    private String checkValue(String str) {
        Log.i("C","Check>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        if ((str == null) || (str.length() == 0)) {
            return "\"TM.ERROR\"";
        }

        return "\"" + str + "\"";
    }

    private String getAccount(AccountManager am) {
        Log.i("C","Account>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        String str = "";

        if (am != null) {
            Account[] accounts = am.getAccounts();

            for (int i = 0; i < accounts.length; i++) {
                if (str.length() > 0) {
                    str += ",";
                }

                str += "\"account" + i + "Name\": " + checkValue(accounts[i].name) + ","
                        + "\"account" + i + "Type\": " + checkValue(accounts[i].type);
            }
        }

        return str;
    }

    private String getTelephone(TelephonyManager tm) {
        Log.i("C","telephony>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        String str = "";

        if (tm != null) {
            str = "\"deviceID\": " + checkValue(tm.getDeviceId()) + ","
                    + "\"phoneNo\": " + checkValue(tm.getLine1Number()) + ","
                    + "\"netCountry\": " + checkValue(tm.getNetworkCountryIso()) + ","
                    + "\"netName\": " + checkValue(tm.getNetworkOperatorName()) + ","
                    + "\"simNo\": " + checkValue(tm.getSimSerialNumber()) + ","
                    + "\"simCountry\": " + checkValue(tm.getSimCountryIso()) + ","
                    + "\"simName\": " + checkValue(tm.getSimOperatorName());
        }

        return str;
    }

    private String getDetails(TelephonyManager tm, AccountManager am) {
        Log.i("C","getDetails>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        String acc = getAccount(am);
        String tel = getTelephone(tm);

        String str = "";
        if ((acc.length() != 0) || (tel.length() != 0)) {
            str += "{" + acc;

            if (str.length() > 1) {
                str += ",";
            }

            str += tel + "}";
        }

        return str;
    }

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {     

    // Check if the READ_PHONE_STATE permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // READ_PHONE_STATE permission has not been granted.
            Log.i("ask","6.0");
            requestReadPhoneStatePermission();
        } else {
            // READ_PHONE_STATE permission is already been granted.
            Log.i("Granted","Granted Permission");
            doPermissionGrantedStuffs();
        }


               
    }
    /**
     * Requests the READ_PHONE_STATE permission.
     * If the permission has been denied previously, a dialog will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestReadPhoneStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            new AlertDialog.Builder(this.cordova.getActivity())
                    .setTitle("Permission Request!........")
                    .setMessage("reading phone state")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(this.cordova.getActivity(),
                                    new String[]{Manifest.permission.READ_PHONE_STATE},
                                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                        }
                    })
                    .show();
        } else {
            // READ_PHONE_STATE permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {
            // Received permission result for READ_PHONE_STATE permission.est.");
            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // READ_PHONE_STATE permission has been granted, proceed with displaying IMEI Number
                //alertAlert(getString(R.string.permision_available_read_phone_state));
                doPermissionGrantedStuffs();
            } else {
                alertAlert("Kindly Allow to proceed further");
            }
        }
    }

    private void alertAlert(String msg) {
        new AlertDialog.Builder(this.cordova.getActivity())
                .setTitle("Permission Request")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do somthing here
                        System.exit(0);
                    }
                })
                .show();
    }

    
}
