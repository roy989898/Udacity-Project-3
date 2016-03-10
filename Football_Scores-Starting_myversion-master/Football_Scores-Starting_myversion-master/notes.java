//Today tomorrow yester day ...... each PagerFragment


//in each PagerFragment has a MainScreenFragment

//each MainScreenFragment has a kist view,it show the score and team



use DatabaseContract.scores_table.buildScoreWithDate() to query the data


   return new CursorLoader(getActivity(),DatabaseContract.scores_table.buildScoreWithDate(),
                null,null,fragmentdate,null);

DatabaseContract.scores_table.buildScoreWithDate() create :

"content://barqsoft.footballscores/date"


				
need the fragmentdate use in MainScreenFragment.

fragmentdate is come from setFragmentDate(String date)

setFragmentDate call at PageFragment


Date fragmentdate = new Date(System.currentTimeMillis() + ((i - 2) * 86400000));
            SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
            viewFragments[i] = new MainScreenFragment();
            viewFragments[i].setFragmentDate(mformat.format(fragmentdate));
			
			
//query:

return new CursorLoader(getActivity(),DatabaseContract.scores_table.buildScoreWithDate(),
                null,null,fragmentdate,null);
				
				
//how to change the day to the day name

getDayName(getActivity(), System.currentTimeMillis() + ((position - 2) * 86400000))

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
				
				