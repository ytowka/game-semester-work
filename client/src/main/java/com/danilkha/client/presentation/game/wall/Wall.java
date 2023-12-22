package com.danilkha.client.presentation.game.wall;

import com.danilkha.client.presentation.game.Sprite;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.danilkha.config.GameConfig;

public class Wall extends Sprite {

    private int hp = GameConfig.WALL_HP;

    final ImageView imageView;
    public Wall(float width, float height, float x, float y) {
        super(width, height);
        imageView = new ImageView(new Image("wall0.png", width, height, true, false));
        imageView.setX(x);
        imageView.setY(y);
    }

    @Override
    public void onAct(int delta) {

    }

    public boolean hit(){
        hp -= GameConfig.DEFAULT_DAMAGE;

        if(hp == 2){
            imageView.setImage(new Image("wall1.png", getWidth(), getHeight(), true, false));
        }
        if(hp == 1){
            imageView.setImage(new Image("wall2.png", getWidth(), getHeight(), true, false));
        }
        return hp <= 0;
    }

    @Override
    public ImageView getImage() {
        return imageView;
    }
}
