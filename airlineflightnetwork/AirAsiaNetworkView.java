/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.airlineflightnetwork;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.*;

public class AirAsiaNetworkView {
    private Graph<? extends Displayable> graph;
    private Stage stage;
    
    // size settings
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int CIRCLE_SIZE = 40;
    
    // color scheme
    private static final Color FILL_COLOR = Color.LIGHTBLUE;
    private static final Color BORDER_COLOR = Color.DARKBLUE;
    private static final Color LINE_COLOR = Color.BLUE;
    private static final Color FONT_COLOR = Color.BLACK;
  
    // constructor 
    public AirAsiaNetworkView(Graph<? extends Displayable> graph) {
        this.graph = graph;
        this.stage = new Stage();
        setupView();
    }

    private void setupView() {
        Pane mainPane = createGraphVisualization();
        Scene scene = new Scene(mainPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setTitle("Air Asia Flight Network Visualization");
        stage.setScene(scene);
        stage.setResizable(true);
    }

    private Pane createGraphVisualization() {
        Pane graphPane = new Pane();
        graphPane.setStyle("-fx-background-color: white;");
        
        if (graph.getSize() == 0) {
            Text noDataText = new Text(350, 225, "No airports to display");
            noDataText.setFont(Font.font("Times New Roman", 16));
            noDataText.setFill(Color.GRAY);
            graphPane.getChildren().add(noDataText);
            return graphPane;
        }

        List<? extends Displayable> vertices = graph.getVertices();
        List<Point> positions = calculatePositions();
        
        connectionLines(graphPane, positions);
        circles(graphPane, vertices, positions);
       
        return graphPane;
    }
    
    private List<Point> calculatePositions(){
        List<Point> positions = new ArrayList<>();
        int numOfAirports = graph.getSize();
        
        if(numOfAirports == 1){
            positions.add(new Point(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2));
        }
        else if(numOfAirports <= 10){
            positions = makeCircleLayout(numOfAirports);
        }
        else{
            positions = makeGridLayout(numOfAirports);
        }
        
        return positions;
    }
    
    private List<Point> makeCircleLayout(int numOfAirports){
        List<Point> positions = new ArrayList<>();
        int centerX = WINDOW_WIDTH / 2;
        int centerY = WINDOW_HEIGHT / 2;
        int radius = 200;
        
        for(int i = 0; i < numOfAirports; i++){
            double angle = 2 * Math.PI * i / numOfAirports;
            int x = (int)(centerX + radius * Math.cos(angle));
            int y = (int)(centerY + radius * Math.sin(angle));
            positions.add(new Point(x, y));
        }
        
        return positions;
    }
    
    private List<Point> makeGridLayout(int numAirports) {
        
        List<Point> positions = new ArrayList<>();
        int columns = (int) Math.ceil(Math.sqrt(numAirports));
        int rows = (int) Math.ceil((double) numAirports / columns);
        
        int spaceX = (WINDOW_WIDTH - 100) / columns;
        int spaceY = (WINDOW_HEIGHT - 100) / rows;
        
        for (int i = 0; i < numAirports; i++) {
            int row = i / columns;
            int col = i % columns;
            int x = 50 + col * spaceX + spaceX/2;
            int y = 50 + row * spaceY + spaceY/2;
            positions.add(new Point(x, y));
        }
        
        return positions;
    }
    
    private void connectionLines(Pane graphPane, List<Point> positions){
        
        Set<String> drawnLines = new HashSet<>();
        
        for(int i = 0; i < graph.getSize(); i++){
            List<Integer> neighbors = graph.getNeighbors(i);
            Point startPoint = positions.get(i);
            
            for(int index : neighbors){
                Point endPoint = positions.get(index);
                
                String lineKey = Math.min(i, index) + "-" + Math.max(i, index);
                if (!drawnLines.contains(lineKey)) {
                    Line line = new Line(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
                    line.setStroke(LINE_COLOR);
                    line.setStrokeWidth(2);
                    graphPane.getChildren().add(line);
                    drawnLines.add(lineKey);
                }
            }
        }
    }
    
    private void circles(Pane graphPane, List<? extends Displayable> vertices, List<Point> positions){
        for(int i = 0; i < graph.getSize(); i++){
            Point position = positions.get(i);
            String cityName = vertices.get(i).getName();
            
            Circle circle = new Circle(position.x, position.y, CIRCLE_SIZE);
            circle.setFill(FILL_COLOR);
            circle.setStroke(BORDER_COLOR);
            circle.setStrokeWidth(2);
            
            Text newText = createText(cityName, position.x, position.y);
            
            graphPane.getChildren().addAll(circle, newText);
        }
    }
    
    private Text createText(String cityName, int centerX, int centerY){
        Text cityText = new Text(cityName);
        cityText.setFont(Font.font("Times New Roman", 14));
        cityText.setFill(FONT_COLOR);
        
        double textWidth = cityText.getBoundsInLocal().getWidth();
        double textHeight = cityText.getBoundsInLocal().getHeight();
        
        cityText.setX(centerX - textWidth / 2);
        cityText.setY(centerY + textHeight / 4);
        
        return cityText;       
    }

    public void show() {
        stage.show();
        stage.toFront();
        stage.requestFocus();
    }
    
    private static class Point{
        int x, y;
        
        Point(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
}
