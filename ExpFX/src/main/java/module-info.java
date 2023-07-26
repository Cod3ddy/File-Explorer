module shadow.expfx {
    requires javafx.controls;
    requires javafx.fxml;

    opens shadow.expfx to javafx.fxml;
    exports shadow.expfx;
    exports shadow.utility;
    opens shadow.utility to javafx.fxml;
}