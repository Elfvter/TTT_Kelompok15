package GraphicTTTFinal;

import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
    public static final int ROWS = 3;
    public static final int COLS = 3;
    public static final int CANVAS_WIDTH = 800;
    public static final int CANVAS_HEIGHT = 450;
    public static final int GRID_WIDTH = 8;
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;
    public static final Color COLOR_GRID = new Color(54, 35, 16);

    public static final int GRID_WIDTH_PIXEL = Cell.SIZE * COLS;
    public static final int GRID_HEIGHT_PIXEL = Cell.SIZE * ROWS;

    // Offset untuk membuat grid benar-benar di tengah
    public static final int X_OFFSET = (CANVAS_WIDTH - GRID_WIDTH_PIXEL) / 2;
    public static final int Y_OFFSET = (CANVAS_HEIGHT - GRID_HEIGHT_PIXEL) / 2;

    Cell[][] cells;

    public Board() {
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        setBackground(Color.WHITE);
        initGame();
    }

    public void initGame() {
        cells = new Cell[ROWS][COLS];
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col] = new Cell(row, col);
            }
        }
    }

    public void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].newGame();
            }
        }
        repaint();
    }

    public State stepGame(Seed player, int selectedRow, int selectedCol) {
        cells[selectedRow][selectedCol].content = player;

        if (cells[selectedRow][0].content == player &&
                cells[selectedRow][1].content == player &&
                cells[selectedRow][2].content == player ||
                cells[0][selectedCol].content == player &&
                        cells[1][selectedCol].content == player &&
                        cells[2][selectedCol].content == player ||
                selectedRow == selectedCol &&
                        cells[0][0].content == player &&
                        cells[1][1].content == player &&
                        cells[2][2].content == player ||
                selectedRow + selectedCol == 2 &&
                        cells[0][2].content == player &&
                        cells[1][1].content == player &&
                        cells[2][0].content == player) {
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        } else {
            for (int row = 0; row < ROWS; ++row) {
                for (int col = 0; col < COLS; ++col) {
                    if (cells[row][col].content == Seed.NO_SEED) {
                        return State.PLAYING;
                    }
                }
            }
            return State.DRAW;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw border kotak luar grid (supaya grid tampak tertutup)
        g.setColor(COLOR_GRID);
        g.fillRect(X_OFFSET - GRID_WIDTH_HALF, Y_OFFSET - GRID_WIDTH_HALF,
                GRID_WIDTH_PIXEL + GRID_WIDTH, GRID_WIDTH); // Top
        g.fillRect(X_OFFSET - GRID_WIDTH_HALF, Y_OFFSET + GRID_HEIGHT_PIXEL - GRID_WIDTH_HALF,
                GRID_WIDTH_PIXEL + GRID_WIDTH, GRID_WIDTH); // Bottom
        g.fillRect(X_OFFSET - GRID_WIDTH_HALF, Y_OFFSET - GRID_WIDTH_HALF,
                GRID_WIDTH, GRID_HEIGHT_PIXEL + GRID_WIDTH); // Left
        g.fillRect(X_OFFSET + GRID_WIDTH_PIXEL - GRID_WIDTH_HALF, Y_OFFSET - GRID_WIDTH_HALF,
                GRID_WIDTH, GRID_HEIGHT_PIXEL + GRID_WIDTH); // Right

        // Draw grid lines
        for (int row = 1; row < ROWS; ++row) {
            int y = Y_OFFSET + row * Cell.SIZE - GRID_WIDTH_HALF;
            g.fillRoundRect(X_OFFSET, y, GRID_WIDTH_PIXEL, GRID_WIDTH, GRID_WIDTH, GRID_WIDTH);
        }
        for (int col = 1; col < COLS; ++col) {
            int x = X_OFFSET + col * Cell.SIZE - GRID_WIDTH_HALF;
            g.fillRoundRect(x, Y_OFFSET, GRID_WIDTH, GRID_HEIGHT_PIXEL, GRID_WIDTH, GRID_WIDTH);
        }

        // Draw cells (with offset)
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate(X_OFFSET, Y_OFFSET);
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].paint(g2d);
            }
        }
        g2d.dispose();
    }
    public State checkGameState() {
        // Cek baris dan kolom
        for (int i = 0; i < 3; i++) {
            if (cells[i][0].content != Seed.NO_SEED &&
                    cells[i][0].content == cells[i][1].content &&
                    cells[i][1].content == cells[i][2].content) {
                return (cells[i][0].content == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
            }

            if (cells[0][i].content != Seed.NO_SEED &&
                    cells[0][i].content == cells[1][i].content &&
                    cells[1][i].content == cells[2][i].content) {
                return (cells[0][i].content == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
            }
        }

        // Cek diagonal
        if (cells[0][0].content != Seed.NO_SEED &&
                cells[0][0].content == cells[1][1].content &&
                cells[1][1].content == cells[2][2].content) {
            return (cells[0][0].content == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        }

        if (cells[0][2].content != Seed.NO_SEED &&
                cells[0][2].content == cells[1][1].content &&
                cells[1][1].content == cells[2][0].content) {
            return (cells[0][2].content == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        }

        // Cek apakah masih ada langkah
        for (int r = 0; r < Board.ROWS; r++) {
            for (int c = 0; c < Board.COLS; c++) {
                if (cells[r][c].content == Seed.NO_SEED) {
                    return State.PLAYING;
                }
            }
        }

        return State.DRAW;
    }

}
