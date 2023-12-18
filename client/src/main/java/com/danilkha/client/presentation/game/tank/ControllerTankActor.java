package com.danilkha.client.presentation.game.tank;

import com.danilkha.client.presentation.game.GameModel;

import java.util.Arrays;

public class ControllerTankActor extends TankActor{
    public ControllerTankActor(int playerIndex, float width, float height) {
        super(playerIndex, width, height);
    }

    @Override
    public void onAct(int delta) {
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

        var deltaX = speed * cos * delta;
        var deltaY = speed * sin * delta;

        var angle = Math.acos(cos);


        if(sin < 0){
            angle *= -1;
        }
        double x = node.getX() + deltaX;
        double y = node.getY() + deltaY;

        var degAngle = Math.toDegrees(angle)+90;
        getGameStage().getPlayerMove().setValue(new float[]{
                (float)x, (float)y, (float)degAngle
        });

        node.setX(x);
        node.setY(y);


        GameModel.debugInfo.setText("x = %s \ny = %s\nmouseX = %s\nmouesY = %s\nkeys = %s\nangle=%s".formatted(
                getCenterX(),
                getCenterY(),
                getGameStage().getMouseX(),
                getGameStage().getMouseY(),
                Arrays.toString(getGameStage().pressedKeys.toArray()),
                degAngle
        ));
        node.setRotate(degAngle);
    }
}
