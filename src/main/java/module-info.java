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

    exports VIEW;
    exports TEST;
    exports DAL;
    opens DAL to javafx.fxml;
    exports BL;
    opens BL to javafx.fxml;
    opens VIEW to javafx.fxml;
    exports DAL.Database;
    opens DAL.Database to javafx.fxml;
    exports DATATYPES;
    opens DATATYPES to javafx.fxml;
}