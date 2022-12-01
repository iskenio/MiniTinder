package kg.megacom.Tinder.services.impl;

import kg.megacom.Tinder.dao.DbHelper;
import kg.megacom.Tinder.dao.impl.DbHelperImpl;
import kg.megacom.Tinder.models.Orders;
import kg.megacom.Tinder.models.Users;
import kg.megacom.Tinder.services.OrderServices;
import kg.megacom.Tinder.services.UserServices;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderServicesImpl implements OrderServices {
    DbHelper dbHelper = new DbHelperImpl();
    UserServices usersService = new UserServicesImpl();

    @Override
    public void save(Users user, Users user2) {
        try (PreparedStatement prepStmt = dbHelper.getStmt
                ("INSERT INTO tb_orders(sender_id, recipient_id, match) VALUES (?,?,?)")){
            prepStmt.setLong(1, user.getId());
            prepStmt.setLong(2, user2.getId());
            prepStmt.executeUpdate();

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Orders> findAll() {
        List<Orders> ordersList = new ArrayList<>();

        try (PreparedStatement prepStmt = dbHelper.getStmt("SELECT * FROM tb_orders")){
            ResultSet resultSet = prepStmt.executeQuery();

            while (resultSet.next()){
                Orders orders = new Orders();
                orders.setId(resultSet.getLong("id"));
                orders.setSendId(usersService.findByID(resultSet.getLong("sender_id")));
                orders.setRecipId(usersService.findByID(resultSet.getLong("recipient_id")));
                orders.setMatch(resultSet.getBoolean("match"));
                ordersList.add(orders);
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return ordersList;
    }

    @Override
    public Orders findByID(long id) {
        Orders order = new Orders();

        try (PreparedStatement prepStmt = dbHelper.getStmt
                ("SELECT * FROM tb_orders WHERE id = ?")){
            prepStmt.setLong(1, id);

            ResultSet resultSet = prepStmt.executeQuery();

            while (resultSet.next()){
                order.setId(resultSet.getLong("id"));
                order.setSendId(usersService.findByID(resultSet.getLong("sender_id")));
                order.setRecipId(usersService.findByID(resultSet.getLong("recipient_id")));
                order.setMatch(resultSet.getBoolean("match"));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return order;
    }

    public void likeById(long senserId, long recipId){
        try (PreparedStatement prepStmt = dbHelper.getStmt
                ("INSERT INTO tb_orders(sender_id, recipient_id) VALUES (?,?)")){
            prepStmt.setLong(1, senserId);
            prepStmt.setLong(2, recipId);
            prepStmt.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public boolean checkId(long senserId, long recipId){
        try (PreparedStatement prepStmt  = dbHelper.getStmt
                ("SELECT recipient_id, sender_id FROM tb_orders WHERE sender_id = ? AND recipient_id = ?")){
            prepStmt.setLong(1, senserId);
            prepStmt.setLong(2, recipId);
            ResultSet resultSet = prepStmt.executeQuery();

            while (resultSet.next()){
                if (senserId == resultSet.getLong("sender_id")
                        & recipId == resultSet.getLong("recipient_id")){
                    return true;
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public List<Users> inWaiting(long senderId) {
        List<Users> usersList = new ArrayList<>();
        try (PreparedStatement prepStmt = dbHelper.getStmt
                ("SELECT * FROM tb_orders WHERE sender_id = ? AND match IS NULL ORDER BY id")){
            prepStmt.setLong(1,senderId);

            ResultSet resultSet = prepStmt.executeQuery();

            while (resultSet.next()) {
                Users users = new Users();
                usersList.add(usersService.findByID(resultSet.getLong("recipient_id")));
            }

            return usersList;
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Users> proposal(long senderId) {
        List<Users> usersList = new ArrayList<>();
        try (PreparedStatement prepStmt = dbHelper.getStmt
                ("SELECT sender_id FROM tb_orders WHERE recipient_id = ? AND match IS NULL")){
            prepStmt.setLong(1,senderId);
            ResultSet resultSet = prepStmt.executeQuery();

            while (resultSet.next()) {
                usersList.add(usersService.findByID(resultSet.getLong("sender_id")));
            }
            return usersList;

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void toAccept(int answer, long senserId, long recipId){
        if (answer == 1){
            try (PreparedStatement prepStmt = dbHelper.getStmt
                    ("UPDATE tb_orders SET match=true WHERE sender_id = ? AND recipient_id = ?")){
                prepStmt.setLong(1, recipId);
                prepStmt.setLong(2, senserId);
                prepStmt.executeUpdate();

            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }else {
            try (PreparedStatement prepStmt = dbHelper.getStmt
                    ("UPDATE tb_orders SET match=false WHERE sender_id = ? AND recipient_id = ?")) {
                prepStmt.setLong(1, recipId);
                prepStmt.setLong(2, senserId);
                prepStmt.executeUpdate();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public List<Users> mutually(long senderId) {
        List<Users> usersList = new ArrayList<>();

        try (PreparedStatement prepStmt = dbHelper.getStmt
                ("SELECT * FROM tb_orders WHERE sender_id = ? AND match = TRUE")){
            prepStmt.setLong(1, senderId);

            ResultSet resultSet = prepStmt.executeQuery();
            while (resultSet.next()){
                usersList.add(usersService.findByID(resultSet.getLong("recipient_id")));
            }
//            return usersList;

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
//        return null;

        try (PreparedStatement prepStmt3 = dbHelper.getStmt
                ("SELECT * FROM tb_orders WHERE recipient_id = ? AND match = TRUE")){
            prepStmt3.setLong(1, senderId);

            ResultSet resultSet3 = prepStmt3.executeQuery();
            while (resultSet3.next()){
                usersList.add(usersService.findByID(resultSet3.getLong("sender_id")));
            }
            return usersList;

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Users> notMutually(long senderId) {
        List<Users> usersList = new ArrayList<>();
        try (PreparedStatement prepStmt = dbHelper.getStmt
                ("SELECT * FROM tb_orders WHERE sender_id = ? AND match = FALSE")){
            prepStmt.setLong(1, senderId);

            ResultSet resultSet = prepStmt.executeQuery();

            while (resultSet.next()){
                usersList.add(usersService.findByID(resultSet.getLong("recipient_id")));
            }
            return usersList;

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
