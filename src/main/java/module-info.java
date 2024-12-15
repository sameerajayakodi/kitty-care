module org.samee.lk.skypos {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens org.samee.lk.skypos to javafx.fxml;
    exports org.samee.lk.skypos;
}