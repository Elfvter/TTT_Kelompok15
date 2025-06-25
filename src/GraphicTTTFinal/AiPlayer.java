package GraphicTTTFinal;

import java.awt.*;
import javax.swing.*;

public class AiPlayer {
    private Seed me, opp;

    public AiPlayer(Seed me) {
        this.me = me;
        this.opp = (me == Seed.CROSS ? Seed.NOUGHT : Seed.CROSS);
    }

    public void moveWithDelay(Board board, int delayMillis, java.util.function.Consumer<Point> callback) {
        Timer timer = new Timer(delayMillis, e -> {
            Point move = nextMove(board);
            callback.accept(move); // Kirim hasil langkah ke pemanggil
        });
        timer.setRepeats(false);
        timer.start();
    }

    public Point nextMove(Board board) {
        int bestScore = Integer.MIN_VALUE;
        Point bestMove = null;

        for (int r = 0; r < Board.ROWS; r++) {
            for (int c = 0; c < Board.COLS; c++) {
                if (board.cells[r][c].content == Seed.NO_SEED) {
                    board.cells[r][c].content = me;
                    int score = minimax(board, 0, false);
                    board.cells[r][c].content = Seed.NO_SEED;

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new Point(c, r);
                    }
                }
            }
        }

        return bestMove != null ? bestMove : new Point(0, 0);
    }

    private int minimax(Board board, int depth, boolean isMaximizing) {
        State state = board.checkGameState();

        if (state == State.CROSS_WON) {
            return (me == Seed.CROSS) ? 10 - depth : depth - 10;
        } else if (state == State.NOUGHT_WON) {
            return (me == Seed.NOUGHT) ? 10 - depth : depth - 10;
        } else if (state == State.DRAW) {
            return 0;
        }

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int r = 0; r < Board.ROWS; r++) {
            for (int c = 0; c < Board.COLS; c++) {
                if (board.cells[r][c].content == Seed.NO_SEED) {
                    board.cells[r][c].content = isMaximizing ? me : opp;
                    int score = minimax(board, depth + 1, !isMaximizing);
                    board.cells[r][c].content = Seed.NO_SEED;

                    if (isMaximizing) {
                        bestScore = Math.max(score, bestScore);
                    } else {
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
        }

        return bestScore;
    }
}
