package com.example.word;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dahyeon on 2015. 9. 7..
 */
public class ListAdapter extends BaseAdapter{

    Context context;
    ArrayList<WordData> data;

    public ListAdapter(Context context, ArrayList<WordData> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {

        if(data == null) return 0;
        else return data.size();
    }

    @Override
    public Object getItem(int position) {

        if(data == null) return null;
        else return data.get(position);
    }

    @Override
    public long getItemId(int position) {

        if(data == null) return -1;
        else return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if(v == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            v = inflater.inflate(R.layout.cell_word_list, null);
        }

        TextView content = (TextView) v.findViewById(R.id.wordContent);

        if(data != null) {
            WordData wd = data.get(position);
            content.setText(wd.getTitle()+"\n"+wd.getMeans()+"\n"+wd.getCount()+"\n"+wd.getTime());
        }

        return v;
    }

    public ArrayList<WordData> getData() {
        return data;
    }

    public void setData(ArrayList<WordData> data) {
        this.data = data;
    }
}
