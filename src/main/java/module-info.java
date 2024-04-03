module com.example.csc325_firebase_webview_auth {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires jdk.jsobject;
    requires java.xml;
    requires java.logging;
    requires javafx.web;
    requires com.google.auth.oauth2;
    requires google.cloud.firestore;
    requires firebase.admin;
    requires com.google.api.apicommon;

    requires google.cloud.core;
    requires com.google.auth;
    requires google.cloud.storage;


    opens com.example.csc325_firebase_webview_auth.viewmodel to jdk.jsobject;
    exports com.example.csc325_firebase_webview_auth.viewmodel;
    opens com.example.csc325_firebase_webview_auth.view;
    exports com.example.csc325_firebase_webview_auth.view;
    exports com.example.csc325_firebase_webview_auth.model;
    opens com.example.csc325_firebase_webview_auth.model;
}