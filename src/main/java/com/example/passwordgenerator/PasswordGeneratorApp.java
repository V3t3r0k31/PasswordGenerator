package com.example.passwordgenerator;

import com.example.passwordgenerator.generator.PasswordGenerator;
import com.example.passwordgenerator.generator.PasswordSettings;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PasswordGeneratorApp extends Application {

    private static final Logger logger = LogManager.getLogger(PasswordGeneratorApp.class);


    // Поля интерфейса
    private Label estimatedTimeLabel;
    private Label actualTimeLabel;
    private TextArea resultArea;
    private TextField lengthField;
    private TextField specialCharactersField;
    private TextField digitsField;
    private CheckBox latinCheckbox;
    private CheckBox cyrillicCheckbox;
    private CheckBox uppercaseCheckbox;
    private CheckBox lowercaseCheckbox;

    private double timePerCharacterMs; // Время на один символ (в миллисекундах)


    @Override
    public void start(Stage primaryStage) {
        logger.info("Запуск приложения...");

        // Генерация базового времени для 100,000 символов
        timePerCharacterMs = calculateTimePerCharacter();

        // Создание и настройка корневого контейнера
        VBox root = createRootContainer();

        // Настройка сцены и окна
        Scene scene = new Scene(root, 600, 500);
        primaryStage.setTitle("Генератор паролей");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Создаёт корневой контейнер для интерфейса.
     *
     * @return VBox, содержащий элементы интерфейса.
     */
    private VBox createRootContainer() {
        VBox root = new VBox(10);

        estimatedTimeLabel = new Label("Предполагаемое время: 0 мс");
        actualTimeLabel = new Label("Фактическое время: 0 мс");

        latinCheckbox = new CheckBox("Латиница");
        cyrillicCheckbox = new CheckBox("Кириллица");

        uppercaseCheckbox = new CheckBox("Заглавные буквы");
        lowercaseCheckbox = new CheckBox("Прописные буквы");

        specialCharactersField = new TextField("!@#$%^&*()");
        digitsField = new TextField("0123456789");
        lengthField = new TextField("12");

        resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPromptText("Сгенерированный пароль будет здесь");

        Button generateButton = new Button("Сгенерировать пароль");
        generateButton.setOnAction(event -> handleGenerateButton());

        // Обновление времени генерации при изменении длины
        lengthField.textProperty().addListener((observable, oldValue, newValue) ->
                updateTimeLabel(newValue));

        //id для тестов
        latinCheckbox.setId("latinCheckbox");
        cyrillicCheckbox.setId("cyrillicCheckbox");
        uppercaseCheckbox.setId("uppercaseCheckbox");
        lowercaseCheckbox.setId("lowercaseCheckbox");
        specialCharactersField.setId("specialCharactersField");
        digitsField.setId("digitsField");
        lengthField.setId("lengthField");
        resultArea.setId("resultArea");
        estimatedTimeLabel.setId("estimatedTimeLabel");
        actualTimeLabel.setId("actualTimeLabel");
        generateButton.setId("generateButton");

        // Добавление всех элементов в контейнер
        root.getChildren().addAll(
                createLanguageSection(),
                createCharacterTypeSection(),
                createCustomInputSection("Спецсимволы:", specialCharactersField),
                createCustomInputSection("Цифры:", digitsField),
                createCustomInputSection("Длина пароля (до 1 млн):", lengthField),
                estimatedTimeLabel,
                actualTimeLabel,
                generateButton,
                resultArea
        );

        return root;
    }

    /**
     * Создаёт секцию для выбора языков.
     * @return контейнер с чекбоксами.
     */
    private HBox createLanguageSection() {
        HBox languageBox = new HBox(10);
        languageBox.getChildren().addAll(latinCheckbox, cyrillicCheckbox);
        return languageBox;
    }

    /**
     * Создаёт секцию для выбора типов символов.
     *
     * @return контейнер с чекбоксами.
     */
    private HBox createCharacterTypeSection() {
        HBox characterTypeBox = new HBox(10);
        characterTypeBox.getChildren().addAll(uppercaseCheckbox, lowercaseCheckbox);
        return characterTypeBox;
    }

    /**
     * Создаёт секцию для пользовательского ввода.
     *
     * @param labelText текст метки.
     * @param textField поле ввода.
     * @return контейнер с меткой и полем ввода.
     */
    private HBox createCustomInputSection(String labelText, TextField textField) {
        HBox inputBox = new HBox(10);
        Label label = new Label(labelText);
        inputBox.getChildren().addAll(label, textField);
        return inputBox;
    }

    /**
     * Обрабатывает нажатие на кнопку "Сгенерировать пароль".
     */
    private void handleGenerateButton() {
        try {
            logger.info("Обработка нажатия кнопки генерации пароля...");
            int passwordLength = Integer.parseInt(lengthField.getText());
            validatePasswordLength(passwordLength);

            // Настройки пароля
            PasswordSettings settings = createPasswordSettings(
                    latinCheckbox, cyrillicCheckbox, uppercaseCheckbox, lowercaseCheckbox,
                    specialCharactersField, digitsField, passwordLength
            );

            // Генерация пароля
            long startTime = System.nanoTime();
            String password = PasswordGenerator.generatePassword(settings);
            long endTime = System.nanoTime();

            double actualTimeMs = (endTime - startTime) / 1_000_000.0;
            logger.info("Пароль сгенерирован за {} мс", actualTimeMs);

            // Обновление метки фактического времени
            actualTimeLabel.setText(String.format("Фактическое время: %.2f мс", actualTimeMs));
            resultArea.setText(password);

            // Обновляем предполагаемое время, чтобы показать расхождение
            double estimatedTime = passwordLength * timePerCharacterMs;
            estimatedTimeLabel.setText(String.format("Предполагаемое время: %.2f мс", estimatedTime));
        } catch (Exception e) {
            logger.error("Ошибка генерации пароля: {}", e.getMessage());
            resultArea.setText("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Обновляет время генерации на основе длины пароля.
     */
    private void updateTimeLabel(String newValue) {
        try {
            int passwordLength = Integer.parseInt(newValue);
            validatePasswordLength(passwordLength); // Проверка длины пароля

            double estimatedTime = passwordLength * timePerCharacterMs;
            estimatedTimeLabel.setText(String.format("Предполагаемое время: %.2f мс", estimatedTime));
        } catch (NumberFormatException e) {
            logger.error("Некорректное значение длины: {}", newValue);
            estimatedTimeLabel.setText("Предполагаемое время: - (некорректное значение)");
            resultArea.setText("Ошибка: Введите корректное числовое значение для длины пароля.");
        } catch (IllegalArgumentException e) {
            logger.error("Некорректная длина пароля: {}", e.getMessage());
            estimatedTimeLabel.setText("Предполагаемое время: - (длина вне диапазона)");
            resultArea.setText("Ошибка: Генерация пароля возможна только для длины от 1 до 1 000 000 символов.");
        }
    }

    /**
     * Рассчитывает среднее время на один символ, генерируя 10 паролей длиной 100_000 символов.
     *
     * @return среднее время генерации одного символа в миллисекундах.
     */
    private double calculateTimePerCharacter() {
        logger.info("Вычисление среднего времени генерации для одного символа...");
        PasswordSettings settings = new PasswordSettings();
        settings.addCharacterGroup("abcdefghijklmnopqrstuvwxyz");
        settings.setPasswordLength(100_000);

        long totalDuration = 0;
        PasswordGenerator.generatePassword(settings);

        // Генерация 10 паролей и измерение времени
        for (int i = 0; i < 10; i++) {
            long startTime = System.nanoTime();
            PasswordGenerator.generatePassword(settings);
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            totalDuration += duration;

            logger.info("Пароль {}: время генерации {} мс", i + 1, duration / 1_000_000.0);
        }

        // Среднее время на один символ
        double averageTimeMs = (totalDuration / 10.0) / 50_000 / 1_000_000.0;
        logger.info("Среднее время на один символ: {} мс", averageTimeMs);
        return averageTimeMs;
    }

    /**
     * Создаёт объект настроек пароля.
     */
    private PasswordSettings createPasswordSettings(
            CheckBox latinCheckbox, CheckBox cyrillicCheckbox,
            CheckBox uppercaseCheckbox, CheckBox lowercaseCheckbox,
            TextField specialCharactersField, TextField digitsField,
            int passwordLength
    ) {
        PasswordSettings settings = new PasswordSettings();
        if (latinCheckbox.isSelected()) {
            addLanguage(settings, "latin", uppercaseCheckbox.isSelected(), lowercaseCheckbox.isSelected());
        }
        if (cyrillicCheckbox.isSelected()) {
            addLanguage(settings, "cyrillic", uppercaseCheckbox.isSelected(), lowercaseCheckbox.isSelected());
        }

        if (!specialCharactersField.getText().isEmpty()) {
            settings.setSpecialCharacters(specialCharactersField.getText());
        }

        if (!digitsField.getText().isEmpty()) {
            settings.setDigits(digitsField.getText());
        }

        settings.setPasswordLength(passwordLength);
        return settings;
    }

    /**
     * Добавляет символы для указанного языка в настройки.
     */
    private void addLanguage(PasswordSettings settings, String language, boolean useUppercase, boolean useLowercase) {
        settings.addLanguage(language,useUppercase,useLowercase);
    }

    /**
     * Проверяет корректность длины пароля.
     */
    private void validatePasswordLength(int length) {
        if (length <= 0 || length > 1_000_000) {
            throw new IllegalArgumentException("Длина пароля должна быть от 1 до 1 000 000");
        }
    }


}
