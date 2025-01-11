module net.abdellahhafid.smartfaceaccess {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.bytedeco.opencv;

    opens net.abdellahhafid.smartfaceaccess to javafx.fxml;
    opens net.abdellahhafid.smartfaceaccess.controllers to javafx.fxml;
    opens net.abdellahhafid.smartfaceaccess.models.enums to javafx.fxml;

    exports net.abdellahhafid.smartfaceaccess;
}
