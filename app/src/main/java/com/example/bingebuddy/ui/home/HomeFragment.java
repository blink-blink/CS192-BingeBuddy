/*
This is a course requirement for CS 192
Software Engineering II under the
supervision of Asst. Prof. Ma. Rowena C.
Solamo of the Department of Computer
Science, College of Engineering, University
of the Philippines, Diliman for the AY 2015-
2016.
*/
package com.example.bingebuddy.ui.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bingebuddy.MainActivity;
import com.example.bingebuddy.R;
import com.example.bingebuddy.ui.shows.Show;
import com.example.bingebuddy.ui.shows.ShowAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView                mRecyclerView;
    private RecyclerView.Adapter        mAdapter;
    private SwipeRefreshLayout          swipeContainer;
    private View root;
    ProgressDialog pd;
    //private RecyclerView.LayoutManager  mLayoutManager;

    //private HomeViewModel homeViewModel;
    public HomeFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        initShows();

        swipeContainer = root.findViewById(R.id.swiperefresh);
        swipeContainer.setColorSchemeResources(R.color.colorPrimaryDark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initShows();
                Toast.makeText(getActivity(),"Watchlist Refreshed", Toast.LENGTH_SHORT).show();
                swipeContainer.setRefreshing(false);
                //fetchTimelineAsync(0);
            }
        });
        return root;
    }

    private void initShows(){
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Fetching shows...");
        pd.setCancelable(false);
        pd.show();

        List<Show> showList = new ArrayList<>();
        showList.add(new Show(1,"Captain Marvel","A really awesome movie"));
        showList.add(new Show(1,"Captain Marvel","A really awesome movie"));
        showList.add(new Show(1,"Captain Marvel","A really awesome movie"));

        mRecyclerView = root.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ShowAdapter(showList);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        pd.dismiss();

        //loadDatabase();
    }

    private void loadDatabase(){
        if (swipeContainer.isRefreshing())
            swipeContainer.setRefreshing(false);
    }
}