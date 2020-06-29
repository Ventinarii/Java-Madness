package Engine.Data;

import Objects.Env.*;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class Game{
    public static int safeMapSize = 1000;
    public Cluster mapRoot;
    public static byte ClusterRecursion = 3; //1 means chunk -> root; 3 means chunk ->cluster ->cluster ->root total of 1000'000 km map edge; ~ 50'000 km of exploration et least; up to +/- 70'000
    public ArrayList<Player> players = new ArrayList<>();
    public static Game currentGame;

    public Game(boolean multiPlayer){
        {
            Cluster t1 = new Cluster(null);//root
            mapRoot = t1;

            players.add(new Player());
        }
        {
            //stuff for applaying game gen prefixes
        }
    }
    public Game(boolean multiPlayer, String saveName){

    }
    public static Node preViev(GridPane gridPane, String name, int x){
        return null;
    }
    public static int[][] getDefaultPos(){
        int[][] loc = {{safeMapSize/2,safeMapSize/2},{0,0},{0,0},{0,0}};
        return loc;
    }
    public static int[][] normalizePos(int[][] input){
        return input;
    }
}
