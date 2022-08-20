package com.bcit.birdfinder;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BirdAdapter extends RecyclerView.Adapter<BirdAdapter.BirdViewHolder> {

    private List<Bird> birdList;
    private OnBirdAdapterItemListener onBirdAdapterItemListener;

    public BirdAdapter(List<Bird> birdList) {
        this.birdList = birdList;
    }

    public void setOnAdapterItemListener(OnBirdAdapterItemListener onAdapterItemListener) {
        this.onBirdAdapterItemListener = onAdapterItemListener;
    }

    @NonNull
    @Override
    public BirdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View birdView = inflater.inflate(R.layout.bird_list_item, parent, false);

        return new BirdViewHolder(birdView);
    }

    @Override
    public void onBindViewHolder(@NonNull BirdViewHolder holder, int position) {
        TextView textViewBirdName = holder.textViewBirdName;
        TextView textViewBirdScientificName = holder.textViewBirdScientificName;
        TextView textViewBirdSpeciesCode = holder.textViewBirdSpeciesCode;

        Bird bird = birdList.get(position);

        textViewBirdName.setText(bird.commName);
        textViewBirdScientificName.setText(bird.sciName);
        textViewBirdSpeciesCode.setText(bird.birdCode);

        if (position % 2 == 0) {
            holder.layoutBirdItem.setBackgroundColor(Color.argb(230, 210, 230, 230));
        } else {
            holder.layoutBirdItem.setBackgroundColor(Color.argb(230, 160, 200, 200));
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBirdAdapterItemListener.OnClick(bird);
            }
        };

        textViewBirdName.setOnClickListener(onClickListener);
        textViewBirdScientificName.setOnClickListener(onClickListener);
        textViewBirdSpeciesCode.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return birdList.size();
    }


    public class BirdViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewBirdName;
        public TextView textViewBirdScientificName;
        public TextView textViewBirdSpeciesCode;
        public LinearLayout layoutBirdItem;

        public BirdViewHolder(@NonNull View birdView) {
            super(birdView);
            textViewBirdName = birdView.findViewById(R.id.bird_textView_name);
            textViewBirdScientificName = birdView.findViewById(R.id.bird_textView_scientific_name);
            textViewBirdSpeciesCode = birdView.findViewById(R.id.bird_textView_species_code);
            layoutBirdItem = birdView.findViewById(R.id.bird_item);
        }
    }
}
