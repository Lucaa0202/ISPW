package org.example.project3.patterns.state;

public interface StateMachine {

    public void goNext();
    public void goBack();
    public void start();
    public void transition(AbstractState abstractState);
    public void setState();
    public AbstractState getCurrentState();

}
