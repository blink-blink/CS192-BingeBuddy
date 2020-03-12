/*
This is a course requirement for CS 192
Software Engineering II under the
supervision of Asst. Prof. Ma. Rowena C.
Solamo of the Department of Computer
Science, College of Engineering, University
of the Philippines, Diliman for the AY 2015-
2016.
*/
//File Creation Date: 02/14/2020
//Software purpose: show class adapter for recyclerView

//changelog
//Update: Samuel Jose, 02/14/2020

package com.example.bingebuddy.ui.shows;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bingebuddy.MainActivity;
import com.example.bingebuddy.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import static android.app.PendingIntent.getActivity;

public class    ShowAdapter extends RecyclerView.Adapter<ShowAdapter.MyViewHolder>{
    private Context mContext;
    private List<Show> showList;

    public ShowAdapter(Context mContext, List<Show> showList){
        this.showList = showList;
        this.mContext = mContext;
    }

    @Override
    public ShowAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_show, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ShowAdapter.MyViewHolder viewHolder, int i){
        viewHolder.title.setText(showList.get(i).getTitle());

        //poster
        String poster = "https://image.tmdb.org/t/p/w500" + "/n6bUvigpRFqSwmPp1m2YADdbRBc.jpg";

        Glide.with(mContext)
                .load(poster)
                .placeholder(/*R.drawable.load*/null)
                .into(viewHolder.thumbnail);

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Show theRemovedItem = showList.get(i);
                // remove your item from data base
                showList.remove(i);  // remove the item from list
                notifyItemRemoved(i); // notify the adapter about the removed item
            }
        });
        //get data from database. ignore this for now
    }

    @Override
    public int getItemCount() {return showList.size();}

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title, language;
        public ImageView thumbnail;
        public Button deleteButton;

        public MyViewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.textViewTitle);
            language = (TextView) view.findViewById(R.id.textViewLanguage);
            thumbnail = (ImageView) view.findViewById((R.id.imageView));
            deleteButton = view.findViewById(R.id.deleteButton);
            //setup delete button


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
