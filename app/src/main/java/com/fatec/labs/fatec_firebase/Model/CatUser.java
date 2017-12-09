package com.fatec.labs.fatec_firebase.Model;

import android.graphics.Bitmap;

public class CatUser {
    private String id;
    private String userName;
    private String userPhone;
    private String userEmail;
    private Bitmap userImage;//Alterado 03/12/2017 17:02
    private String userSenha;//Alterado 03/12/2017 17:02

    public CatUser(){}
    public CatUser(String id,String userName,String userPhone,String userEmail){
        setId(id);
        setUserName(userName);
        setUserPhone(userPhone);
        setUserEmail(userEmail);
    }

    public String getUserSenha() { return userSenha; }//Alterado 03/12/2017 17:02
    public void setUserSenha(String userSenha) { this.userSenha = userSenha; }//Alterado 03/12/2017 17:02
    public Bitmap getUserImage() { return userImage; }//Alterado 03/12/2017 17:02
    public void setUserImage(Bitmap userImage) { this.userImage = userImage; }//Alterado 03/12/2017 17:02
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public String getUserName() {return userName;}
    public void setUserName(String userName) {this.userName = userName;}
    public String getUserPhone() {return userPhone;}
    public void setUserPhone(String userPhone) {this.userPhone = userPhone;}
    public String getUserEmail() {return userEmail;}
    public void setUserEmail(String userEmail) {this.userEmail = userEmail;}
}
