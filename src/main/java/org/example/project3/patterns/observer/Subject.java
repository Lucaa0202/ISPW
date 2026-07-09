package org.example.project3.patterns.observer;

import java.util.ArrayList;
import java.util.List;

public class Subject {
    private final List<Observer> observers;

    public Subject() {
        observers = new ArrayList<>();
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    protected void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
