package Objects.Env;

import Engine.Data.Player;
import Objects.SimAble;

public interface MapAble extends SimAble{
    public void preRender(Player player);
    public void simulate();
}
