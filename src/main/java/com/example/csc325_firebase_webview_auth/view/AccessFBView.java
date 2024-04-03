package com.example.csc325_firebase_webview_auth.view;//package modelview;

import com.example.csc325_firebase_webview_auth.model.Person;
import com.example.csc325_firebase_webview_auth.viewmodel.AccessDataViewModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class AccessFBView {


    @FXML
    private TextField nameField;
    @FXML
    private TextField majorField;
    @FXML
    private TextField ageField;
    @FXML
    private Button addButton;
    @FXML
    private Button readButton;
    @FXML
    private MenuItem registerMenuItem;
    @FXML
    private MenuItem closeMenuItem;
    @FXML
    private TextArea outputField;
    @FXML
    private TableView <Person> tableView;
    @FXML
    private TableColumn <Person, String> nameColumn;
    @FXML
    private TableColumn <Person, String> majorColumn;
    @FXML
    private TableColumn <Person, Integer> ageColumn;
    private boolean key;
    private ObservableList<Person> listOfUsers = FXCollections.observableArrayList();
    private Person person;
    public ObservableList<Person> getListOfUsers() {
        return listOfUsers;
    }

    public void initialize() {
        AccessDataViewModel accessDataViewModel = new AccessDataViewModel();
        nameField.textProperty().bindBidirectional(accessDataViewModel.userNameProperty());
        majorField.textProperty().bindBidirectional(accessDataViewModel.userMajorProperty());


        nameColumn.setCellValueFactory(
                new PropertyValueFactory<Person,String>("name"));
        majorColumn.setCellValueFactory(
                new PropertyValueFactory<Person,String>("major"));
        ageColumn.setCellValueFactory(
                new PropertyValueFactory<Person,Integer>("age"));
    }

    @FXML
    private void addRecord() {
        System.out.println ("Add Button pressed");
        addData();
    }

    @FXML
    private void readRecord() {
        System.out.println ("Read Button pressed");
        readFirebase();
    }

    @FXML
    private void switchToSecondary() throws IOException {
        System.out.println ("Switch Button pressed");
        App.setRoot("/files/WebContainer.fxml");
    }

    public void addData() {

        DocumentReference docRef = App.fstore.collection("References").document(UUID.randomUUID().toString());

        Map<String, Object> data = new HashMap<>();
        data.put("Name", nameField.getText());
        data.put("Major", majorField.getText());
        data.put("Age", Integer.parseInt(ageField.getText()));
        //asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);
    }

    public boolean readFirebase()
    {
        key = false;

        //asynchronously retrieve all documents
        ApiFuture<QuerySnapshot> future =  App.fstore.collection("References").get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents;
        try
        {
            documents = future.get().getDocuments();
            if(documents.size()>0) {
                List <Person> people = new ArrayList<>();
                outputField.clear();
                System.out.println("Outing....");
                for (QueryDocumentSnapshot document : documents)
                {
                    outputField.setText(outputField.getText()+ "Name: " + document.getData().get("Name")+ " , Major: "+
                            document.getData().get("Major")+ " , Age: "+
                            document.getData().get("Age")+ " \n ");
                    System.out.println(document.getId() + " => " + document.getData().get("Name"));
                    person  = new Person(String.valueOf(document.getData().get("Name")),
                            document.getData().get("Major").toString(),
                            Integer.parseInt(document.getData().get("Age").toString()));

                    String name = document.getString("Name");
                    String major = document.getString("Major");
                    int age = document.getLong("Age").intValue();

                    System.out.println("Name: " + name + ", Major: " + major + ", Age: " + age);

                    person = new Person(name, major, age);
                    listOfUsers.add(person);
                    people.add(person);
                }
                tableView.setItems(FXCollections.observableArrayList(people));
            }
            else
            {
                System.out.println("No data");
            }
            key=true;

        }
        catch (InterruptedException | ExecutionException ex)
        {
            ex.printStackTrace();
        }
        return key;
    }

    public void sendVerificationEmail() {
        try {
            UserRecord user = App.fauth.getUser("name");
            //String url = user.getPassword();

        } catch (Exception e) {
        }
    }

    @FXML
    public void handleRegisterMenuItem() throws IOException {
        System.out.println ("handleRegisterMenuItem called");
        App.setRoot("/files/Register.fxml");
    }

    @FXML
    public void handleCloseMenuItem() {
        System.out.println ("handleCloseMenuItem called");
        Platform.exit();
    }
}
