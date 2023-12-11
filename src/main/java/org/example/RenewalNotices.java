package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDate;

public class RenewalNotices {

    private static final String JDBC_URL = "jdbc:mysql:aws://sysenghealthclub.cmrd2f4vkt0f.us-east-2.rds.amazonaws.com:3306/sysenghealthclub";
    private static final String USER = "nczap";
    private static final String PASSWORD = "group2healthclub";

    public static void main(String[] args) {
        try {
            Class.forName("software.aws.rds.jdbc.mysql.Driver");
            Connection con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            generateRenewalReport(con);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void generateRenewalReport(Connection con) throws SQLException {

        LocalDate tomorrow = LocalDate.now();
        tomorrow = tomorrow.plusDays(1);

        String inactiveMemberQuery = "SELECT firstname, lastname, phonenum, email, address, city, state, expiration_date " +
                "FROM hcmember " +
                "WHERE expiration_date = ?";

        PreparedStatement statement = con.prepareStatement(inactiveMemberQuery);

        statement.setDate(1, Date.valueOf(tomorrow));

        ResultSet rs = statement.executeQuery();

        String outputPath = "ExpiringMembers.txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {

            // Iterate through the result set and write each row to the file
            while (rs.next()) {
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");
                String phonenum = rs.getString("phonenum");
                String email = rs.getString("email");
                String address = rs.getString("address");
                String city = rs.getString("city");
                String state = rs.getString("state");
                String expiration_date = rs.getString("expiration_date");

                // Write the data to the file
                writer.println("First Name:" + firstname + "\n" +
                        "Last Name: " + lastname + "\n" +
                        "Phone Number: " + phonenum + "\n" +
                        "Email: " + email + "\n" +
                        "Address: " + address + "\n" +
                        "City: " + city + "\n" +
                        "State: " + state + "\n" +
                        "Expiration Date: " + expiration_date + "\n");
            }

            System.out.println("Results successfully written to " + outputPath);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
