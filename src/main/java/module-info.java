module org.openjfx {
    exports algorithms;
    exports automaton;
    exports regexp;
    exports org.openjfx;
    exports org.openjfx.Controllers;

    opens org.openjfx.Controllers;
    
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires com.google.common;
    requires com.github.librepdf.openpdf;
}