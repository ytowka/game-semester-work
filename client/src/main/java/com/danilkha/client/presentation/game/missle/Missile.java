package com.danilkha.client.presentation.game.missle;

import com.danilkha.client.presentation.game.Sprite;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.danilkha.config.GameConfig;

public class Missile extends Sprite {

    private final boolean fromPlayer;
    private final ImageView imageView;

    private final float angle;

    public Missile(boolean fromPlayer, float width, float height, float x, float y, float angle) {
        super(width, height);
        this.fromPlayer = fromPlayer;

        imageView = new ImageView(new Image("missile.png", width, height, false, false));

        imageView.setX(x);
        imageView.setY(y);
        imageView.setRotate(angle);

        this.angle = angle;
    }

    @Override
    public void onAct(int delta) {

        double dx = GameConfig.MISSILE_SPEED * Math.cos(Math.toRadians(angle))/1000f;
        double dy = GameConfig.MISSILE_SPEED * Math.sin(Math.toRadians(angle))/1000f;

        imageView.setX(imageView.getX() + dx);
        imageView.setY(imageView.getY() + dy);
    }

    @Override
    public ImageView getImage() {
        return imageView;
    }

    public boolean isFromPlayer() {
        return fromPlayer;
    }
}
