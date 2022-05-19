package com.savonikyurii.beatifulkrivbas.ui.list;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.savonikyurii.beatifulkrivbas.R;
import com.savonikyurii.beatifulkrivbas.GeolocationAPI.AdditionalClasses.Categories;
import com.savonikyurii.beatifulkrivbas.GeolocationAPI.Place;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    //оголошення полів класу
    public static Place place;
    private RecyclerView list;
    private List<Place> all_places;
    private DatabaseReference mRefData;
    private ListAllAdapter adapter;
    private CardView cardEmpty;
    private Button btnReturnToCatalog;
    public static String category = Categories.AllObjects;

    @Override //методж створення вікна
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        //викликаємо ініціалізацію
        init(root);
        //викликаємо зчитування даних
        ReadData(category);
        //повертаємо створений View
        return root;
    }

    //метод ініциалізації
    private void init(View root){
        //ініціалізуємо змінні
        list = root.findViewById(R.id.list);
        btnReturnToCatalog = (Button)root.findViewById(R.id.btnReturnToCatalog);
        all_places = new ArrayList<>();
        cardEmpty = root.findViewById(R.id.card_list_empty);
        //отримання посилання на джерело даних
        mRefData = FirebaseDatabase.getInstance().getReference();
        //створення адаптера для списку
        adapter = new ListAllAdapter(getActivity(), all_places, this);
        //встановлення адаптера
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        //встановлення слухача натискання на кнопку: повернутися до каталогу
        btnReturnToCatalog.setOnClickListener(view -> {
            NavHostFragment.findNavController(this).navigateUp();
        });
    }

    //метод перевірки яку саме категорію місць потрібно зчитати з джерела даних
    private void ReadData(String category){
        //відповідно до потрібної категорії зчитуємо потрібні дані
        if (category.equals(Categories.AllObjects)) {
            ReadAll();
        }else if (category.equals(Categories.MilitaryPatriotic)){
            ReadAllCategory(Categories.MilitaryPatriotic);
        }else if (category.equals(Categories.CulturalObjects)){
            ReadAllCategory(Categories.CulturalObjects);
        }else if (category.equals(Categories.ReligiousObjects)){
            ReadAllCategory(Categories.ReligiousObjects);
        }else if (category.equals(Categories.HistoricalObjects)){
            ReadAllCategory(Categories.HistoricalObjects);
        }else if (category.equals(Categories.ArchitecturalObjects)){
            ReadAllCategory(Categories.ArchitecturalObjects);
        }else if (category.equals((Categories.NaturalObjects))){
            ReadAllCategory(Categories.NaturalObjects);
        }else if (category.equals(Categories.UrbanObjects)){
            ReadAllCategory(Categories.UrbanObjects);
        }else{
            ReadAllCategory(Categories.OtherObjects);
        }
    }
    //метод зчитування даних з джерела
    private void ReadAllCategory(String category){
        //звертаємося до джерела даних
        mRefData.child("places").child(category).addValueEventListener(new ValueEventListener() {
            @Override //при отриманні даних
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //очищаємо список місць, щоб уникнути дублювання
                if (all_places.size() > 0) all_places.clear();
                //якщо дані які ми отримали з джерела пусті, то повідомляємо про це користувача
                if (!snapshot.hasChildren()) cardEmpty.setVisibility(View.VISIBLE);
                //проходимося по всім даним що отримали
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //конвертуємо отримані дані в потрібний нам формат
                    Place place = ds.getValue(Place.class);
                    assert place != null;//перевіряємо чи зміння місця не пуста
                    all_places.add(place);//додаємо місце в список
                    updateUI();//оновлюємо інтерфейс
                }
            }
            @Override //при помилці отриманні даних виводимо помилку
            public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(getActivity(), "Помилка отримання даних", Toast.LENGTH_SHORT).show(); }
        });
    }
    //метод зчитування даних з джерела
    private void ReadAll(){
        //звертаємося до джерела даних
        mRefData.child("places").addValueEventListener(new ValueEventListener() {
            @Override //при отриманні даних
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //очищаємо список місць, щоб уникнути дублювання
                if (all_places.size() > 0) all_places.clear();
                //якщо дані які ми отримали з джерела пусті, то повідомляємо про це користувача
                if (!snapshot.hasChildren()) cardEmpty.setVisibility(View.VISIBLE);
                //проходимося по всім даним що отримали
                for (DataSnapshot ds : snapshot.getChildren()) {
                    for (DataSnapshot data : ds.getChildren()) {
                        //конвертуємо отримані дані в потрібний нам формат
                        Place place = data.getValue(Place.class);
                        assert place != null; //перевіряємо чи зміння місця не пуста
                        all_places.add(place);//додаємо місце в список
                        updateUI();//оновлюємо інтерфейс
                    }
                }
            }
            @Override //при помилці отриманні даних виводимо помилку
            public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(getActivity(), "Помилка отримання даних", Toast.LENGTH_SHORT).show(); }
        });
    }
    //метод оновлення інтерфейсу
    @SuppressLint("NotifyDataSetChanged")
    private void updateUI() {
        //оновлюємо список
        adapter.notifyDataSetChanged();
    }


}