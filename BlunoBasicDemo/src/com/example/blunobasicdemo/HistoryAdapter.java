package com.example.blunobasicdemo;

import java.util.List;
import java.util.Random;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;


public class HistoryAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<RiverData> RiverDataItems;

    public HistoryAdapter(Activity activity, List<RiverData> RiverDataItems) {
        this.activity = activity;
        this.RiverDataItems = RiverDataItems;
    }

    @Override
    public int getCount() {
        return RiverDataItems.size();
    }

    @Override
    public Object getItem(int location) {
        return RiverDataItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        //so we probably want to change most of this

        ProgressBar health = (ProgressBar) convertView.findViewById(R.id.progressBar2);

        Random r = new Random();

        if(android.os.Build.VERSION.SDK_INT >= 11){
            ObjectAnimator animation = ObjectAnimator.ofInt(health, "progress", 0, r.nextInt(100));
            animation.setDuration(1000);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.start();
        }
        else
        	health.setProgress(r.nextInt(100));


//        health.setProgress(70);

        //health.setProgress(progress);
        TextView date = (TextView) convertView.findViewById(R.id.date);

        // getting RiverData data for the row
        RiverData data = RiverDataItems.get(position);

        if(date != null)

        date.setText("Reading taken on: " + data.readingDate);

        //do something...

        //set button listener

        //
        return convertView;
    }

}