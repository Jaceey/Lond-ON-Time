package com.pantone448c.ltccompanion.ui.directions;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.pantone448c.ltccompanion.R;

public class DirectionsFragment extends Fragment {

    private Context context;
    private ImageButton getDirectionsBtn;
    private EditText startAddress;
    private EditText endAddress;
    private RecyclerView recyclerView;
    private DirectionsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_directions, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onStart()
    {
        super.onStart();
        adapter = new DirectionsAdapter();
        recyclerView = getActivity().findViewById(R.id.directions);
        recyclerView.setAdapter(adapter);
        startAddress = getActivity().findViewById(R.id.startAddress);
        endAddress = getActivity().findViewById(R.id.endAddress);
        getDirectionsBtn = getActivity().findViewById(R.id.submitSearch);
        getDirectionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDirections();
            }
        });
    }

    private void getDirections()
    {
        adapter.setTrip(RouteBuilder.getDirections(startAddress.getText().toString(), endAddress.getText().toString()));
    }

}
