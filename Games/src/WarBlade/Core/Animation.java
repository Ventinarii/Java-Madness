package WarBlade.Core;

import javafx.animation.AnimationTimer;

public class Animation extends AnimationTimer{
    public static void boot(){
        Main.animation = new Animation();
        Main.animation.start();
    }
    @Override
    public void handle(long now) {
        BackGroud.render();
        for(int x = 0; x<Main.Vehicle.size(); x++)
            Main.Vehicle.get(x).render();
        for(int x = 0; x<Main.projectiles.size(); x++)
            Main.projectiles.get(x).render();
    }
}
