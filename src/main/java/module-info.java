module net.abdellahhafid.smartfaceaccess {
    requires javafx.controls;
    requires javafx.fxml;


    opens net.abdellahhafid.smartfaceaccess to javafx.fxml;
    exports net.abdellahhafid.smartfaceaccess;
}