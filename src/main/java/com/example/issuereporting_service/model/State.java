package com.example.issuereporting_service.model;

/* This class is used to hold the data object of the pie chart
 *  The pie chart has the percentage values of the states of the issues  */
public class State {

    // y holds the percentage value of an issue state
    private double y;

    // labels holds the state of that percentage
    private String label;

    public State() {
    }

    public State(double y, String label) {
        this.y = y;
        this.label = label;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "State{" +
                "y=" + y +
                ", label='" + label + '\'' +
                '}';
    }
}
