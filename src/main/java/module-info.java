module net.abdellahhafid.smartfaceaccess {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.bytedeco.opencv;

    opens net.abdellahhafid.smartfaceaccess to javafx.fxml;
    exports net.abdellahhafid.smartfaceaccess;
}