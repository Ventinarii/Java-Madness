package Engine.OverSeer;

import javafx.animation.AnimationTimer;

public class Clock extends AnimationTimer{
    private static Clock clock;
    private static byte time = 1;
    @Override
    public void handle(long now) {
        time++;
        if(61==time)time=1;
    }
    public static Clock getClock(){
        if(clock != null)
            return clock;
        else {
            clock = new Clock();
            clock.start();
            return clock;
        }
    }
    public static byte getTime(){
        return time;
    }
}
