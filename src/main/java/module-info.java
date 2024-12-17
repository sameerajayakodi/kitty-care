module org.samee.lk.skypos {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.sql;
    requires mysql.connector.j;
    opens org.samee.lk.skypos.tm to javafx.base;
    opens org.samee.lk.skypos to javafx.fxml;
    exports org.samee.lk.skypos;
    exports org.samee.lk.skypos.controllers;
    opens org.samee.lk.skypos.controllers to javafx.fxml;
}