import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Date;



public class form extends JDialog {
    private JTextField tfname;
    private JTextField tfdob;
    private JTextField tfemail;
    private JTextField tfaddress;
    private JPasswordField pfpassword;
    private JPasswordField pfconfirmpassword;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPanel registerPanel;

    public form(JFrame parent) {
        super(parent);
        setTitle("Create a new account");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });




setVisible(true);
    }

    private void registerUser() {
        String name = tfname.getText();
        String dob = tfdob.getText();
        String email = tfemail.getText();
        String address = tfaddress.getText();
        String password = String.valueOf(pfpassword.getPassword());
        String confirmPassword = String.valueOf(pfconfirmpassword.getPassword());


        if (name.isEmpty() || email.isEmpty() || dob.isEmpty()  || address.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Please enter all fields",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;

        }
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Confirm Password does not match",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);
            return;


        }
        form = addUserToDatabase(name, email, String.valueOf(dob), address, password);
        if (form != null) {
            dispose();

        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to register new user",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
        }


    }

    public user form;

    private user addUserToDatabase(String name, String email, String dob, String address, String password) {
        user form = null;
        final String DBurl = "jdbc:mysql://localhost:3306/user";
        final String USERNAME = "root";
        final String PASSWORD = "8777487938";

        try {
            Connection conn = DriverManager.getConnection(DBurl, USERNAME, PASSWORD);
            String sql = "INSERT INTO form (name, dob, email, address, password)" + "VALUES(?, ?, ?, ?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setDate(2, Date.valueOf(String.valueOf(dob)));
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, password);
            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0) {
                form = new user();
                form.name = name;
                form.dob = dob;
                form.email = email;
                form.address = address;
                form.password = password;

            }

            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return form;

    }

    public static void main(String[] args) {
        form myForm = new form(null);

        user user = myForm.form;
        if (user != null) {
            System.out.println("Successful registration of: " + user.name);
        } else {
            System.out.println("Registration cancelled");
        }


    }
}