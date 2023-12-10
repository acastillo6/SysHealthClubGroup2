package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class HealthClubAccountApp extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField phoneNumberField;
    private JTextField emailField;
    private JTextField addressField;
    private JTextField cityField;
    private JTextField stateField;
    private JTextField membershipTypeField;


    public HealthClubAccountApp() {
        setTitle("Health Club Account Creation");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(11, 1, 10, 10));

        mainPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        mainPanel.add(usernameField);

        mainPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        mainPanel.add(passwordField);

        mainPanel.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        mainPanel.add(firstNameField);

        mainPanel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        mainPanel.add(lastNameField);

        mainPanel.add(new JLabel("Phone Number:"));
        phoneNumberField = new JTextField();
        mainPanel.add(phoneNumberField);

        mainPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        mainPanel.add(emailField);

        mainPanel.add(new JLabel("Addresss:"));
        addressField = new JTextField();
        mainPanel.add(addressField);

        mainPanel.add(new JLabel("City:"));
        cityField = new JTextField();
        mainPanel.add(cityField);
  
        mainPanel.add(new JLabel("State:"));
        stateField = new JTextField();
        mainPanel.add(stateField);

        mainPanel.add(new JLabel("Membership Type:"));
        membershipTypeField = new JTextField();
        mainPanel.add(membershipTypeField);

        String url = "jdbc:mysql:aws://sysenghealthclub.cmrd2f4vkt0f.us-east-2.rds.amazonaws.com:3306/sysenghealthclub";
        String username = "nczap";
        String password = "group2healthclub";

        try {
            Class.forName("software.aws.rds.jdbc.mysql.Driver");

            Connection con = DriverManager.getConnection(url, username, password);

            JButton createAccountButton = new JButton("Create Account");
            createAccountButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    createAccount(con);
                }
            });

            mainPanel.add(createAccountButton);

            add(mainPanel);

            setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createAccount(Connection con) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        String state = stateField.getText();
        String city = cityField.getText();
        String membershipType = membershipTypeField.getText();

        int id = getUniqueId(con);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        Date expirationDate = calendar.getTime();
        Date currentDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


        String insertNewMemberQuery = "INSERT INTO " +
                "hcmember (member_id, firstname, lastname, phonenum, email, address, city, state, membership_type, last_visit, expiration_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement insertNewMember = con.prepareStatement(insertNewMemberQuery);

            insertNewMember.setInt(1,id);
            insertNewMember.setString(2, firstName);
            insertNewMember.setString(3, lastName);
            insertNewMember.setString(4, phoneNumber);
            insertNewMember.setString(5, email);
            insertNewMember.setString(6, address);
            insertNewMember.setString(7, city);
            insertNewMember.setString(8, state);
            insertNewMember.setString(9, membershipType);
            insertNewMember.setDate(10, new java.sql.Date(currentDate.getTime()));
            insertNewMember.setDate(11, new java.sql.Date(expirationDate.getTime()));

            int rowsAdded = insertNewMember.executeUpdate();

            System.out.println(rowsAdded);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String userInfo = "Expiration Date: " + dateFormat.format(expirationDate) + "\n"
                + "First Name: " + firstName + "\n"
                + "Last Name: " + lastName + "\n"
                + "Phone Number: " + phoneNumber + "\n"
                + "Email: " + email + "\n"
                + "Address: " + address + "\n"
                + "City: " + city + "\n"
                + "State: " + state + "\n"
                + "Last Login: " + dateFormat.format(currentDate) + "\n"
                + "Membership Type: " + membershipType + "\n"
                + "ID: " + id;

        String fileName = username + ".txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("Password: " + password);
            writer.println(userInfo);
            System.out.println("Account created and information saved to " + fileName);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Error saving user information to file");
        }
    }

    public static int getUniqueId(Connection con){
        Random random = new Random();
        List<Integer> memberIDs = new LinkedList<Integer>();
        try{
             memberIDs = getMemberIDs(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int newID;
        while(true){
            newID = random.nextInt(Integer.MAX_VALUE);
            boolean unique = true;
            for(int id: memberIDs){
                if(newID == id){
                    unique = false;
                    break;
                }
            }
            if(unique)
                break;
        }
        System.out.println("Your ID number is: " + newID);
        return newID;
    }

    public static List<Integer> getMemberIDs(Connection con) throws SQLException {
        String memberIdQuery = "SELECT member_id FROM hcmember;";
        Statement statement = con.createStatement();
        List<Integer> memberIDsList = new LinkedList<Integer>();

        ResultSet memberIDsRS = statement.executeQuery(memberIdQuery);

        while(memberIDsRS.next()){
            memberIDsList.add(memberIDsRS.getInt("member_id"));
        }

        return memberIDsList;

    }

    public static void main(String[] args) {
        String url = "jdbc:mysql:aws://sysenghealthclub.cmrd2f4vkt0f.us-east-2.rds.amazonaws.com:3306/sysenghealthclub";
        String username = "nczap";
        String password = "group2healthclub";
        try {
            Class.forName("software.aws.rds.jdbc.mysql.Driver");

            // Establish the connection
            try {
                Connection con = DriverManager.getConnection(url, username, password);
                SwingUtilities.invokeLater(() -> new HealthClubAccountApp());
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
