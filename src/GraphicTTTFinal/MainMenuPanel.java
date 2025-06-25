package GraphicTTTFinal;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainMenuPanel extends JPanel {
    private Font minecraftFont;

    public MainMenuPanel(MainFrame parent) {
        setOpaque(false);
        setLayout(new GridBagLayout());

        loadMinecraftFont();

        JLabel titleLabel = new JLabel("TIC TAC TOE");
        if (minecraftFont != null) {
            titleLabel.setFont(minecraftFont.deriveFont(48f));
        } else {
            titleLabel.setFont(new Font("Minecraft", Font.BOLD, 48));
        }
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton onePlayer = new JButton("1 Player");
        JButton twoPlayer = new JButton("2 Player");

        Dimension buttonSize = new Dimension(200, 50);
        onePlayer.setPreferredSize(buttonSize);
        twoPlayer.setPreferredSize(buttonSize);

        onePlayer.addActionListener(e -> parent.showGame(true));
        twoPlayer.addActionListener(e -> parent.showGame(false));

        // Panel tombol
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(onePlayer);
        buttonPanel.add(twoPlayer);

        // Gabungkan semuanya dalam panel vertikal
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);
        container.add(titleLabel);
        container.add(Box.createVerticalStrut(30));
        container.add(buttonPanel);
        container.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(container);
    }

    private void loadMinecraftFont() {
        try {
            // Pastikan path-nya benar dan font sudah ada di folder resources/fonts
            minecraftFont = Font.createFont(Font.TRUETYPE_FONT, new File("Minecraft.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(minecraftFont);
        } catch (FontFormatException | IOException e) {
            System.out.println("Gagal memuat font Minecraft, menggunakan font default.");
            minecraftFont = null;
        }
    }
}
