package com.example.quiz;


 import android.content.Intent;
 import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import  android.content.Intent;
public class SetsAdapter extends BaseAdapter {

private int numOfSets;

    public SetsAdapter(int numOfSets) {
        this.numOfSets = numOfSets;
    }

    @Override
    public int getCount() {
        return numOfSets;
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
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.set_grid,parent,false);

        }else {
            v = convertView;
        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(parent.getContext(),QuestionActivity.class);
                i.putExtra("SETNO",position+1);
                parent.getContext().startActivity(i);
            }
        });

        ((TextView) v.findViewById(R.id.setNum)).setText(String.valueOf(position+1));


        return v;
    }
}
