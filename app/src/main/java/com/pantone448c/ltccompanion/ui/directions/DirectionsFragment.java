package com.pantone448c.ltccompanion.ui.directions;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.pantone448c.ltccompanion.R;

import java.util.ArrayList;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
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
        TransitTrip trip = RouteBuilder.getDirections(startAddress.getText().toString(), endAddress.getText().toString());
        Step[] steps = convertSteps(trip.routes[0].legs[0].steps);
        adapter.setSteps(steps);
    }

    private Step[] convertSteps(Step[] steps)
    {
        ArrayList<Step> newSteps = new ArrayList<>();
        for (int i=0; i<steps.length; ++i)
        {
            steps[i].html_instructions = Html.fromHtml(steps[i].html_instructions, Html.FROM_HTML_MODE_COMPACT, null, null).toString();
            newSteps.add(steps[i]);
            if (steps[i].getClass().equals(WalkingStep.class))
            {
                WalkingStep step = (WalkingStep)steps[i];
                for (int n=0; n<step.steps.length; ++n)
                {
                    step.steps[n].html_instructions = Html.fromHtml(step.steps[n].html_instructions, Html.FROM_HTML_MODE_COMPACT, null, null).toString();
                    newSteps.add(step.steps[n]);
                }
            }
        }
        Step[] arraySteps = new Step[newSteps.size()];
        arraySteps = newSteps.toArray(arraySteps);
        return arraySteps;
    }

}
