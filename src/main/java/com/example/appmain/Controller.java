package com.example.appmain;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Controller {
    private Stage stage;
    private Parent root;
    @FXML
    private Button exitBtn;
    @FXML
    private AnchorPane scenePane;

    // Выход с программы шифрования
    public void logout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You`re about to logout!");

        if (alert.showAndWait().get() == ButtonType.OK) {
            stage = (Stage) scenePane.getScene().getWindow();
            stage.close();
        }
    }
    // Возврат к выбору языка
    public void switchToLang(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("langChoose.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private <T extends Node> T getElement(Parent root, String id) {
        return (T) root.lookup("#" + id);
    }

    public boolean checkKey() { // проверяю не пусто ли поле с ключом
        TextField keyField = getElement(scenePane, "keyInput");
        return !keyField.getText().isEmpty();
    }

    private boolean checkAppLang() {
        Label currLanguage = getElement(scenePane, "currLanguage");
        return Objects.equals(currLanguage.getText(), "RU");
    }

    // язык, результаты encrypt, decrypt

    private String langApp;
    private String result1;
    private String result2;

    private void encryptFunction(String encryptInput, String keyField) {

        if (checkAppLang()){
            langApp = "RU";
        }
        else {
            langApp = "EN";
        }
        result1 = PlayfairCipher.encryptFinal(encryptInput, langApp, keyField);
    }

    private void decryptFunction(String decryptInput, String keyField) {
        if (checkAppLang()){
            langApp = "RU";
        }
        else {
            langApp = "EN";
        }
        result2 = PlayfairCipher.decryptFinal(decryptInput, langApp, keyField);
    }

    private void displayMatrix(String keyField, String langApp) {
        char[][] keyMatrix = PlayfairCipher.generateMatrixGeneral(keyField, langApp);
        int dimension_grid = langApp.equals("RU") ? 6 : 5;

        for (int i = 0; i < dimension_grid; i++) {
            for (int j = 0; j < dimension_grid; j++) {
                Label label = getElement(scenePane, String.format("lab%s_%d", j, i));
                label.setText(String.valueOf(keyMatrix[i][j]));
            }
        }
    }

    private static boolean isRussianAlphabet(String str) {
        return str.matches("[а-яА-Я]+");
    }

    private static void showAlertRu() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Некорректный ввод");
        alert.setContentText("Пожалуйста, только буквы русского алфавита");
        alert.showAndWait();
    }

    public static void showAlertEn() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("Incorrect input");
        alert.setContentText("Please, use only letters of Enlgish alphabet");
        alert.showAndWait();
    }

    public void buttonToEncrypt() { // проверить что ключ получен
        if (checkAppLang()) {
            langApp = "RU";
        }
        else {
            langApp = "EN";
        }
        TextField keyField = getElement(scenePane, "keyInput");
        if (checkKey()) {
            // Ключ передан, можно проверять наличие слова
            TextField encryptInput = getElement(scenePane, "encryptInput");
            // Язык русский
            if (Objects.equals(langApp, "RU")) {
                if (isRussianAlphabet(keyField.getText()) && (isRussianAlphabet(encryptInput.getText()))) {
                    if (!encryptInput.getText().isEmpty()) {
                        encryptFunction(encryptInput.getText(), keyField.getText());
                        displayMatrix(keyField.getText(), langApp);
                        Label cipherText = getElement(scenePane, "cipherText");
                        if (!result1.isEmpty()) {
                            cipherText.setText(result1);
                        }
                        else {
                            cipherText.setText("Что-то пошло не так");
                        }
                    }
                    else {
                        encryptInput.setPromptText("Поле пустое");
                    }
                }
                // Если не на русском
                else {
                    showAlertRu();
                }
            }
            // Язык английский
            else {
                if (!isRussianAlphabet(keyField.getText()) && !isRussianAlphabet(encryptInput.getText())) {
                    if (!encryptInput.getText().isEmpty()) {
                        encryptFunction(encryptInput.getText(), keyField.getText());
                        displayMatrix(keyField.getText(), langApp);
                        Label cipherText = getElement(scenePane, "cipherText");
                        if (!result1.isEmpty()) {
                            cipherText.setText(result1);
                        }
                        else {
                            cipherText.setText("Something went wrong");
                        }
                    }
                    else {
                        encryptInput.setPromptText("Empty field");
                    }
                }
                // Если на русском
                else {
                    showAlertEn();
                }
            }
        }
        else {
            // Ключ не передан
            if (checkAppLang()) {
                keyField.setPromptText("Поле пустое");
            }
            else {
                keyField.setPromptText("Key is empty");
            }
        }
    }



    public void buttonToDecrypt() {
        TextField keyField = getElement(scenePane, "keyInput");
        if (checkKey()) {
            // Ключ передан, можно проверять наличие слова
            TextField decryptInput = getElement(scenePane, "decryptInput");
            // Язык русский
            if (Objects.equals(langApp, "RU")) {
                if (isRussianAlphabet(keyField.getText()) && (isRussianAlphabet(decryptInput.getText()))) {
                    if (!decryptInput.getText().isEmpty()) {
                        decryptFunction(decryptInput.getText(), keyField.getText());
                        displayMatrix(keyField.getText(), langApp);
                        Label cipherText = getElement(scenePane, "cipherText");
                        if (!result2.isEmpty()) {
                            cipherText.setText(result2);
                        }
                        else {
                            cipherText.setText("Что-то пошло не так");
                        }
                    }
                    else {
                        decryptInput.setPromptText("Поле пустое");
                    }
                }
                // Если не на русском
                else {
                    showAlertRu();
                }
            }
            // Язык английский
            else {
                if (!isRussianAlphabet(keyField.getText()) && !isRussianAlphabet(decryptInput.getText())) {
                    if (!decryptInput.getText().isEmpty()) {
                        decryptFunction(decryptInput.getText(), keyField.getText());
                        displayMatrix(keyField.getText(), langApp);
                        Label cipherText = getElement(scenePane, "cipherText");
                        if (!result2.isEmpty()) {
                            cipherText.setText(result2);
                        }
                        else {
                            cipherText.setText("Something went wrong");
                        }
                    }
                    else {
                        decryptInput.setPromptText("Empty field");
                    }
                }
                // Если на русском
                else {
                    showAlertEn();
                }
            }
        } else {
            // Ключ не передан
            if (checkAppLang()) {
                keyField.setPromptText("Поле пустое");
            } else {
                keyField.setPromptText("Key is empty");
            }
        }
    }


}