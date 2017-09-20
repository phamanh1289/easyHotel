package com.example.phamanh.easyhotel.utils;
/*
 * Created by HoangDong on 25/07/2017.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.interfaces.DialogListener;
import com.example.phamanh.easyhotel.other.view.SelectSinglePopup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppUtils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void showAlert(final Context context, String title, String content, @Nullable final DialogListener listener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.setPositiveButton(R.string.accept, (dialogInterface, i) -> {
            if (listener != null) {
                listener.onConfirmClicked();
            }
        });
        builder.create().show();
    }

    public static String convertNumberOrdinal(int i) {
        String[] sufixes = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + sufixes[i % 10];

        }
    }

    public static void showAlertClaimStatus(final Context context, String title, String content, String confirm, String cancel, @Nullable final DialogListener listener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.setCancelable(false);
        builder.setPositiveButton(confirm, (dialogInterface, i) -> {
            if (listener != null) {
                listener.onConfirmClicked();
            }
        });
        builder.setNegativeButton(cancel, (dialog, which) -> {
            if (listener != null) {
                listener.onCancelClicked();
            }
        });
        builder.create().show();
    }

    public static boolean isURL(String url) {
        return Patterns.WEB_URL.matcher(url).matches();
    }

    public static String getAuthorization(String token) {
        return "Bearer " + token;
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static float convertDpToPx(final Context context, final int dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static void hidenStatusBar(Activity activity, int colorStatusBar, boolean isVisibility) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (isVisibility)
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            else
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().setStatusBarColor(colorStatusBar);
        }
    }

    public static String convertTimeStampToDate(long timeStamp) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date(timeStamp * 1000L));
    }

    public static long getFolderSize(File f) {
        long size = 0;
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                size += getFolderSize(file);
            }
        } else {
            size = f.length();
        }
        return size;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static String changeFile(String s) {
        String[] result = s.split("/");
        for (int i = 0; i < result.length; i++) {
            if (i == result.length - 1)
                return s = System.currentTimeMillis() + ".pdf";
            else
                return s += "/" + result[i] + "/";
        }

        return s;
    }


    public static String convertKeyToString(String keyNamePolicy) {
        String result = "";
        switch (keyNamePolicy) {
            case "ci_sum_assured":
                return "Critical Illness";
            case "tpd_sum_assured":
                return "Total Permanent Disability Sum Assured";
            case "early_ci_sum_assured":
                return "Early Critical Illness";
            case "insured_relation":
                return "Relationship";
            case "insured_dob":
                return "Date of Birth";
            case "first_name":
                return "First Name";
            case "last_name":
                return "Last Name";
            case "annual_premium_amount":
                return "Annual Premium Amount";
            case "accident_death_sa":
                return "Accidental Death S.A";
            case "tcm_benfit":
                return "TCM Benfit";
            case "singe_premium":
                return "Single Premium";
            case "annual_payment_amt":
                return "Annual Payment AMT";
        }
        if (TextUtils.isEmpty(result)) {
            result = keyNamePolicy.replaceAll("_", " ");
            result = UppercaseFirstLetters(result);
        }
        return result;
    }

    public static String UppercaseFirstLetters(String str) {
        boolean prevWasWhiteSp = true;
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (Character.isLetter(chars[i])) {
                if (prevWasWhiteSp) {
                    chars[i] = Character.toUpperCase(chars[i]);
                }
                prevWasWhiteSp = false;
            } else {
                prevWasWhiteSp = Character.isWhitespace(chars[i]);
            }
        }
        return new String(chars);
    }

    public static String formatMoney(Double money) {
        NumberFormat dutchFormat = NumberFormat.getCurrencyInstance(Locale.US);
        String twoDecimals = dutchFormat.format(money);
        if (twoDecimals.matches(".*[.]...[,]00$")) {
            String zeroDecimals = twoDecimals.substring(0, twoDecimals.length() - 3);
            return zeroDecimals;
        }
        if (twoDecimals.endsWith(",00")) {
            String zeroDecimals = String.format("€ %.0f,-", money);
            return zeroDecimals;
        } else {
            return twoDecimals;
        }
    }

    public static void showPickTime(Context context, TextView tvDate) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                (view, year, monthOfYear, dayOfMonth) -> tvDate.setText(toConveMonth(dayOfMonth) + "-" + (toConveMonth(monthOfYear + 1)) + "-" + year), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private static String toConveMonth(int month) {
        if (month < 10)
            return "0" + month;
        return String.valueOf(month);
    }

    public static void toGetPopup(Context context, View view, SelectSinglePopup singlePopup, List<String> mData, TextView textView) {
        if (singlePopup == null) {
            singlePopup = new SelectSinglePopup(context, mData, false);
        }
        SelectSinglePopup finalSinglePopup = singlePopup;
        singlePopup.setOnItemListener(pos -> {
            textView.setText(mData.get(pos));
            textView.setTextColor(ContextCompat.getColor(context, R.color.denimBlue));
            finalSinglePopup.dismiss();
        });
        singlePopup.showAsDropDown(view);
    }

    public static boolean toDoCheckDate(String start, String end) {
        if (start.isEmpty() && end.isEmpty()) {
            return true;
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            try {
                return !start.isEmpty() && !end.isEmpty() && (formatter.parse(start).before(formatter.parse(end)) || formatter.parse(start).equals(formatter.parse(end)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}
