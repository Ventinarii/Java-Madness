package WarBlade.Vehicle;

import WarBlade.Core.Compute;
import WarBlade.Core.Main;
import WarBlade.Matter;
import WarBlade.Projectile.Projectile;
import WarBlade.Thing;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;


public class Player extends Matter{
    private Image ship = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Vehicle/PlayerShip.png");
    public int x = 512,y = 512, offsetX = 64, offsetY = 64;
    public boolean enemy;
    public static int speed = 3, tolerance = 35;

    public static boolean[] move1 = {false,false,false,false,false};
    public static boolean[] move2 = {false,false,false,false,false};
    public IntegerProperty weapon;
    public ArrayList<IntegerProperty> ammo;
    public IntegerProperty hp;
    public IntegerProperty money;

    public Player(boolean enemy){
        weapon = new SimpleIntegerProperty(this, "selected ammo", 1);
        ammo = new ArrayList<>();
        hp = new SimpleIntegerProperty(this,"hp",100);
        money = new SimpleIntegerProperty(this,"money",0);
        hp.addListener(event ->{
            if(hp.get()<=0) {
                ship = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/wybuch2.png");
                x = (int)(x + offsetX - (ship.getWidth()/2));
                y = (int)(y + offsetY - (ship.getHeight()/2));
            }
        });
        if(enemy) ship = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Vehicle/PlayerShip2.png");
        this.enemy=enemy;
        for(int x = 0; x<9; x++)
           ammo.add(new SimpleIntegerProperty(this, "Ammunition", 99));
    }
    @Override
    public void render() {
        Main.GC.drawImage(ship, x, y);
    }
    @Override
    public void simulate() {
        if(enemy){
            {//carry steering
                if (move2[0]) y -= speed;
                if (move2[1]) y += speed;
                if (move2[2]) x -= speed;
                if (move2[3]) x += speed;
                if (move2[4]) shoot();
            }
            {//look for border
                if (y < (0 - tolerance)) y = 0 - tolerance;
                if ((512 - ship.getHeight() + tolerance) < y) y = (512 - (int) ship.getHeight() + tolerance);
                if (x < (0 - tolerance)) x = 0 - tolerance;
                if ((1024 - ship.getWidth() + tolerance) < x) x = (1024 - (int) ship.getWidth() + tolerance);
            }
        }else{
            {//carry steering
                if (move1[0]) y -= speed;
                if (move1[1]) y += speed;
                if (move1[2]) x -= speed;
                if (move1[3]) x += speed;
                if (move1[4]) shoot();
            }
            {//look for border
                if (y < (512 - tolerance)) y = 512 - tolerance;
                if ((1024 - ship.getHeight() + tolerance) < y) y = (1024 - (int) ship.getHeight() + tolerance);
                if (x < (0 - tolerance)) x = 0 - tolerance;
                if ((1024 - ship.getWidth() + tolerance) < x) x = (1024 - (int) ship.getWidth() + tolerance);
            }
        }
        collision();//check for impact
    }
    @Override
    public boolean getEnemy() {
        return enemy;
    }
    @Override
    public int getX() {
        return x;
    }
    @Override
    public int getY() {
        return y;
    }
    @Override
    public IntegerProperty getHP() {
        return this.hp;
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
        money.set(money.get()+thing.getMoneyz());
    }
    @Override
    public int getMoneyz() {
        return 10;
    }
    private void collision(){
        for(int x = 0; x<Main.projectiles.size(); x++)
            Main.projectiles.get(x).collision(this);
    }
    private long time = System.currentTimeMillis();
    private void shoot(){//todo add sound
        if(time<System.currentTimeMillis()) {
            Projectile tmp = new Projectile(weapon.get(), this);
            time = System.currentTimeMillis()+tmp.firerate;
            if(0<ammo.get((weapon.get())-1).get()){
                ammo.get((weapon.get())-1).set(ammo.get((weapon.get())-1).get()-1);
                Main.projectiles.add(tmp);
            }
        }
    }
    @Override
    public void listSettings(GridPane gridPane, int rowindex){//todo list settings

    }
}
