package WarBlade.Core;
import javafx.scene.image.Image;
public class BackGroud {
    private static Image space = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Core/pve&pvp.jpg");
    public static int y = 0;
    public static void render(){
        Main.GC.drawImage(space,0,y-1024);
        Main.GC.drawImage(space,0,y);
    }
    public static void move(){
        y++;
        if(y>1024)y=0;
    }
}
