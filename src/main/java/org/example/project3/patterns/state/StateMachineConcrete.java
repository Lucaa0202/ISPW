package org.example.project3.patterns.state;

import org.example.project3.beans.LoggedUserBean;

import java.util.ArrayDeque;

public class StateMachineConcrete implements StateMachine {

    private AbstractState currentState;
    private LoggedUserBean currentLoggedUser;
    private ArrayDeque<AbstractState> stateHistory;
    public StateMachineConcrete() {
        this.currentState = new InitialState();
        this.stateHistory = new ArrayDeque<>();
    }

    @Override
    public void goNext(){
        if(currentState!=null){
            this.currentState.action(this);
        }
    }

    @Override
    public void goBack(){
        if(!stateHistory.isEmpty()){
            this.currentState.exit(this);
            this.currentState=stateHistory.pop();
            this.currentState.enter(this);
            goNext();
        }
    }

    @Override
    public void transition(AbstractState state) {
        this.currentState.exit(this);
        if(currentState!=null){
            stateHistory.push(currentState);
        }
        this.currentState = state;
        this.currentState.enter(this);
        goNext();
    }

    @Override
    public void setState(){
        this.currentState = null;
    }

    @Override
    public AbstractState getCurrentState() {
        return currentState;
    }

    public LoggedUserBean getCurrentLoggedUser() {
        return currentLoggedUser;
    }

    public void setCurrentLoggedUser(LoggedUserBean currentLoggedUser) {
        this.currentLoggedUser = currentLoggedUser;
    }

    @Override
    public void start() {
        currentState= new InitialState();
        goNext();
    }

}
