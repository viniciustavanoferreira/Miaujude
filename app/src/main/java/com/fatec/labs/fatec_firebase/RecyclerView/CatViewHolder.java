package com.fatec.labs.fatec_firebase.RecyclerView;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.fatec.labs.fatec_firebase.Activity.MyCatInfoActivity;
import com.fatec.labs.fatec_firebase.Common.App;
import com.fatec.labs.fatec_firebase.DAO.CatDAO;
import com.fatec.labs.fatec_firebase.Model.Cat;
import com.fatec.labs.fatec_firebase.R;


public class CatViewHolder extends RecyclerView.ViewHolder {
    final ImageView imgCat;
    final TextView tvwCatName;
    final TextView tvwCatRace;
    final TextView tvwCatDescription;
    final TextView tvwCatId;

    public CatViewHolder(View view){
        super(view);
        this.imgCat =(ImageView) view.findViewById(R.id.imgCat);
        this.tvwCatName = (TextView) view.findViewById(R.id.tvwCatName);
        this.tvwCatRace = (TextView) view.findViewById(R.id.tvwCatRace);
        this.tvwCatDescription = (TextView) view.findViewById(R.id.tvwCatDescription);
        this.tvwCatId =(TextView) view.findViewById(R.id.tvwCatId);

        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                imgCat.buildDrawingCache();
                CatDAO.selectedCat = new Cat();
                CatDAO.selectedCat.setCatPicture(imgCat.getDrawingCache());
                CatDAO.selectedCat.setCatName((String) tvwCatName.getText());
                CatDAO.selectedCat.setCatRace((String) tvwCatRace.getText());
                CatDAO.selectedCat.setCatDescription((String) tvwCatDescription.getText());

                //Open the Cat profile screen
                Intent catIntent = new Intent(App.getContext(), MyCatInfoActivity.class);
                App.getContext().startActivity(catIntent);
            }
        });
    }


}
