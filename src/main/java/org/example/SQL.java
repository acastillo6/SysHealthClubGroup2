package org.example;

import java.sql.*;
import java.time.LocalDate;
// Use this code snippet in your app.
// If you need more information about configurations or implementing the sample
// code, visit the AWS docs:
// https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/home.html

// Make sure to import the following packages in your code
//import com.amazonaws.regions.Region;
//import com.amazonaws.services.secretsmanager.AWSSecretsManagerClient;
//import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
//import com.amazonaws.services.secretsmanager.model.GetSecretValueResponse;


public class SQL {
    public static void main(String[] args) {

        String url = "jdbc:mysql:aws://sysenghealthclub.cmrd2f4vkt0f.us-east-2.rds.amazonaws.com:3306/sysenghealthclub";
        String localhost = "jdbc:mysql://localhost:3306/sysenghealthclub";
        String username = "nczap";
        String password = "group2healthclub";

        Connection con = null;
        try {
            Class.forName("software.aws.rds.jdbc.mysql.Driver");

            con = DriverManager.getConnection(url, username, password);

            Statement statement = con.createStatement();

            //String create_health_club_database = "CREATE DATABASE sysenghealthclub";

            /*String create_member_table = "CREATE TABLE hcmember ("
                    + "member_id INT PRIMARY KEY,"
                    + "firstname VARCHAR(20),"
                    + "lastname VARCHAR(20),"
                    + "phonenum VARCHAR(15),"
                    + "email VARCHAR(100),"
                    + "address VARCHAR(100),"
                    + "city VARCHAR(32),"
                    + "state VARCHAR(20),"
                    + "membership_type VARCHAR(20),"
                    + "last_visit DATE,"
                    + "expiration_date DATE)";*/

            //String drop_member_table = "DROP TABLE hcmember";

            String get_all_data = "SELECT * FROM hcmember;";

            ResultSet all_data = statement.executeQuery(get_all_data);

            printAllData(all_data);

            /*String insert_new_member = "INSERT INTO " +
                    "hcmember (member_id, firstname, lastname, phonenum, email, address, city, state, membership_type, last_visit, expiration_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement insert_member = con.prepareStatement(insert_new_member);

            insert_member.setInt(1,6);
            insert_member.setString(2, "John");
            insert_member.setString(3, "Joseph");
            insert_member.setString(4, "(445) 876-9024");
            insert_member.setString(5, "jj@yahoo.com");
            insert_member.setString(6, "1500 Rosemont Drive");
            insert_member.setString(7, "Lincoln");
            insert_member.setString(8, "Nebraska");
            insert_member.setString(9, "6 month");
            insert_member.setDate(10, Date.valueOf(LocalDate.of(2023,12,07)));
            insert_member.setDate(11, Date.valueOf(LocalDate.of(2024,01,07)));*/

            //insert_member.executeUpdate();

            //String renewal_query = "SELECT firstname, lastname, phonenum, email, expiration_date " +
                    //"FROM hcmember";

            //ResultSet renewal_data = statement.executeQuery(renewal_query);

            //printRenewalData(renewal_data);

            /*String check_in_query = "SELECT expiration_date " +
                    "FROM hcmember " +
                    "WHERE member_id = ";*/

            //ResultSet check_in_expiration_date = statement.executeQuery(check_in_query + "1");

            //printCheckInData(check_in_expiration_date);

            /*String inactive_member_query = "SELECT * " +
                    "FROM hcmember " +
                    "WHERE last_visit < ";*/

            //ResultSet inactive_members = statement.executeQuery(inactive_member_query + "20231108");

            //printAllData(inactive_members);

            /*String expiring_memberships_3_days = "SELECT * " +
                    "FROM hcmember " +
                    "WHERE expiration_date <  AND expiration date > ";

            String expiring_memberships_current_date = "SELECT * " +
                    "FROM hcmember " +
                    "WHERE expiration_date = ";*/







        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            try {
                con.close();
            } catch (NullPointerException | SQLException e) {
                System.out.println("No connection was made :(");
            }
        }
    }

    public static void printAllData(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.println(
                    "id: " + rs.getInt("member_id")
                            + "\nFirst Name: " + rs.getString("firstname")
                            + "\nLast Name: " + rs.getString("lastname")
                            + "\nPhone Number: " + rs.getString("phonenum")
                            + "\nEmail: " + rs.getString("email")
                            + "\nAddress: " + rs.getString("address")
                            + "\nCity: " + rs.getString("city")
                            + "\nState: " + rs.getString("state")
                            + "\nMembership Type: " + rs.getString("membership_type")
                            + "\nLast Visit: " + rs.getDate("last_visit")
                            + "\nExpiration Date: " + rs.getDate("expiration_date") + "\n"
            );
        }
    }

    public static void printRenewalData(ResultSet rs) throws SQLException {
        System.out.println("Renewal Data:");
        while (rs.next()) {
            System.out.println(
                    "First Name: " + rs.getString("firstname")
                            + "\nLast Name: " + rs.getString("lastname")
                            + "\nPhone Number: " + rs.getString("phonenum")
                            + "\nEmail: " + rs.getString("email")
                            + "\nExpiration Date: " + rs.getDate("expiration_date") + "\n"
            );
        }
    }

    public static void printCheckInData(ResultSet rs) throws SQLException {
        while(rs.next()) {
            System.out.println("Expiration Date: " + rs.getDate("expiration_date"));
        }
    }

        /*public static void getSecret() {

            String secretName = "rds-db-credentials/db-6FQYQNEGHVEL2T7KQOY55ECDNI/nczap/1701737280315";
            Region region = new Region("us-east-2");

            // Create a Secrets Manager client
            AWSSecretsManagerClient client = AWSSecretsManagerClient.builder()
                    .region(region)
                    .build();

            GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                    .secretId(secretName)
                    .build();

            GetSecretValueResponse getSecretValueResponse;

            try {
                getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
            } catch (Exception e) {
                // For a list of exceptions thrown, see
                // https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html
                throw e;
            }

            String secret = getSecretValueResponse.secretString();


    }*/
}


