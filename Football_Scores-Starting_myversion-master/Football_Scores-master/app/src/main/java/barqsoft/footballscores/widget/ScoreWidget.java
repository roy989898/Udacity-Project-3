package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.text.format.Time;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;

import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;

/**
 * Implementation of App Widget functionality.
 */
public class ScoreWidget extends AppWidgetProvider {
    final static public String BRODCAST_MESSAGE = "barqsoft.footballscores.WIDGET_UPDATE_ACTION";
    final static public String APPWIDGEIt_KEY = "W_KEY";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_score_layout);
        //set the click action

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_bar, pendingIntent);
//        views.setTextViewText(R.id.appwidget_text, widgetText);
        //set the array adapter
        Intent brodcastIntent = new Intent(context, ScoreWidgetService.class);
        // Add the app widget ID to the intent extras.
        brodcastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        brodcastIntent.setData(Uri.parse(brodcastIntent.toUri(Intent.URI_INTENT_SCHEME)));

        //set the day number that want  to show in the widget,show ScoreWidgetService can know what day to show
        int day = getTheDay(context, appWidgetId);
        putTheDay(context,appWidgetId,day);
//        brodcastIntent.putExtra(context.getString(R.string.brodcastIntent_get_day_key),day);
        Log.d("onReceive_ScoreWidget", day + "");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            views.setRemoteAdapter(R.id.widget_listview_score, brodcastIntent);
        }


        //et the left button
        Intent leftintent = new Intent(WidgetLeftButtonReceiver.WidgetLeftButtonReceiver_ACTION);
        Log.d("widgetID", appWidgetId + "");
        leftintent.putExtra(APPWIDGEIt_KEY, appWidgetId);
        PendingIntent leftpendingIntent = PendingIntent.getBroadcast(context, appWidgetId, leftintent, 0);
        views.setOnClickPendingIntent(R.id.widget_im_left, leftpendingIntent);

        // set the right bytton
        Intent rightintent = new Intent(WidgetRightButtonReceiver.WidgetRightButtonReceiver_ACTION);
        rightintent.putExtra(APPWIDGEIt_KEY, appWidgetId);
        PendingIntent rightpendingIntent = PendingIntent.getBroadcast(context, appWidgetId, rightintent, 0);
        views.setOnClickPendingIntent(R.id.widget_im_right, rightpendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /*    get the day from the share Preference  that we want to show 0-4
        default is 2(today)*/
    private static int getTheDay(Context context, int appWidgetId) {
        //get the day that we want to show 0-4
        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.sharedpreference_name), Context.MODE_PRIVATE);
        //default is 2(today)

        return sp.getInt(context.getString(R.string.sharedpreference_day_key) + appWidgetId, 2);
    }

    private static void putTheDay(Context context, int appWidgetId, int day) {
        //get the day that we want to show 0-4
        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.sharedpreference_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(context.getString(R.string.sharedpreference_day_key) + appWidgetId, day);
        editor.commit();

    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);


        AppWidgetManager awm = AppWidgetManager.getInstance(context);
        int id = intent.getIntExtra(ScoreWidget.APPWIDGEIt_KEY, -101);
        Log.d("widget", "id " + id);
        int[] ids;
        if (id == -101) {
            //the default value,update all
            ids = awm.getAppWidgetIds(new ComponentName(context, ScoreWidget.class));
        } else {
            //only the specific one
            ids = new int[1];
            ids[0] = id;
        }

//        onUpdate(context, awm, ids);
        awm.notifyAppWidgetViewDataChanged(ids, R.id.widget_listview_score);

        //only update the bar title in the remoteview
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_score_layout);
        int day = getTheDay(context, id);
        String datName = getDayName(context, System.currentTimeMillis() + ((day - 2) * 86400000));
        views.setTextViewText(R.id.widget_textViewDate, datName);
        awm.partiallyUpdateAppWidget(id, views);

    }

    public String getDayName(Context context, long dateInMillis) {
        // If the date is today, return the localized version of "Today" instead of the actual
        // day name.

        Time t = new Time();
        t.setToNow();
        int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
        int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
        if (julianDay == currentJulianDay) {
            return context.getString(R.string.today);
        } else if (julianDay == currentJulianDay + 1) {
            return context.getString(R.string.tomorrow);
        } else if (julianDay == currentJulianDay - 1) {
            return context.getString(R.string.yesterday);
        } else {
            Time time = new Time();
            time.setToNow();
            // Otherwise, the format is just the day of the week (e.g "Wednesday".
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            return dayFormat.format(dateInMillis);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        for (int appWidgetId : appWidgetIds) {

            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


}




