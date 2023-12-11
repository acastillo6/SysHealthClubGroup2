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

    private JPasswordField passwordField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField phoneNumberField;
    private JTextField emailField;
    private JTextField addressField;
    private JTextField cityField;
    private JTextField stateField;

    private JFrame loginFrame;
    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;

    private JCheckBox membershipTypeCheckBox1;
    private JCheckBox membershipTypeCheckBox2;
    private JCheckBox membershipTypeCheckBox3;





    public HealthClubAccountApp() {
        setTitle("Health Club Account Creation");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(11, 2, 10, 10));

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

        mainPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        mainPanel.add(passwordField);

        JPanel membershipTypePanel = new JPanel(new GridLayout(0, 3, 10, 10));
        membershipTypeCheckBox1 = new JCheckBox("3 Month");
        membershipTypeCheckBox2 = new JCheckBox("6 Month");
        membershipTypeCheckBox3 = new JCheckBox("1 Year");
        membershipTypePanel.add(membershipTypeCheckBox1);
        membershipTypePanel.add(membershipTypeCheckBox2);
        membershipTypePanel.add(membershipTypeCheckBox3);
        membershipTypePanel.setPreferredSize(new Dimension(600, 50));
        addFieldToPanel(mainPanel, "Membership Type:", membershipTypePanel);


        String url = "jdbc:mysql:aws://sysenghealthclub.cmrd2f4vkt0f.us-east-2.rds.amazonaws.com:3306/sysenghealthclub";
        String username = "nczap";
        String password = "group2healthclub";

        try {
            Class.forName("software.aws.rds.jdbc.mysql.Driver");

            Connection con = DriverManager.getConnection(url, username, password);

            JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
            JButton createAccountButton = new JButton("Create Account");
            createAccountButton.addActionListener(e -> createAccount(con));

            // Add a "Login" button
            JButton loginButton = new JButton("Login");
            loginButton.addActionListener(e -> {
                showLoginFrame(con);
                dispose();

            });

            buttonsPanel.add(createAccountButton);
            buttonsPanel.add(loginButton);

            mainPanel.add(new JPanel());
            mainPanel.add(membershipTypePanel);
            mainPanel.add(new JPanel());


            mainPanel.add(buttonsPanel);


            add(mainPanel);

            setVisible(true);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showLoginFrame(Connection con) {
        loginFrame = new JFrame("Login");
        loginFrame.setSize(300, 150);
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2, 10, 10));

        loginPanel.add(new JLabel("ID:"));
        loginUsernameField = new JTextField();
        loginPanel.add(loginUsernameField);

        loginPanel.add(new JLabel("Password:"));
        loginPasswordField = new JPasswordField();
        loginPanel.add(loginPasswordField);

        JButton loginSubmitButton = new JButton("Login");
        loginSubmitButton.addActionListener(e -> {
            try {
                if (checkLogIn(con)) {
                    createWelcomeWindow(Integer.parseInt(loginUsernameField.getText()), con);
                    loginFrame.dispose();
                } else {
                    System.out.println("Incorrect login credentials. Please try again.");
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        JButton backButton = new JButton("Back to Account Creation");
        backButton.addActionListener(e -> {
            loginFrame.dispose();
            new HealthClubAccountApp();
        });




        loginFrame.add(loginPanel, BorderLayout.CENTER);
        loginFrame.add(loginSubmitButton, BorderLayout.EAST);  // Moved the "Login" button to the right
        loginFrame.add(backButton, BorderLayout.SOUTH);

        loginFrame.setVisible(true);
    }

    private void createWelcomeWindow(int userId, Connection con) {
        // con = null; // Remove this line

        JFrame welcomeFrame = new JFrame("Welcome");
        welcomeFrame.setSize(300, 150);
        welcomeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        welcomeFrame.setLocationRelativeTo(null);

        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new GridLayout(3, 1, 10, 10));

        // Assuming you have a method to get the user's first name based on their ID
        String firstName = getFirstNameById(userId, con);

        JLabel welcomeLabel = new JLabel("Welcome, " + firstName + "!");
        welcomePanel.add(welcomeLabel);

        JButton checkInButton = new JButton("Check In");
        checkInButton.addActionListener(e -> {
            // Add logic for the "Check In" button
            if (checkIn(userId)) {
                System.out.println("Check In successful");
            } else {
                System.out.println("Check In failed");
            }

        });
        welcomePanel.add(checkInButton);

        if (isMembershipExpired(userId)) {
            JButton renewMembershipButton = new JButton("Renew Membership");
            renewMembershipButton.addActionListener(e -> {

                System.out.println("Renew Membership button clicked");
            });
            welcomePanel.add(renewMembershipButton);
        }

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            welcomeFrame.dispose();
            showLoginFrame(con);
        });
        welcomePanel.add(logoutButton);

        welcomeFrame.add(welcomePanel);
        welcomeFrame.setVisible(true);
    }
    public boolean checkIn(int userId) {
        if (isMembershipExpired(userId)) {
            System.out.println("Cannot check in. Membership is expired.");
            return false;
        }

        String url = "jdbc:mysql:aws://sysenghealthclub.cmrd2f4vkt0f.us-east-2.rds.amazonaws.com:3306/sysenghealthclub";
        String username = "nczap";
        String password = "group2healthclub";

        try (Connection con = DriverManager.getConnection(url, username, password)) {
            String updateLastVisit = "UPDATE hcmember " +
                    "SET last_visit = ? " +
                    "WHERE member_id = ?;";

            java.util.Date currentDate = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());

            try (PreparedStatement preparedStatement = con.prepareStatement(updateLastVisit)) {
                preparedStatement.setDate(1, sqlDate);
                preparedStatement.setInt(2, userId);

                int rowsUpdated = preparedStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Check-in successful!");
                    return true;
                } else {
                    System.out.println("Check-in failed. Please try again.");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    private void handleLogout(Connection con) {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }





    private static boolean isMembershipExpired(int userId) {
        String url = "jdbc:mysql:aws://sysenghealthclub.cmrd2f4vkt0f.us-east-2.rds.amazonaws.com:3306/sysenghealthclub";
        String username = "nczap";
        String password = "group2healthclub";

        try (Connection con = DriverManager.getConnection(url, username, password)) {
            String getMemberExpiration = "SELECT expiration_date " +
                    "FROM hcmember " +
                    "WHERE member_id = ?";

            try (PreparedStatement preparedStatement = con.prepareStatement(getMemberExpiration)) {
                preparedStatement.setInt(1, userId);

                try (ResultSet expiration = preparedStatement.executeQuery()) {
                    if (!expiration.next()) {
                        System.out.println("Member Does Not Exist!");
                        return false;
                    }

                    LocalDate expirationDate = expiration.getDate("expiration_date").toLocalDate();
                    LocalDate currentDate = LocalDate.now();

                    return currentDate.isAfter(expirationDate);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getFirstNameById(int userId, Connection con) {
        String firstName = "";

        try {
            String getFirstNameQuery = "SELECT firstname FROM hcmember WHERE member_id = ?";
            try (PreparedStatement preparedStatement = con.prepareStatement(getFirstNameQuery)) {
                preparedStatement.setInt(1, userId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        firstName = resultSet.getString("firstname");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return firstName;
    }

    private void addFieldToPanel(JPanel panel, String label, JComponent component) {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rowPanel.add(new JLabel(label));
        rowPanel.add(component);
        panel.add(rowPanel);
    }

    private void createAccount(Connection con) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        String state = stateField.getText();
        String city = cityField.getText();
        String password = new String(passwordField.getPassword());

        boolean isMembershipType1Selected = membershipTypeCheckBox1.isSelected();
        boolean isMembershipType2Selected = membershipTypeCheckBox2.isSelected();
        boolean isMembershipType3Selected = membershipTypeCheckBox3.isSelected();

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        String membershipType = "";

        if (isMembershipType1Selected) {
            calendar.add(Calendar.MONTH, 3);
            membershipType = "3 month";
        }
        if (isMembershipType2Selected) {
            calendar.add(Calendar.MONTH, 6);
            membershipType = "6 month";
        }
        if (isMembershipType3Selected) {
            calendar.add(Calendar.YEAR, 1);
            membershipType = "1 year";
        }

        Date expirationDate = calendar.getTime();

        int id = getUniqueId(con);

        String insertNewMemberQuery = "INSERT INTO " +
                "hcmember (member_id, firstname, lastname, phonenum, email, address, city, state, membership_type, last_visit, expiration_date, password) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            insertNewMember.setString(12, password);

            int rowsAdded = insertNewMember.executeUpdate();

            System.out.println(rowsAdded);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

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

        String fileName = lastName + "," + firstName + ".txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("Password: " + password);
            writer.println(userInfo);
            System.out.println("Account created and information saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving user information to file");
        }
    }

    private boolean checkLogIn(Connection con) throws SQLException {
        int idInput = Integer.parseInt((loginUsernameField.getText()));
        String passwordInput = new String(loginPasswordField.getPassword());

        String getIDPasswordQuery = "SELECT password " +
                "FROM hcmember " +
                "WHERE member_id = " + idInput;

        try (Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery(getIDPasswordQuery)) {

            if (!rs.next()) {
                System.out.println("ID does not exist.");
                return false;
            }

            String passwordStored = rs.getString("password").trim();

            if (passwordInput.equals(passwordStored)) {
                System.out.println("Login Successful!");
                return true;
            }

            System.out.println("Incorrect Password.");
            return false;
        }
    }

    public static int getUniqueId(Connection con){
        Random random = new Random();
        List<Integer> memberIDs = new LinkedList<>();
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
        List<Integer> memberIDsList = new LinkedList<>();

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
                SwingUtilities.invokeLater(HealthClubAccountApp::new);
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
