module org.example.keenmarksmanfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;

    opens org.example.keenmarksmanfx to javafx.fxml;
    exports org.example.keenmarksmanfx;
}