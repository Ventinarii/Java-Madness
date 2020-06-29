package WarBlade;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public interface Thing {
    public void render();
    public void simulate();
    public boolean getEnemy();
    public int getX();
    public int getY();
    public IntegerProperty getHP();
    public int getoffsetX();
    public int getoffsetY();
    public void killed(Thing thing);
    public int getMoneyz();
    public void listSettings(GridPane gridPane, int rowindex);
}
