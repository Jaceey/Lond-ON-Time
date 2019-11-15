package com.pantone448c.ltccompanion.ui.savedstops;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pantone448c.ltccompanion.R;
import com.pantone448c.ltccompanion.SavedStopsAdapter;
import com.pantone448c.ltccompanion.Stop;
import com.pantone448c.ltccompanion.viewmodels.StopViewModel;

import java.util.ArrayList;
import java.util.List;

public class SavedStopsFragment extends Fragment {
    private Context context;
    private RecyclerView recyclerView;
    private SavedStopsAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private View stopsFragmentView;
    public static StopViewModel stopViewModel;

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        stopsFragmentView = inflater.inflate(R.layout.fragment_savedstops, container, false);
        recyclerView = stopsFragmentView.findViewById(R.id.stopsRecycler);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new SavedStopsAdapter();
        stopViewModel = new ViewModelProvider(getActivity()).get(StopViewModel.class);
        stopViewModel.getStops().observe(getActivity(), new Observer<List<Stop>>() {
            @Override
            public void onChanged(@Nullable final List<Stop> stops) {
                mAdapter.setStops(stops);
            }
        });

        recyclerView.setAdapter(mAdapter);
        return stopsFragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

    }
}
