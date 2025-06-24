package GraphicTTTFinal;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    public interface MenuCallback {
        void onPlayVsPlayer();
        void onPlayVsBot();
        void onLogout();
    }

    public MainMenuPanel(MenuCallback callback) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel title = new JLabel("Tic Tac Toe Menu");
        title.setFont(new Font("Poppins", Font.BOLD, 20));

        JButton btnVsPlayer = new JButton("2 Pemain");
        JButton btnVsBot = new JButton("1 Pemain");
        JButton btnLogout = new JButton("Logout");

        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.gridx = 0; gbc.gridy = 0;
        add(title, gbc);

        gbc.gridy++;
        add(btnVsPlayer, gbc);

        gbc.gridy++;
        add(btnVsBot, gbc);

        gbc.gridy++;
        add(btnLogout, gbc);

        btnVsPlayer.addActionListener(e -> callback.onPlayVsPlayer());
        btnVsBot.addActionListener(e -> callback.onPlayVsBot());
        btnLogout.addActionListener(e -> callback.onLogout());
    }
}
