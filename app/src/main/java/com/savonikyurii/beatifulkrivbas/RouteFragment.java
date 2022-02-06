package com.savonikyurii.beatifulkrivbas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.savonikyurii.beatifulkrivbas.databinding.FragmentRouteBinding;
import com.savonikyurii.beatifulkrivbas.helpers.Route;
import com.savonikyurii.beatifulkrivbas.ui.list.ListAllAdapter;

public class RouteFragment extends Fragment {
    private FragmentRouteBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRouteBinding.inflate(inflater, container, false);

        initRoute();

        return binding.getRoot();
    }

    private void initRoute(){
        ListAllAdapter adapter = new ListAllAdapter(getActivity(), Route.getRoute(),this);
        binding.routelist.setAdapter(adapter);
        binding.routelist.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}