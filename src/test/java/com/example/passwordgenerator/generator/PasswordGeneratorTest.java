package com.example.passwordgenerator.generator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordGeneratorTest {
    @Test
    void testGeneratePasswordValidSettings() {
        PasswordSettings settings = new PasswordSettings();
        settings.addCharacterGroup("abc");
        settings.setPasswordLength(5);
        String password = PasswordGenerator.generatePassword(settings);
        assertEquals(5, password.length());
        assertTrue(password.matches("[abc]+"));
    }

    @Test
    void testGeneratePasswordEmptyCharacterPool() {
        PasswordSettings settings = new PasswordSettings();
        settings.setPasswordLength(5);
        assertThrows(IllegalStateException.class, () -> PasswordGenerator.generatePassword(settings));
    }

    @Test
    void testGeneratePasswordCorrectLength() {
        PasswordSettings settings = new PasswordSettings();
        settings.addCharacterGroup("abc");
        settings.setPasswordLength(10);
        String password = PasswordGenerator.generatePassword(settings);
        assertEquals(10, password.length());
    }

    @Test
    void testGeneratePasswordDifferentCharacterPools() {
        PasswordSettings settings = new PasswordSettings();
        settings.addCharacterGroup("abc");
        settings.addCharacterGroup("123");
        settings.setPasswordLength(10);
        String password = PasswordGenerator.generatePassword(settings);
        assertTrue(password.matches("[abc123]+"));
    }
    @Test
    void testPasswordGenerationWithMultipleCharacterGroups() {
        PasswordSettings settings = new PasswordSettings();
        settings.addLanguage("latin", true, true); // Латиница с верхним и нижним регистром
        settings.setSpecialCharacters("!@#$");
        settings.setDigits("0123456789");
        settings.setPasswordLength(20);

        String password = PasswordGenerator.generatePassword(settings);
        assertEquals(20, password.length());
        assertTrue(password.matches("[a-zA-Z0-9!@#$]+"));
    }

    @Test
    void testPasswordGenerationWithCyrillic() {
        PasswordSettings settings = new PasswordSettings();
        settings.addLanguage("cyrillic", true, true); // Кириллица с верхним и нижним регистром
        settings.setPasswordLength(15);

        String password = PasswordGenerator.generatePassword(settings);
        assertEquals(15, password.length());
        assertTrue(password.matches("[а-яА-Я]+"));
    }

    @Test
    void testPasswordGenerationComplexSettings() {
        PasswordSettings settings = new PasswordSettings();
        settings.addLanguage("latin", true, true);    // Латиница с верхним и нижним регистром
        settings.addLanguage("cyrillic", false, true); // Кириллица только нижний регистр
        settings.setSpecialCharacters("!@#$");
        settings.setDigits("0123456789");
        settings.setPasswordLength(50);

        String password = PasswordGenerator.generatePassword(settings);
        assertEquals(50, password.length());
        assertTrue(password.matches("[a-zA-Zа-я0-9!@#$]+"));
    }

    @Test
    void testPasswordGenerationFailsOnEmptySettings() {
        PasswordSettings settings = new PasswordSettings(); // Пустые настройки
        settings.setPasswordLength(10); // Устанавливаем длину

        assertThrows(IllegalStateException.class, () -> PasswordGenerator.generatePassword(settings),
                "Генерация должна провалиться, если не задан ни один символ.");
    }

    @Test
    void testPasswordGenerationWithLatinLowercaseOnly() {
        PasswordSettings settings = new PasswordSettings();
        settings.addLanguage("latin", false, true); // Только нижний регистр латиницы
        settings.setPasswordLength(10);

        String password = PasswordGenerator.generatePassword(settings);
        assertEquals(10, password.length());
        assertTrue(password.matches("[a-z]+"));
    }

    @Test
    void testPasswordGenerationWithCyrillicUppercaseOnly() {
        PasswordSettings settings = new PasswordSettings();
        settings.addLanguage("cyrillic", true, false); // Только верхний регистр кириллицы
        settings.setPasswordLength(12);

        String password = PasswordGenerator.generatePassword(settings);
        assertEquals(12, password.length());
        assertTrue(password.matches("[А-Я]+"));
    }
}