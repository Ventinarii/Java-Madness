package WarBlade.Core;
import WarBlade.Projectile.Projectile;
import WarBlade.Thing;
import WarBlade.Vehicle.Player;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;

public class Main extends Application{
    private static String Name = "WarBlade";
    public static Font menuFont = new Font("Comic Sans MS", 20);
    private static int sizeX = 200, sizeY = 40, gap = 50, steeringdelay = 100;                                          //size of interface buttons
    private static MediaPlayer mediaPlayer;                                                                             //music
    private static ArrayList<Thing> things = new ArrayList<>();                                                         //game items
    private static ArrayList<User> PastPlayers = new ArrayList<>();                                                     //list for leaderboard

    public static Group root;                                                                                           //componets for render
    public static GraphicsContext GC;
    public static ArrayList<Thing> Vehicle;
    public static ArrayList<Projectile> projectiles;
    public static Animation animation;
    public static Compute compute;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        root = new Group();
        loadFun();
        primaryStage.setTitle(Name);
        menu(primaryStage);
    }
    private void menu(Stage stage){
        root.getChildren().removeAll(root.getChildren());
        Scene scene = new Scene(root, 1024, 1024);
        {
            Canvas canvas = new Canvas(1024,1024);
            root.getChildren().add(canvas);

            GC = canvas.getGraphicsContext2D();
            {//backgroud
                GC.drawImage(new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Core/menu.jpg"),0,0);
            }
            {//title
                GC.setFill(Color.RED);
                GC.setStroke(Color.BLACK);
                GC.setLineWidth(2);
                GC.setFont(new Font("Impact", 100));
                GC.fillText(Name, 300, 100);
                GC.strokeText(Name, 300, 100);
            }
        }
        {//buttons
            Button pve = new Button("PvE");
            pve.setFont(menuFont);
            pve.setPrefSize(sizeX,sizeY);
            pve.setLayoutX(512-sizeX/2);
            pve.setLayoutY(512-2*gap);
            pve.setOnAction(event ->{
                pve(stage);
            });
            root.getChildren().add(pve);

            Button pvp = new Button("PvP");
            pvp.setFont(menuFont);
            pvp.setPrefSize(sizeX,sizeY);
            pvp.setLayoutX(512-sizeX/2);
            pvp.setLayoutY(512-gap);
            pvp.setOnAction(event ->{
                pvp(stage,scene);
            });
            root.getChildren().add(pvp);

            Button settings = new Button("SETINGS");
            settings.setFont(menuFont);
            settings.setPrefSize(sizeX,sizeY);
            settings.setLayoutX(512-sizeX/2);
            settings.setLayoutY(512);
            settings.setOnAction(event ->{
                settings(stage);
            });
            root.getChildren().add(settings);

            Button leaderboard = new Button("LEADERBOARD");
            leaderboard.setFont(menuFont);
            leaderboard.setPrefSize(sizeX,sizeY);
            leaderboard.setLayoutX(512-sizeX/2);
            leaderboard.setLayoutY(512+gap);
            leaderboard.setOnAction(event ->{
                leaderboard(stage);
            });
            root.getChildren().add(leaderboard);

            Button exit = new Button("EXIT");
            exit.setFont(menuFont);
            exit.setPrefSize(sizeX,sizeY);
            exit.setLayoutX(512-sizeX/2);
            exit.setLayoutY(512+2*gap);
            exit.setOnAction(event ->{
                exitFun();
            });
            root.getChildren().add(exit);
        }
        stage.setScene(scene);
        stage.show();
    }
    private void pve(Stage stage){
        animation=null;
        compute=null;

        root.getChildren().removeAll(root.getChildren());
        Scene scene = new Scene(root, 1024, 1024);
        Canvas canvas = new Canvas(1024,1024);
        root.getChildren().add(canvas);
        GC = canvas.getGraphicsContext2D();

        //preparing for play

        Vehicle = new ArrayList<>();
        projectiles = new ArrayList<>();
        Player player = new Player(false);
        Vehicle.add(player);

        player.hp.addListener(event -> {
            if(player.hp.get()<=0){
                gameOver(stage,player);
            }
        });

        {//Animation & Computing
            {//Keys
                scene.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.W)Player.move1[0] = true;
                    if (event.getCode() == KeyCode.S) Player.move1[1] = true;
                    if (event.getCode() == KeyCode.A) Player.move1[2] = true;
                    if (event.getCode() == KeyCode.D) Player.move1[3] = true;
                    if (event.getCode() == KeyCode.ALT) Player.move1[4] = true;
                    if (event.getCode() == KeyCode.Q) player.weapon.set(((player.weapon.get()==1)?(9):(player.weapon.get()-1)));
                    if (event.getCode() == KeyCode.E) player.weapon.set(((player.weapon.get()==9)?(1):(player.weapon.get()+1)));
                });
                scene.setOnKeyReleased(event -> {
                    if (event.getCode() == KeyCode.W) Player.move1[0] = false;
                    if (event.getCode() == KeyCode.S) Player.move1[1] = false;
                    if (event.getCode() == KeyCode.A) Player.move1[2] = false;
                    if (event.getCode() == KeyCode.D) Player.move1[3] = false;
                    if (event.getCode() == KeyCode.ALT) Player.move1[4] = false;
                });
            }
            {//animation & rest
                Compute.boot();
                Animation.boot();
            }
            {//hud
                HUD(stage, player, animation, compute);
            }
        }
        stage.setScene(scene);
        stage.show();
    }
    private void pvp(Stage stage, Scene scene){
        root.getChildren().removeAll(root.getChildren());
        //root = new Group();

        //Scene scene = new Scene(root, 1024, 1024);

        Canvas canvas = new Canvas(1024,1024);
        root.getChildren().add(canvas);

        //preparing for play

        Vehicle = new ArrayList<>();
        projectiles = new ArrayList<>();
        Player player1 = new Player(false);
        Vehicle.add(player1);
        Player player2 = new Player(true);
        Vehicle.add(player2);

        player1.hp.addListener(event -> {
            if(player1.hp.get()<=0){
                gameOver(stage,player2);
            }
        });
        player2.hp.addListener(event -> {
            if(player2.hp.get()<=0){
                gameOver(stage,player1);
            }
        });

        //all set for animation

        GC = canvas.getGraphicsContext2D();
        {//Animation & Computing
            {//Keys
                scene.setOnKeyPressed(event ->{
                    if(event.getCode()== KeyCode.W)Player.move1[0] = true;
                    if(event.getCode()== KeyCode.S)Player.move1[1] = true;
                    if(event.getCode()== KeyCode.A)Player.move1[2] = true;
                    if(event.getCode()== KeyCode.D)Player.move1[3] = true;
                    if(event.getCode()== KeyCode.ALT)Player.move1[4] = true;
                    if(event.getCode()== KeyCode.I)Player.move2[0] = true;
                    if(event.getCode()== KeyCode.K)Player.move2[1] = true;
                    if(event.getCode()== KeyCode.J)Player.move2[2] = true;
                    if(event.getCode()== KeyCode.L)Player.move2[3] = true;
                    if(event.getCode()== KeyCode.SHIFT)Player.move2[4] = true;
                    if (event.getCode() == KeyCode.Q) player1.weapon.set(((player1.weapon.get()==1)?(9):(player1.weapon.get()-1)));
                    if (event.getCode() == KeyCode.E) player1.weapon.set(((player1.weapon.get()==9)?(1):(player1.weapon.get()+1)));
                    if (event.getCode() == KeyCode.U) player2.weapon.set(((player2.weapon.get()==1)?(9):(player2.weapon.get()-1)));
                    if (event.getCode() == KeyCode.O) player2.weapon.set(((player2.weapon.get()==9)?(1):(player2.weapon.get()+1)));
                });
                scene.setOnKeyReleased(event ->{
                    if(event.getCode()== KeyCode.W)Player.move1[0] = false;
                    if(event.getCode()== KeyCode.S)Player.move1[1] = false;
                    if(event.getCode()== KeyCode.A)Player.move1[2] = false;
                    if(event.getCode()== KeyCode.D)Player.move1[3] = false;
                    if(event.getCode()== KeyCode.ALT)Player.move1[4] = false;
                    if(event.getCode()== KeyCode.I)Player.move2[0] = false;
                    if(event.getCode()== KeyCode.K)Player.move2[1] = false;
                    if(event.getCode()== KeyCode.J)Player.move2[2] = false;
                    if(event.getCode()== KeyCode.L)Player.move2[3] = false;
                    if(event.getCode()== KeyCode.SHIFT)Player.move2[4] = false;
                });
            }
            {//animation & rest
                Compute.boot();
                Animation.boot();
            }
            {//hud
                HUD(stage,player1, player2, animation, compute);
            }
        }
        //stage.setScene(scene);
        //stage.show();
    }
    private void settings(Stage stage){
        root.getChildren().removeAll(root.getChildren());
        {
            Canvas canvas = new Canvas(1024,1024);
            root.getChildren().add(canvas);
            GraphicsContext GC = canvas.getGraphicsContext2D();
            if(true){//backgroud
                GC.drawImage(new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Core/menu.jpg"),0,0);
            }
        }
        if(true){//buttons
            ScrollPane scrollPane = new ScrollPane();
            GridPane gridPane = new GridPane();
            Button exit = new Button("BACK");
            exit.setFont(menuFont);
            exit.setPrefSize(sizeX,sizeY);
            exit.setOnAction(event ->{
                menu(stage);
            });
            gridPane.add(exit,0,0);
            listSettings(gridPane,0);
            for(int x = 0; x<things.size(); x++)things.get(x).listSettings(gridPane,x+1);
            scrollPane.setContent(gridPane);
            root.getChildren().add(scrollPane);
        }
        stage.setScene(new Scene(root, 1024, 1024));
        stage.show();
    }
    private void leaderboard(Stage stage){//todo add leaderboard?
        root.getChildren().removeAll(root.getChildren());
        {
            Canvas canvas = new Canvas(1024,1024);
            root.getChildren().add(canvas);
            GraphicsContext GC = canvas.getGraphicsContext2D();
            if(true){//backgroud
                GC.drawImage(new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Core/pve&pvp.jpg"),0,0);
            }
        }
        {//buttons
            Button exit = new Button("BACK");
            exit.setFont(menuFont);
            exit.setPrefSize(sizeX,sizeY);
            exit.setLayoutX(512-sizeX/2);
            exit.setLayoutY(1024-gap);
            exit.setOnAction(event ->{
                menu(stage);
            });
            root.getChildren().add(exit);
        }
        {// leaderboard
            for(int x  = 0; x<((PastPlayers.size()<10)?(PastPlayers.size()):(10)); x++) {
                User tmp = PastPlayers.get(x);
                Text out = new Text();
                out.setFont(menuFont);
                out.setFill(Color.WHITE);
                out.setLayoutX(300);
                out.setLayoutY(100+gap / 2+x*gap);
                out.setText(tmp.toString());
                root.getChildren().add(out);
            }
        }
        stage.setScene(new Scene(root, 1024, 1024));
        stage.show();
    }
    private void gameOver(Stage stage,Player player){//todo implement gameover.
        Compute.halt();
        animation.stop();
        for(int x = 0; x<5; x++) {
            Player.move1[x] = false;
            Player.move2[x] = false;
        }
        root.getChildren().removeAll(root.getChildren());
        {
            Canvas canvas = new Canvas(1024,1024);
            root.getChildren().add(canvas);
            GC = canvas.getGraphicsContext2D();
            GC.drawImage(new Image("file:///C:/Users/Admin/Desktop/Tools/Programing(1)/Room%2312/src/WarBlade/Core/menu.jpg"),0,0);
            {//text
                Text title = new Text("GameOver");
                title.setFont(new Font("Impact", 100));
                title.setX(300);
                title.setY(100);
                title.setFill(Color.RED);
                title.setStroke(Color.WHITE);
                root.getChildren().add(title);
            }
            {
                Text out = new Text();
                out.setFont(menuFont);
                out.setFill(Color.WHITE);
                out.setLayoutX(300);
                out.setLayoutY(200+gap / 2);
                out.setText("Moneyz: "+player.money.get()+"$");
                root.getChildren().add(out);
            }
            {
                TextField in = new TextField();
                in.setFont(menuFont);
                in.setLayoutX(300);
                in.setLayoutY(200+gap / 2+gap);
                in.setPrefWidth(1024-2*in.getLayoutX());
                in.setText("Write your name POWERFULL looser.");
                in.setOnAction(event -> {
                    PastPlayers.add(new User(in.getText(),player.money.get()));
                    menu(stage);
                });
                root.getChildren().add(in);
            }
        }
        {//buttons
            Button exit = new Button("BACK");
            exit.setFont(menuFont);
            exit.setPrefSize(sizeX,sizeY);
            exit.setLayoutX(512-sizeX/2);
            exit.setLayoutY(1024-gap);
            exit.setOnAction(event ->{
                menu(stage);
            });
            root.getChildren().add(exit);
        }
        stage.setScene(new Scene(root, 1024, 1024));
        stage.show();
    }
    private static void music() {
        mediaPlayer = new MediaPlayer(new Media("file:///C:/Users/Admin/Desktop/Games/Muzyka/AAA_chosen_one/tumblr_m17sz69v3l1qctlo8o1.mp3"));
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setStartTime(Duration.seconds(0));
        mediaPlayer.setStopTime(Duration.seconds(168));
    }
    private void shop(Stage stage,Player player,Animation animation, Compute compute){//todo shop
        Group wallmart = new Group();
        {//actual shop

        }
        {//buttons
            Button exit = new Button("BACK");
            exit.setFont(menuFont);
            exit.setPrefSize(sizeX, sizeY);
            exit.setLayoutX(0);
            exit.setLayoutY(1024 - gap);
            exit.setOnAction(event -> {
                animation.stop();
                compute.stop();
                menu(stage);
            });
            wallmart.getChildren().add(exit);
            Button back = new Button("return");
            back.setFont(menuFont);
            back.setPrefSize(sizeX, sizeY);
            back.setLayoutX(0);
            back.setLayoutY(1024 - 2 * gap);
            back.setOnAction(event -> {
                Compute.boot();
                root.getChildren().removeAll(wallmart);
                HUD(stage, player, animation, compute);
            });
            wallmart.getChildren().add(back);
        }
        root.getChildren().add(wallmart);
    }
    private void HUD(Stage stage,Player player,Animation animation, Compute compute){
        Group hud = new Group();
        {//actual HUD
            {
                Text showHP = new Text();
                showHP.setFont(menuFont);
                showHP.setFill(Color.WHITE);
                showHP.setLayoutX(0);
                showHP.setLayoutY(gap / 2);
                showHP.setText("HP: "+player.hp.get());
                player.hp.addListener( event ->{
                    showHP.setText("HP: "+player.hp.get());
                });
                hud.getChildren().add(showHP);
            }
            for(int x = 0; x<9; x++){
                Text showAmmo = new Text();
                showAmmo.setFont(menuFont);
                showAmmo.setFill(Color.WHITE);
                showAmmo.setLayoutX(0);
                showAmmo.setLayoutY((gap / 2)+gap*(x+1));
                showAmmo.setText(Projectile.name(x+1)+": "+player.ammo.get(x).get());
                int y=x;
                player.ammo.get(x).addListener( event ->{
                    showAmmo.setText(Projectile.name(y+1)+": "+player.ammo.get(y).get());
                });
                hud.getChildren().add(showAmmo);
            }
            {
                Text SW = new Text();
                SW.setFont(menuFont);
                SW.setFill(Color.WHITE);
                SW.setLayoutX(0);
                SW.setLayoutY((gap / 2)+9*gap+gap);
                SW.setText("selected: "+Projectile.name(player.weapon.get()));
                player.weapon.addListener( event ->{
                    SW.setText("selected: "+Projectile.name(player.weapon.get()));
                });
                hud.getChildren().add(SW);
            }
            {
                Text money = new Text();
                money.setFont(menuFont);
                money.setFill(Color.WHITE);
                money.setLayoutX(0);
                money.setLayoutY((gap / 2)+9*gap+gap+gap);
                money.setText("Moneyz: "+player.money.get()+"$");
                player.money.addListener( event ->{
                    money.setText("Moneyz: "+player.money.get()+"$");
                });
                hud.getChildren().add(money);
            }
        }
        {//buttons
            Button exit = new Button("BACK");
            exit.setFont(menuFont);
            exit.setPrefSize(sizeX, sizeY);
            exit.setLayoutX(0);
            exit.setLayoutY(1024 - gap);
            exit.setOnAction(event -> {
                animation.stop();
                compute.stop();
                menu(stage);
            });
            hud.getChildren().add(exit);
            Button shop = new Button("shop");
            shop.setFont(menuFont);
            shop.setPrefSize(sizeX, sizeY);
            shop.setLayoutX(0);
            shop.setLayoutY(1024 - 2 * gap);
            shop.setOnAction(event -> {
                Compute.halt();
                root.getChildren().removeAll(hud);
                shop(stage, player, animation, compute);
            });
            hud.getChildren().add(shop);
        }
        root.getChildren().add(hud);
    }
    private void HUD(Stage stage,Player player1, Player player2,Animation animation, Compute compute){//todo hud
        Group hud = new Group();
        {//actual HUD
            {
                Text showHP = new Text();
                showHP.setFont(menuFont);
                showHP.setFill(Color.WHITE);
                showHP.setLayoutX(0);
                showHP.setLayoutY(gap / 2);
                showHP.setText("HP: "+player1.hp.get()+" "+player2.hp.get());
                player1.hp.addListener( event ->{
                    showHP.setText("HP: "+player1.hp.get()+" "+player2.hp.get());
                });
                player2.hp.addListener( event ->{
                    showHP.setText("HP: "+player1.hp.get()+" "+player2.hp.get());
                });
                hud.getChildren().add(showHP);
            }
            for(int x = 0; x<9; x++){
                Text showAmmo = new Text();
                showAmmo.setFont(menuFont);
                showAmmo.setFill(Color.WHITE);
                showAmmo.setLayoutX(0);
                showAmmo.setLayoutY((gap / 2)+gap*(x+1));
                showAmmo.setText(Projectile.name(x+1)+": "+player1.ammo.get(x).get()+" "+player2.ammo.get(x).get());
                int y=x;
                player1.ammo.get(x).addListener( event ->{
                    showAmmo.setText(Projectile.name(y+1)+": "+player1.ammo.get(y).get()+" "+player2.ammo.get(y).get());
                });
                player2.ammo.get(x).addListener( event ->{
                    showAmmo.setText(Projectile.name(y+1)+": "+player1.ammo.get(y).get()+" "+player2.ammo.get(y).get());
                });
                hud.getChildren().add(showAmmo);
            }
            {
                Text SW = new Text();
                SW.setFont(menuFont);
                SW.setFill(Color.WHITE);
                SW.setLayoutX(0);
                SW.setLayoutY((gap / 2)+9*gap+gap);
                SW.setText("selected: "+Projectile.name(player1.weapon.get())+" "+Projectile.name(player2.weapon.get()));
                player1.weapon.addListener( event ->{
                    SW.setText("selected: "+Projectile.name(player1.weapon.get())+" "+Projectile.name(player2.weapon.get()));
                });
                player2.weapon.addListener( event ->{
                    SW.setText("selected: "+Projectile.name(player1.weapon.get())+" "+Projectile.name(player2.weapon.get()));
                });
                hud.getChildren().add(SW);
            }
        }
        {//buttons
            Button exit = new Button("BACK");
            exit.setFont(menuFont);
            exit.setPrefSize(sizeX, sizeY);
            exit.setLayoutX(0);
            exit.setLayoutY(1024 - gap);
            exit.setOnAction(event -> {
                animation.stop();
                compute.stop();
                menu(stage);
            });
            hud.getChildren().add(exit);
        }
        root.getChildren().add(hud);
    }
    private void listSettings(GridPane gridPane, int rowindex){//todo list settings

    }
    private void loadFun(){//todo implement loading from file
        things.add(new Player(false));
        Projectile smallBullet = new Projectile(Projectile.SMALLARMS);
        things.add(smallBullet);
        PastPlayers.add(new User("Template",100));
    }
    private void exitFun(){//todo add saving.
        Platform.exit();
        System.exit(0);
    }
}
