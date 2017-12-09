package com.fatec.labs.fatec_firebase.DAO;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {
    //TODO: verificar se a classe é realmente necessária
    private static DatabaseReference referenciaFireBase;
    private static FirebaseAuth autenticacao;
    public static DatabaseReference getFireBase() {
        if(referenciaFireBase == null){
            referenciaFireBase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaFireBase;
    }
    public static FirebaseAuth getAutenticacao(){
        if(autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }
}
