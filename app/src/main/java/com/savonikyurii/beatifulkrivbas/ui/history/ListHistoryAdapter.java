package com.savonikyurii.beatifulkrivbas.ui.history;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.savonikyurii.beatifulkrivbas.R;
import com.savonikyurii.beatifulkrivbas.GeolocationAPI.Place;
import com.savonikyurii.beatifulkrivbas.ui.details.DetailsFragment;
import com.squareup.picasso.Picasso;

import java.util.List;
//класс адаптер для списку історії
public class ListHistoryAdapter extends RecyclerView.Adapter<ListHistoryAdapter.ListHistoryViewHolder>{
    //оголошення змінних
    private List<Place> list;
    private Context context;
    private Fragment fragment;
    private final int MENU_DELETE = 1;
    //конструктори
    public ListHistoryAdapter(Context ct, List<Place> list_of_place, HistoryFragment fragment){
        this.context = ct;
        this.list = list_of_place;
        this.fragment = (Fragment) fragment;
    }

    public ListHistoryAdapter(Context ct, List<Place> list_of_place, Fragment fragment){
        this.context = ct;
        this.list = list_of_place;
        this.fragment = fragment;
    }

    public ListHistoryAdapter(Context ct, List<Place> list_of_place){
        this.context = ct;
        this.list = list_of_place;
    }

    @NonNull
    @Override
    public ListHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //створення вью для елемента списка
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_history_item, parent, false);
        return new ListHistoryViewHolder(view);
    }

    @Override //заповнення елемента списка даними
    public void onBindViewHolder(@NonNull ListHistoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.category.setText(list.get(position).getCategory());
        Picasso.get().load(list.get(position).getImageuri()).into(holder.image);
        holder.btnDelete.setImageResource(R.drawable.trashcan);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailsFragment.place = list.get(position);
                DetailsFragment.isVisible = false;
                NavHostFragment.findNavController(fragment).navigate(R.id.nav_details);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = builder(context, position).create();
                dialog.show();
            }
        });
    }
    //метод створення діалогового вікна видалення місця з історії
    private AlertDialog.Builder builder(Context c, int p){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.delete_history);  // заголовок
        builder.setMessage(R.string.delete_ask); // сообщение
        builder.setIcon(R.drawable.trashcan);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference mRefData = FirebaseDatabase.getInstance().getReference();
                mRefData.child("userdata").child(FirebaseAuth.getInstance().getUid()).child("visited").child("other").child(list.get(p).getTitle()).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Log.d("delete", "complete");
                    }
                });
                Log.d("delete",mRefData.getRef().toString()+" | "+list.get(p).getTitle());
                list.remove(p);
            }
        });
        builder.setNegativeButton(R.string.NO, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setCancelable(true);

        return builder;
    }

    @Override // метод повернення кількості елементів в списку
    public int getItemCount() {
        return list.size();
    }

    //знаходження елементів інтерфейсу
    public class ListHistoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title, category;
        ImageView btnDelete;
        ConstraintLayout constraintLayout;
        public ListHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.list_all_image);
            title = itemView.findViewById(R.id.list_all_title);
            category = itemView.findViewById(R.id.list_all_category);
            constraintLayout = itemView.findViewById(R.id.list_all_container);
            btnDelete = itemView.findViewById(R.id.deleteImg);
        }
    }
}
