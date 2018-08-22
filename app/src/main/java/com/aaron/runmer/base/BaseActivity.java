package com.aaron.runmer.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


public class BaseActivity extends AppCompatActivity {

    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mContext = this;

        setStatusBar();

    }

    /**
     * To change status bar to transparent.
     *
     * @notice this method have to be used before setContentView.
     */
    private void setStatusBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);//calculateStatusColor(Color.WHITE, (int) alphaValue)
        }
    }

//    public void showUserInfoLog() {
//
//        Log.i(Constants.TAG, "---------------------User Info-------------------------");
//        Log.i(Constants.TAG, "User id: " + UserData.getInstance().getUserId());
//        Log.i(Constants.TAG, "User name: " + UserData.getInstance().getUserName());
//        Log.i(Constants.TAG, "User email: " + UserData.getInstance().getUserEmail());
//        Log.i(Constants.TAG, "User image: " + UserData.getInstance().getUserImage());
//        Log.i(Constants.TAG, "User token: " + UserData.getInstance().getUserToken());
//        Log.i(Constants.TAG, "-------------------------------------------------------");
//
//    }
}
