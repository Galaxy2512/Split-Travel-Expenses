package Model;

/**
 * Klasa UserModel predstavlja model korisnika.
 * Model korisnika sadrzi listu korisnika.
 *
 * @version 1.0
 *
 *
 */

import java.util.ArrayList;
import java.util.List;


public class UserModel {
    private List<User> users;

    public UserModel() {
        this.users = new ArrayList<>();
    }


}
