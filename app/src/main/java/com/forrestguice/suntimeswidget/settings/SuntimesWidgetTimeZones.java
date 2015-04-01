package com.forrestguice.suntimeswidget.settings;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.forrestguice.suntimeswidget.R;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class SuntimesWidgetTimeZones
{
    private static ArrayList<TimeZoneItem> timezones = new ArrayList<>();
    private static boolean initialized = false;

    private static void initTimezoneList()
    {
        timezones.clear();

        String[] allTimezoneValues = TimeZone.getAvailableIDs();
        for (int i = 0; i < allTimezoneValues.length; i++)
        {
            TimeZone timezone = TimeZone.getTimeZone(allTimezoneValues[i]);
            timezones.add(new TimeZoneItem(timezone.getID(), timezone.getDisplayName()));
        }

        initialized = true;
    }

    public static int ordinal( String timezoneID )
    {
        if (!initialized)
        {
            initTimezoneList();
        }

        timezoneID = timezoneID.trim();

        int ord = -1;
        for (int i=0; i<timezones.size(); i++)
        {
            String otherID = timezones.get(i).getID().trim();
            if (timezoneID.equals(otherID))
            {
                ord = i;
                break;
            }
        }
        return ord;
    }

    public static TimeZoneItem[] values()
    {
        if (!initialized)
        {
            initTimezoneList();
        }

        int numTimeZones = timezones.size();
        TimeZoneItem[] retArray = new TimeZoneItem[numTimeZones];
        for (int i=0; i<numTimeZones; i++)
        {
            retArray[i] = timezones.get(i);
        }
        return retArray;
    }

    public static List<TimeZoneItem> getValues()
    {
        if (!initialized)
        {
            initTimezoneList();
        }

        return timezones;
    }

    ///////////////////////////////////////
    ///////////////////////////////////////

    public static class TimeZoneItem
    {
        private String timeZoneID;
        private String displayString;

        public TimeZoneItem(String timeZoneID, String displayString)
        {
            this.timeZoneID = timeZoneID;
            this.displayString = displayString;
        }

        public String getID()
        {
            return timeZoneID;
        }

        public String getDisplayString()
        {
            return displayString;
        }

        public String toString()
        {
            StringBuilder retString = new StringBuilder(timeZoneID);
            retString.append(" (");
            retString.append(displayString);
            retString.append(")");
            return retString.toString();
        }
    }

    ///////////////////////////////////////
    ///////////////////////////////////////

    public static class TimeZoneItemAdapter extends ArrayAdapter<TimeZoneItem>
    {
        public TimeZoneItemAdapter(Context context, int textViewResourceId)
        {
            super(context, textViewResourceId);
        }

        public TimeZoneItemAdapter(Context context, int resource, List<TimeZoneItem> items)
        {
            super(context, resource, items);
        }

        private View getItemView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view = layoutInflater.inflate(R.layout.layout_listitem_twoline, parent, false);

            TimeZoneItem timezone = getItem(position);
            if (timezone == null)
            {
                Log.w("getItemView", "timezone at position " + position + " is null.");
                return view;
            }

            TextView primaryText = (TextView)view.findViewById(android.R.id.text1);
            primaryText.setText( timezone.getID() );

            TextView secondaryText = (TextView)view.findViewById(android.R.id.text2);
            if (secondaryText != null)
            {
                secondaryText.setText( timezone.getDisplayString() );
            }

            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent)
        {
            return getItemView(position, convertView, parent);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            return getItemView(position, convertView, parent);
        }
    }
}