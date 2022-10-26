package com.lewickiy.coffeeboardapp.database.local;

import com.lewickiy.coffeeboardapp.database.discount.Discount;
import com.lewickiy.coffeeboardapp.database.outlet.Outlet;
import com.lewickiy.coffeeboardapp.database.paymentType.PaymentType;
import com.lewickiy.coffeeboardapp.database.product.Product;
import com.lewickiy.coffeeboardapp.database.product.ProductCategory;
import com.lewickiy.coffeeboardapp.database.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static com.lewickiy.coffeeboardapp.database.Query.*;
import static com.lewickiy.coffeeboardapp.database.discount.DiscountList.discounts;
import static com.lewickiy.coffeeboardapp.database.outlet.OutletList.outlets;
import static com.lewickiy.coffeeboardapp.database.paymentType.PaymentTypeList.paymentTypes;
import static com.lewickiy.coffeeboardapp.database.product.ProductCategoryList.productCategories;
import static com.lewickiy.coffeeboardapp.database.product.ProductList.products;
import static com.lewickiy.coffeeboardapp.database.user.UserList.users;

public class SyncLocalDB {
    static final String LOCAL_DB = "local_database";
    static final String NETWORK_DB = "network_database";

    public static void syncUsersList() throws SQLException {
        System.out.println("Start " + new Object(){}.getClass().getEnclosingMethod().getName() + "();");

        ResultSet resultSet = selectAllFromSql(NETWORK_DB,"user");
        while(resultSet.next()) {
            int userId = resultSet.getInt("user_id");
            String login = resultSet.getString("login");
            String password = resultSet.getString("password");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String patronymic = resultSet.getString("patronymic");
            Date birthday = resultSet.getDate("birthday");
            String phone = resultSet.getString("phone");
            boolean administrator = resultSet.getBoolean("administrator");
            boolean activeStuff = resultSet.getBoolean("active_stuff");
            users.add(new User(userId, login, password, firstName, lastName, patronymic, birthday, phone, administrator, activeStuff));

        }
        resultSet.close();

        deleteFromSql(LOCAL_DB, "user", "delete");

        for (User user : users) {
            insertToSql(LOCAL_DB,"user", "user_id, "
                    + "login, "
                    + "first_name, "
                    + "last_name, "
                    + "patronymic, "
                    + "birthday, "
                    + "phone, "
                    + "password, "
                    + "administrator, "
                    + "active_stuff) VALUES ('"
                    + user.getUserId() + "', '"
                    + user.getLogin() + "', '"
                    + user.getFirstName() + "', '"
                    + user.getLastName() + "', '"
                    + user.getPatronymic() + "', '"
                    + user.getBirthday() + "', '"
                    + user.getPhone() + "', '"
                    + user.getPassword() + "', '"
                    + user.isAdministrator() + "', '"
                    + user.isActiveStuff() + "'");
        }
    }

    public static void syncOutletsList() throws SQLException {
        System.out.println("Start " + new Object(){}.getClass().getEnclosingMethod().getName() + "();");

        ResultSet resultSet = selectAllFromSql(NETWORK_DB,"outlet");
        while(resultSet.next()) {
            int outletId = resultSet.getInt("outlet_id");
            String outlet = resultSet.getString("outlet");
            outlets.add(new Outlet(outletId, outlet));

        }
        resultSet.close();

        deleteFromSql(LOCAL_DB, "outlet", "delete");

        for (Outlet outlet : outlets) {
            insertToSql(LOCAL_DB,"outlet", "outlet_id, "
                    + "outlet) VALUES ('"
                    + outlet.getOutletId() + "', '"
                    + outlet.getOutlet() + "'");
        }
    }

