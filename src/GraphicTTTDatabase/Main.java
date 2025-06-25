package GraphicTTTDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_CROSS = new Color(239, 105, 80);
    public static final Color COLOR_NOUGHT = new Color(64, 154, 225);

    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private boolean hasShownDialog = false;
    private CardLayout cardLayout;
    private JPanel container;

    public Main(CardLayout cardLayout, JPanel container) {
        this.cardLayout = cardLayout;
        this.container = container;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX(), mouseY = e.getY();
                int row = mouseY / Cell.SIZE, col = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                            && board.cells[row][col].content == Seed.NO_SEED) {
                        currentState = board.stepGame(currentPlayer, row, col);
                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                    }

                    if (currentState == State.PLAYING) SoundEffect.DIE.play();
                    else if (currentState == State.DRAW) SoundEffect.EAT_FOOD.play();
                    else SoundEffect.EXPLODE.play();
                } else {
                    newGame();
                }
                repaint();
            }
        });

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        initGame();
        newGame();
    }

    public void initGame() {
        board = new Board();
    }

    public void newGame() {
        for (int r = 0; r < Board.ROWS; r++)
            for (int c = 0; c < Board.COLS; c++)
                board.cells[r][c].content = Seed.NO_SEED;
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
        hasShownDialog = false; // reset flag
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG);
        board.paint(g);

        if (currentState == State.DRAW && !hasShownDialog) {
            hasShownDialog = true;
            int option = JOptionPane.showOptionDialog(
                    this,
                    "Permainan berakhir seri!",
                    "Hasil",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[]{"Main Lagi", "Menu"},
                    "Main Lagi"
            );
            if (option == JOptionPane.YES_OPTION) newGame();
            else cardLayout.show(container, "MENU");
        } else if (currentState == State.CROSS_WON && !hasShownDialog) {
            hasShownDialog = true;
            int option = JOptionPane.showOptionDialog(
                    this,
                    "Pemenangnya adalah: X",
                    "Hasil",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[]{"Main Lagi", "Menu"},
                    "Main Lagi"
            );
            if (option == JOptionPane.YES_OPTION) newGame();
            else cardLayout.show(container, "MENU");
        } else if (currentState == State.NOUGHT_WON && !hasShownDialog) {
            hasShownDialog = true;
            int option = JOptionPane.showOptionDialog(
                    this,
                    "Pemenangnya adalah: O",
                    "Hasil",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[]{"Main Lagi", "Menu"},
                    "Main Lagi"
            );
            if (option == JOptionPane.YES_OPTION) newGame();
            else cardLayout.show(container, "MENU");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(TITLE);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30);

            CardLayout cardLayout = new CardLayout();
            JPanel container = new JPanel(cardLayout);

            Main gamePanel = new Main(cardLayout, container);
            BotGame botGamePanel = new BotGame(cardLayout, container);

            MainMenuPanel menuPanel = new MainMenuPanel(new MainMenuPanel.MenuCallback() {
                public void onPlayVsPlayer() {
                    gamePanel.newGame();
                    cardLayout.show(container, "GAME");
                }

                public void onPlayVsBot() {
                    botGamePanel.newGame();
                    cardLayout.show(container, "BOT");
                }

                public void onLogout() {
                    cardLayout.show(container, "LOGIN");
                }
            });

            LoginPanel loginPanel = new LoginPanel(username -> {
                cardLayout.show(container, "MENU");
            });

            container.add(loginPanel, "LOGIN");
            container.add(menuPanel, "MENU");
            container.add(gamePanel, "GAME");
            container.add(botGamePanel, "BOT");

            frame.setContentPane(container);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            cardLayout.show(container, "LOGIN");
        });
    }
}
