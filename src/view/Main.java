package view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.FlightSimulatorModel;
import view_model.ViewModel;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxl=new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root= fxl.load();
        Image icon = new Image("files/icon.jpeg");
        primaryStage.setTitle("Flight Simulator");
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        FlightSimulatorModel flightGearModel=new FlightSimulatorModel();
        ViewModel vm=new ViewModel(flightGearModel);
        MainController mwc=fxl.getController();
        mwc.setViewModel(vm);
        vm.addObserver(mwc);
        primaryStage.setOnCloseRequest(event -> {
            vm.shutdownExecutor();
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
