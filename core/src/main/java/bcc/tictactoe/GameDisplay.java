package bcc.tictactoe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class GameDisplay extends ScreenAdapter {
    private final TicTacToe game;
    private Stage stage;
    private Skin skin;

    private final float BOARD_X = 90;
    private final float BOARD_Y = 70;
    private final float BOARD_WIDTH = 300;
    private final float BOARD_HEIGHT = 300;
    Table boardTable;


    private boolean gameOver = false;
    private Label resultLabel;
    private TextButton playAgainButton;

    
    private Container<Label> curPlayerDisplay; 
    private Container<Label> player1Stats;
    private Container<Label> player2Stats;


    public GameDisplay(TicTacToe game) {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("skins/glassy/glassy-ui.json"));
        // Load the skin

        // load image
        Texture backgroundTexture = new Texture(Gdx.files.internal("space_tictactoe.png"));// note that this is stored
                                                                                           // in assets directory
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        
        // title
        Container<Label> titleLabel = Constants.createLabelWithBackgrounColor(
                game.getPlayer1() + "(x) vs " + game.getPlayer2() + "(o)", Color.BLACK, skin);
            
        

        titleLabel.setPosition(50, 20);
        titleLabel.pack();
        stage.addActor(titleLabel);

        curPlayerDisplay= Constants.createLabelWithBackgrounColor("Current Player: X" ,Color.BLACK,  skin);
        curPlayerDisplay.setPosition(10, 440);
        curPlayerDisplay.pack();
        stage.addActor(curPlayerDisplay);

        player1Stats = Constants.createLabelWithBackgrounColor(game.getPlayer1().toString() + "(X): " + "0W 0L 0D", Color.BLACK, skin);
        player2Stats = Constants.createLabelWithBackgrounColor(game.getPlayer1().toString() + "(O): " + "0W 0L 0D", Color.BLACK, skin);
        
        player1Stats.setPosition(210, 430);
        player2Stats.setPosition(210, 400);
        
        player1Stats.pack();
        player2Stats.pack();

        stage.addActor(player1Stats);
        stage.addActor(player2Stats);

        game.setBoardState(new Board());
        initTableDisplay();
        updateBoardDisplay();
    }

    public void initTableDisplay() {
        boardTable = new Table();
        boardTable.setPosition(BOARD_X, BOARD_Y);
        boardTable.setSize(BOARD_WIDTH, BOARD_HEIGHT);

        // Set the background image.
        // (Replace "path/to/background.png" with the actual path later.)
        Texture backgroundTexture = new Texture(Gdx.files.internal("tictactoe_board.png"));
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(backgroundTexture));
        boardTable.setBackground(backgroundDrawable);

       
        // Force the table to layout from the top (so that row 0 appears at the top).
        boardTable.top();

        boardTable.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float clickX, float clickY) {
                // The click coordinates (clickX, clickY) are relative to the table's
                // bottom-left.
                float cellWidth = BOARD_WIDTH / 3;
                float cellHeight = BOARD_HEIGHT / 3;

                int col = (int) (clickX / cellWidth);
                int rowFromBottom = (int) (clickY / cellHeight);
                // Convert the y-coordinate to a row index where row 0 is at the top.
                int row = 2 - rowFromBottom;

                // Call your board click handler.
                handleBoardClick(row, col);
            }
        });
        stage.addActor(boardTable);
    }

    
    public void handleBoardClick(int row, int col) {
        if(gameOver) return;

        if(game.getCurPlayerObj() instanceof Human) {
            if(game.getBoardState().makeMove(row, col, game.getCurPlayerMark())){
                handleMoveMade();
            }
        }
    }

    public void handleMoveMade(){
        updateBoardDisplay();
        Mark winner = game.getBoardState().checkWin();
        String result = "";
        if(winner == null){//no winner yet, its still going
            game.nextPlayer();
            curPlayerDisplay.getActor().setText("Current Player: " + game.getCurPlayerMark());

        } else if (winner == Mark.TIE) {//tie
            game.getPlayer1().incrementTies();
            game.getPlayer2().incrementTies();
            result = "Tie!";
        } else if(winner == Mark.X) {//X wins
            game.getPlayer1().incrementWins();
            game.getPlayer2().incrementLosses();
            result = "X wins!";
        } else if(winner == Mark.O) {//O wins
            game.getPlayer2().incrementWins();
            game.getPlayer1().incrementLosses();
            result = "O wins!";
        }
        if (!result.isEmpty()) {
            player1Stats.getActor().setText(game.getPlayer1().toString() + "(X): " + game.getPlayer1().getRecord());
            player2Stats.getActor().setText(game.getPlayer2().toString() + "(O): " + game.getPlayer2().getRecord());
            gameOver = true;
            if(game.getIsSimulated()){
                if(game.getRound() < game.getNumberOfRounds()){
                    resetGame();
                    game.incrementRound();
                } else {
                    //do nothing
                }
            } else {
                showResult(result);
            }
        }
    }

    private void showResult(String result) {
        // Create an overlay table to show the result.
        final Table resultTable = new Table();
        resultTable.setFillParent(true);
        resultTable.center();
        stage.addActor(resultTable);

        resultLabel = new Label(result, skin);
        resultLabel.setFontScale(3f);
        resultLabel.setColor(Color.RED);

        playAgainButton = new TextButton("Play Again", skin);
        playAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Reset the game when the button is clicked.
                resetGame();
                resultTable.remove(); // Remove the result overlay.
            }
        });

        resultTable.add(resultLabel).pad(10);
        resultTable.row();
        resultTable.add(playAgainButton).pad(10);
    }
    public void resetGame() {
        game.getBoardState().reset();
        gameOver = false;
        updateBoardDisplay();
        game.resetCurPlayer();
    }

    public void updateBoardDisplay() {
        boardTable.clearChildren();
        Mark[][] grid = game.getBoardState().getGrid(); // Assumes boardState is accessible
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Mark mark = grid[row][col];
                String text = "";
                if (mark == Mark.X) {
                    text = "X";
                } else if (mark == Mark.O) {
                    text = "O";
                }
                Label cellLabel = new Label(text, skin);
                cellLabel.setAlignment(Align.center);     // Center text within the label.
                cellLabel.setFontScale(5f); 
                // Each cell gets equal width and height.
                boardTable.add(cellLabel).width(BOARD_WIDTH / 3).height(BOARD_HEIGHT / 3);
            }
            boardTable.row();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();

        if(!(game.getCurPlayerObj() instanceof Human) && !gameOver) {
            Move move = game.getCurPlayerObj().makeMove(game.getBoardState().clone(), game.getCurPlayerMark());
            game.getBoardState().makeMove(move, game.getCurPlayerMark());
           handleMoveMade();
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
