package bcc.tictactoe;

import java.util.ArrayList;
import java.util.List;

public class SmartAI extends Player {

    @Override
    public Move makeMove(Board board, Mark aiMark) {
        int bestScore = Integer.MIN_VALUE;
        List<Move> bestMoves = new ArrayList<>();

        // Loop through each cell on the board.
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board.getGrid()[row][col].equals(Mark.EMPTY)) {
                    // Try the move for the AI.
                    board.makeMove(row, col, aiMark);
                    // Evaluate this move using minimax (opponent's turn next).
                    int score = minimax(board, false, aiMark);
                    // Undo the move.
                    board.clearCell(row, col);
                    
                    // Collect moves with the best score.
                    if (score > bestScore) {
                        bestScore = score;
                        bestMoves.clear();
                        bestMoves.add(new Move(row, col));
                    } else if (score == bestScore) {
                        bestMoves.add(new Move(row, col));
                    }
                }
            }
        }
        
        // Fallback: In a full board situation (should not occur if called correctly),
        // return any valid move.
        if (bestMoves.isEmpty()) {
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (board.getGrid()[row][col].equals(Mark.EMPTY)) {
                        return new Move(row, col);
                    }
                }
            }
        }
        
        // Randomly select one of the best moves.
        int randomIndex = (int) (Math.random() * bestMoves.size());
        return bestMoves.get(randomIndex);
    }
    
    /**
     * Recursively evaluate the board using the minimax algorithm.
     *
     * @param board       The current board state.
     * @param isMaximizing True if it’s the AI's turn (maximizing score),
     *                     false if it’s the opponent's turn (minimizing score).
     * @param aiMark       The mark the AI is playing as.
     * @return The score for the board state from the perspective of the AI.
     */
    private int minimax(Board board, boolean isMaximizing, Mark aiMark) {
        Mark result = board.checkWin();
        if (result != null) {
            // Terminal state: evaluate score.
            if (result.equals(aiMark)) {
                return 1;
            } else if (result.equals(Mark.TIE)) {
                return 0;
            } else {
                return -1;
            }
        }
        
        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            // Try all possible moves for the AI.
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (board.getGrid()[row][col].equals(Mark.EMPTY)) {
                        board.makeMove(row, col, aiMark);
                        int score = minimax(board, false, aiMark);
                        board.clearCell(row, col);
                        bestScore = Math.max(bestScore, score);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            // Determine the opponent's mark.
            Mark opponentMark = (aiMark.equals(Mark.X)) ? Mark.O : Mark.X;
            // Try all possible moves for the opponent.
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (board.getGrid()[row][col].equals(Mark.EMPTY)) {
                        board.makeMove(row, col, opponentMark);
                        int score = minimax(board, true, aiMark);
                        board.clearCell(row, col);
                        bestScore = Math.min(bestScore, score);
                    }
                }
            }
            return bestScore;
        }
    }
    
    @Override
    public String toString() {
        return "Smart AI";
    }
}
