package Objects.Env;

import Engine.Data.Player;
import Objects.Simulate.Buildings.Building;
import javafx.scene.image.Image;

public class Chunk extends MapOrigin {//tile size 32x32px; chunk size 4x4 tiles <=> 128x128px
    private Cluster owner = null; //if null then is root otherwise is subcluster.

    private Image ground = new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/FactorioLite/src/ThrowMeOutOfProgram/textures/grass-1.png");
    //temp for testing in the eand it will be thown out to TextureMap
    public Building[][] buildings = new Building[4][4]; //exept for enything bigger.
    // todo add building slot occupied linked to bulding holding it and make the whole rely on it. also add drine and pull slots;

    public Chunk(Cluster cluster){
        owner = cluster;
    }

    @Override
    public void preRender(Player player) {

    }
    @Override
    public void simulate(){

    }
}
