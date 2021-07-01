package view;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Observable;
import java.util.Observer;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view_model.ViewModel;

public class MainController implements Observer {
    private ViewModel vm;
    public StringProperty anomalyFlightPath,propertiesPath;
    @FXML private Slider throttle, rudder;
    @FXML private Circle joystickControl;
    public StringProperty selectedFeature;
    public ListProperty<String> features;
    public ListView<String> attributesListView;
    public DoubleProperty _throttle, _rudder,aileron,elevators;
    @FXML public Slider progressBar;
    @FXML public Label currentTime;
    public DoubleProperty playSpeed;
    public Runnable play,pause,forward,rewind;


    @FXML
    private void pressButtonPlay(){
        if(play!=null){
            play.run();
        }
    }
    @FXML
    private void pressButtonPause(){
        if(pause!=null){
            pause.run();
        }
    }
    @FXML
    private void pressButtonForward(){
        if(forward!=null){
            forward.run();
        }
    }
    @FXML
    private void pressButtonRewind(){
        if(rewind!=null){
            rewind.run();
        }
    }
    @FXML
    private void pressButtonStop() {
        if (!progressBar.isDisabled()) {
            progressBar.setValue(1);
        }
    }

    public Slider getThrottle() { return throttle; }

    public Slider getRudder() {
        return rudder;
    }

    public Circle getJoystickControl() { return joystickControl; }

    public void LoadList() {
            features=new SimpleListProperty<>(FXCollections.observableArrayList());
            features.addListener((observable, oldValue, newValue) -> {
            attributesListView.setItems(newValue);
            attributesListView.getSelectionModel().select(0);
            selectedFeature=new SimpleStringProperty(attributesListView.getSelectionModel().getSelectedItems().get(0));
        });

        attributesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(selectedFeature!=null) {
                selectedFeature.setValue(newValue);
            }
        });
    }

    public void setViewModel(ViewModel vm) {
        this.vm=vm;
        playSpeed=new SimpleDoubleProperty(1.0);
        aileron = getJoystickControl().centerXProperty();
        elevators = getJoystickControl().centerYProperty();
        _rudder = getRudder().valueProperty();
        _throttle = getThrottle().valueProperty();
        anomalyFlightPath=new SimpleStringProperty();
        vm.anomalyFlightPath.bind(anomalyFlightPath);
        propertiesPath=new SimpleStringProperty();
        vm.propertiesPath.bind(propertiesPath);
        progressBar.valueProperty().bindBidirectional(vm.progression);
        currentTime.textProperty().bind(vm.currentTime);
        vm.playSpeed.bind(playSpeed);
        play=()->{
            if(anomalyFlightPath.getValue()==null) {
                showErrorMessage("You must upload a CSV file.");
                return;
            }
            vm.play();
        };
        pause=()->vm.pause();
        forward=()->vm.forward();
        rewind=()->vm.rewind();
        _rudder.bind(vm.rudder);
        _throttle.bind(vm.throttle);
        aileron.bind(vm.aileron);
        elevators.bind(vm.elevators);
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    private String uploadFile(String title,String description,String extensions){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(description, extensions));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            return file.getPath();
        }
        return null;
    }

    @FXML
    private void pressButtonLoad(){
        String filePath=uploadFile("Upload flight recording file - CSV","CSV file","*.csv*");
        if (filePath != null) {
            anomalyFlightPath.setValue(filePath);
            LoadList();
            features.bind(vm.features);
            vm.selectedFeature.bind(selectedFeature);
        }
    }
    public AnchorPane getPainter() throws Exception {
        return vm.getPainter().call();
    }

    @FXML
    private void pressButtonAnomalyDetection(){
        if(anomalyFlightPath.getValue()==null){
            showErrorMessage("You must upload flight recording file before!");
            return;
        }
        String filePath=uploadFile("Upload anomaly detection algorithm","JAR file","*.jar*");
        if (filePath != null) {
            try {
                vm.setAnomalyDetector(filePath);
            }
            catch (MalformedURLException e) {
                showErrorMessage("This is not a valid JAR file.");
            }
            catch (ClassNotFoundException e) {
                showErrorMessage("There is no anomalyDetectors.Algorithm class");
            }
            catch (InstantiationException e) {
                showErrorMessage("You don't realized the methods of the interface as well");
            }
            catch (IllegalAccessException e) {
                showErrorMessage("You don't realized the methods of the interface as well");
            }
            catch (Exception e) {
                showErrorMessage("The paint method is wrong");
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        switch (arg.toString()) {
            case "CSV file error":
                showErrorMessage("You need upload a CSV file");
                anomalyFlightPath.setValue(null);
                break;
            case "Properties file error":
                showErrorMessage("Please try again");
                break;
        }
    }
}