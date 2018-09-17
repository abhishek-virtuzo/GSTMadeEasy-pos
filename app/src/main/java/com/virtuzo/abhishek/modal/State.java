package com.virtuzo.abhishek.modal;

/**
 * Created by Aman Bansal on 19-12-2017.
 */

public class State {

    private int StateID;
    private String StateName;

    public State() {
    }

    public State(int stateID, String stateName) {
        StateID = stateID;
        StateName = stateName;
    }

    public int getStateID() {
        return StateID;
    }

    public void setStateID(int stateID) {
        StateID = stateID;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    @Override
    public String toString() {
        return getStateName();
    }
}
