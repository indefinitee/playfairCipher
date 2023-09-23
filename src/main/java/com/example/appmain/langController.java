package com.example.appmain;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class langController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Button closeBtn;
    @FXML
    private AnchorPane scenePane;
    public String selectedLang = "Russian";
    @FXML
    private RadioButton ruBtn, enBtn;



    // Выбор языка программы
    public void currLang(ActionEvent event) {
        if (enBtn.isSelected()) {
            selectedLang = "English";
        }
        if (ruBtn.isSelected()) {
            selectedLang = "Russian";
        }
    }

    private <T extends Node> T getElement(Parent root, String id) {
        return (T) root.lookup("#" + id);
    }

    public void setLabelText(Label label, String text) {
        label.setText(text);
    }

    public void setButtonText(Button button, String text) {
        button.setText(text);
    }

    public void setTextFieldText(TextField textField, String text) {
        textField.setPromptText(text);
    }

    public void logout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You`re about to logout!");

        if (alert.showAndWait().get() == ButtonType.OK) {
            stage = (Stage) scenePane.getScene().getWindow();
            stage.close();
        }
    }


    private double xOffset = 0;
    private double yOffset = 0;

    // Переход к GUI шифрования с определенным языком
    public void switchToMain(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();


        // Пример как вытащить что-то из root
        Label titleHead = getElement(root, "titleHead");
        Label cipherDesc = getElement(root, "cipherDesc");
        Label encryptTitle = getElement(root, "encryptTitle");
        Label decryptTitle = getElement(root, "decryptTitle");
        Label answerTitle = getElement(root, "answerTitle");
        Label keyTitle = getElement(root, "keyTitle");
        Label keyMatrix = getElement(root, "keyMatrix");
        Label currLanguage = getElement(root, "currLanguage");
        Button backBtn = getElement(root, "backBtn");
        Button exitBtn = getElement(root, "exitBtn");
        Button encryptBtn = getElement(root, "encryptBtn");
        Button decryptBtn = getElement(root, "decryptBtn");

        TextField encryptInput = getElement(root, "encryptInput");
        TextField keyInput = getElement(root, "keyInput");
        TextField decryptInput = getElement(root, "decryptInput");


        if (Objects.equals(selectedLang, "Russian")) {
            setLabelText(titleHead, "Шифр Плейфера");
            setLabelText(cipherDesc, "Шифр Плейфера (Playfair Cipher) - Это ручная симметричная техника шифрования," +
                    " то есть для шифрования и расшифрования применяется" +
                    " один и тот же криптографический ключ.");
            setLabelText(encryptTitle, "Шифрование");
            setLabelText(decryptTitle, "Расшифрование");
            setLabelText(answerTitle, "Готовый текст:");
            setLabelText(keyTitle, "Введите ключ");
            setLabelText(keyMatrix, "Ключевая матрица");
            setLabelText(currLanguage, "RU");

            setButtonText(backBtn, "Вернуться назад");
            setButtonText(exitBtn, "Выход");
            setButtonText(encryptBtn, "Зашифровать");
            setButtonText(decryptBtn, "Расшифровать");

            setTextFieldText(encryptInput, "Слово");
            setTextFieldText(keyInput, "Ключ");
            setTextFieldText(decryptInput, "Слово");

        } else {
            setLabelText(titleHead, "Playfair Cipher");
            setLabelText(cipherDesc, "The Playfair Cipher is a manual symmetric encryption technique," +
                    " meaning that the same cryptographic key is used for encryption and decryption.");
            setLabelText(encryptTitle, "Encrypt");
            setLabelText(decryptTitle, "Decrypt");
            setLabelText(answerTitle, "Converted text:");
            setLabelText(keyTitle, "Enter the key");
            setLabelText(keyMatrix, "Key matrix");
            setLabelText(currLanguage, "EN");

            setButtonText(backBtn, "Back");
            setButtonText(exitBtn, "Exit");
            setButtonText(encryptBtn, "Encrypt");
            setButtonText(decryptBtn, "Decrypt");

            setTextFieldText(encryptInput, "Word");
            setTextFieldText(keyInput, "Key");
            setTextFieldText(decryptInput, "Word");


        }


        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });

        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });

        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
