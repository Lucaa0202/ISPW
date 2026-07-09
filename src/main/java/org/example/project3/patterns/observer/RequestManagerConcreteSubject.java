package org.example.project3.patterns.observer;

import org.example.project3.model.Request;

import java.util.ArrayList;
import java.util.List;

public class RequestManagerConcreteSubject extends Subject {
    private static RequestManagerConcreteSubject instance = null;
    private List<Request> requests = new ArrayList<Request>();

    private RequestManagerConcreteSubject() {}

    //Singleton
    public static RequestManagerConcreteSubject getInstance() {
        if (instance == null) {
            instance = new RequestManagerConcreteSubject();
        }
        return instance;
    }

    public void addRequest(Request request) {
        requests.add(request);
        notifyObservers();
    }

    public void removeRequest(Request request) {
        requests.removeIf(r->r.getID()==request.getID());
        notifyObservers();
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void loadRequests(List<Request> requests) {
        this.requests = requests;
    }

}
