package com.danilkha.client.presentation.game.tank;

import com.danilkha.client.presentation.game.GameModel;
import com.danilkha.client.presentation.game.Sprite;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.danilkha.config.GameConfig;

public abstract class TankSprite extends Sprite {

    private final int playerIndex;
    protected final ImageView node;

    protected float speed;
    protected float rotation;

    public TankSprite(int playerIndex, float width, float height){
        super(width, height);
        this.playerIndex = playerIndex;

        String image = switch (playerIndex){
            case 0 -> "tank-blue.png";
            case 1 -> "tank-yellow.png";
            case 2 -> "tank-red.png";
            default -> "tank-green.png";
        };

        this.node = new ImageView(new Image(image, width, height, false, false));

        node.setX(GameModel.getActualSize(GameConfig.PLAYER_START_POSITIONS[playerIndex][0]));
        node.setY(GameModel.getActualSize(GameConfig.PLAYER_START_POSITIONS[playerIndex][1]));
    }


    public int getPlayerIndex() {
        return playerIndex;
    }

    @Override
    public ImageView getImage() {
        return node;
    }

}
