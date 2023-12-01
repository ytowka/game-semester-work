package com.danilkha.client.presentation.menu;

import com.danilkha.client.utils.BaseScreen;
import com.danilkha.client.utils.LiveData;

public class MenuModel extends BaseScreen<MenuController> {

    final LiveData<String> text = new LiveData<>();
    public MenuModel() {
        super("menu.fxml");
        controller.init(this);
        new Thread(() ->{
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            text.postValue("showed after 5 seconds");
        }).start();
    }
}
