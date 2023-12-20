package com.danilkha.client.presentation.game.tank;

import com.danilkha.client.presentation.game.GameModel;
import org.danilkha.config.GameConfig;

import java.util.Arrays;

public class ControllerTankSprite extends TankSprite {
    public ControllerTankSprite(int playerIndex, float width, float height) {
        super(playerIndex, width, height);
    }

    private double degAngle = 0f;

    private int hp = GameConfig.PLAYER_HP;

    @Override
    public void onAct(int delta) {
        if(getGameStage().isKeyPressed(87)){
            speed = GameConfig.TANK_MOVE_SPEED;
        }else {
            speed = 0;
        }

        float vectorX = getGameStage().getMouseX() - getCenterX();
        float vectorY = getGameStage().getMouseY() - getCenterY();


        var len = (float) Math.sqrt(vectorX*vectorX + vectorY*vectorY);
        var cos = vectorX/len;
        var sin = vectorY/len;

        var deltaX = speed * cos * delta/1000f;
        var deltaY = speed * sin * delta/1000f;

        var angle = Math.acos(cos);


        if(sin < 0){
            angle *= -1;
        }
        double x = node.getX() + deltaX;
        double y = node.getY() + deltaY;



        degAngle = Math.toDegrees(angle);
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
        node.setRotate(degAngle+90);
    }

    public double getDegAngle() {
        return degAngle;
    }

    public int getHp() {
        return hp;
    }

    public boolean hit(){
        hp -=1;
        return hp <= 0;
    }

    public boolean isDead(){
        return hp <= 0;
    }
}
