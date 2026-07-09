module org.example.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires java.xml.crypto;
    requires javafx.base;
    requires javafx.graphics;
    requires mysql.connector.j;

    opens org.example.project3.view to javafx.fxml;

    exports org.example.project3.view.gui;
    opens org.example.project3.view.gui to javafx.fxml;

    exports org.example.project3.controller;
    opens org.example.project3.controller to javafx.fxml;

    exports org.example.project3.model;
    opens org.example.project3.model to javafx.fxml;

    exports org.example.project3.beans;
    opens org.example.project3.beans to javafx.fxml;

    exports org.example.project3.exceptions;
    opens org.example.project3.exceptions to javafx.fxml;

    exports org.example.project3.dao.demo;
    opens org.example.project3.dao.demo to javafx.fxml;

    exports org.example.project3.dao.full.sql;
    opens org.example.project3.dao.full.sql to javafx.fxml;

    exports org.example.project3.utilities.others;
    opens org.example.project3.utilities.others to javafx.fxml;

    exports org.example.project3.utilities.others.mappers;
    opens org.example.project3.utilities.others.mappers to javafx.fxml;

    exports org.example.project3.patterns.factory;
    opens org.example.project3.patterns.factory to javafx.fxml;

    exports  org.example.project3.utilities.enums;

    exports org.example.project3.patterns.observer;
    opens org.example.project3.patterns.observer to javafx.fxml;

    opens org.example.project3 to javafx.fxml;
    exports org.example.project3;
}