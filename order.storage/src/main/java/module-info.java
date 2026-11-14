module order.storage {
    requires net.datafaker;
    requires static lombok;

    exports org.example.model;
    exports org.example.storage;
    exports org.example.exception;
}