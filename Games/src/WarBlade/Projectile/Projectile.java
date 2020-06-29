package WarBlade.Projectile;

import WarBlade.Core.Main;
import WarBlade.Matter;
import WarBlade.Thing;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

public class Projectile extends Matter {
    private Image TextureF, TextureE;
    private int x = 0,y = 0, offsetX, offsetY;
    private boolean enemy;
    public int speed = 0, tolerance = 0, type,firerate;//here tolerance means DMG//
    public static int SMALLARMS = 1;
    private IntegerProperty hp = new SimpleIntegerProperty(this,"hp",10);private long expiretime;
    private Thing owner;

    public Projectile(int TYPE){
        switch (TYPE){
            case 1: {
                type = 1;
                TextureE = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/f883cd3dc5b2be7b90095aa87141de4e.gif");//215x300
                TextureF = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/ezgif.com-rotate.gif");
                offsetX = (215/2);
                offsetY = (300/2);
                speed = (1024/60);
                tolerance = 1;
            }break;
            case 2: {
                type = 2;
                TextureE = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/f883cd3dc5b2be7b90095aa87141de4e.gif");//215x300
                TextureF = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/ezgif.com-rotate.gif");
                offsetX = (215/2);
                offsetY = (300/2);
                speed = (1024/60);
                tolerance = 1;
            }break;
            case 3: {
                type = 3;
                TextureE = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/f883cd3dc5b2be7b90095aa87141de4e.gif");//215x300
                TextureF = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/ezgif.com-rotate.gif");
                offsetX = (215/2);
                offsetY = (300/2);
                speed = (1024/60);
                tolerance = 1;
            }break;
            case 4: {
                type = 4;
                TextureE = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/f883cd3dc5b2be7b90095aa87141de4e.gif");//215x300
                TextureF = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/ezgif.com-rotate.gif");
                offsetX = (215/2);
                offsetY = (300/2);
                speed = (1024/60);
                tolerance = 1;
            }break;
            case 5: {
                type = 5;
                TextureE = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/f883cd3dc5b2be7b90095aa87141de4e.gif");//215x300
                TextureF = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/ezgif.com-rotate.gif");
                offsetX = (215/2);
                offsetY = (300/2);
                speed = (1024/60);
                tolerance = 1;
            }break;
            case 6: {
                type = 6;
                TextureE = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/f883cd3dc5b2be7b90095aa87141de4e.gif");//215x300
                TextureF = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/ezgif.com-rotate.gif");
                offsetX = (215/2);
                offsetY = (300/2);
                speed = (1024/60);
                tolerance = 1;
            }break;
            case 7: {
                type = 7;
                TextureE = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/f883cd3dc5b2be7b90095aa87141de4e.gif");//215x300
                TextureF = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/ezgif.com-rotate.gif");
                offsetX = (215/2);
                offsetY = (300/2);
                speed = (1024/60);
                tolerance = 1;
            }break;
            case 8: {
                type = 8;
                TextureE = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/f883cd3dc5b2be7b90095aa87141de4e.gif");//215x300
                TextureF = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/ezgif.com-rotate.gif");
                offsetX = (215/2);
                offsetY = (300/2);
                speed = (1024/60);
                tolerance = 1;
            }break;
            case 9: {
                type = 9;
                TextureE = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/f883cd3dc5b2be7b90095aa87141de4e.gif");//215x300
                TextureF = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/ezgif.com-rotate.gif");
                offsetX = (215/2);
                offsetY = (300/2);
                speed = (1024/60);
                tolerance = 1;
            }break;
        }
    }
    public Projectile(int TYPE, Thing caller){
        switch (TYPE){
            case 1: {
                type = 1;
                TextureE = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/smallarms.png");
                TextureF = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/smallarms.png");
                offsetX = (int)TextureE.getWidth()/2;
                offsetY = (int)TextureE.getHeight()/2;
                speed = (1024/60);
                tolerance = 1;
                firerate = 1;
                this.enemy = caller.getEnemy();
            }break;
            case 2: {
                type = 2;
                TextureE = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/medarms.png");
                TextureF = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/medarms.png");
                offsetX = (int)TextureE.getWidth()/2;
                offsetY = (int)TextureE.getHeight()/2;
                speed = (1024/60);
                tolerance = 5;
                firerate = 10;
                this.enemy = caller.getEnemy();
            }break;
            case 3: {
                type = 3;
                TextureE = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/bigarms2.png");
                TextureF = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/bigarms.png");
                offsetX = (int)TextureE.getWidth()/2;
                offsetY = (int)TextureE.getHeight()/2;
                speed = (256/60);
                tolerance = 10;
                firerate = 100;
                this.enemy = caller.getEnemy();
            }break;
            case 4: {
                type = 4;
                TextureE = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/Missle2.png");
                TextureF = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/Missle.png");
                offsetX = (int)TextureE.getWidth()/2;
                offsetY = (int)TextureE.getHeight()/2;
                speed = (1024/60);
                tolerance = 99;
                firerate = 5000;
                this.enemy = caller.getEnemy();
            }break;
            case 5: {
                type = 5;
                TextureE = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/Laser.png");
                TextureF = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/Laser.png");
                offsetX = (int)TextureE.getWidth()/2;
                offsetY = (int)TextureE.getHeight()/2;
                speed = (1024/60);
                tolerance = 1;
                firerate = 1;
                this.enemy = caller.getEnemy();
            }break;
            case 6: {
                type = 6;
                TextureE = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/Plasma.png");
                TextureF = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/Plasma.png");
                offsetX = (int)TextureE.getWidth()/2;
                offsetY = (int)TextureE.getHeight()/2;
                speed = (1024/60);
                tolerance = 50;
                firerate = 1000;
                this.enemy = caller.getEnemy();
            }break;
            case 7: {
                type = 7;
                TextureE = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/Anitimatter.png");
                TextureF = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/Anitimatter.png");
                offsetX = (int)TextureE.getWidth()/2;
                offsetY = (int)TextureE.getHeight()/2;
                speed = (1024/60);
                tolerance = 20;
                firerate = 1;
                this.enemy = caller.getEnemy();
            }break;
            case 8: {
                type = 8;
                TextureE = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/Pozitionium.png");
                TextureF = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/Pozitionium.png");
                offsetX = (int)TextureE.getWidth()/2;
                offsetY = (int)TextureE.getHeight()/2;
                speed = (1024/60);
                tolerance = 30;
                firerate = 1;
                this.enemy = caller.getEnemy();
            }break;
            case 9: {
                type = 9;
                TextureE = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/blackhole.png");
                TextureF = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/blackhole.png");
                offsetX = (int)TextureE.getWidth()/2;
                offsetY = (int)TextureE.getHeight()/2;
                speed = (1024/60);
                tolerance = 10000;
                firerate = 10000;
                this.enemy = caller.getEnemy();

            }break;
        }
        this.x = caller.getX()+caller.getoffsetX()-offsetX;
        this.y = ((enemy)?(caller.getY()+caller.getoffsetY()*2-offsetY):(caller.getY()-offsetY));
        owner = caller;
        hp.addListener(event->{
            if(hp.get()<=0){
                speed=0;
                if(type==1||type==2||type==3||type==5||type==7||type==9){
                    TextureE = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/impsmall.png");
                    TextureF = TextureE;
                    expiretime = System.currentTimeMillis()+100;
                }else {
                    TextureE = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Projectile/impbig.png");
                    TextureF = TextureE;
                    expiretime = System.currentTimeMillis()+500;
                }
                x = (int)(x + offsetX - (TextureE.getWidth()/2));
                y = (int)(y + offsetY - (TextureE.getHeight()/2));

            }
        });
    }
    @Override
    public void render() {
        if(enemy)
            Main.GC.drawImage(TextureE,x,y);
        else
            Main.GC.drawImage(TextureF,x,y);
    }
    @Override
    public void simulate() {
        if(enemy){
            y+=speed;
        }else{
            y-=speed;
        }
        if(y<-512||1536<y)
            Main.projectiles.remove(this);
        if(hp.get()<=0)
            if(expiretime<=System.currentTimeMillis())
                Main.projectiles.remove(this);
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
        owner.killed(thing);
    }//nie powinno być wykorzystywane możliwość błeu nullpoiter Exeption. należy zrobić zęby potomkowie (szhapnel)) dziedziczył ownera!!!
    @Override
    public int getMoneyz() {
        return 1;
    }
    @Override
    public IntegerProperty getHP() {
        return null;
    }
    private int Mod(int x){
        if(0<=x)return x;
        else return -x;
    }
    public void collision(Matter caller){
        if(0<hp.get())
        if(caller.getEnemy()!=enemy)
            if(
                            (Mod((x+offsetX)-(caller.getX()+caller.getoffsetX())))<(offsetX+caller.getoffsetX())&&
                            (Mod((y+offsetY)-(caller.getY()+caller.getoffsetY())))<(offsetY+caller.getoffsetY())
                    ) {
                if (caller.getHP().get()<=tolerance)
                    owner.killed(caller);
                caller.getHP().set(caller.getHP().get() - tolerance);
                hp.set(0);
            }
    }
    @Override
    public void listSettings(GridPane gridPane, int rowindex) {//todo list settings c=> aby to zrobić zrobi się tablice wartości do edycji dla użytkownika ;-;

    }
    public static String name(int x){
        switch (x) {
            case 1: return "SMALLARMS  ";
            case 2: return "MEDIUMARMS ";
            case 3: return "BIGARMS    ";
            case 4: return "MISSLE     ";
            case 5: return "LASER      ";
            case 6: return "PLASMA     ";
            case 7: return "ANITIMATER ";
            case 8: return "POZYTONIUM ";
            case 9: return "BLACKHOLES ";
        }
        return null;
    }
}
