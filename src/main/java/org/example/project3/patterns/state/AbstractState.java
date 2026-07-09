package org.example.project3.patterns.state;

public abstract class AbstractState {


    protected AbstractState(){}

    public void enter(StateMachineConcrete context){}
    public void exit(StateMachineConcrete context){}
    public void goBack(StateMachineConcrete context){context.goBack();}
    public void goNext(StateMachineConcrete context, AbstractState nextState){
        context.transition(nextState);
    }

    public void action(StateMachineConcrete context){}
    public void showMenu(){}
    public void stampa(){}

}
