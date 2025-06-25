package GraphicTTTDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BotGame extends JPanel {
    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private boolean hasShownDialog = false;
    private CardLayout cardLayout;
    private JPanel container;

    public BotGame(CardLayout cardLayout, JPanel container) {
        this.cardLayout = cardLayout;
        this.container = container;
        board = new Board();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentState != State.PLAYING) return;

                int row = e.getY() / Cell.SIZE;
                int col = e.getX() / Cell.SIZE;
                if (row < 0 || col < 0 || row >= Board.ROWS || col >= Board.COLS) return;

                if (board.cells[row][col].content == Seed.NO_SEED && currentPlayer == Seed.CROSS) {
                    currentState = board.stepGame(currentPlayer, row, col);
                    SoundEffect.DIE.play();
                    repaint();
                    updateGame();

                    if (currentState == State.PLAYING) {
                        currentPlayer = Seed.NOUGHT;
                        Timer botTimer = new Timer(600, evt -> {
                            botMove();
                        });
                        botTimer.setRepeats(false);
                        botTimer.start();
                    }
                }
            }
        });

        newGame();
    }

    public void newGame() {
        board.newGame();
        currentState = State.PLAYING;
        currentPlayer = Seed.CROSS;
        hasShownDialog = false;
        repaint();
    }

    private void botMove() {
        List<Point> empty = new ArrayList<>();
        for (int r = 0; r < Board.ROWS; r++) {
            for (int c = 0; c < Board.COLS; c++) {
                if (board.cells[r][c].content == Seed.NO_SEED) {
                    empty.add(new Point(r, c));
                }
            }
        }
        if (!empty.isEmpty()) {
            Point move = empty.get(new Random().nextInt(empty.size()));
            currentState = board.stepGame(currentPlayer, move.x, move.y);
            SoundEffect.DIE.play();
            repaint();

            // ⬇ Tambahkan ini supaya updateGame() jalan setelah semua selesai
            SwingUtilities.invokeLater(() -> {
                updateGame();
                if (currentState == State.PLAYING) {
                    currentPlayer = Seed.CROSS;
                }
            });
        }
    }


    private void updateGame() {
        if (currentState == State.CROSS_WON) {
            if (!hasShownDialog) showEndDialog("Pemenangnya adalah: X");
        } else if (currentState == State.NOUGHT_WON) {
            if (!hasShownDialog) showEndDialog("Pemenangnya adalah: O");
        } else if (currentState == State.DRAW) {
            if (!hasShownDialog) showEndDialog("Permainan berakhir seri!");
        }
    }

    private void showEndDialog(String message) {
        hasShownDialog = true;
        int option = JOptionPane.showOptionDialog(this, message + "\nMain lagi?",
                "Game Over", JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE, null,
                new String[]{"Main Lagi", "Menu"}, "Main Lagi");

        if (option == JOptionPane.YES_OPTION) {
            newGame();
        } else {
            cardLayout.show(container, "MENU");
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        board.paint(g);
    }
}
