package org.example.project3.dao.demo;

import org.example.project3.dao.SubscriptionDAO;
import org.example.project3.dao.demo.shared.SharedResources;
import org.example.project3.exceptions.NoResultException;

import org.example.project3.model.Subscription;

public class SubscriptionDAOP implements SubscriptionDAO {
    @Override
    public void addSubscription(Subscription subscription) {
        SharedResources.getInstance().getSubscriptions().putIfAbsent(subscription.getId(), subscription);
    }

    @Override
    public void removeSubscription(Subscription subscription) {
        SharedResources.getInstance().getSubscriptions().remove(subscription.getId());
    }

    @Override
    public void retrieveSubscription(Subscription subscription) throws NoResultException {
        Subscription storedSubscription = SharedResources.getInstance().getSubscriptions().get(subscription.getId());
        if (storedSubscription == null) {
            throw new NoResultException(subscription.getClass().getSimpleName() + " non trovato");
        }
        subscription.setId(storedSubscription.getId());
        subscription.setName(storedSubscription.getName());
        subscription.setType(storedSubscription.getType());
        subscription.setPrice(storedSubscription.getPrice());
        subscription.setDisconut(storedSubscription.getDisconut());
    }

    @Override
    public void updateSubscription(Subscription subscription) {
        SharedResources.getInstance().getSubscriptions().put(subscription.getId(), subscription);
    }
}
