package com.israel.livraisonexpresspos.uiComponents;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.israel.livraisonexpresspos.R;

/**
 * Created by Israel MEKOMOU [06/07/2020].
 */
public class Utilities {
    public static void hideKeyBoard(Activity activity){
        InputMethodManager methodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null){
            view = new View(activity);
        }
        methodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static ProgressDialog getProgressDialog(Context context){
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(context.getString(R.string.loading));
        dialog.setCancelable(false);
        dialog.create();
        return dialog;
    }

    public static void showAlertDialog(Activity activity, int icon, String title, String message, boolean cancelable){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setIcon(icon)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        if (!activity.isFinishing())builder.create().show();
    }

    public static void displayImageFromUrl(Context context, String url, ImageView imageView){
        Glide.with(context)
                .load(url)
                .into(imageView);
    }

    public static void displayImageFromUrlWithError(Context context, String url, ImageView imageView){
        Glide.with(context)
                .load(url)
                .error(R.drawable.logo_module_coursier)
                .into(imageView);
    }

    public static void setCheckedStep(Context context,  RelativeLayout layout) {
        layout.removeAllViews();
        ImageButton img = new ImageButton(context);
        img.setImageResource(R.drawable.ic_round_done_24);
        img.setBackgroundColor(Color.TRANSPARENT);
        img.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        layout.addView(img);
    }

    public static void setBW(boolean set, ImageView imageView){
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        imageView.setColorFilter(set ? filter : null);
    }

    //[Start : added by Marlonne]
    public static void showForwardTransition(Activity activity) {
        activity.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }

    public static void showBackwardTransition(Activity activity) {
        activity.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }
    //[END : added by Marlonne]
}
