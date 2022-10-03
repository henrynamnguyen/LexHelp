module com.teamortix.lexhelp.lexhelpapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
//    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
//    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires software.amazon.awssdk.services.lexruntimev2;
    requires software.amazon.awssdk.regions;
    requires software.amazon.awssdk.auth;
    requires org.reactivestreams;
    requires software.amazon.awssdk.core;
    requires java.desktop;
    requires software.amazon.awssdk.services.s3;
    requires jlayer;

    opens com.teamortix.lexhelp.lexhelpapp to javafx.fxml;
    exports com.teamortix.lexhelp.lexhelpapp;
}