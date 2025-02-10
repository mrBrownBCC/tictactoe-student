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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class PlayerSelectionScreen extends ScreenAdapter{
    private final TicTacToe game;
    private Stage stage;
    private Skin skin;

    public PlayerSelectionScreen(TicTacToe game, int curPlayer) {
        this.game = game;
        System.out.println("created!!");
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("skins/glassy/glassy-ui.json"));
        // Load the skin

        //load image
        Texture backgroundTexture = new Texture(Gdx.files.internal("space_tictactoe.png"));//note that this is stored in assets directory
        TextureRegionDrawable backgroundDrawable =
                new TextureRegionDrawable(new TextureRegion(backgroundTexture));

        //title
        Container<Label> titleLabel = Constants.createLabelWithBackgrounColor("Please select Player " + (curPlayer + 1),Color.BLACK, skin);
      

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        
        table.add(titleLabel).pad(10).row();
        table.setBackground(backgroundDrawable);
        table.defaults().maxHeight(90).fillY().pad(2);

        for (String option : Constants.PLAYER_OPTIONS) {
            if(game.getIsSimulated() && option.equals("Human")) continue;
            TextButton button = new TextButton(option, skin);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setPlayer(curPlayer, option);
                }
            });
           // button.setTransform(true); 
           // button.setScale(.5f);
            table.add(button).pad(2).row();
        }
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
