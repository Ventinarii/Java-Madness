package Engine.Visible;

import Engine.Data.Game;
import Engine.Data.TextureMap;
import Engine.Data.Player;
import javafx.animation.AnimationTimer;

public class Drawer extends AnimationTimer{
    public static Drawer drawer;
    public static TextureMap textureMap = new TextureMap();
    public static Game game;
    public static Player player;
    public void handle(long now) {
        //game.mapRoot.render(player);
    }
}
