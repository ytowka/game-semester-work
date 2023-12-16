package com.danilkha.client.presentation.game;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Arrays;

public class TankActor extends Actor{

    private final int playerIndex;
    private final ImageView node;

    private float speed;
    private float rotation;

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
    void onAct(int delta) {
        if(getGameStage().isKeyPressed(87)){
            speed = 1f;
        }else {
            speed = 0;
        }



        float vectorX = getGameStage().getMouseX() - getCenterX();
        float vectorY = getGameStage().getMouseY() - getCenterY();


        var len = (float) Math.sqrt(vectorX*vectorX + vectorY*vectorY);
        var cos = vectorX/len;
        var sin = vectorY/len;

        var deltaX = speed * cos * delta / 1000f;
        var deltaY = speed * sin * delta / 1000f;

        var angle = Math.acos(cos);

        if(sin < 0){
            angle *= -1;
        }

        node.setX(node.getX() + deltaX);
        node.setY(node.getY() + deltaY);


        GameModel.debugInfo.setText("x = %s \ny = %s\nmouseX = %s\nmouesY = %s\nkeys = %s\nangle=%s".formatted(
                getCenterX(),
                getCenterY(),
                getGameStage().getMouseX(),
                getGameStage().getMouseY(),
                Arrays.toString(getGameStage().pressedKeys.toArray()),
                angle
        ));
        node.setRotate(Math.toDegrees(angle)+90);
    }

    @Override
    ImageView getImage() {
        return node;
    }

    public static double calculateVectorAngle(float x1, float y1, float x2, float y2) {
        float vectorX = x2 - x1;
        float vectorY = y2 - y1;

        return Math.atan(vectorY/vectorX);
    }
}
