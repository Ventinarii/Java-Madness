package Stages;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MyErrorPopup extends Stage {
    public MyErrorPopup(String title, Node data, boolean critical){
        Group root = new Group();
        setScene(new Scene(root,512,512));
        setTitle(title);
        root.getChildren().add(data);
        show();
        if(!critical)
            setOnCloseRequest((event)-> this.close());
        else {
            App.kill();
            Chat.kill();
            setOnCloseRequest(event -> System.exit(-2));
        }
    }//DONE
}
