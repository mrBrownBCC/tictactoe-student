package bcc.tictactoe;

public class SlightlySmartAI extends Player {
    @Override
    public Move makeMove(Board board, Mark mark) {//note - board coming in is a copy, so we can modify it
        // Check if the AI can win in the next move
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board.getGrid()[row][col].equals(Mark.EMPTY)) {
                    board.makeMove(row, col, mark); // Try the move
                    if (board.checkWin() == mark) {
                        return new Move(row, col);
                    }
                    board.clearCell(row, col); // Undo move
                }
            }
        }


        // If no immediate win, pick a random empty cell
        int row, col;
        do {
            row = (int) (Math.random() * 3);
            col = (int) (Math.random() * 3);
        } while (!board.getGrid()[row][col].equals(Mark.EMPTY));
        
        return new Move(row, col);
    }

    public String toString() {
        return "Slightly Smart AI";
    }
    
}
