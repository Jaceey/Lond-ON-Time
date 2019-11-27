package com.pantone448c.ltccompanion.ui.routes;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.pantone448c.ltccompanion.R;
import com.pantone448c.ltccompanion.RouteListAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static android.provider.Telephony.Mms.Part.FILENAME;

public class RoutesFragment extends Fragment {
    private Context context;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private View routesFragmentView;
    private ImageButton imageBtn;
    private Button othBtn;
    private Button oneBtn;
    
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
        routesFragmentView = inflater.inflate(R.layout.fragment_routes, container, false);
        recyclerView = routesFragmentView.findViewById(R.id.routesRecycler);
        imageBtn = routesFragmentView.findViewById(R.id.imageView);
        othBtn = routesFragmentView.findViewById(R.id.buttonDir1);
        oneBtn = routesFragmentView.findViewById(R.id.buttonDir2);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new RouteListAdapter(new RouteListAdapter.RouteListAdapterListener() {
            @Override
            public void ImageBtnOnClick(View v, int position) {
                Toast.makeText(getContext(), "Image Button Clicked at Position: " + Integer.toString(position), Toast.LENGTH_LONG);

            }

            @Override
            public void ButtonDirectionMain(View v, int position) {
                Toast.makeText(getContext(), "MAIN Direction Clicked at Position: " + Integer.toString(position), Toast.LENGTH_LONG);
            }

            @Override
            public void ButtonDirectionOther(View v, int position) {
                Toast.makeText(getContext(), "OTHER Direction Clicked at Position: " + Integer.toString(position), Toast.LENGTH_LONG);
            }
        });
        recyclerView.setAdapter(mAdapter);

        return routesFragmentView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

    }



}
