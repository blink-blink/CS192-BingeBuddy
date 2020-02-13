/*
This is a course requirement for CS 192
Software Engineering II under the
supervision of Asst. Prof. Ma. Rowena C.
Solamo of the Department of Computer
Science, College of Engineering, University
of the Philippines, Diliman for the AY 2015-
2016.
*/
package com.example.bingebuddy.ui.shows;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bingebuddy.MainActivity;
import com.example.bingebuddy.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import static android.app.PendingIntent.getActivity;

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.MyViewHolder>{
    //private Context mContext;
    private List<Show> showList;

    public ShowAdapter(List<Show> showList){
        this.showList = showList;
    }

    @Override
    public ShowAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_show, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ShowAdapter.MyViewHolder viewHolder, int i){
        //get data from database. ignore this for now
    }

    @Override
    public int getItemCount() {return showList.size();}

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title, language;
        public ImageView thumbnail;

        public MyViewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.textViewTitle);
            language = (TextView) view.findViewById(R.id.textViewLanguage);
            ImageView thumbnail = (ImageView) view.findViewById((R.id.imageView));

            //set up on click listener
            view.setOnClickListener(new View.OnClickListener(){
                //get details
                @Override
                public void onClick(View v){

                }
            });

            //set up onlongclicklistener for edit mode
            view.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    //edit mode toolbar
                    Snackbar.make(v, "EDIT MODE", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return true;
                }
            });
        }
    }
}