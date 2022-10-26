package com.lewickiy.coffeeboardapp.database.user;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.lewickiy.coffeeboardapp.database.Query.selectAllFromSql;

public class UserList {
    public static ArrayList<User> users = new ArrayList<>();
    /**
     * Здесь есть объект "Текущий пользователь", с которым дальше работает программа.
     * Данные о нём заполняются при входе в систему, после верификации.
     * На момент запуска этот пользователь не имеет никаких данных (см. ниже),
     * но после прохождения верификации, данные о нём заполняются из ArrayList users.
     * В последующем я буду работать с этими данными в системе при создании продаж,
     * SellerController
     * подсчёта выручки и прочего.
     */
    public static User currentUser = new User(0
            , "unknown"
            , "noPassword"
            , "noName"
            , "noSurname"
            , "noPhone"
            ,  false
            , false);

    public static void createUsersList() throws SQLException, ParseException {
        ResultSet resultSet = selectAllFromSql("local_database","user");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        while(resultSet.next()) {
            int userId = resultSet.getInt("user_id");
            String login = resultSet.getString("login");
            String password = resultSet.getString("password");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String patronymic = resultSet.getString("patronymic");
            Date birthday = formatter.parse(resultSet.getString("birthday"));
            String phone = resultSet.getString("phone");
            boolean administrator = resultSet.getBoolean("administrator");
            boolean activeStuff = resultSet.getBoolean("active_stuff");
            users.add(new User(userId, login, password, firstName, lastName, patronymic, birthday, phone, administrator, activeStuff));
        }
    }
}