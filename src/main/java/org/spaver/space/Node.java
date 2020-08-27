package org.spaver.space;

import org.spaver.shape.Point;

//import com.sun.javafx.geom.Point2D;

public class Node {

    public Node parent;
    public Point point;
    public double distance;

    public double helper = 0;

    public Node(Node parent, Point point){
        this.parent = parent;
        this.point = point;

        this.distance = parent.distance + Math.sqrt((parent.point.x-point.x)*(parent.point.x-point.x) +
                (parent.point.y-point.y)*(parent.point.y-point.y));
    }

    public Node(Point point){
        this.point = point;
        this.parent = null;
        this.distance = 0;
    }

}

