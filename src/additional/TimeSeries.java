package additional;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TimeSeries {
    private Map<String, ArrayList<Double>> ts;
    private ListProperty<String> attr;

    public TimeSeries(String csvFileName) throws IOException {
        ts=new HashMap();
        attr =new SimpleListProperty<>(FXCollections.observableArrayList());
        BufferedReader in=new BufferedReader(new FileReader(csvFileName));
        String line=in.readLine();
        for(String att : line.split(",")) {
            attr.add(att);
            ts.put(att, new ArrayList<>());
        }
        while((line=in.readLine())!=null) {
            int i=0;
            for(String val : line.split(",")) {
                ts.get(attr.get(i)).add(Double.parseDouble(val));
                i++;
            }
        }
        in.close();
    }

    public ArrayList<Double> getAttributeData(String featureName){
        return ts.get(featureName);
    }

    public ListProperty<Point> getListOfPointsUntilSpecificRow(String featureName, int rowNumber){
        ListProperty<Point> tempList=new SimpleListProperty(FXCollections.observableArrayList());
        for(int i=0; i<rowNumber;i++){
            tempList.add(new Point(i,ts.get(featureName).get(i)));
        }
        return tempList;
    }

    public ListProperty<String> getAttributes(){
        return attr;
    }

    public int getRowSize() {
        return ts.get(attr.get(0)).size();
    }

    public int getNumOfColumns(){
        return ts.size();
    }

    public String getRowByRowNumber(int rowNumber) {
        StringBuilder result= new StringBuilder();
        for(int i = 0; i< attr.size()-1; i++) {
            result.append(ts.get(attr.get(i)).get(rowNumber)+",");
        }
        result.append(ts.get(attr.get(attr.size()-1)).get(rowNumber));
        return result.toString();
    }

    public double getDataFromSpecificRowAndColumn(String featureName,int rowNumber){
        return ts.get(featureName).get(rowNumber);
    }
}