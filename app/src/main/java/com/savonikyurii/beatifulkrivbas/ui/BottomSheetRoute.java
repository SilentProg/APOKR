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
import com.savonikyurii.beatifulkrivbas.GeolocationAPI.Place;
//клас контроллер BottomSheet діалогу успадковує BottomSheetDialogFragment
public class BottomSheetRoute extends BottomSheetDialogFragment {
    //оголошення змінних
    private BottomSheetRouteListener mListener;
    private Button btnReturn;
    private Button btnStart;
    private Button btnWholeRoute;
    private Place place;
    //конструктор
    public BottomSheetRoute(Place p){
        this.place = p;
    }

    @Override//створення інтерфейсу діалога
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_route, container, false);
        //виклик ініціалізації
        init(view);
        //повернення інтерфейсу
        return view;
    }
    //метод ініціалізації
    private void init(View view){
        //знаходимо елементи інтерфейсу
        btnReturn = (Button) view.findViewById(R.id.btnBottomSheetReturnToList);
        btnStart = (Button) view.findViewById(R.id.btnBottomSheetStart);
        btnWholeRoute = (Button) view.findViewById(R.id.btnBottomSheetWholeRoute);
        //встановлення обробників натискання на кнопку
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
    //ствронення інтерфейсу
    public interface BottomSheetRouteListener{
        void onButtonClicked(int id);
    }

    @Override //метод який відбувається при виклику класу
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetRouteListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+" must implement BottomSheetRouteListener");
        }

    }
}
