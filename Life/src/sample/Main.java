package sample;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    private static final Color white=Color.WHITE,black=Color.BLACK;
    private static final int recSize = 9,deathBellow=2,deathAbove=3,bornAbove=2;
    private int sideSize;
    private Group root = new Group();
    private Stage primaryStage;
    private TextArea numberOfStepsTextAera,logTextAera;
    private Button startSimButton;
    private Rectangle[][] rectangles;
    private byte[][] matrixNow,matrixNew;
    private AnimationTimer animationTimer;
    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage=primaryStage;
        setUpWindow();
        setUpMap();
        setUpPanel();
        setUpAnimation();
    }
    private void setUpMap(){
        sideSize=(int)(primaryStage.getHeight()-90)/(recSize+1);
        matrixNow=new byte[sideSize][sideSize];
        rectangles=new Rectangle[sideSize][sideSize];
        for(int y=0;y<sideSize;y++)
            for(int x=0;x<sideSize;x++) {
                matrixNow[y][x]=-1;

                rectangles[y][x] = new Rectangle(10+(recSize+2)*x,10+(recSize+2)*y,recSize,recSize);
                rectangles[y][x].setFill(white);
                root.getChildren().add(rectangles[y][x]);
            }
    }
    private void setUpWindow(){
        primaryStage.setTitle("Game of life");
        primaryStage.setScene(new Scene(root, 1000, 500));
        primaryStage.getScene().setFill(Color.GRAY);
        primaryStage.minHeightProperty().bind(primaryStage.widthProperty().multiply(0.5));
        primaryStage.maxHeightProperty().bind(primaryStage.widthProperty().multiply(0.5));
        primaryStage.show();
    }
    private void setUpPanel(){
        numberOfStepsTextAera = new TextArea();
        numberOfStepsTextAera.setPrefHeight(25);
        numberOfStepsTextAera.setPrefWidth(100);
        numberOfStepsTextAera.setLayoutX(primaryStage.getHeight());
        numberOfStepsTextAera.setLayoutY(10);
        root.getChildren().add(numberOfStepsTextAera);

        startSimButton = new Button("start Simulation");
        startSimButton.setPrefHeight(25);
        startSimButton.setPrefWidth(100);
        startSimButton.setLayoutX(primaryStage.getHeight());
        startSimButton.setLayoutY(50);
        root.getChildren().add(startSimButton);

        startSimButton.setOnAction((a)->runSim());
        primaryStage.widthProperty().addListener((a,b,c)->resize());
        primaryStage.heightProperty().addListener((a,b,c)->resize());

        primaryStage.getScene().setOnMouseClicked((e)->click(e));
    }
    private void setUpAnimation(){
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                try{
                    for(int y=0;y<matrixNow.length;y++)
                        for(int x=0;x<matrixNow[y].length;x++)
                            rectangles[y][x].setFill(((matrixNow[y][x]==1)?(black):(white)));
                }catch (Exception ex){ex.printStackTrace();}
            }
        };
        animationTimer.start();
    }
    public void runSim(){
        Thread thread = new Thread(()->{
            int steps=1;
            try {
                steps = Integer.parseInt(numberOfStepsTextAera.getText().trim());
            }catch (Exception ex){}
            try{
                for(int tmp=0;tmp<steps;tmp++) {
                    matrixNew = new byte[matrixNow.length][matrixNow.length];
                    for (int x = 0; x < matrixNow.length; x++)
                        matrixNew[0][x] = matrixNow[0][x];
                    for (int x = 0; x < matrixNow.length; x++)
                        matrixNew[matrixNow.length - 1][x] = matrixNow[matrixNow.length - 1][x];

                    for (int x = 0; x < matrixNow.length; x++)
                        matrixNew[x][0] = matrixNow[x][0];
                    for (int x = 0; x < matrixNow.length; x++)
                        matrixNew[x][matrixNow.length - 1] = matrixNow[x][matrixNow.length - 1];

                    for (int y = 1; y < matrixNow.length - 1; y++)
                        for (int x = 1; x < matrixNow.length - 1; x++)
                            matrixNew[y][x] = (byte) ((live(matrixNow[y][x],y, x)) ? (1) : (-1));

                    byte[][] swap=matrixNow;
                    matrixNow=matrixNew;
                    matrixNew=matrixNow;
                }
            }catch (Exception ex){
                for(int y=0;y<sideSize;y++)
                    for(int x=0;x<sideSize;x++)
                        matrixNow[y][x]=-1;
                ex.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
    public boolean live(byte now,int y,int x){
        int number=
                ((matrixNow[y-1][x-1]==1)?(1):(0))+((matrixNow[y-1][x  ]==1)?(1):(0))+((matrixNow[y-1][x+1]==1)?(1):(0))+
                ((matrixNow[y  ][x-1]==1)?(1):(0))+                                   ((matrixNow[y  ][x+1]==1)?(1):(0))+
                ((matrixNow[y+1][x-1]==1)?(1):(0))+((matrixNow[y+1][x  ]==1)?(1):(0))+((matrixNow[y+1][x+1]==1)?(1):(0));
        //System.out.println(
        //        ((matrixNow[y-1][x-1]==1)?("X"):("O"))+" "+((matrixNow[y-1][x  ]==1)?("X"):("O"))+" "+((matrixNow[y-1][x+1]==1)?("X"):("O"))+System.lineSeparator()+
        //        ((matrixNow[y  ][x-1]==1)?("X"):("O"))+" "+((matrixNow[y  ][x  ]==1)?("X"):("O"))+" "+((matrixNow[y  ][x+1]==1)?("X"):("O"))+System.lineSeparator()+
        //        ((matrixNow[y+1][x-1]==1)?("X"):("O"))+" "+((matrixNow[y+1][x  ]==1)?("X"):("O"))+" "+((matrixNow[y+1][x+1]==1)?("X"):("O"))+System.lineSeparator()+
        //        ((now==1)?(deathBellow<=number&&number<=deathAbove):(bornAbove<number)));
        return (now==1)?
                (deathBellow<=number&&number<=deathAbove):
                ((bornAbove<number)&&(deathBellow<=number&&number<=deathAbove));
    }
    public void click(MouseEvent e){
        try {
            if (e.getButton().toString().equals("PRIMARY")) {
                int x = (int) e.getSceneX(), y = (int) e.getSceneY();
                x -= 10;
                y -= 10;
                x = x / (recSize + 2);
                y = y / (recSize + 2);
                matrixNow[y][x] = (byte) -matrixNow[y][x];
            }
        }catch (Exception ex){System.out.println(ex);}
    }
    public void resize() {

    }
}
