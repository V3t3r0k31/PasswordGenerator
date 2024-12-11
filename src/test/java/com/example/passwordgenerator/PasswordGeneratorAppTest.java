package com.example.passwordgenerator;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

class PasswordGeneratorAppTest extends ApplicationTest {
    @Override
    public void start(Stage stage) {
        PasswordGeneratorApp app = new PasswordGeneratorApp();
        app.start(stage);
    }

    @Test
    void testGeneratePasswordWithValidSettings() {
        clickOn("#latinCheckbox");
        clickOn("#uppercaseCheckbox");
        clickOn("#lowercaseCheckbox");

        clearAndWrite("#specialCharactersField", "!@#$%");
        clearAndWrite("#digitsField", "0123456789");
        clearAndWrite("#lengthField", "12");

        clickOn("#generateButton");

        TextArea resultArea = lookup("#resultArea").queryAs(TextArea.class);
        String password = resultArea.getText();

        assertNotNull(password);
        assertEquals(12, password.length());
        assertTrue(password.matches("[a-zA-Z0-9!@#$%]+"));
    }

    @Test
    void testErrorOnEmptyCharacterPool() {
        clearAndWrite("#specialCharactersField", "");
        clearAndWrite("#digitsField", "");

        clickOn("#generateButton");

        TextArea resultArea = lookup("#resultArea").queryAs(TextArea.class);
        assertTrue(resultArea.getText().contains("Ошибка"));
    }

    @Test
    void testEstimatedTimeUpdates() {
        clearAndWrite("#lengthField", "100");

        Label estimatedTimeLabel = lookup("#estimatedTimeLabel").queryAs(Label.class);
        assertTrue(estimatedTimeLabel.getText().contains("Предполагаемое время:"));
    }

    @Test
    void testActualTimeDisplayed() {
        clickOn("#latinCheckbox");
        clickOn("#uppercaseCheckbox");

        clearAndWrite("#lengthField", "20");
        clickOn("#generateButton");

        Label actualTimeLabel = lookup("#actualTimeLabel").queryAs(Label.class);
        assertTrue(actualTimeLabel.getText().contains("Фактическое время:"));
    }
    private void clearAndWrite(String query, String text) {
        clickOn(query).press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL).eraseText(1).write(text);
    }

}
