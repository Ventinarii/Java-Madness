package Objects.Entity;

import Engine.Data.Game;
import Engine.Structures.Screen;
import Objects.MapGererationAble;
import Objects.SimAble;

public class Figure implements SimAble, MapGererationAble{
    public int[][] location = Game.getDefaultPos();
    // layer in following pattern:
    // [root =>t1] [cluster =>t2] [cluster t=>3] [chunk =>tn]
    // [x]---- [x]------ [x] ...
    // [y]---- [y]------ [y] ...
    // x value of -1 is invalid == no more values (or set default value from 0 => 1<x<128/savValue)
    //image offset? resolution => 1980x1080// player 50x100 px
    private int buffer = 10, X = Screen.width/128+buffer, Y = Screen.height/128+buffer;
    public void WorldEngineCheck(){
        int[][] workbench = location.clone();
        workbench[2][0]-=buffer;
        workbench[2][1]-=buffer;
        for(int y = -buffer; y<Y; y++) {
            for (int x = -buffer; x < X; x++) {
                Game.currentGame.mapRoot.getChunk(Game.normalizePos(workbench));
                workbench[2][0]++;
            }
            workbench[2][0]++;
        }
    }
}
