package org.spaver.planner;


import org.spaver.shape.Point;
import org.spaver.space.WeightedGraph;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class UserPanel extends VBox {

    private ComboBox<String> strategyBox;
    private ComboBox<String> obstacleSet;

    private enum Strategy {
        PRM,
        RRT,
        RRTstar;
    }

    private Strategy strategy;
    private WeightedGraph weightedGraph;
    public UserPanel(WeightedGraph graph){
        super();
        this.weightedGraph = graph;
        setup();
    }

    private void setup(){
        HBox addition = new HBox();
        Button addOne = new Button("Add 1 Point");
        addOne.setOnMouseClicked(event -> {
            if(strategy == Strategy.PRM) weightedGraph.addPRM(1);
            else if(strategy == Strategy.RRT) weightedGraph.addRRT(1);
            else if(strategy == Strategy.RRTstar) weightedGraph.addRRTStar(1);
        });

        Button addTwo = new Button("Add 50 Points");
        addTwo.setOnMouseClicked(event -> {
            if(strategy == Strategy.PRM) weightedGraph.addPRM(50);
            else if(strategy == Strategy.RRT) weightedGraph.addRRT(50);
            else if(strategy == Strategy.RRTstar) weightedGraph.addRRTStar(50);
        });

        Button addThree = new Button("Add 500 Points");
        addThree.setOnMouseClicked(event -> {
            if(strategy == Strategy.PRM) weightedGraph.addPRM(500);
            else if(strategy == Strategy.RRT) weightedGraph.addRRT(500);
            else if(strategy == Strategy.RRTstar) weightedGraph.addRRTStar(500);
        });

        Button connect = new Button("Connect PRM");
        connect.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                weightedGraph.connect();
            }
        });

        addition.getChildren().addAll(addOne, addTwo, addThree, connect);
        addition.setSpacing(5);

        Button clear = new Button("Clear Space");
        clear.setOnMouseClicked(event -> {
            weightedGraph.reset();
        });

        strategyBox = new ComboBox<String>();
        strategyBox.setPromptText("Search Strategy");
        strategyBox.setOnAction(this::updateStrategy);
        strategyBox.getItems().addAll(
                "Probabilistic Road Map",
                "Rapidly Expanding Random Tree",
                "Rapidly Expanding Random Tree Star"
        );

        obstacleSet = new ComboBox<String>();
        obstacleSet.setPromptText("Obstacle Set");
        obstacleSet.setOnAction(this::updateObstacles);
        obstacleSet.getItems().addAll(
                "Set 1",
                "Set 2",
                "Set 3",
                "No Obstacles"
        );

        Button path = new Button("Path planning");
        path.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	weightedGraph.connectShortestPath();
            }
        });        
        
//        HBox slider = new HBox();
//
//        Label label = new Label("RRT Increment:  ");
//
//        Slider multiplierSlider = new Slider();
//        multiplierSlider.setMin(0);
//        multiplierSlider.setMax(30);
//        multiplierSlider.setValue(10);
//        multiplierSlider.setShowTickLabels(true);
//        multiplierSlider.setShowTickMarks(true);
//        multiplierSlider.setMajorTickUnit(2);
//        multiplierSlider.setMinorTickCount(1);
//        multiplierSlider.setBlockIncrement(10);
//        multiplierSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
//            weightedGraph.setRRTMultiplier(newValue.intValue());
//        });
//        multiplierSlider.setPrefWidth(300);
//        slider.getChildren().addAll(label, multiplierSlider);

//        this.getChildren().addAll(addition, clear, strategyBox, obstacleSet, path, slider);

        this.getChildren().addAll(addition, clear, strategyBox, obstacleSet, path);

        this.setSpacing(5);
        this.setPadding(new Insets(10));
    }

    

    
    /**
     * 
     * @param event
     */
    private void updateObstacles(Event event) {
        if(obstacleSet.getValue().equals("Set 1")) weightedGraph.setObstacles(0);
        else if(obstacleSet.getValue().equals("Set 2")) weightedGraph.setObstacles(1);
        else if(obstacleSet.getValue().equals("Set 3")) weightedGraph.setObstacles(2);
        else if(obstacleSet.getValue().equals("No Obstacles")) weightedGraph.setObstacles(3);
    }


    /**
     * 
     * @param event
     */
    private void updateStrategy(Event event) {
        if(strategyBox.getValue().equals("Probabilistic Road Map")) strategy = Strategy.PRM;
        else if(strategyBox.getValue().equals("Rapidly Expanding Random Tree")) strategy = Strategy.RRT;
        else if(strategyBox.getValue().equals("Rapidly Expanding Random Tree Star")) strategy = Strategy.RRTstar;
    }

}

