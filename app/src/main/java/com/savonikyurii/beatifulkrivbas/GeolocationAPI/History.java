package com.savonikyurii.beatifulkrivbas.GeolocationAPI;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
/*Клас Історії*/
public class History {
    /*Поля класу*/
    private DatabaseReference mRefData; // поислання на джерело даних
    private ArrayList<Place> historyList; // список відвіданих місць
    private Place lastPlace; // останнє відвідане місце
    /*Інітіалізація класу*/
    {
        //отримуємо писилання на джерело даних
        mRefData = FirebaseDatabase.getInstance().getReference();
        historyList = new ArrayList<>();
        lastPlace = new Place();
    }
    /*Конструктор*/
    public History(DatabaseReference mRefData) {
        this.mRefData = mRefData;
        //отримуємо дані про останнє відвідане місце з джерела даних
        mRefData.child("last").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //Приводимо отримані дані к потрібному нам форматі
                for (DataSnapshot child : snapshot.getChildren()) {
                    lastPlace = child.getValue(Place.class);
                } }
            @Override
            public void onCancelled(DatabaseError error) { }
        });
        //отримуємо дані відвідані місця з джерела даних
        mRefData.child("other").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //Приводимо отримані дані к потрібному нам форматі
                //та заодно заповнюємо ними список для подальшого використання
                for (DataSnapshot ds: snapshot.getChildren()) {
                    historyList.add(ds.getValue(Place.class));
                }}
            @Override
            public void onCancelled(DatabaseError error) {}
        });
        //Затримка програми для зчитування даних з джерела
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) { e.printStackTrace(); } }
    /*Сеттери та Геттери*/
    public DatabaseReference getmRefData() { return mRefData; }
    public void setmRefData(DatabaseReference mRefData) { this.mRefData = mRefData; }
    public ArrayList<Place> getHistoryList() { return historyList; }
    public void setHistoryList(ArrayList<Place> historyList) { this.historyList = historyList; }
    public Place getLastPlace() { return lastPlace; }
    public void setLastPlace(Place lastPlace) {this.lastPlace = lastPlace;}
}