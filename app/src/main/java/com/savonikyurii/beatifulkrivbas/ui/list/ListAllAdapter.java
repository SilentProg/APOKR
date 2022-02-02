package com.savonikyurii.beatifulkrivbas.ui.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.savonikyurii.beatifulkrivbas.R;
import com.savonikyurii.beatifulkrivbas.helpers.Place;
import com.savonikyurii.beatifulkrivbas.ui.details.DetailsFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListAllAdapter extends RecyclerView.Adapter<ListAllAdapter.ListAllViewHolder> {
    private List<Place> list;
    private Context context;
    private ListFragment fragment;

    public ListAllAdapter(Context ct, List<Place> list_of_place, ListFragment fragment){
        this.context = ct;
        this.list = list_of_place;
        this.fragment = fragment;
    }

    public ListAllAdapter(Context ct, List<Place> list_of_place){
        this.context = ct;
        this.list = list_of_place;
    }

    @NonNull
    @Override
    public ListAllViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_all_item, parent, false);
        return new ListAllViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAllViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.category.setText(list.get(position).getCategory());
        Picasso.get().load(list.get(position).getImageuri()).into(holder.image);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailsFragment.place = list.get(position);
                NavHostFragment.findNavController(fragment).navigate(R.id.nav_details);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ListAllViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title, category;
        ConstraintLayout constraintLayout;
        public ListAllViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.list_all_image);
            title = itemView.findViewById(R.id.list_all_title);
            category = itemView.findViewById(R.id.list_all_category);
            constraintLayout = itemView.findViewById(R.id.list_all_container);
        }
    }
}
