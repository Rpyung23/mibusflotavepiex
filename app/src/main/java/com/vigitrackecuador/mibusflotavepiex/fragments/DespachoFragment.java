package com.vigitrackecuador.mibusflotavepiex.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.vigitrackecuador.mibusflotavepiex.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DespachoFragment extends Fragment {
    BottomBar bottomBar;

    public DespachoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_despacho, container, false);
        bottomBar = view.findViewById(R.id.bottombar_despacho);
        bottomBar.setDefaultTab(R.id.tab_vueltas);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                switch (tabId) {
                    case R.id.tab_vueltas:
                        despachoVueltasFragment odv = new despachoVueltasFragment();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contenedor_despacho, odv)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null).commit();
                        break;
                    case R.id.tab_time:
                        DespachoHorasFragment odh = new DespachoHorasFragment();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contenedor_despacho,odh)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null).commit();
                        break;
                }
            }
        });
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(int tabId)
            {
                switch (tabId) {
                    case R.id.tab_vueltas:
                        despachoVueltasFragment odv = new despachoVueltasFragment();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contenedor_despacho, odv)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null).commit();
                        break;
                    case R.id.tab_time:
                        DespachoHorasFragment odh = new DespachoHorasFragment();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contenedor_despacho,odh)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null).commit();
                        break;
                }
            }
        });
        return view;
    }
}