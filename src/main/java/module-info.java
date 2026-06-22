module codeladder {
    requires com.fasterxml.jackson.databind;
    requires javafx.controls;

    opens codeladder.data.dto to com.fasterxml.jackson.databind;
    exports codeladder.app;
}
