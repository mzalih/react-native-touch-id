package com.rnfingerprint;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.facebook.react.bridge.ReadableMap;

public class FingerprintNonDialog  implements FingerprintHandler.Callback {

    private FingerprintManager.CryptoObject mCryptoObject;
    private FingerprintDialog.DialogResultListener dialogCallback;
    private FingerprintHandler mFingerprintHandler;
    private boolean isAuthInProgress;

    private ImageView mFingerprintImage;
    private TextView mFingerprintSensorDescription;
    private TextView mFingerprintError;

    private String authReason;
    private int imageColor = 0;
    private int imageErrorColor = 0;
    private String dialogTitle = "";
    private String cancelText = "";
    private String sensorDescription = "";
    private String sensorErrorDescription = "";
    private String errorText = "";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void show(Context ctx, String tag){
        this.mFingerprintHandler = new FingerprintHandler(ctx, this);
        this.mFingerprintHandler.startAuth(mCryptoObject);
    }


    public void setCryptoObject(FingerprintManager.CryptoObject cryptoObject) {
        this.mCryptoObject = cryptoObject;
    }

    public void setDialogCallback(FingerprintDialog.DialogResultListener newDialogCallback) {
        this.dialogCallback = newDialogCallback;
    }

    public void setReasonForAuthentication(String reason) {
        this.authReason = reason;
    }

    public void setAuthConfig(final ReadableMap config) {
        if (config == null) {
            return;
        }

        if (config.hasKey("title")) {
            this.dialogTitle = config.getString("title");
        }

        if (config.hasKey("cancelText")) {
            this.cancelText = config.getString("cancelText");
        }

        if (config.hasKey("sensorDescription")) {
            this.sensorDescription = config.getString("sensorDescription");
        }

        if (config.hasKey("sensorErrorDescription")) {
            this.sensorErrorDescription = config.getString("sensorErrorDescription");
        }

        if (config.hasKey("imageColor")) {
            this.imageColor = config.getInt("imageColor");
        }

        if (config.hasKey("imageErrorColor")) {
            this.imageErrorColor = config.getInt("imageErrorColor");
        }
    }

    @Override
    public void onAuthenticated() {
        this.isAuthInProgress = false;
        this.dialogCallback.onAuthenticated();
    }

    @Override
    public void onError(String errorString, int errorCode) {
        this.mFingerprintHandler.endAuth();
        this.dialogCallback.onError(errorString,errorCode);
    }
    public void stop() {
        this.isAuthInProgress = false;
        this.mFingerprintHandler.endAuth();
    }
    @Override
    public void onCancelled() {
        this.isAuthInProgress = false;
        this.mFingerprintHandler.endAuth();
        this.dialogCallback.onCancelled();
    }
}
