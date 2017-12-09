package com.fatec.labs.fatec_firebase.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fatec.labs.fatec_firebase.Common.App;
import com.fatec.labs.fatec_firebase.RecyclerView.RecyclerViewAdapter;
import com.fatec.labs.fatec_firebase.DAO.CatDAO;
import com.fatec.labs.fatec_firebase.DAO.UserDAO;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.fatec.labs.fatec_firebase.*;
import com.fatec.labs.fatec_firebase.Common.Notify;
import com.fatec.labs.fatec_firebase.DAO.FirebaseStorageUtils;
import com.fatec.labs.fatec_firebase.Model.User;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public Bitmap catPic;

    public FirebaseAuth mAuth;
    public DatabaseReference refFirebase;
    public User loggedUser;
    private FirebaseStorageUtils firebaseStorageUtils;
    public static TextView tNome, tEmail;
    public Bitmap userPhoto;
    public static CircleImageView imgUser;
    String strUserID;
    public TextView tvwAdoptedCats, tvwCatsForAdoption;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Drawer layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Navigation view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Navigation header items
        //Here we get the user DisplayName and e-Mail
        View header=navigationView.getHeaderView(0);
        tNome = (TextView) header.findViewById(R.id.nameLoggedUser);
        tEmail = (TextView) header.findViewById(R.id.emailLoggedUser);
        imgUser = (CircleImageView) header.findViewById(R.id.iconLoggedUser);

        //Notify.showNotify(this, "Nome: " + user.getDisplayName());

        LinearLayout dashboard_menu = (LinearLayout) findViewById(R.id.dashboard_menu);
        int adoptedCats = CatDAO.arrAdoptedCatList.size();
        int catsForAdoption = CatDAO.arrListCat.size();
        catsForAdoption -= adoptedCats;

        tvwCatsForAdoption = (TextView) dashboard_menu.findViewById(R.id.tvwCatsForAdoption);
        tvwAdoptedCats  = (TextView) dashboard_menu.findViewById(R.id.tvwAdoptedCats );

        tvwAdoptedCats.setText(getString(R.string.adopted_cats,Integer.toString(adoptedCats)));
        tvwCatsForAdoption.setText(this.getString(R.string.cats_for_adoption, Integer.toString(catsForAdoption)));
        /*
        //Floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //vai pra tela de cadastro do gato
                Intent intentAddCat = new Intent(MenuActivity.this,CatIncludeActivity.class);
                startActivity(intentAddCat);
            }
        });*/

        //Get the current user
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user==null) {
            Notify.showNotify(this,getString(R.string.error_login_failed));
            this.finish();
            finishActivity(0);
        }

        new App(this);

        strUserID = user.getUid();

        //search on the database for details
        FirebaseDatabase dbsFirebase = FirebaseDatabase.getInstance();
        DatabaseReference refFirebase;
        refFirebase = dbsFirebase.getReference("user/" + strUserID);

        refFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    User loggedUser = dataSnapshot.getValue(User.class);

                    if (loggedUser == null) {
                        Notify.showNotify(MenuActivity.this, getString(R.string.error_user_not_found));
                        MenuActivity.this.finish();
                        MenuActivity.this.finishActivity(0);
                    } else {
                        //get user info
                        UserDAO.getCatUserFromLoggedUser(loggedUser);
                        tNome.setText(UserDAO.loggedCatUser.getUserName());
                        tEmail.setText(UserDAO.loggedCatUser.getUserEmail());
                        UserDAO.loggedCatUser.setUserSenha(loggedUser.getUserSenha());
                        getUserImage();

                        //get cat info
                        //getCatListFromQuery();
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Erro: " + databaseError.getDetails());
                MenuActivity.this.finish();
                MenuActivity.this.finishActivity(0);
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setAdapter(new RecyclerViewAdapter(CatDAO.arrMyCatList, this));

        RecyclerView.LayoutManager layout = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layout);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getUserImage (){
        //this function is responsible for getting user image into the navigation drawer
        try{
            StorageReference firebaseStorage;
            firebaseStorage = FirebaseStorage.getInstance().getReference();
            StorageReference fotoReference = firebaseStorage.child("user/" + strUserID + ".png");
            final File temp = File.createTempFile("img", strUserID + ".png");
            fotoReference.getFile(temp).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    userPhoto = BitmapFactory.decodeFile(temp.getPath());
                    UserDAO.loggedCatUser.setUserImage(userPhoto);
                    imgUser.setImageBitmap(Bitmap.createScaledBitmap(userPhoto, 100, 100, true));
                }
            });
        }
        catch (IOException e){
            Notify.showNotify(this,e.getMessage());
            System.out.println(e.toString());
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //prepare intent
        Intent intentMap;
        Intent intentCatRegister;
        Intent intentProfile;

        if (id == R.id.nav_register_cat) {
            intentCatRegister = new Intent(MenuActivity.this,CatIncludeActivity.class);
            startActivity(intentCatRegister);


        } else if (id == R.id.nav_adopt_cat) {
            intentMap = new Intent(MenuActivity.this,MapsActivity.class);
            startActivity(intentMap);


        } else if (id == R.id.nav_profile) {
            intentProfile = new Intent(MenuActivity.this,UserProfileActivity.class);
            startActivity(intentProfile);

    }
        else if (id == R.id.nav_appExit) {
                finish();
                System.exit(0);//Alterdo 07/12/2017
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
