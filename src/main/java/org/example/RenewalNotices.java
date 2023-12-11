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

        LocalDate currentDate = LocalDate.now();
        currentDate = currentDate.minusMonths(1);

        String inactiveMemberQuery = "SELECT firstname, lastname, phonenum, email, address, city, state, last_visit " +
                "FROM hcmember " +
                "WHERE last_visit < ?";

        PreparedStatement statement = con.prepareStatement(inactiveMemberQuery);

        statement.setDate(1, Date.valueOf(currentDate));

        ResultSet rs = statement.executeQuery();

        String outputPath = "InactiveMembers.txt";

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
                String last_visit = rs.getString("last_visit");

                // Write the data to the file
                writer.println("First Name:" + firstname + "\n" +
                        "Last Name: " + lastname + "\n" +
                        "Phone Number: " + phonenum + "\n" +
                        "Email: " + email + "\n" +
                        "Address: " + address + "\n" +
                        "City: " + city + "\n" +
                        "State: " + state + "\n" +
                        "Last Visit: " + last_visit + "\n");
            }

            System.out.println("Results successfully written to " + outputPath + ".txt");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
