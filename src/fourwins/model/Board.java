package fourwins.model;

public class Board {
    private final Chip[][] board;

    public Board(int playerNumber) {
        this.board = new Chip[playerNumber * 3][playerNumber * 3 + 1];
    }

    public int getHeight() {
        return board.length;
    }

    public int getWidth() {
        return board[0].length;
    }

    public int placeChip(Chip chip, int position) {
        checkParameters(position);
        if (isColumnFull(position)) {
            return -1;
        } else {
            for (int row = board.length - 1; row >= 0 ; row--) {
                Chip candidate = board[row][position];
                if (candidate == null) {
                    board[row][position] = chip;
                    return row;
                }
            }
        }
        return -1;
    }

    private void checkParameters(int position) {
        if (position < 0 || position > board[0].length) {
            throw new IllegalArgumentException("illegal position.");
        }
    }

    private boolean isColumnFull(int col) {
        return board[0][col] != null;
    }

    public boolean isFull() {
        for (Chip[] row : board) {
            for (Chip chip : row) {
                if (chip == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isWon(int colPosition) {
        checkParameters(colPosition);
        int rowPosition = findPosition(colPosition);
        Player current = board[rowPosition][colPosition].player();

        int horizontalSum = checkChips(HORIZONTAL, rowPosition, colPosition, current);
        int verticalSum = checkChips(VERTICAL, rowPosition, colPosition, current);
        int diagonalSum = checkChips(DIAGONAL, rowPosition, colPosition, current);
        int invDiagonalSum = checkChips(INV_DIAGONAL, rowPosition, colPosition, current);
        return horizontalSum > 3 || verticalSum > 3 || diagonalSum > 3 || invDiagonalSum > 3;
    }

    private static final int HORIZONTAL = 0;
    private static final int VERTICAL = 1;
    private static final int DIAGONAL = 2;
    private static final int INV_DIAGONAL = 3;

    private int checkChips(int dimension, int row, int col, Player player) {
        int sum = 0;
        assert dimension >= HORIZONTAL && dimension <= INV_DIAGONAL;
        switch (dimension) {
            case HORIZONTAL -> sum = getSum(row, col, player, 0, 1);
            case VERTICAL -> sum = getSum(row, col, player, 1, 0);
            case DIAGONAL -> sum = getSum(row, col, player, 1, 1);
            case INV_DIAGONAL -> sum = getSum(row, col, player, -1 , 1);
        }
        return sum;
    }

    private int getSum(int row, int col, Player player, int rowBias, int colBias) {
        int sum = 1;
        for (int i = 1; i < 4; i++) {
            try {
                if (board[row + i * rowBias][col + i * colBias].player() == player) {
                    ++sum;
                } else {
                    break;
                }
            } catch (ArrayIndexOutOfBoundsException | NullPointerException c) {
                break;
            }
        }
        for (int i = 1; i < 4; i++) {
            try {
                if (board[row - i * rowBias][col - i * colBias].player() == player) {
                    ++sum;
                } else {
                    break;
                }
            } catch (ArrayIndexOutOfBoundsException | NullPointerException c) {
                break;
            }
        }
        return sum;
    }

    private int findPosition(int colPosition) {
        for (int i = 0; i < board.length; i++) {
            if (board[i][colPosition] != null) {
                return i;
            }
        }
        return -1;
    }

    public void printBoard() {
        for (Chip[] chips : board) {
            for (int column = 0; column < board[0].length; column++) {
                System.out.print("|");
                Chip current = chips[column];
                if (current == null) {
                    System.out.print(" ");
                } else {
                    System.out.print(current.player().name().charAt(0));
                }
            }
            System.out.println("|");
        }
    }
}
