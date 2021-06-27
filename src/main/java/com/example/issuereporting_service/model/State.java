package com.example.issuereporting_service.model;

import javax.persistence.*;

public class State {

    private double y;
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
//    private double openState;
//    private double waitingState;
//    private double progressState;
//    private double resolvedState;
//    private Issue issue;
//
//    public State(double openState, double waitingState, double progressState, double resolvedState) {
//        this.openState = openState;
//        this.waitingState = waitingState;
//        this.progressState = progressState;
//        this.resolvedState = resolvedState;
//    }
//
//    public State() { }
//
//    public double getOpenState() {
//        return openState;
//    }
//
//    public void setOpenState(double openState) {
//        this.openState = openState;
//    }
//
//    public double getWaitingState() {
//        return waitingState;
//    }
//
//    public void setWaitingState(double waitingState) {
//        this.waitingState = waitingState;
//    }
//
//    public double getProgressState() {
//        return progressState;
//    }
//
//    public void setProgressState(double progressState) {
//        this.progressState = progressState;
//    }
//
//    public double getResolvedState() {
//        return resolvedState;
//    }
//
//    public void setResolvedState(double resolvedState) {
//        this.resolvedState = resolvedState;
//    }
//
//    @Override
//    public String toString() {
//        return "State{" +
//                ", openState='" + openState + '\'' +
//                ", waitingState='" + waitingState + '\'' +
//                ", progressState='" + progressState + '\'' +
//                ", resolvedState='" + resolvedState + '\'' +
//                '}';
//    }
}
