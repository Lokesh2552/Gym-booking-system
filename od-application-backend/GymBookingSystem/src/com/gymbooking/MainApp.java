package com.gymbooking;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.gymbooking.dao.BookingDAO;
import com.gymbooking.dao.GymDAO;
import com.gymbooking.dao.UserDAO;
import com.gymbooking.model.Booking;
import com.gymbooking.model.Gym;
import com.gymbooking.model.User;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainApp extends Application {
    private Stage primaryStage;
    private User currentUser;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        showLoginScene();
        stage.setTitle("Gym Booking System");
        stage.setMinWidth(400);
        stage.setMinHeight(300);
        stage.show();
    }

    private void showLoginScene() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        Label title = new Label("Login");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");
        registerButton.setStyle("-fx-background-color: #2196F3;");  // Different color for register

        HBox buttons = new HBox(10, loginButton, registerButton);
        buttons.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(title, usernameField, passwordField, buttons);

        loginButton.setOnAction(e -> {
            UserDAO userDAO = new UserDAO();
            currentUser = userDAO.loginUser(usernameField.getText(), passwordField.getText());
            if (currentUser != null) {
                showMainMenu();
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid credentials!");
            }
        });

        registerButton.setOnAction(e -> showRegisterScene());

        Scene scene = new Scene(vbox);
        scene.getStylesheets().add((getClass().getResource("/styles.css")).toExternalForm());
        primaryStage.setScene(scene);
    }

    private void showRegisterScene() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        Label title = new Label("Register");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");

        Button registerButton = new Button("Register");

        vbox.getChildren().addAll(title, usernameField, passwordField, emailField, nameField, registerButton);

        registerButton.setOnAction(e -> {
            UserDAO userDAO = new UserDAO();
            boolean success = userDAO.registerUser(usernameField.getText(), passwordField.getText(),
                    emailField.getText(), nameField.getText());
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Registered successfully!");
                showLoginScene();
            } else {
                showAlert(Alert.AlertType.ERROR, "Registration failed! Username or email may exist.");
            }
        });

        Scene scene = new Scene(vbox);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    private void showMainMenu() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        Label title = new Label("Main Menu");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button viewGymsButton = new Button("View Gyms");
        Button bookGymButton = new Button("Book Gym");
        Button viewProfileButton = new Button("View Profile");
        Button viewBookingsButton = new Button("View Bookings");
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #f44336;");  // Red for logout

        vbox.getChildren().addAll(title, viewGymsButton, bookGymButton, viewProfileButton, viewBookingsButton, logoutButton);

        viewGymsButton.setOnAction(e -> showGymsScene());
        bookGymButton.setOnAction(e -> showBookGymScene());
        viewProfileButton.setOnAction(e -> showProfileScene());
        viewBookingsButton.setOnAction(e -> showBookingsScene());
        logoutButton.setOnAction(e -> {
            currentUser = null;
            showLoginScene();
        });

        Scene scene = new Scene(vbox);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    private void showGymsScene() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Available Gyms");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GymDAO gymDAO = new GymDAO();
        List<Gym> gyms = gymDAO.getAllGyms();
        for (Gym gym : gyms) {
            Label gymLabel = new Label(gym.getId() + ": " + gym.getName() + " - " + gym.getLocation() + "\n" + gym.getDescription());
            gymLabel.setWrapText(true);
            gymLabel.setStyle("-fx-padding: 10; -fx-background-color: #e0e0e0; -fx-border-radius: 5; -fx-background-radius: 5;");
            vbox.getChildren().add(gymLabel);
        }

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showMainMenu());

        vbox.getChildren().add(backButton);

        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 400, 400);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
        primaryStage.setScene(scene);
    }

    private void showBookGymScene() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        Label title = new Label("Book a Gym");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Dropdown for gyms
        ComboBox<Gym> gymComboBox = new ComboBox<>();
        gymComboBox.setPromptText("Select Gym");
        GymDAO gymDAO = new GymDAO();
        List<Gym> gyms = gymDAO.getAllGyms();
        gymComboBox.getItems().addAll(gyms);
        gymComboBox.setCellFactory(param -> new ListCell<Gym>() {
            @Override
            protected void updateItem(Gym gym, boolean empty) {
                super.updateItem(gym, empty);
                if (empty || gym == null) {
                    setText(null);
                } else {
                    setText(gym.getName() + " - " + gym.getLocation());
                }
            }
        });
        gymComboBox.setButtonCell(gymComboBox.getCellFactory().call(null));

        // DatePicker for date
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select Date");

        // ChoiceBox for time slots
        ChoiceBox<String> timeSlotChoice = new ChoiceBox<>();
        timeSlotChoice.getItems().addAll("Morning (8-12)", "Afternoon (12-4)", "Evening (4-8)");
        timeSlotChoice.setValue("Morning (8-12)");  // Default

        Button bookButton = new Button("Book");
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showMainMenu());

        HBox buttons = new HBox(10, bookButton, backButton);
        buttons.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(title, gymComboBox, datePicker, timeSlotChoice, buttons);

        bookButton.setOnAction(e -> {
            Gym selectedGym = gymComboBox.getValue();
            LocalDate localDate = datePicker.getValue();
            String timeSlot = timeSlotChoice.getValue();

            if (selectedGym == null || localDate == null || timeSlot == null) {
                showAlert(Alert.AlertType.ERROR, "Please select all fields!");
                return;
            }

            Date bookingDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            BookingDAO bookingDAO = new BookingDAO();
            boolean success = bookingDAO.bookGym(currentUser.getId(), selectedGym.getId(), bookingDate, timeSlot);
            showAlert(Alert.AlertType.INFORMATION, success ? "Booked successfully!" : "Booking failed!");
            if (success) {
                showMainMenu();
            }
        });

        Scene scene = new Scene(vbox);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
        primaryStage.setScene(scene);
    }

    private void showProfileScene() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Your Profile");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserById(currentUser.getId());
        if (user != null) {
            Label nameLabel = new Label("Full Name: " + user.getFullName());
            Label usernameLabel = new Label("Username: " + user.getUsername());
            Label emailLabel = new Label("Email: " + user.getEmail());
            vbox.getChildren().addAll(nameLabel, usernameLabel, emailLabel);
        }

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showMainMenu());
        vbox.getChildren().add(backButton);

        Scene scene = new Scene(vbox);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
        primaryStage.setScene(scene);
    }

    private void showBookingsScene() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Your Bookings");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        BookingDAO bookingDAO = new BookingDAO();
        List<Booking> bookings = bookingDAO.getUserBookings(currentUser.getId());
        for (Booking booking : bookings) {
            Label bookingLabel = new Label("Gym ID: " + booking.getGymId() + ", Date: " + booking.getBookingDate() + ", Slot: " + booking.getTimeSlot());
            bookingLabel.setWrapText(true);
            bookingLabel.setStyle("-fx-padding: 10; -fx-background-color: #e0e0e0; -fx-border-radius: 5; -fx-background-radius: 5;");
            vbox.getChildren().add(bookingLabel);
        }

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showMainMenu());
        vbox.getChildren().add(backButton);

        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 400, 400);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
        primaryStage.setScene(scene);
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}