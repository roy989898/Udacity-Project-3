package barqsoft.footballscores.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import barqsoft.footballscores.R;

public class WidgetLeftButtonReceiver extends BroadcastReceiver {
    public static final String WidgetLeftButtonReceiver_ACTION = "barqsoft.footballscores.LEFT_ACTION";

    public WidgetLeftButtonReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //get the current day
        int wid = intent.getIntExtra(ScoreWidget.APPWIDGEIt_KEY, 0);
        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.sharedpreference_name), Context.MODE_PRIVATE);
        int day = sp.getInt(context.getString(R.string.sharedpreference_day_key) + wid, 2);
        day = decreaseTheDay(day);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(context.getString(R.string.sharedpreference_day_key) + wid, day);
        editor.commit();


        Log.d("widget", wid + "");
        Intent brodcastIntent = new Intent(ScoreWidget.BRODCAST_MESSAGE);
        brodcastIntent.putExtra(ScoreWidget.APPWIDGEIt_KEY, wid);
        context.sendBroadcast(brodcastIntent);//send out the brodcast ,ScoreWidget will receive,and load the day from sharedPreference again

    }

    private int decreaseTheDay(int i) {
        //day is 0-4,can't >=5
        int day = i - 1;
        if (day < 0) {
            return 4;
        } else {
            return day;
        }


    }
}
