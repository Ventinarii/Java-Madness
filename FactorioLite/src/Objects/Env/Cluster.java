package Objects.Env;

import Engine.Data.Game;
import Engine.Data.Player;

public class Cluster extends MapOrigin{
    public MapOrigin[][] MapType;
    private Cluster owner = null; //if null then is root otherwise is subcluster.

    public Cluster(Cluster cluster){
        owner = cluster;
    }

    public void preRender(Player player) {

    }
    @Override
    public void simulate() {

    }
    public Chunk getChunk(int[][] location){
        return null;
    }
}
