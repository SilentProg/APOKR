package com.savonikyurii.beatifulkrivbas.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.savonikyurii.beatifulkrivbas.R;
import com.savonikyurii.beatifulkrivbas.API.Place;

public class BottomSheetRoute extends BottomSheetDialogFragment {
    private BottomSheetRouteListener mListener;
    private Button btnReturn;
    private Button btnStart;
    private Button btnWholeRoute;
    private Place place;

    public BottomSheetRoute(Place p){
        this.place = p;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottomsheet_route, container, false);

        init(view);


        return view;
    }

    private void init(View view){
        btnReturn = (Button) view.findViewById(R.id.btnBottomSheetReturnToList);
        btnStart = (Button) view.findViewById(R.id.btnBottomSheetStart);
        btnWholeRoute = (Button) view.findViewById(R.id.btnBottomSheetWholeRoute);

        btnReturn.setOnClickListener(view1 -> {
            mListener.onButtonClicked(btnReturn.getId());
            dismiss();
        });
        btnStart.setOnClickListener(view1 -> {
            mListener.onButtonClicked(btnStart.getId());
            dismiss();
        });
        btnWholeRoute.setOnClickListener(view1 -> {
            mListener.onButtonClicked(btnWholeRoute.getId());
            dismiss();
        });
    }

    public interface BottomSheetRouteListener{
        void onButtonClicked(int id);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetRouteListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+" must implement BottomSheetRouteListener");
        }

    }
}
