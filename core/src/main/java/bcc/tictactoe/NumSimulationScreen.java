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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class NumSimulationScreen extends ScreenAdapter {
    private final TicTacToe game;
    private Stage stage;
    private Skin skin;

    public NumSimulationScreen(TicTacToe game) {
        this.game = game;
        System.out.println("created!!");
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("skins/glassy/glassy-ui.json"));
        // Load the skin

        // load image
        Texture backgroundTexture = new Texture(Gdx.files.internal("space_tictactoe.png"));// note that this is stored
                                                                                           // in assets directory
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(backgroundTexture));

        // title
        Container<Label> titleLabel = Constants.createLabelWithBackgrounColor("How many rounds?", Color.BLACK, skin);

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(titleLabel).pad(10).row();
        table.setBackground(backgroundDrawable);
        table.defaults().maxHeight(120).fillY().pad(2);

        TextField roundsInput = new TextField("", skin);
        roundsInput.setMessageText("Enter number of rounds");
        // Set a preferred width (adjust as needed).
        table.add(roundsInput).pad(10).row();

        TextButton continueButton = new TextButton("Continue", skin);
        continueButton.getLabel().setFontScale(.6f);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    int numberOfRounds = Integer.parseInt(roundsInput.getText());
                    
                    game.setNumberOfRounds(numberOfRounds);
                    game.setScreen(new GameDisplay(game));
                   
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number entered: " + roundsInput.getText());
                    // Optionally: show an error message to the user via a dialog or label.
                }
            }
        });
        table.add(continueButton).width(150).pad(10).row();
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
