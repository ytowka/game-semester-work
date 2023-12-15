package com.danilkha.client.presentation.game;

public interface InputListener {

    void onMouseMoved(float x, float y);
    void onMouseClicked(float x, float y);

    void onKeyDown(int code);
    void onKeyUp(int code);
}
