package Engine.Invisible;

import Engine.Data.Game;
import Engine.Structures.Screen;
import Engine.Data.Player;
import Objects.MapGererationAble;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;

import java.util.ArrayList;

public class WorldEngine extends AnimationTimer{
    public static WorldEngine worldEngine;

    public static ArrayList<MapGererationAble> mapGererationAbles = new ArrayList<>();

    public void handle(long now) {
        for (MapGererationAble mapGererationAble : mapGererationAbles) mapGererationAble.WorldEngineCheck();
    }
}
