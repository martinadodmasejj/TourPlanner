module org.openjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires junit;
    requires postgresql;
    requires java.sql;
    requires jdk.jsobject;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires org.opentest4j;
    requires org.mockito;
    requires java.net.http;
    requires org.json;
    requires java.desktop;
    requires javafx.swing;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires itextpdf;
    requires com.jfoenix;
    requires de.jensd.fx.glyphs.fontawesome;
    requires javafx.graphics;
    requires thumbnailator;

    exports View;
    exports TestingPackage;
    exports DataAccessLayer;
    opens DataAccessLayer to javafx.fxml;
    exports BusinessLayer;
    opens BusinessLayer to javafx.fxml;
    opens View to javafx.fxml;
    exports DataAccessLayer.Database;
    opens DataAccessLayer.Database to javafx.fxml;
    exports DataAccessLayer.Local;
    opens DataAccessLayer.Local to javafx.fxml;
    exports Datatypes;
    opens Datatypes to javafx.fxml;
}