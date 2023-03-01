module sr.unasat.convertertools {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens sr.unasat.convertertools.controller to javafx.fxml;
    exports sr.unasat.convertertools;
}