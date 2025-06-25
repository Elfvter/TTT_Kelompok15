package GraphicTTTFinal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel {
    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private boolean vsAI;
    private AiPlayer ai;
    private MainFrame parent;

    public GamePanel(boolean vsAI, MainFrame parent) {
        this.vsAI = vsAI;
        this.parent = parent;
        if (vsAI) ai = new AiPlayer(Seed.NOUGHT);

        board = new Board();
        newGame();

        setOpaque(false);
        setLayout(new BorderLayout());


        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (currentState == State.PLAYING) {
                    int row = e.getY() / Cell.SIZE;
                    int col = e.getX() / Cell.SIZE;
                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                            && board.cells[row][col].content == Seed.NO_SEED) {
                        step(row, col);
                        repaint();

                        // AI bergerak setelah pemain, dengan delay 500ms
                        if (vsAI && currentState == State.PLAYING && currentPlayer == Seed.NOUGHT) {
                            Timer timer = new Timer(2000, new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    Point move = ai.nextMove(board);
                                    step(move.y, move.x);
                                    repaint();
                                }
                            });
                            timer.setRepeats(false);
                            timer.start();
                        }
                    }
                } else {
                    showResultPopup();
                }
            }
        });
    }

    private void step(int row, int col) {
        currentState = board.stepGame(currentPlayer, row, col);
        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
    }

    private void newGame() {
        board.newGame();
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
    }

    private void showResultPopup() {
        String msg;
        if (currentState == State.CROSS_WON) msg = "X menang!";
        else if (currentState == State.NOUGHT_WON) msg = vsAI ? "Bot menang!" : "O menang!";
        else msg = "Hasilnya seri!";

        int opt = JOptionPane.showOptionDialog(parent, msg, "Game Over",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, new String[]{"Main Lagi", "Menu Utama"}, "Main Lagi");

        if (opt == JOptionPane.YES_OPTION) parent.showGame(vsAI);
        else parent.showMenu();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        board.paint(g);
    }
}
