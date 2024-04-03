package com.example.csc325_firebase_webview_auth.view;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Register {
    @FXML
    private MenuItem closeMenuItem;
    @FXML
    private MenuItem goBackMenuItem;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField verifyEmailTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private Button registerButton;

    public void handleCloseMenuItem() {
        System.out.println("handleCloseMenuItem called");
        Platform.exit();
    }

    public void handleGoBackMenuItem() throws IOException {
        System.out.println("handleGoBackMenuItem called");
        App.setRoot("/files/AccessFBView.fxml");
    }

    public void handleRegisterButton() {
        System.out.println("handleRegisterButton called");

        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String email = emailTextField.getText();
        String verifyEmail = verifyEmailTextField.getText();
        String password = passwordTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();

        // Check if any field is empty
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || verifyEmail.isEmpty() || password.isEmpty() || phoneNumber.isEmpty()) {
            System.out.println("Please fill in all fields");
        }
        else if (!email.equals(verifyEmail)) {
            Alert alert = new Alert (Alert.AlertType.WARNING);
            alert.setTitle("Passwords Don't Match!");
            alert.setContentText("Both passwords inputted do not match each other. Please try again.");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                System.out.println ("User acknowledged incorrect input.");
            }
            System.out.println ("Invalid input please try again.");
            System.out.println("Passwords do not match");
            return;
        }

        registerUser(firstName, lastName, email, password, phoneNumber);

        // Print the information inputted
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        System.out.println("Phone Number: " + phoneNumber);

        Alert alert = new Alert (Alert.AlertType.INFORMATION);
        alert.setTitle("Registration Successful!");
        alert.setContentText("All information successfully stored in Firebase.");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            System.out.println ("User acknowledged incorrect input.");
        }

        firstNameTextField.clear();
        lastNameTextField.clear();
        emailTextField.clear();
        verifyEmailTextField.clear();
        passwordTextField.clear();
        phoneNumberTextField.clear();
    }

    public void registerUser(String firstName, String lastName, String email, String password, String phoneNumber) {
        try {
            DocumentReference docRef = App.fstore.collection("Reference_Registration").document(UUID.randomUUID().toString());

            // Create a map with user data
            Map<String, Object> userData = new HashMap<>();
            userData.put("First Name", firstName);
            userData.put("Last Name", lastName);
            userData.put("Email", email);
            userData.put("Password", password);
            userData.put("Phone Number", phoneNumber);

            ApiFuture<WriteResult> result = docRef.set(userData);

            System.out.println ("User information added to Firebase successfully!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
