package org.example;

import java.sql.*;

import java.time.LocalDate;

public class CheckIn {

    public static void main(String[] args){
        try {
            System.out.println(isAccountValid(2));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean isAccountValid(int id) throws SQLException, ClassNotFoundException {
        String url = "jdbc:mysql:aws://sysenghealthclub.cmrd2f4vkt0f.us-east-2.rds.amazonaws.com:3306/sysenghealthclub";
        String localhost = "jdbc:mysql://localhost:3306/sysenghealthclub";
        String username = "nczap";
        String password = "group2healthclub";

        Class.forName("software.aws.rds.jdbc.mysql.Driver");

        Connection con = DriverManager.getConnection(url, username, password);

        String getMemberExpiration = "SELECT expiration_date " +
                "FROM hcmember " +
                "WHERE member_id = " + id;

        Statement statement = con.createStatement();

        ResultSet expiration = statement.executeQuery(getMemberExpiration);

        expiration.next();
        Date expirationDateInitial = expiration.getDate("expiration_date");
        LocalDate expirationDate = expirationDateInitial.toLocalDate();

        System.out.println(expirationDate);

        LocalDate currentDate = LocalDate.now();

        System.out.println(currentDate);

        return (expirationDate.isBefore(currentDate));
    }
}
