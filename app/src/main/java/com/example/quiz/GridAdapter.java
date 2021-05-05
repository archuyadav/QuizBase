package com.example.quiz;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class GridAdapter extends BaseAdapter {

    private List<String> List;

    public GridAdapter(java.util.List<String> list) {
        List = list;
    }

    @Override
    public int getCount() {
        return List.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v;
        if (convertView==null){
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item,parent,false);
        }
        else{
            v=convertView;
        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(parent.getContext(),SetsActivity.class);
                intent.putExtra("CATEGORY",List.get(position));
                intent.putExtra("CATEGORY_ID",position+1);
                parent.getContext().startActivity(intent);
            }
        });

        ((TextView) v.findViewById(R.id.catName)).setText(List.get(position));

        Random rnd=new Random();
        int color= Color.argb(255,rnd.nextInt(255),rnd.nextInt(255),rnd.nextInt(255));
        v.setBackgroundColor(color);

        return v;
    }
}
