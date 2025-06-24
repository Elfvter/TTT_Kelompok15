package GraphicTTTFinal;

import GraphicTTTFinal.DatabaseConnection;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    public interface LoginCallback {
        void onLoginSuccess(String username);
    }

    private final LoginCallback callback;

    public LoginPanel(LoginCallback callback) {
        this.callback = callback;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("Welcome to Tic Tac Toe");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JTextField usernameField = new JTextField(12);
        JPasswordField passwordField = new JPasswordField(12);

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton skipButton = new JButton("Skip Login");

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridwidth = 2; gbc.gridx = 0; gbc.gridy = 0;
        add(titleLabel, gbc);

        gbc.gridy++; gbc.gridwidth = 1;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; add(usernameField, gbc);

        gbc.gridy++; gbc.gridx = 0;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; add(passwordField, gbc);

        gbc.gridy++; gbc.gridx = 0;
        add(loginButton, gbc);
        gbc.gridx = 1;
        add(registerButton, gbc);

        gbc.gridy++; gbc.gridwidth = 2; gbc.gridx = 0;
        add(skipButton, gbc);

        loginButton.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());
            if (DatabaseConnection.verifyLogin(user, pass)) {
                JOptionPane.showMessageDialog(this, "Login sukses!");
                callback.onLoginSuccess(user);
            } else {
                JOptionPane.showMessageDialog(this, "Login gagal. Coba lagi.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());
            if (DatabaseConnection.registerUser(user, pass)) {
                JOptionPane.showMessageDialog(this, "Registrasi berhasil! Silakan login.");
            } else {
                JOptionPane.showMessageDialog(this, "Username sudah digunakan.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        skipButton.addActionListener(e -> callback.onLoginSuccess(null));
    }
}
