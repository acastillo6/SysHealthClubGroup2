package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.*;
import java.awt.*;

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

        SwingUtilities.invokeLater(() -> {
            JFrame renewalReportFrame = new JFrame("Renewal Report");
            renewalReportFrame.setSize(800, 600);
            renewalReportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            renewalReportFrame.setLocationRelativeTo(null);

            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);

            try {
                String reportContent = buildReportContent(rs);
                textArea.setText(reportContent);

                try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {
                    writeToFile(rs, writer);
                    System.out.println("Results successfully written to " + outputPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            JScrollPane scrollPane = new JScrollPane(textArea);

            renewalReportFrame.add(scrollPane);
            renewalReportFrame.setVisible(true);
        });
    }

    private static String buildReportContent(ResultSet rs) throws SQLException {
        StringBuilder reportContent = new StringBuilder();
        while (rs.next()) {
            String firstname = rs.getString("firstname");
            String lastname = rs.getString("lastname");
            String phonenum = rs.getString("phonenum");
            String email = rs.getString("email");
            String address = rs.getString("address");
            String city = rs.getString("city");
            String state = rs.getString("state");
            String expiration_date = rs.getString("expiration_date");

            reportContent.append("First Name:").append(firstname).append("\n")
                    .append("Last Name: ").append(lastname).append("\n")
                    .append("Phone Number: ").append(phonenum).append("\n")
                    .append("Email: ").append(email).append("\n")
                    .append("Address: ").append(address).append("\n")
                    .append("City: ").append(city).append("\n")
                    .append("State: ").append(state).append("\n")
                    .append("Expiration Date: ").append(expiration_date).append("\n\n");
        }

        return reportContent.toString();
    }

    private static void writeToFile(ResultSet rs, PrintWriter writer) throws SQLException {
        while (rs.next()) {
            String firstname = rs.getString("firstname");
            String lastname = rs.getString("lastname");
            String phonenum = rs.getString("phonenum");
            String email = rs.getString("email");
            String address = rs.getString("address");
            String city = rs.getString("city");
            String state = rs.getString("state");
            String expiration_date = rs.getString("expiration_date");

            writer.println("First Name:" + firstname + "\n" +
                    "Last Name: " + lastname + "\n" +
                    "Phone Number: " + phonenum + "\n" +
                    "Email: " + email + "\n" +
                    "Address: " + address + "\n" +
                    "City: " + city + "\n" +
                    "State: " + state + "\n" +
                    "Expiration Date: " + expiration_date + "\n");
        }
    }
}
