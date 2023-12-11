package org.example;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RenewMembership {
    private static final String JDBC_URL = "jdbc:mysql:aws://sysenghealthclub.cmrd2f4vkt0f.us-east-2.rds.amazonaws.com:3306/sysenghealthclub";
    private static final String USER = "nczap";
    private static final String PASSWORD = "group2healthclub";
    public static void main(String[] args){
        try {
            Class.forName("software.aws.rds.jdbc.mysql.Driver");
            Connection con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            renewMembership(2, "3 month",con);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void renewMembership(int id, String membershipType, Connection con)throws SQLException{
        String memberExpiration = "SELECT expiration_date " +
                "FROM hcmember " +
                "WHERE member_id = ?";

        PreparedStatement statement1 = con.prepareStatement(memberExpiration);

        statement1.setInt(1,id);

        ResultSet rs = statement1.executeQuery();

        if(!rs.next()){
            System.out.println("ID does not exist!");
            return;
        }

        LocalDate expirationDate = rs.getDate("expiration_date").toLocalDate();

        LocalDate currentDate = LocalDate.now();

        System.out.println(expirationDate);
        System.out.println(currentDate);

        LocalDate baseDate = null;
        if(currentDate.isAfter(expirationDate))
            baseDate = currentDate;
        else
            baseDate = expirationDate;

        System.out.println(baseDate);

        if(membershipType.equals("3 month"))
            baseDate = baseDate.plus(3, ChronoUnit.MONTHS);
        else if (membershipType.equals("6 month"))
            baseDate = baseDate.plus(6, ChronoUnit.MONTHS);
        else if  (membershipType.equals("1 year"))
            baseDate = baseDate.plus(1, ChronoUnit.YEARS);

        System.out.println(baseDate);

        String updateExpirationDate = "UPDATE hcmember " +
                "SET expiration_date = ?" +
                "WHERE member_id = ?";

        PreparedStatement statement2 = con.prepareStatement(updateExpirationDate);

        statement2.setDate(1, Date.valueOf(baseDate));
        statement2.setInt(2,id);

        int rowsAffected = statement2.executeUpdate();
        System.out.println("Rows Affected: " + rowsAffected);

        System.out.println("Your " + membershipType + " membership has been added to your account!");
    }
}
