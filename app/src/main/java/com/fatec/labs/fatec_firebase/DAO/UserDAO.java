package com.fatec.labs.fatec_firebase.DAO;

import com.fatec.labs.fatec_firebase.Model.CatUser;
import com.fatec.labs.fatec_firebase.Model.User;

public abstract class UserDAO {
    public static CatUser loggedCatUser;
    public static void getCatUserFromLoggedUser(User user) {
        if (user==null) {
            loggedCatUser = null;
        } else {
            loggedCatUser = new CatUser(user.getId(),user.getUserName(),user.getUserPhone(),user.getUserEmail());
        }

    }
}
