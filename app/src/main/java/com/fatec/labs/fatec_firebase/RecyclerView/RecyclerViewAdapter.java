package com.fatec.labs.fatec_firebase.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fatec.labs.fatec_firebase.Model.Cat;
import com.fatec.labs.fatec_firebase.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<Cat> arrListCat;
    private Context context;

    public RecyclerViewAdapter(ArrayList<Cat> cat, Context context){
        this.arrListCat = cat;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        CatViewHolder catViewHolder = new CatViewHolder(view);

        return catViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CatViewHolder catViewHolder = (CatViewHolder) holder;
        Cat cat = arrListCat.get(position);
        ((CatViewHolder) holder).imgCat.setImageBitmap(cat.getCatPicture());
        ((CatViewHolder) holder).tvwCatName.setText(cat.getCatName());
        ((CatViewHolder) holder).tvwCatDescription.setText(cat.getCatDescription());
        ((CatViewHolder) holder).tvwCatId.setText(cat.getId());
        ((CatViewHolder) holder).tvwCatRace.setText(cat.getCatRace());
    }

    

    @Override
    public int getItemCount() {
        return arrListCat.size();
    }
}
