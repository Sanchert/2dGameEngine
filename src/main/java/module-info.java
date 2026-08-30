module org.example.keenmarksmanfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;
    requires org.jetbrains.annotations;
    requires org.lwjgl.glfw;
    requires org.lwjgl.opengl;

    opens org.example.keenmarksmanfx to javafx.fxml;
    exports org.example.keenmarksmanfx;
}