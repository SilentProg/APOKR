package com.savonikyurii.beatifulkrivbas.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.savonikyurii.beatifulkrivbas.R;
import com.savonikyurii.beatifulkrivbas.databinding.FragmentHistoryBinding;
import com.savonikyurii.beatifulkrivbas.helpers.Categories;
import com.savonikyurii.beatifulkrivbas.helpers.Place;
import com.savonikyurii.beatifulkrivbas.ui.list.ListAllAdapter;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    FragmentHistoryBinding binding;
    private RecyclerView list;
    private List<Place> all_places;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        list = root.findViewById(R.id.list_history);
        all_places = new ArrayList<>();
        String uri = "https://st2.depositphotos.com/1064024/10769/i/600/depositphotos_107694484-stock-photo-little-boy.jpg";
        all_places.add(new Place("Гданцевский парк", "Центрально-Міський район, Кривий Ріг, 50000", uri, Categories.CULTURAL_HERITAGE, "none", "none", 47.89788844983029, 33.33293223353166));

        ListAllAdapter adapter = new ListAllAdapter(getActivity(), all_places);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));

        return root;
    }
}