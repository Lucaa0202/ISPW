package org.example.project3.dao;

import org.example.project3.model.Subscription;

public interface SubscriptionDAO {
    void addSubscription(Subscription subscription);
    void retrieveSubscription(Subscription subscription);
    void updateSubscription(Subscription subscription);
    void removeSubscription(Subscription subscription);
}
