/*
This is a course requirement for CS 192
Software Engineering II under the
supervision of Asst. Prof. Ma. Rowena C.
Solamo of the Department of Computer
Science, College of Engineering, University
of the Philippines, Diliman for the AY 2015-
2016.
*/
//File Creation Date: 01/30/2020
//Software purpose: handles data for suggestions section

//changelog
//Update: Samuel Jose, 01/30/2020

package com.example.bingebuddy.ui.suggestions;

import android.app.ProgressDialog;
import android.content.res.Configuration;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bingebuddy.BuildConfig;
import com.example.bingebuddy.MainActivity;
import com.example.bingebuddy.R;
import com.example.bingebuddy.ui.api.ApiServices;
import com.example.bingebuddy.ui.api.TmdbClient;
import com.example.bingebuddy.ui.shows.Movie;
import com.example.bingebuddy.ui.shows.MoviesAdapter;
import com.example.bingebuddy.ui.shows.MoviesResponse;
import com.example.bingebuddy.ui.shows.Show;
import com.example.bingebuddy.ui.shows.ShowAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuggestionsFragment extends Fragment {
    private String accessToken;
    private String clientId;
    private SuggestionsViewModel suggestionsViewModel;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter        mAdapter;
    private SwipeRefreshLayout swipeContainer;
    private View root;
    ProgressDialog pd;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accessToken  = ((MainActivity)getActivity()).getAccessToken();
        clientId  = ((MainActivity)getActivity()).getClientId();

        root = inflater.inflate(R.layout.fragment_suggestions, container, false);
        initItems();

        swipeContainer = root.findViewById(R.id.swiperefresh_suggestions);
        swipeContainer.setColorSchemeResources(R.color.colorPrimaryDark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initItems();
                Toast.makeText(getActivity(),"Suggestions Refreshed", Toast.LENGTH_SHORT).show();
                swipeContainer.setRefreshing(false);
                //fetchTimelineAsync(0);
            }
        });


        return root;
    }

    private void initItems(){
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Fetching shows...");
        pd.setCancelable(false);
        pd.show();

        List<Movie> MovieList = new ArrayList<>();

        mRecyclerView = root.findViewById(R.id.recyclerView_suggestions);
        //mRecyclerView.setHasFixedSize(true);
        mAdapter = new MoviesAdapter(getContext(),MovieList);

        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),4));
        }
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        //pd.dismiss();

        loadJSON();
    }

    public void loadJSON(){

        try{
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()){
                pd.dismiss();
                return;
            }

            TmdbClient tmdbClient = new TmdbClient();
            ApiServices apiService = tmdbClient.getClient().create(ApiServices.class);

            Call<MoviesResponse> call = apiService.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {

                    List<Movie> movies = response.body().getResults();

                    //mRecyclerView.setHasFixedSize(true);
                    mAdapter = new MoviesAdapter(getContext(), movies);
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.smoothScrollToPosition(0);

                    if (swipeContainer.isRefreshing()){
                        swipeContainer.setRefreshing(false);
                    }
                    pd.dismiss();
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    if (t instanceof IOException) {
                        Toast.makeText(getContext(), "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        // logging probably not necessary
                    }
                    else {
                        Toast.makeText(getContext(), "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                        // todo log to some central bug tracking service
                    }
                    pd.dismiss();
                }
            });
        }catch (Exception e){
            //some error stuff
            Toast.makeText(getContext(),"ERROR!",Toast.LENGTH_SHORT).show();
        }
    }
}