package com.danilkha.client.presentation.game.tank;

import com.danilkha.client.presentation.game.GameModel;
import org.danilkha.config.GameConfig;

import java.util.Arrays;

import static java.lang.Math.*;

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
        }else if(getGameStage().isKeyPressed(83)){
            speed = -GameConfig.TANK_MOVE_SPEED;
        }else {
            speed = 0;
        }

        float vectorX = getGameStage().getMouseX() - getCenterX();
        float vectorY = getGameStage().getMouseY() - getCenterY();


        var len = (float) Math.sqrt(vectorX*vectorX + vectorY*vectorY);
        var cos = vectorX/len;
        var sin = vectorY/len;



        var targetAngle = Math.acos(cos);


        if(sin < 0){
            targetAngle *= -1;
        }


        double targetDegAngle = Math.toDegrees(targetAngle);


        float frameSpeed = GameConfig.GUN_ROTATE_SPEED * (delta / 1000f);
        if(Math.abs(targetDegAngle - degAngle) < frameSpeed){
            degAngle = targetDegAngle;
        }else{
            double diff = degAngle - targetDegAngle;
            if(0 < diff && diff < 180){
                degAngle -= frameSpeed;
            }

            if(-180 < diff && diff < 0){
                degAngle += frameSpeed;
            }

            if(diff < -180){
                degAngle -= frameSpeed;
            }

            if(diff > 180){
                degAngle += frameSpeed;
            }

        }

        if(degAngle < -180){
            degAngle = 360 + degAngle;
        }

        if(degAngle > 180){
            degAngle = -360 + degAngle;
        }


        var deltaX = speed * cos(toRadians(degAngle)) * delta/1000f;
        var deltaY = speed * sin(toRadians(degAngle)) * delta/1000f;

        double x = node.getX() + deltaX;
        double y = node.getY() + deltaY;

        double[] collisions = getGameStage().calculateCollisionCorrection(x, y, getWidth(), getHeight());

        x += collisions[0];
        y += collisions[1];

        getGameStage().getPlayerMove().setValue(new float[]{
                (float)x, (float)y, (float)degAngle
        });

        node.setX(x);
        node.setY(y);


        GameModel.debugInfo.setText("x = %s \ny = %s\nmouseX = %s\nmouesY = %s\nkeys = %s\nangle=%s\ntarget angle=%s\nhp = %s".formatted(
                getCenterX(),
                getCenterY(),
                getGameStage().getMouseX(),
                getGameStage().getMouseY(),
                Arrays.toString(getGameStage().pressedKeys.toArray()),
                Math.round(degAngle),
                Math.round(targetDegAngle),
                hp
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
        hp -= GameConfig.DEFAULT_DAMAGE;
        return hp <= 0;
    }

    public boolean isDead(){
        return hp <= 0;
    }
}
