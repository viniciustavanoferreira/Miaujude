package com.fatec.labs.fatec_firebase.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.fatec.labs.fatec_firebase.DAO.CatDAO;
import com.fatec.labs.fatec_firebase.Model.Cat;
import com.fatec.labs.fatec_firebase.Model.CatUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.fatec.labs.fatec_firebase.Common.Internet;
import com.fatec.labs.fatec_firebase.Common.Notify;
import com.fatec.labs.fatec_firebase.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    public int count;
    public FirebaseAuth mAuth;
    private UserLoginTask mAuthTask = null;

    //===================================================================================================
    public static boolean verRequisicao;//Roberto Floro
//===================================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


//===================================================================================================
        verRequisicao = false;//Roberto Floro
//===================================================================================================

        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                try {
                    attemptLogin();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //debug
        mEmailView.setText("vtf.fatec@hotmail.com");
        mPasswordView.setText("sap123");
    }

    private void attemptLogin() throws InterruptedException {
        if (mAuthTask != null) {
            return;
        }

        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            if (!Internet.isNetworkAvailable(this)) {
                Notify.showNotify(this,getString(R.string.error_not_connected));
            } else {
                //showProgress(true);
                mAuthTask = new UserLoginTask(email, password);
                mAuthTask.execute((Void) null);

                mAuth.signInWithEmailAndPassword(email, password).
                        addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            public static final String TAG = "attemptLogin";

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();




//===================================================================================================
                                    //Roberto Floro
                                    Intent Splash2 = new Intent(LoginActivity.this, Splash2.class);
                                    startActivity(Splash2);//Roberto Floro
//===================================================================================================






                                    getCatListFromQuery();
                                    //Notify.showNotify(LoginActivity.this, getString(R.string.message_login_successful));

                                } else {
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    System.out.println(task.getException().toString());
                                    Notify.showNotify(LoginActivity.this, getString(R.string.error_login_failed));
                                }
                            }
                        });
            }
        }
    }

    public void getCatListFromQuery() {
        //clean the arraylist

        CatDAO.arrListCat.clear();
        CatDAO.arrMyCatList.clear();
        CatDAO.arrAdoptedCatList.clear();

        //Instancia o Firebase
        DatabaseReference refFirebase; refFirebase = FirebaseDatabase.getInstance().getReference("/cat");
        Query query = refFirebase.orderByKey();

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userID,userName,userPhone,userEmail;
                String catID,catName,catRace,catDescription;
                CatUser catUserOwner;
                double catLatitude,catLongitude;
                boolean catAdopted;

                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                    while (iterator.hasNext()) {
                        DataSnapshot next = (DataSnapshot) iterator.next();
                        //System.out.println("Value = " + next.child("id").getValue());

                        userID = next.child("catUserOwner").child("id").getValue().toString();
                        userName = next.child("catUserOwner").child("userName").getValue().toString();
                        userPhone = next.child("catUserOwner").child("userPhone").getValue().toString();
                        userEmail = next.child("catUserOwner").child("userEmail").getValue().toString();
                        catUserOwner = new CatUser(userID, userName, userPhone, userEmail);

                        catID = next.child("id").getValue().toString();
                        catName = next.child("catName").getValue().toString();
                        catRace = next.child("catRace").getValue().toString();
                        catDescription = next.child("catDescription").getValue().toString();
                        catLatitude = Double.parseDouble(next.child("catLatitude").getValue().toString());
                        catLongitude = Double.parseDouble(next.child("catLongitude").getValue().toString());
                        catAdopted = (next.child("catDescription").getValue()=="true"?true:false);

                        final Cat cat = new Cat(catID, catName, catRace, catDescription, catUserOwner, catLatitude, catLongitude,catAdopted);
                        System.out.println(cat);
                        //Including the cat on the list
                        CatDAO.arrListCat.add(cat);

                        //Including the cat on my list
                        String uid = mAuth.getCurrentUser().getUid();
                        if (userID.equals(mAuth.getCurrentUser().getUid())) {
                            CatDAO.arrMyCatList.add(cat);
                        }

                        //This cat is adopted?
                        if (catAdopted) {
                            CatDAO.arrAdoptedCatList.add(cat);
                        }
                    }

                    for (final Cat cat : CatDAO.arrMyCatList){
                        try {

                            StorageReference fotoReference = FirebaseStorage.getInstance().getReference().child("cat/" + cat.getId() + ".png");
                            final File temp = File.createTempFile(cat.getId(),null,null);

                            OnSuccessListener<FileDownloadTask.TaskSnapshot> successListener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap catPic = (BitmapFactory.decodeFile(temp.getPath()));
                                    cat.setCatPicture(catPic);
                                    if (count == CatDAO.arrMyCatList.size()){



//===================================================================================================
                                        verRequisicao = true;//Roberto Floro
//===================================================================================================



                                    }


                                }

                            };

                            OnFailureListener failureListener = new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {


//===================================================================================================
                                    verRequisicao = true;//Roberto Floro
//===================================================================================================

                                }
                            };

                            fotoReference.getFile(temp).addOnSuccessListener(successListener);
                            fotoReference.getFile(temp).addOnFailureListener(failureListener);

                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        catch (NullPointerException e){
                            e.printStackTrace();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                CatDAO.arrListCat.clear();
                CatDAO.arrMyCatList.clear();
                CatDAO.arrAdoptedCatList.clear();
            }
        };
        query.addValueEventListener(eventListener);
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            shortAnimTime = 99999999;

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(10000).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(10000).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
        //return new CursorLoader(this,
                /*
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                        ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                        .CONTENT_ITEM_TYPE},
                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                        ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
                */
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cursor.moveToNext();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;
        private final String mPassword;
        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return false;
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;



//===================================================================================================
            //showProgress(false);//Roberto Floro
//===================================================================================================



            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

