package kg.megacom.Tinder.services;

import kg.megacom.Tinder.models.Gender;
import kg.megacom.Tinder.models.Users;

import java.util.List;

public interface UserServices {
    void save(Users user);
    List<Users> findAll();
    Users findByID(long id);
    boolean logPass(String login, String password);
    Users welcome(String login, String password);
    List<Users> findMale(Gender gender, List<Long> recipId);
    public List<Long> idUsers(long senderId);
}
