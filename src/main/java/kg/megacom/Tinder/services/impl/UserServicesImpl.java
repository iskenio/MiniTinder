package kg.megacom.Tinder.services.impl;

import kg.megacom.Tinder.dao.DbHelper;
import kg.megacom.Tinder.dao.impl.DbHelperImpl;
import kg.megacom.Tinder.models.Gender;
import kg.megacom.Tinder.models.Users;
import kg.megacom.Tinder.services.UserServices;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserServicesImpl implements UserServices {
    DbHelper dbHelper = new DbHelperImpl();

    @Override
    public void save(Users user) {
        try (PreparedStatement preparestatement = dbHelper.getStmt
                ("INSERT INTO tb_users(full_name,gender,login,password,info) VALUES (?,?,?,?,?)")){

            preparestatement.setString(1,user.getFullName());
            preparestatement.setString(2, user.getGender().toString());
            preparestatement.setString(3, user.getLogin());
            preparestatement.setString(4, user.getPassword());
            preparestatement.setString(5, user.getInfo());
            preparestatement.executeUpdate();

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Users> findAll() {
        List<Users> usersList = new ArrayList<>();

        try (PreparedStatement preparestatement = dbHelper.getStmt("SELECT * FROM tb_users")){
            ResultSet resultSet = preparestatement.executeQuery();

            while (resultSet.next()) {
                Users user = new Users();
                user.setId(resultSet.getLong("id"));
                user.setFullName(resultSet.getString("full_name"));
                user.setGender(Gender.valueOf(resultSet.getString("gender")));
                user.setLogin(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password"));
                user.setInfo(resultSet.getString("info"));
                usersList.add(user);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return usersList;
    }

    @Override
    public Users findByID(long id) {
        Users user = new Users();

        try (PreparedStatement preparestatement = dbHelper.getStmt("SELECT * FROM tb_users WHERE id = ? ORDER by id")){
            preparestatement.setLong(1, id);
            ResultSet resultSet = preparestatement.executeQuery();

            while (resultSet.next()){
                user.setId(resultSet.getLong("id"));
                user.setFullName(resultSet.getString("full_name"));
                user.setGender(Gender.valueOf(resultSet.getString("gender")));
                user.setLogin(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password"));
                user.setInfo(resultSet.getString("info"));
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    public boolean logPass(String login, String password) {
        try (PreparedStatement preparestatement = dbHelper.getStmt
                ("SELECT login, password FROM tb_users WHERE login = ? AND password = ?")) {
            preparestatement.setString(1, login);
            preparestatement.setString(2, password);

            ResultSet resultSet = preparestatement.executeQuery();
            String log = null;
            String pass = null;

            while (resultSet.next()) {
                log = resultSet.getString("login");
                pass = resultSet.getString("password");
            }

            if (login.equals(log) & password.equals(pass)) {
                return true;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Users welcome(String login, String password){
        Users user = new Users();
        try (PreparedStatement preparestatement = dbHelper.getStmt
                ("SELECT * FROM tb_users WHERE login = ? AND password = ?")) {
            preparestatement.setString(1, login);
            preparestatement.setString(2, password);

            ResultSet resultSet = preparestatement.executeQuery();

            while (resultSet.next()) {
                user.setId(resultSet.getLong("id"));
                user.setFullName(resultSet.getString("full_name"));
                user.setGender(Gender.valueOf(resultSet.getString("gender")));
                user.setLogin(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password"));
                user.setInfo(resultSet.getString("info"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    @Override
    public List<Users> findMale(Gender gender, List<Long> recipId) {
        List<Users> males = new ArrayList<>();
        try (PreparedStatement preparestatement = dbHelper.getStmt
                ("SELECT * FROM tb_users WHERE GENDER = ?")) {
            preparestatement.setString(1, String.valueOf(gender));
            ResultSet resultSet = preparestatement.executeQuery();

            while (resultSet.next()) {
                Users user = new Users();
                user.setId(resultSet.getLong("id"));
                user.setFullName(resultSet.getString("full_name"));
                user.setGender(Gender.valueOf(resultSet.getString("gender")));
                user.setLogin(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password"));
                user.setInfo(resultSet.getString("info"));
                males.add(user);
            }

            for (Long i: recipId) {
                males.removeIf(n -> n.getId() == i);
            }

            return males;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Long> idUsers(long senderId) {
        List<Long> longID = new ArrayList<>();

        try (PreparedStatement preparestatement = dbHelper.getStmt
                ("SELECT recipient_id FROM tb_orders WHERE sender_id = ?")){
            preparestatement.setLong(1, senderId);
            ResultSet resultSet = preparestatement.executeQuery();

            while (resultSet.next()) {
                long x;
                x = resultSet.getLong("recipient_id");
                longID.add(x);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        try (PreparedStatement prepStmte = dbHelper.getStmt
                ("SELECT sender_id FROM tb_orders WHERE recipient_id = ?")){
            prepStmte.setLong(1, senderId);
            ResultSet resultSete = prepStmte.executeQuery();

            while (resultSete.next()) {
                long x;
                x = resultSete.getLong("sender_id");
                longID.add(x);
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return longID;
    }

}
