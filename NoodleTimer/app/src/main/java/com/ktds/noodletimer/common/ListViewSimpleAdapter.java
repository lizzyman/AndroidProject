package com.ktds.noodletimer.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class ListViewSimpleAdapter extends BaseAdapter {

    private Context context;
    private List list;
    private int layoutResource;

    public ListViewSimpleAdapter(Context context, int layoutResource, List list) {
        this.context = context;
        this.list = list;
        this.layoutResource = layoutResource;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;

        if ( convertView == null ) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutResource, parent, false);
            holder = createHolder(convertView);

            convertView.setTag(holder);
        }
        else {
            holder = (Holder) convertView.getTag();
        }

        setContent(holder, position);

        return convertView;
    }

    public abstract Holder createHolder(View view);

    public abstract void setContent(Holder holder, int position);

    public static class Holder {

        public Holder(View v) {

        }

    }
}