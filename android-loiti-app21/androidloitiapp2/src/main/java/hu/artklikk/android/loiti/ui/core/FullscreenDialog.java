package hu.artklikk.android.loiti.ui.core;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by garik on 2013.10.25..
 */
public class FullscreenDialog extends Dialog {


    public FullscreenDialog(Context context) {
        super(context, android.R.style.Theme_NoTitleBar);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Making sure there's no title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Making dialog content transparent.
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Removing window dim normally visible when dialog are shown.
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);


    }
}
