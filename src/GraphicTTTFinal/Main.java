package GraphicTTTFinal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final String TITLE = "Tic Tac Toe";

    private enum ViewState { MENU, PLAYING }

    private Board board;
    private State currentState;
    private Seed currentPlayer;

    private Image backgroundImage;

    private boolean isSinglePlayer = false;
    private ViewState currentView = ViewState.MENU;

    private JButton onePlayerButton, twoPlayerButton;
    private JLabel titleLabel;

    public Main() {
        ImageIcon bgIcon = new ImageIcon(getClass().getResource("background.gif"));
        backgroundImage = bgIcon.getImage();

        setLayout(null);

        titleLabel = new JLabel("TIC TAC TOE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Minecraft", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 80, Board.CANVAS_WIDTH, 60);
        add(titleLabel);

        onePlayerButton = new JButton("1 Player");
        onePlayerButton.setBounds((Board.CANVAS_WIDTH - 140) / 2, 180, 140, 40);
        onePlayerButton.setFont(new Font("Minecraft", Font.BOLD, 16));
        onePlayerButton.addActionListener(e -> {
            isSinglePlayer = true;
            startGame();
        });
        add(onePlayerButton);

        twoPlayerButton = new JButton("2 Player");
        twoPlayerButton.setBounds((Board.CANVAS_WIDTH - 140) / 2, 240, 140, 40);
        twoPlayerButton.setFont(new Font("Minecraft", Font.BOLD, 16));
        twoPlayerButton.addActionListener(e -> {
            isSinglePlayer = false;
            startGame();
        });
        add(twoPlayerButton);

        board = new Board();
        board.setOpaque(false);
        board.setBounds((Board.CANVAS_WIDTH - Board.CANVAS_WIDTH) / 2, 60, Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT);
        board.setAlignmentX(CENTER_ALIGNMENT);
        board.setAlignmentY(CENTER_ALIGNMENT);
        board.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentView != ViewState.PLAYING) return;

                int mouseX = e.getX();
                int mouseY = e.getY();

                if (mouseX >= Board.X_OFFSET && mouseX < Board.X_OFFSET + Board.COLS * Cell.SIZE &&
                        mouseY >= Board.Y_OFFSET && mouseY < Board.Y_OFFSET + Board.ROWS * Cell.SIZE) {

                    int row = (mouseY - Board.Y_OFFSET) / Cell.SIZE;
                    int col = (mouseX - Board.X_OFFSET) / Cell.SIZE;

                    if (currentState == State.PLAYING &&
                            board.cells[row][col].content == Seed.NO_SEED) {

                        currentState = board.stepGame(currentPlayer, row, col);

                        if (currentState == State.PLAYING) {
                            currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;

                            if (isSinglePlayer && currentPlayer == Seed.NOUGHT) {
                                makeAIMove();
                                currentPlayer = Seed.CROSS;
                            }
                        }

                        if (currentState == State.PLAYING) {
                            SoundEffect.STEVE.play();
                        } else {
                            SoundEffect.DIE.play();
                            showGameOverDialog(currentState);
                        }

                        repaint();
                        board.repaint();
                    }
                }
            }
        });
        board.setVisible(false);
        add(board);

        setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT));
        setAlignmentX(CENTER_ALIGNMENT);
        setAlignmentY(CENTER_ALIGNMENT);
    }

    private void startGame() {
        currentView = ViewState.PLAYING;
        titleLabel.setVisible(false);
        onePlayerButton.setVisible(false);
        twoPlayerButton.setVisible(false);
        board.setVisible(true);

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int boardX = (panelWidth - Board.CANVAS_WIDTH) / 2;
        int boardY = (panelHeight - Board.CANVAS_HEIGHT) / 2;
        board.setBounds(boardX, boardY, Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT);

        revalidate();
        repaint();

        newGame();
    }

    private void backToMenu() {
        currentView = ViewState.MENU;
        titleLabel.setVisible(true);
        onePlayerButton.setVisible(true);
        twoPlayerButton.setVisible(true);
        board.setVisible(false);
    }

    public void newGame() {
        board.newGame();
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
        repaint();
        board.repaint();
    }

    private void showGameOverDialog(State finalState) {
        SwingUtilities.invokeLater(() -> {
            String message;
            if (finalState == State.CROSS_WON) {
                message = "Permainan selesai!\nPemenang: Steve";
            } else if (finalState == State.NOUGHT_WON) {
                message = "Permainan selesai!\nPemenang: Skeleton";
            } else {
                message = "Permainan berakhir seri!";
            }

            int option = JOptionPane.showOptionDialog(
                    this,
                    message + "\nMau main lagi?",
                    "Permainan Selesai",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[]{"Main Lagi", "Kembali ke Menu"},
                    "Main Lagi"
            );
            if (option == JOptionPane.YES_OPTION) {
                newGame();
            } else {
                backToMenu();
            }
        });
    }

    private void makeAIMove() {
        AiPlayer ai = new AiPlayer(Seed.NOUGHT);
        Point move = ai.nextMove(board);

        if (move != null) {
            int row = move.y;
            int col = move.x;
            currentState = board.stepGame(Seed.NOUGHT, row, col);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            float alpha = 0.75f;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            int imgWidth = backgroundImage.getWidth(this);
            int imgHeight = backgroundImage.getHeight(this);
            int x = (getWidth() - imgWidth) / 2;
            int y = (getHeight() - imgHeight) / 2;
            g2d.drawImage(backgroundImage, x, y, this);
            g2d.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(TITLE);
            frame.setContentPane(new Main());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
