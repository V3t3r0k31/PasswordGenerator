package com.example.passwordgenerator.generator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordSettingsTest {
    @Test
    void testAddLanguageWithBothCases() {
        PasswordSettings settings = new PasswordSettings();
        settings.addLanguage("latin", true, true);
        String characterPool = settings.getCombinedCharacterPool();

        assertTrue(characterPool.contains("a"));
        assertTrue(characterPool.contains("Z"));
        assertFalse(characterPool.isEmpty());
    }

    @Test
    void testAddLanguageUppercaseOnly() {
        PasswordSettings settings = new PasswordSettings();
        settings.addLanguage("latin", true, false);
        String characterPool = settings.getCombinedCharacterPool();

        assertTrue(characterPool.contains("a"));
        assertFalse(characterPool.contains("A"));
    }

    @Test
    void testAddLanguageLowercaseOnly() {
        PasswordSettings settings = new PasswordSettings();
        settings.addLanguage("latin", false, true);
        String characterPool = settings.getCombinedCharacterPool();

        assertTrue(characterPool.contains("A"));
        assertFalse(characterPool.contains("a"));
    }

    @Test
    void testAddLanguageCyrillicBothCases() {
        PasswordSettings settings = new PasswordSettings();
        settings.addLanguage("cyrillic", true, true);
        String characterPool = settings.getCombinedCharacterPool();

        assertTrue(characterPool.contains("а"));
        assertTrue(characterPool.contains("Я"));
    }

    @Test
    void testAddLanguageCyrillicLowercaseOnly() {
        PasswordSettings settings = new PasswordSettings();
        settings.addLanguage("cyrillic", false, true);
        String characterPool = settings.getCombinedCharacterPool();

        assertTrue(characterPool.contains("А"));
        assertFalse(characterPool.contains("а"));
    }

    @Test
    void testAddLanguageCyrillicUppercaseOnly() {
        PasswordSettings settings = new PasswordSettings();
        settings.addLanguage("cyrillic", true, false);
        String characterPool = settings.getCombinedCharacterPool();

        assertTrue(characterPool.contains("а"));
        assertFalse(characterPool.contains("А"));
    }

    @Test
    void testAddLanguageUnknownThrowsException() {
        PasswordSettings settings = new PasswordSettings();
        assertThrows(IllegalArgumentException.class, () -> settings.addLanguage("unknown", true, true),
                "Неизвестный язык должен вызывать исключение.");
    }


    @Test
    void testCombinedCharacterPoolWithMultipleLanguages() {
        PasswordSettings settings = new PasswordSettings();
        settings.addLanguage("latin", true, true);
        settings.addLanguage("cyrillic", true, true);
        settings.setSpecialCharacters("!@#$");
        settings.setDigits("0123456789");

        String characterPool = settings.getCombinedCharacterPool();
        assertTrue(characterPool.contains("z"));
        assertTrue(characterPool.contains("Я"));
        assertTrue(characterPool.contains("!"));
        assertTrue(characterPool.contains("9"));
    }
}