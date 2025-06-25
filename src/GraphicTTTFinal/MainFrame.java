package GraphicTTTFinal;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public MainFrame() {
        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 450);
        setLocationRelativeTo(null);
        setResizable(false);

        // Background GIF
        JLabel backgroundLabel = new JLabel(new ImageIcon("resources/background.gif")); // Sesuaikan path
        backgroundLabel.setLayout(new BorderLayout());
        setContentPane(backgroundLabel);

        // CardLayout untuk berpindah antar panel
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setOpaque(false); // Supaya background GIF tetap kelihatan
        backgroundLabel.add(contentPanel, BorderLayout.CENTER);

        // Tambah panel menu
        MainMenuPanel menuPanel = new MainMenuPanel(this);
        contentPanel.add(menuPanel, "Menu");

        // Tampilkan menu pertama kali
        cardLayout.show(contentPanel, "Menu");

        setVisible(true);
    }

    // Panggil ini saat ingin mulai permainan baru
    public void showGame(boolean vsAI) {
        GamePanel gamePanel = new GamePanel(vsAI, this);
        contentPanel.add(gamePanel, "Game");
        cardLayout.show(contentPanel, "Game");
    }

    // Kembali ke menu utama
    public void showMenu() {
        cardLayout.show(contentPanel, "Menu");
    }
}
