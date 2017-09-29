package com.example.phamanh.easyhotel.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.interfaces.DialogListener;
import com.example.phamanh.easyhotel.other.view.ConfirmDialog;
import com.example.phamanh.easyhotel.other.view.ConfirmListenerDialog;
import com.example.phamanh.easyhotel.other.view.SelectSinglePopup;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class AppUtils {
    private static String service;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void showAlert(Context context, String title, DialogListener clickListener) {
        ConfirmDialog dialog = new ConfirmDialog(context, title);
        dialog.setOnItemClickListener(clickListener);
        dialog.show();
    }

    public static void showAlertConfirm(Context context, String title, DialogListener clickListener) {
        ConfirmListenerDialog dialog = new ConfirmListenerDialog(context,"", title,"Cancel","Submit");
        dialog.setOnItemClickListener(clickListener);
        dialog.show();
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

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
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


    public static void showPickTime(Context context, final EditText tvDate, boolean isCheck) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {
            Calendar now = Calendar.getInstance();
            int yearCurrent = now.get(Calendar.YEAR);
            if (!isCheck) {
                if ((yearCurrent - year < 18))
                    showAlert(context, "You must 18 year old.", null);
                else
                    tvDate.setText(toConveMonth(dayOfMonth) + "-" + (toConveMonth(month + 1)) + "-" + year);
            } else
                tvDate.setText(toConveMonth(dayOfMonth) + "-" + (toConveMonth(month + 1)) + "-" + year);
        }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        if (!TextUtils.isEmpty(tvDate.getText().toString())) {
            String[] arrUpdateDatePicker = tvDate.getText().toString().split("-");
            datePickerDialog.updateDate(Integer.parseInt(arrUpdateDatePicker[2]), Integer.parseInt(arrUpdateDatePicker[1]) - 1, Integer.parseInt(arrUpdateDatePicker[0]));
        } else
            datePickerDialog.updateDate(calendar.get(Calendar.YEAR) - 18, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        if (isCheck)
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        else
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }

    public static boolean toCheck18YearOld(Context context, int year, int month, int day) {
        Calendar userAge = new GregorianCalendar(year, month, day);
        Calendar minAdultAge = new GregorianCalendar();
        minAdultAge.add(Calendar.YEAR, -18);
        if (minAdultAge.before(userAge)) {
            showAlert(context, "You must 18 year old.", null);
            return false;
        }
        return true;
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
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public static String convertTime(long time) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        return format.format(date);
    }

    public static byte[] changeBytetoBitMap(Bitmap bm) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, new ByteArrayOutputStream());
        return stream.toByteArray();
    }

    public static String toChangeBitmap(Bitmap bitmap) {
        return Base64.encodeToString(AppUtils.changeBytetoBitMap(AppUtils.getResizedBitmap(bitmap, 1080)), Base64.DEFAULT);
    }

    public static Bitmap toChangeString(String s) {
        return BitmapFactory.decodeByteArray(Base64.decode(s, Base64.DEFAULT), 0, Base64.decode(s, Base64.DEFAULT).length);
    }

    public static String getTimeAgo(long timestamp) {

        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();//get your local time zone.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        sdf.setTimeZone(tz);//set time zone.
        String localTime = sdf.format(new Date(timestamp));
        Date date = new Date();
        try {
            date = sdf.parse(localTime);//get local date
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date == null) {
            return null;
        }

        long time = date.getTime();

        Date curDate = currentDate();
        long now = curDate.getTime();
        if (time > now || time <= 0) {
            return null;
        }

        int timeDIM = getTimeDistanceInMinutes(time);

        String timeAgo;
        if (timeDIM < 1) {
            return "A few second.";
        } else if (timeDIM == 1) {
            return "1 minute";
        } else if (timeDIM >= 2 && timeDIM <= 59) {
            timeAgo = timeDIM + " minutes";
        } else if (timeDIM >= 60 && timeDIM <= 119) {
            timeAgo = "1 hour";
        } else if (timeDIM >= 120 && timeDIM <= 1439) {
            timeAgo = (Math.round(timeDIM / 60)) + " hours";
        } else if (timeDIM >= 1440 && timeDIM <= 2879) {
            timeAgo = "1 day";
        } else if (timeDIM >= 2880 && (Math.round(timeDIM / 1440)) < 7) {
            timeAgo = (Math.round(timeDIM / 1440)) + " days";
        } else timeAgo = convertTime(time);

        return (Math.round(timeDIM / 1440)) < 7 ? timeAgo + " ago" : timeAgo;
    }

    public static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    private static int getTimeDistanceInMinutes(long time) {
        long timeDistance = currentDate().getTime() - time;
        return Math.round((Math.abs(timeDistance) / 1000) / 60);
    }
}