    public static void syncPaymentTypesList() throws SQLException {
        System.out.println("Start " + new Object(){}.getClass().getEnclosingMethod().getName() + "();");

        ResultSet resultSet = selectAllFromSql(NETWORK_DB,"paymenttype");
        while(resultSet.next()) {
            int paymenttypeId = resultSet.getInt("paymenttype_id");
            String paymenttype = resultSet.getString("paymenttype");
            paymentTypes.add(new PaymentType(paymenttypeId, paymenttype));

        }
        resultSet.close();

        deleteFromSql(LOCAL_DB, "paymenttype", "delete");

        for (PaymentType paymentType : paymentTypes) {
            insertToSql(LOCAL_DB,"paymenttype", "paymenttype_id, "
                    + "paymenttype) VALUES ('"
                    + paymentType.getPaymentTypeId() + "', '"
                    + paymentType.getPaymentType() + "'");
        }
    }

    public static void syncProductCategoriesList() throws SQLException {
        System.out.println("Start " + new Object(){}.getClass().getEnclosingMethod().getName() + "();");

        ResultSet resultSet = selectAllFromSql(NETWORK_DB,"product_category");
        while(resultSet.next()) {
            int productCategoryId = resultSet.getInt("product_category_id");
            String productCategory = resultSet.getString("product_category");
            productCategories.add(new ProductCategory(productCategoryId, productCategory));

        }
        resultSet.close();

        deleteFromSql(LOCAL_DB, "product_category", "delete");

        for (ProductCategory productCategory : productCategories) {
            insertToSql(LOCAL_DB,"product_category", "product_category_id, "
                    + "product_category) VALUES ('"
                    + productCategory.getProductCategoryId() + "', '"
                    + productCategory.getProductCategory() + "'");
        }
    }

    public static void syncProductsList() throws SQLException {
        System.out.println("Start " + new Object(){}.getClass().getEnclosingMethod().getName() + "();");

        ResultSet resultSet = selectAllFromSql(NETWORK_DB,"product");
        while(resultSet.next()) {
            int productId = resultSet.getInt("product_id");
            String product = resultSet.getString("product");
            String description = resultSet.getString("description");
            int numberOfUnit = resultSet.getInt("number_of_unit");
            String unitOfMeasurement = resultSet.getString("unit_of_measurement");
            int category = resultSet.getInt("product_category_id");
            double price = resultSet.getDouble("price");
            products.add(new Product(productId, product, description, numberOfUnit, unitOfMeasurement, category, price));
        }
        resultSet.close();

        deleteFromSql(LOCAL_DB, "product", "delete");

        for (Product product : products) {
            insertToSql(LOCAL_DB,"product", "product_id, "
                    + "product, "
                    + "description, "
                    + "number_of_unit, "
                    + "unit_of_measurement, "
                    + "product_category_id, "
                    + "price) VALUES ('"
                    + product.getProductId() + "', '"
                    + product.getProduct() + "', '"
                    + product.getDescription() + "', '"
                    + product.getNumberOfUnit() + "', '"
                    + product.getUnitOfMeasurement() + "', '"
                    + product.getCategory() + "', '"
                    + product.getPrice() + "'");
        }
    }

    public static void syncDiscountsList() throws SQLException {
        System.out.println("Start " + new Object(){}.getClass().getEnclosingMethod().getName() + "();");

        ResultSet resultSet = selectAllFromSql(NETWORK_DB,"discount");
        while(resultSet.next()) {
            int discountId = resultSet.getInt("discount_id");
            int discount = resultSet.getInt("discount");
            boolean active = resultSet.getBoolean("active");
            discounts.add(new Discount(discountId, discount, active));
        }
        resultSet.close();

        deleteFromSql(LOCAL_DB, "discount", "delete");

        for (Discount discount : discounts) {
            insertToSql(LOCAL_DB,"discount", "discount_id, "
                    + "discount, "
                    + "active) VALUES ('"
                    + discount.getDiscountId() + "', '"
                    + discount.getDiscount() + "', '"
                    + discount.isActive() + "'");
        }
    }
}