package Engine.Structures;

import Engine.OverSeer.Clock;
import Engine.Invisible.WorldEngine;
import Engine.Visible.Drawer;
import Engine.Data.Game;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class Screen extends Application{
    //================================================================================================================== zakotwiczone zmienne
    public static Group root;
    public static ArrayList<Node> local;
    public static Scene scene;
    public static Canvas canvas;
    public static GraphicsContext GC;
    public static int width = 1920, height = 1080;
    //================================================================================================================== wstępne definicje (graficzne)
    private static String title = "Factorio Lite";
    private static Font menuFont = new Font("Comic Sans MS", 20);
    private static int sizeX = 200, sizeY = 40, gap = 50;
    //================================================================================================================== start programu => skończony
    public static void main(String[] atgs){
        launch(atgs);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        loadFun(primaryStage);
        menu();
    }
    private void menu(){
        {//backgroud
            GC.drawImage(new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/FactorioLite/src/ThrowMeOutOfProgram/textures/background-image.jpg"),0,0);
        }
        {//title
            GC.setFill(Color.RED);
            GC.setStroke(Color.BLACK);
            GC.setLineWidth(2);
            GC.setFont(new Font("Impact", 100));
            GC.fillText(title, width/2-225, 100);
            GC.strokeText(title, width/2-225, 100);
        }
        {//buttons
            Button singlePlayer = new Button("SinglePlayer");
            singlePlayer.setFont(menuFont);
            singlePlayer.setPrefSize(sizeX,sizeY);
            singlePlayer.setLayoutX(width/2-sizeX/2);
            singlePlayer.setLayoutY(512-2*gap);
            singlePlayer.setOnAction(event ->{
                root.getChildren().removeAll(local);
                local.removeAll(local);
                prepareGame(false);
            });
            root.getChildren().add(singlePlayer);
            local.add(singlePlayer);

            Button multiPlayer = new Button("MultiPlayer");
            multiPlayer.setFont(menuFont);
            multiPlayer.setPrefSize(sizeX,sizeY);
            multiPlayer.setLayoutX(width/2-sizeX/2);
            multiPlayer.setLayoutY(512-gap);
            multiPlayer.setOnAction(event ->{
                root.getChildren().removeAll(local);
                local.removeAll(local);
                prepareGame(true);
            });
            root.getChildren().add(multiPlayer);
            local.add(multiPlayer);

            Button settings = new Button("Settings");
            settings.setFont(menuFont);
            settings.setPrefSize(sizeX,sizeY);
            settings.setLayoutX(width/2-sizeX/2);
            settings.setLayoutY(512);
            settings.setOnAction(event ->{
                root.getChildren().removeAll(local);
                local.removeAll(local);
                settings();
            });
            root.getChildren().add(settings);
            local.add(settings);

            Button exit = new Button("Exit");
            exit.setFont(menuFont);
            exit.setPrefSize(sizeX,sizeY);
            exit.setLayoutX(width/2-sizeX/2);
            exit.setLayoutY(512+2*gap);
            exit.setOnAction(event ->{
                root.getChildren().removeAll(local);
                local.removeAll(local);
                exitFun();
            });
            root.getChildren().add(exit);
            local.add(exit);
        }
    }
    private void prepareGame(boolean multiPlayer){
        {//buttons
            Button newGame = new Button("NEW");
            newGame.setFont(menuFont);
            newGame.setPrefSize(sizeX,sizeY);
            newGame.setLayoutX(width/2-sizeX/2);
            newGame.setLayoutY(512-2*gap);
            newGame.setOnAction(event ->{
                root.getChildren().removeAll(local);
                local.removeAll(local);
                startGame(multiPlayer,new Game(multiPlayer));
            });
            root.getChildren().add(newGame);
            local.add(newGame);

            Button oldGame = new Button("LOAD");
            oldGame.setFont(menuFont);
            oldGame.setPrefSize(sizeX,sizeY);
            oldGame.setLayoutX(width/2-sizeX/2);
            oldGame.setLayoutY(512-gap);
            oldGame.setOnAction(event ->{
                root.getChildren().removeAll(local);
                local.removeAll(local);
                loadGame(multiPlayer);
            });
            root.getChildren().add(oldGame);
            local.add(oldGame);

            Button gameSetUp = new Button("SETINGS");
            gameSetUp.setFont(menuFont);
            gameSetUp.setPrefSize(sizeX,sizeY);
            gameSetUp.setLayoutX(width/2-sizeX/2);
            gameSetUp.setLayoutY(512);
            gameSetUp.setOnAction(event ->{
                root.getChildren().removeAll(local);
                local.removeAll(local);
                gameSetUp(multiPlayer,new Game(multiPlayer));
            });
            root.getChildren().add(gameSetUp);
            local.add(gameSetUp);

            Button back = new Button("BACK");
            back.setFont(menuFont);
            back.setPrefSize(sizeX,sizeY);
            back.setLayoutX(width/2-sizeX/2);
            back.setLayoutY(512+2*gap);
            back.setOnAction(event ->{
                root.getChildren().removeAll(local);
                local.removeAll(local);
                menu();
            });
            root.getChildren().add(back);
            local.add(back);
        }
    }
    private void exitFun(){
        saveFun();
        Platform.exit();
        System.exit(0);
    }
    //================================================================================================================== WIP => do dopracowania
    private void loadGame(boolean multiPlayer){
        ScrollPane scrollPane = new ScrollPane();
        GridPane gridPane = new GridPane();
        scrollPane.setContent(gridPane);
        root.getChildren().add(scrollPane);

        Button exit = new Button("BACK");
        exit.setFont(menuFont);
        exit.setPrefSize(sizeX,sizeY);
        exit.setOnAction(event ->{
            root.getChildren().removeAll(scrollPane);
            prepareGame(multiPlayer);
        });
        gridPane.add(exit,0,0);

        String[] saves = new String[0];
        {
            try {
                FileInputStream fileInputStream = new FileInputStream(new File("C:\\Users\\Admin\\Desktop\\Tools\\Programing(1)\\FactorioLite\\src\\ThrowMeOutOfProgram\\Saves\\Register"));
                byte[] bytes = fileInputStream.readAllBytes();
                saves = new String(bytes).split(" ");
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        for(int x = 0; x<saves.length; x++)
            Game.preViev(gridPane,saves[x],x);
    }
    private void loadFun(Stage stage){
        {//load settings

            //todo create console
            //todo load from CFG
        }//all set start =>
        {//create window
            root = new Group();
            local = new ArrayList();
            scene = new Scene(root, width, height);
            stage.setScene(scene);
            stage.show();
            stage.setFullScreen(true);

            canvas = new Canvas(width,height);
            root.getChildren().add(canvas);
            GC = canvas.getGraphicsContext2D();
            stage.setTitle(title);
        }//winow set; GO =>
        Clock.getClock();
    }
    private void saveFun(){
        //todo save to CFG
    }
    private void settings(){
        ScrollPane scrollPane = new ScrollPane();
        GridPane gridPane = new GridPane();
        scrollPane.setContent(gridPane);
        root.getChildren().add(scrollPane);

        Button exit = new Button("BACK");
        exit.setFont(menuFont);
        exit.setPrefSize(sizeX,sizeY);
        exit.setOnAction(event ->{
            root.getChildren().removeAll(scrollPane);
            menu();
        });
        gridPane.add(exit,0,0);
    }
    private void gameSetUp(boolean multiPlayer, Game game){
        ScrollPane scrollPane = new ScrollPane();
        GridPane gridPane = new GridPane();
        scrollPane.setContent(gridPane);
        root.getChildren().add(scrollPane);

        Button exit = new Button("BACK");
        exit.setFont(menuFont);
        exit.setPrefSize(sizeX,sizeY);
        exit.setOnAction(event ->{
            root.getChildren().removeAll(scrollPane);
            prepareGame(multiPlayer);
        });
        gridPane.add(exit,0,0);
    }

    private void startGame(boolean multiPlayer, Game game){
        Game.currentGame = game;
        WorldEngine.worldEngine = new WorldEngine();
        WorldEngine.mapGererationAbles.add(game.players.get(0).figure);
        WorldEngine.worldEngine.start();

    }
}
