package WarBlade.Vehicle;

import WarBlade.Core.Main;
import WarBlade.Thing;
import javafx.beans.property.IntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

public class Fighter extends Enemy{
    private Image Texture;
    public int x = 0,y = 0, offsetX, offsetY;
    public boolean enemy;
    public static int speed = 0, tolerance = 0;
    public static int BASIC = 1;
    public IntegerProperty hp;
    public Fighter(int TYPE){

    }
    public Fighter(Fighter fighter, int x, int y){

    }
    @Override
    public void render() {
        Main.GC.drawImage(Texture,x,y);
    }

    @Override
    public void simulate() {

        collision();
    }
    @Override
    public boolean getEnemy() {
        return enemy;
    }
    @Override
    public int getX() {
        return x+offsetX;
    }
    @Override
    public int getY() {
        return y+offsetY;
    }
    @Override
    public int getoffsetX() {
        return offsetX;
    }
    @Override
    public int getoffsetY() {
        return offsetY;
    }
    @Override
    public void killed(Thing thing) {

    }
    @Override
    public int getMoneyz() {
        return 100;
    }
    @Override
    public IntegerProperty getHP() {
        return this.hp;
    }
    private void collision(){
        for(int x = 0; x<Main.projectiles.size(); x++)
            Main.projectiles.get(x).collision(this);
    }
    @Override
    public void listSettings(GridPane gridPane, int rowindex) {//todo list settings

    }
}
