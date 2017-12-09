package com.fatec.labs.fatec_firebase.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fatec.labs.fatec_firebase.Common.Notify;
import com.fatec.labs.fatec_firebase.DAO.CatDAO;
import com.fatec.labs.fatec_firebase.R;

public class CatProfileActivity extends AppCompatActivity {

    private ImageView imgPerfGato;
    private TextView tvwCatPerfName;
    private TextView tvwCatPerfRaca;
    private TextView tvwCatPerfDescription;
    private EditText edtCatPerfName;
    private EditText edtCatPerfRaca;
    private EditText edtCatPerfDescription;
    private Button btnAlterarGato;

    @Override
    protected void onStart() {
        super.onStart();

        //Get the information on the screen
        if (CatDAO.selectedCat==null){
            Notify.showNotify(this,getString(R.string.message_cat_not_found));
        } else {
            edtCatPerfName.setText(CatDAO.selectedCat.getCatName());
            edtCatPerfRaca.setText(CatDAO.selectedCat.getCatRace());
            edtCatPerfDescription.setText(CatDAO.selectedCat.getCatDescription());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_profile);

        ImageView imgPerfGato = (ImageView) findViewById(R.id.imgPerfGato);
        TextView tvwCatPerfName = (TextView) findViewById(R.id.tvwCatPerfName);
        TextView tvwCatPerfRaca = (TextView) findViewById(R.id.tvwCatPerfRaca);
        TextView tvwCatPerfDescription = (TextView) findViewById(R.id.tvwCatPerfDescription);

        EditText edtCatPerfName = (EditText) findViewById(R.id.edtCatPerfName);
        EditText edtCatPerfRaca = (EditText) findViewById(R.id.edtCatPerfRaca);
        EditText edtCatPerfDescription = (EditText) findViewById(R.id.edtCatPerfDescription );

        Button btnAlterarGato = (Button) findViewById(R.id.btnAlterarGato);
    }
}
