package com.fatec.labs.fatec_firebase.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.fatec.labs.fatec_firebase.DAO.ConfiguracaoFirebase;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {

    private transient static User instance;
    private String id;
    private String userName;
    private String userPhone;
    private String userEmail;
    private String userSenha;
    private boolean active;

    @Exclude
    public static User getInstance (){
        return instance == null ? instance = new User() : instance;
    }

    public User(){}

    public void salvarUsuario(){
        DatabaseReference referenciaDataBase = ConfiguracaoFirebase.getFireBase();
        referenciaDataBase.child("user").child(String.valueOf(getId())).setValue(this);
    }

    @Exclude
    public Map<String, Object> ToMap(){
        HashMap<String, Object> hashMapUsuario = new HashMap<>();
        hashMapUsuario.put("id", getId());
        hashMapUsuario.put("userName", getUserName());
        hashMapUsuario.put("userPhone", getUserPhone());
        hashMapUsuario.put("userEmail", getUserEmail());
        hashMapUsuario.put("userSenha", getUserSenha());
        hashMapUsuario.put("active", isActive());
        return hashMapUsuario;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserPhone() {
        return userPhone;
    }
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public String getUserSenha() {
        return userSenha;
    }
    public void setUserSenha(String userSenha) {
        this.userSenha = userSenha;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
}
