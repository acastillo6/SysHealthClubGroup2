package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InactiveMembersList {

    // Database connection parameters
    private static final String JDBC_URL = "jdbc:mysql:aws://sysenghealthclub.cmrd2f4vkt0f.us-east-2.rds.amazonaws.com:3306/sysenghealthclub";
    private static final String USER = "nczap";
    private static final String PASSWORD = "group2healthclub";

    public static void main(String[] args) {
        // Retrieve member information from the database
        List<List<String>> members = retrieveMembersInformation();

        for(List<String> memberInformation: members) {
            // Print or use the retrieved member information
            System.out.println("\nMember Information:");
            for (String info : memberInformation) {
                System.out.println(info);
            }
        }
    }



    private static List<List<String>> retrieveMembersInformation() {
        List<List<String>> members = new ArrayList<>();

        // Database query to retrieve member information
        String sqlQuery = "SELECT * " +
                "FROM hcmember " +
                "WHERE last_visit <= ?";

        Connection connection = null;
        try {
            Class.forName("software.aws.rds.jdbc.mysql.Driver");

            // Establish a database connection
            connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);

            // Create a PreparedStatement with the SQL query
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            // Set the parameter for the query (current date - 30 days)
            Date currentDate = new Date();
            Date thirtyDaysAgo = new Date(currentDate.getTime() - (30L * 24 * 60 * 60 * 1000));
            java.sql.Date sqlDate = new java.sql.Date(thirtyDaysAgo.getTime());
            preparedStatement.setDate(1, sqlDate);



            // Execute the query and retrieve the result set
            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    // Retrieve member details from the result set
                    int membershipID = resultSet.getInt("member_ID");
                    String firstName = resultSet.getString("firstname");
                    String lastName = resultSet.getString("lastname");
                    String phoneNumber = resultSet.getString("phonenum");
                    String email = resultSet.getString("email");
                    String address = resultSet.getString("address");
                    String city = resultSet.getString("city");
                    String state = resultSet.getString("state");
                    String membershipType = resultSet.getString("membership_type");
                    Date lastVisit = resultSet.getDate("last_visit");
                    Date expirationDate = resultSet.getDate("expiration_date");
                    String password = resultSet.getString("password");

                    List<String> memberInformation = new ArrayList<>();
                    // Add member information to the list
                    memberInformation.add("Name: " + firstName + " " + lastName);
                    memberInformation.add("Phone Number: " + phoneNumber);
                    memberInformation.add("Email: " + email);
                    memberInformation.add("Address: " + address + ", " + city + ", " + state);
                    memberInformation.add("Membership ID: " + membershipID);
                    memberInformation.add("Membership Type: " + membershipType);
                    memberInformation.add("Last Visit: " + formatDate(lastVisit));
                    memberInformation.add("Expiration Date: " + formatDate(expirationDate));

                    members.add(memberInformation);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (NullPointerException | SQLException e) {
                e.printStackTrace();
            }
        }

        return members;
    }

    // Helper method to format a date
    private static String formatDate(Date date) {
        if (date != null) {
            return new SimpleDateFormat("yyyy-MM-dd").format(date);
        } else {
            return "N/A";
        }
    }
}
