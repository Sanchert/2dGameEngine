module io.github.sanchert.namelessgf {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;
    requires org.jetbrains.annotations;
    requires org.lwjgl.glfw;
    requires org.lwjgl.opengl;
    requires org.lwjgl.stb;

    opens io.github.sanchert.namelessgf to javafx.fxml;
    exports io.github.sanchert.namelessgf;
}