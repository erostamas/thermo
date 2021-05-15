package com.erostamas.thermo.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.erostamas.thermo.R;
import com.erostamas.thermo.UdpMessage;
import com.erostamas.thermo.UdpRequestResponse;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        final Handler handler = new Handler();
        final int delay = 3000; // 1000 milliseconds == 1 second

        handler.postDelayed(new Runnable() {
            class GaugeSetter implements UdpRequestResponse.ResponseHandler {
                private Gauge _gauge;

                GaugeSetter(Gauge gauge) {
                    this._gauge = gauge;
                }

                @Override
                public void handleResponse(String response) {
                    _gauge.set(Double.parseDouble(response));
                }
            };
            public void run() {
                final Gauge gauge = root.findViewById(R.id.current_temp);
                UdpRequestResponse x = new UdpRequestResponse(new GaugeSetter(gauge));
                x.execute(new UdpMessage("192.168.1.68", 50001, "hello"));
                handler.postDelayed(this, delay);
            }
        }, delay);
        return root;
    }
}