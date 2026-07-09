package org.example.project3.dao.full.sql;

import org.example.project3.dao.SubscriptionDAO;
import org.example.project3.exceptions.DbOperationException;
import org.example.project3.model.Subscription;
import org.example.project3.query.SubscriptionQuery;
import org.example.project3.utilities.enums.Type;
import org.example.project3.utilities.others.Printer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SubscriptionDAOSQL implements SubscriptionDAO {
    private static final String ID="id";
    private static final String NAME="name";
    private static final String TYPE="type";
    private static final String PRICE="price";
    private static final String DISCOUNT="discount";

    @Override
    public void addSubscription(Subscription subscription){
        try (Connection conn = ConnectionSQL.getConnection()) {
            SubscriptionQuery.addSubscription(conn, subscription);
        } catch (SQLException | DbOperationException e) {
            handleException(e);
        }
    }

    @Override
    public void retrieveSubscription(Subscription subscription) {
        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = SubscriptionQuery.retrieveSubscription(conn, subscription.getName())) {
            if (rs.next()) {
                subscription.setName(rs.getString(NAME));
                subscription.setType(Type.valueOf(rs.getString(TYPE)));
                subscription.setPrice(rs.getFloat(PRICE));
                subscription.setDisconut(rs.getInt(DISCOUNT));
            }
        } catch (SQLException e) {
            handleException(e);
        }
    }

    @Override
    public void removeSubscription(Subscription subscription) {
        try (Connection conn = ConnectionSQL.getConnection()) {
            SubscriptionQuery.deleteSubscription(conn, subscription.getId());
        } catch (SQLException | DbOperationException e) {
            handleException(e);
        }
    }

    @Override
    public void updateSubscription(Subscription subscription) {
        try(Connection conn = ConnectionSQL.getConnection()){
            SubscriptionQuery.modifySubscription(conn, subscription);
        } catch(SQLException | DbOperationException e){
            handleException(e);
        }
    }

    private void handleException(Exception e) {
        Printer.errorPrint(String.format("%s", e.getMessage()));
    }
}
