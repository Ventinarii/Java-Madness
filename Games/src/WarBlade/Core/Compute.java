package WarBlade.Core;

import javafx.animation.AnimationTimer;

public class Compute extends AnimationTimer{
    public static void boot(){
        Main.compute = new Compute();
        Main.compute.start();
    }
    public static void halt(){
        Main.compute.stop();
    }
    @Override
    public void handle(long now) {
        BackGroud.move();
        for(int x = 0; x<Main.Vehicle.size(); x++)
            Main.Vehicle.get(x).simulate();
        for(int x = 0; x<Main.projectiles.size(); x++)
            Main.projectiles.get(x).simulate();
    }
}
