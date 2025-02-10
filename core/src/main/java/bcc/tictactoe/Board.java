
package bcc.tictactoe;
public class Board implements Cloneable {
    private Mark[][] grid;
    public Board() {
        grid = new Mark[3][3];
        reset();
    }

    public void reset() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                grid[row][col] = Mark.EMPTY;
            }
        }
    }
    public boolean makeMove(Move move, Mark mark) {
        return makeMove(move.row, move.col, mark);
    }

    public boolean makeMove(int row, int col, Mark mark) {
        // Validate that row/col are in range and cell is EMPTY
        if (row < 0 || row >= 3 || col < 0 || col >= 3) return false;
        if (grid[row][col] != Mark.EMPTY) return false;

        grid[row][col] = mark;
        return true;
    }

    public void clearCell(int row, int col) {
        if (row < 0 || row >= 3 || col < 0 || col >= 3) return;

        grid[row][col] = Mark.EMPTY;
    }
    public boolean isFull() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (grid[row][col] == Mark.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    public Mark[][] getGrid() {
        // Might return a copy for safety, or direct reference for speed
        return grid;
    }

    /**
     * @return 'X' if X wins, 'O' if O wins, 'T' if tie, or ' ' if still in progress
     */
    public Mark checkWin() {//return null if game not over
        // Check rows
        for (int row = 0; row < 3; row++) {
            if (grid[row][0] != Mark.EMPTY &&
                grid[row][0] == grid[row][1] &&
                grid[row][1] == grid[row][2]) {
                return (grid[row][0] == Mark.X) ? Mark.X : Mark.O;
            }
        }
        // Check columns
        for (int col = 0; col < 3; col++) {
            if (grid[0][col] != Mark.EMPTY &&
                grid[0][col] == grid[1][col] &&
                grid[1][col] == grid[2][col]) {
                return (grid[0][col] == Mark.X) ? Mark.X : Mark.O;
            }
        }
        // Check diagonals
        if (grid[1][1] != Mark.EMPTY &&
            ((grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2]) ||
             (grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0]))) {
            return (grid[1][1] == Mark.X) ? Mark.X : Mark.O;
        }

        // Check tie
        if (isFull()) {
            return Mark.TIE; // T for Tie
        }

        return null; // Game not over
    }

    @Override
    public Board clone() {
        try {
            // Create a shallow copy first.
            Board copy = (Board) super.clone();
            // Now deep copy the grid array.
            copy.grid = new Mark[3][3];
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    copy.grid[row][col] = this.grid[row][col];
                }
            }
            return copy;
        } catch (CloneNotSupportedException e) {
            // This should never happen because we implement Cloneable.
            throw new AssertionError("Cloning not supported", e);
        }
    }
}
