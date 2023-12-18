package com.danilkha.client.presentation.game.tank;

import com.danilkha.client.presentation.game.Actor;
import com.danilkha.client.presentation.game.GameModel;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Arrays;

public abstract class TankActor extends Actor {

    private final int playerIndex;
    protected final ImageView node;

    protected float speed;
    protected float rotation;

    public TankActor(int playerIndex, float width, float height){
        super(width, height);
        this.playerIndex = playerIndex;

        String image = switch (playerIndex){
            case 0 -> "tank-blue.png";
            case 1 -> "tank-yellow.png";
            case 2 -> "tank-red.png";
            default -> "tank-green.png";
        };

        this.node = new ImageView(new Image(image, width, height, false, false));
    }




    @Override
    public ImageView getImage() {
        return node;
    }

}
