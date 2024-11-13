package inkball;

import java.util.ArrayList;
/**
 * Constructor for the Line class.
 * Initializes the line with the provided list of points.
 * If the list is empty, a message is printed to indicate that there are no points.
 */
public class Line {
    public ArrayList<int[]> points;
    public Line(ArrayList<int[]> points) {

        this.points = points;
        if(points.size()==0) System.out.println("empty points");

    }

}
