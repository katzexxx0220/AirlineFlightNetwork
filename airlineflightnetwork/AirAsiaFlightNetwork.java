/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author User
 */
package com.mycompany.airlineflightnetwork;

import java.util.Scanner;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.util.*;

public class AirAsiaFlightNetwork extends Application {
    private static UnweightedGraph<String> flightGraph = new UnweightedGraph<>();
    private static Scanner scanner = new Scanner(System.in);
    private static boolean javaFXLaunced = false;
    
    @Override
    public void start(Stage primaryStage) {
        // This will be called when JavaFX is launched
        javaFXLaunced = true;
        
       new Thread(() -> {
            showWelcomeAndMenu();
        }).start();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    private static void showWelcomeAndMenu() {
        System.out.println("==========================================");
        System.out.println(String.format("%33s", "Welcome to Air Asia Flight!"));
        System.out.println("==========================================");
        System.out.println("Staff ID: MAS01\n");
        
        while (true) {
            showMainMenu();
            int choice = getValidChoice(0, 3);
            
            switch (choice) {
                case 1:
                    handleCreateGraph();
                    break;
                case 2:
                    handleSearchAirport();
                    break;
                case 3:
                    handleViewNetwork();
                    break;
                case 0:
                    System.out.println("Thank you for using Air Asia Flight Network System!");
                    Platform.exit();
                    System.exit(0);
                    return;
            }
        }
    }
    
    private static void showMainMenu() {
        System.out.println("\nMain Menu (Press '0' to back to main menu)");
        System.out.println("Menu: Enter \"1\" to \"3\" for computation option.");
        System.out.println("================================================");
        System.out.println("1. Create Graph");
        System.out.println("2. Search for an Airport");
        System.out.println("3. View the Air Asia Flight Network");
        System.out.println("0. Exit");
        System.out.println();
    }
    
    private static void handleCreateGraph() {
        while (true) {
            System.out.println("\nCreate Graph: Enter \"1\" to \"4\" for updating the graph.");
            System.out.println("=========================================================");
            System.out.println("1. Add a vertex");
            System.out.println("2. Remove a vertex");
            System.out.println("3. Add an edge");
            System.out.println("4. Remove an edge");
            System.out.println("5. Return to the main menu");
            System.out.println();
            
            int choice = getValidChoice(1, 5);
            
            switch (choice) {
                case 1:
                    handleAddVertex();
                    break;
                case 2:
                    handleRemoveVertex();
                    break;
                case 3:
                    handleAddEdge();
                    break;
                case 4:
                    handleRemoveEdge();
                    break;
                case 5:
                    return;
            }
            
            if (choice != 5) {
                if (!getContinueChoice()) {
                    return;
                }
            }
        }
    }
    
    private static boolean getContinueChoice() {
        while (true) {
            System.out.print("Continue? Y/N: ");
            String input = scanner.nextLine().trim();
            
            if (input.length() == 1) {
                char choice = input.charAt(0);
                if (choice == 'Y' || choice == 'y') {
                    return true;
                } else if (choice == 'N' || choice == 'n') {
                    return false;
                } else {
                    System.out.println("Please Enter Y/N only!!!");
                }
            } else {
                System.out.println("Please Enter Y/N only!!!");
            }
        }
    }
    
    private static void handleAddVertex() {
        String cityName = getValidCityName();
        
        if (flightGraph.addVertex(cityName)) {
            System.out.println("City \"" + cityName + "\" has been successfully added!");
        } else {
            System.out.println("City \"" + cityName + "\" already exists!");
        }
    }
    
    private static void handleRemoveVertex() {
        if (flightGraph.getSize() == 0) {
            System.out.println("No airports available to remove.");
            return;
        }
        
        System.out.println("Available airports:");
        for (int i = 0; i < flightGraph.getSize(); i++) {
            System.out.println((i + 1) + ". " + flightGraph.getVertex(i));
        }
        
        System.out.print("Enter the name of the city to remove: ");
        String cityName = scanner.nextLine().trim();
        
        if (flightGraph.removeVertex(cityName)) {
            System.out.println("City \"" + cityName + "\" has been removed successfully!");
        } else {
            System.out.println("City \"" + cityName + "\" not found!");
        }
    }
    
    private static void handleAddEdge() {
        if (flightGraph.getSize() < 2) {
            System.out.println("Need at least 2 airports to create a flight path.");
            return;
        }
        
        System.out.println("Available airports:");
        for (int i = 0; i < flightGraph.getSize(); i++) {
            System.out.println((i + 1) + ". " + flightGraph.getVertex(i));
        }
        
        System.out.print("Enter the 1st vertex name: ");
        String city1 = scanner.nextLine().trim();
        System.out.print("Enter the 2nd vertex name: ");
        String city2 = scanner.nextLine().trim();
        
        int index1 = flightGraph.getIndex(city1);
        int index2 = flightGraph.getIndex(city2);
        
        if (index1 == -1) {
            System.out.println("Airport \"" + city1 + "\" not found!");
            return;
        }
        if (index2 == -1) {
            System.out.println("Airport \"" + city2 + "\" not found!");
            return;
        }
        if (index1 == index2) {
            System.out.println("Cannot create flight path from an airport to itself!");
            return;
        }
        
        // Add bidirectional edges
        boolean added1 = flightGraph.addEdge(index1, index2);
        boolean added2 = flightGraph.addEdge(index2, index1);
        
        if (added1 && added2) {
            System.out.println("There is now a flight path between " + city1 + " and " + city2 + ".");
        } else {
            System.out.println("Flight path between " + city1 + " and " + city2 + " already exists!");
        }
    }
    
    private static void handleRemoveEdge() {
        if (flightGraph.getSize() < 2) {
            System.out.println("Need at least 2 airports to remove a flight path.");
            return;
        }
        
        System.out.println("Available airports:");
        for (int i = 0; i < flightGraph.getSize(); i++) {
            System.out.println((i + 1) + ". " + flightGraph.getVertex(i));
        }
        
        System.out.print("Enter the 1st vertex name: ");
        String city1 = scanner.nextLine().trim();
        System.out.print("Enter the 2nd vertex name: ");
        String city2 = scanner.nextLine().trim();
        
        int index1 = flightGraph.getIndex(city1);
        int index2 = flightGraph.getIndex(city2);
        
        if (index1 == -1) {
            System.out.println("Airport \"" + city1 + "\" not found!");
            return;
        }
        if (index2 == -1) {
            System.out.println("Airport \"" + city2 + "\" not found!");
            return;
        }
        
        // Remove bidirectional edges
        boolean removed1 = flightGraph.removeEdge(index1, index2);
        boolean removed2 = flightGraph.removeEdge(index2, index1);
        
        if (removed1 || removed2) {
            System.out.println("Flight path between " + city1 + " and " + city2 + " has been removed.");
        } else {
            System.out.println("No flight path exists between " + city1 + " and " + city2 + "!");
        }
    }
    
    private static void handleSearchAirport() {
        if (flightGraph.getSize() == 0) {
            System.out.println("No airports available. Please add airports first.");
            return;
        }
        
        System.out.println("Existing airports:");
        for (int i = 0; i < flightGraph.getSize(); i++) {
            System.out.println("- " + flightGraph.getVertex(i));
        }
        
        String airportName = getValidCityName();
        
        int index = flightGraph.getIndex(airportName);
        if (index == -1) {
            System.out.println("The airport \"" + airportName + "\" is not found.");
            return;
        }
        
        System.out.println("Airport: " + airportName);
        System.out.println("Vertex Index: " + index);
        System.out.println("Degree: " + flightGraph.getDegree(index));
        
        java.util.List<Integer> neighbors = flightGraph.getNeighbors(index);
        if (neighbors.isEmpty()) {
            System.out.println("Connections: No flights yet");
        } else {
            System.out.print("Connected to: ");
            for (int i = 0; i < neighbors.size(); i++) {
                System.out.print(flightGraph.getVertex(neighbors.get(i)));
                if (i < neighbors.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
        
        // graph traversal
        if(!neighbors.isEmpty() || flightGraph.getSize() > 1){
            System.out.println("\nGraph Traversal Demonstration: ");
            System.out.println("---------------------------------");
            
            // bfs
            AbstractGraph<String>.Tree bfs = flightGraph.bfs(index);
            List<Integer> bfsOrder = bfs.getSearchOrder();
            for(int i = 0; i < bfsOrder.size(); i++){
                System.out.print(flightGraph.getVertex(bfsOrder.get(i)));
                if(i < bfsOrder.size() - 1){
                    System.out.print(" > ");
                }
            }
            System.out.println();  
        }
    }
    
    private static boolean isValidCityName(String cityName) {
        for (int i = 0; i < cityName.length(); i++) {
            char ch = cityName.charAt(i);
            if (!Character.isLetter(ch) && ch != ' ') {
                return false;
            }
        }
        return true;
    }
    
    private static String getValidCityName() {
        while (true) {
            System.out.print("Enter an airport name: ");
            String cityName = scanner.nextLine().trim();
            
            if (cityName.isEmpty()) {
                System.out.println("Error: City name cannot be empty!");
                continue;
            }
            
            if (isValidCityName(cityName)) {
                return cityName;
            } else {
                System.out.println("Error: City name can only contain letters and spaces!");
            }
        }
    }
    
    
    
    private static void handleViewNetwork() {
        if (flightGraph.getSize() == 0) {
            System.out.println("No airports available. Please add airports first.");
            return;
        }
        
        // Convert String graph to City graph for visualization
        City[] cities = new City[flightGraph.getSize()];
        for (int i = 0; i < flightGraph.getSize(); i++) {
            // Calculate positions in a circle for better visualization
            double angle = 2 * Math.PI * i / flightGraph.getSize();
            int x = (int) (375 + 200 * Math.cos(angle)); // Center at (375, 225)
            int y = (int) (225 + 150 * Math.sin(angle));
            cities[i] = new City(flightGraph.getVertex(i), x, y);
        }
        
        // Create edges array
        List<int[]> edgeList = new ArrayList<>();
        for (int i = 0; i < flightGraph.getSize(); i++) {
            List<Integer> neighbors = flightGraph.getNeighbors(i);
            for (int neighbor : neighbors) {
                // Only add edge once (avoid duplicate bidirectional edges in visualization)
                if (i < neighbor) {
                    edgeList.add(new int[]{i, neighbor});
                }
            }
        }
        
        int[][] edges = edgeList.toArray(new int[edgeList.size()][]);
        
        // Create new graph for visualization
        UnweightedGraph<City> visualGraph = new UnweightedGraph<>(cities, edges);
        
        // Launch JavaFX window
        Platform.runLater(() -> {
            AirAsiaNetworkView networkView = new AirAsiaNetworkView(visualGraph);
            networkView.show();
        });
        
        System.out.println("Opening Air Asia Flight Network visualization...");
    }
    
    private static int getValidChoice(int min, int max) {
        while (true) {
            System.out.print("Selection: ");
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("Please enter a valid choice (" + min + "-" + max + ")");
                    continue;
                }
                int choice = Integer.parseInt(input);
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number (" + min + "-" + max + ")");
            }
        }
    }
    
    // City class for visualization
    static class City implements Displayable {
        private int x, y;
        private String name;
        
        City(String name, int x, int y) {
            this.name = name;
            this.x = x;
            this.y = y;
        }
        
        @Override
        public int getX(){
            return x;
        }
        
        @Override
        public int getY(){
            return y;
        }
        
        @Override
        public String getName(){
            return name;
        }
        
        @Override
        public String toString(){
            return name;
        }
    }
}
