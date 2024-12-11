package com.example.passwordgenerator.generator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.SecureRandom;

/**
 * Класс для генерации паролей на основе заданных настроек.
 */
public class PasswordGenerator {

    private static final Logger logger = LogManager.getLogger(PasswordGenerator.class);
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Генерирует пароль на основе переданных настроек.
     *
     * @param settings настройки генерации пароля.
     *                 Включают длину пароля и группы символов, которые можно использовать.
     * @return сгенерированный пароль.
     * @throws IllegalStateException если пул символов пуст (не выбраны символы для генерации).
     */
    public static String generatePassword(PasswordSettings settings) {
        logger.info("Начало генерации пароля...");

        // Получаем пул символов из настроек
        String characterPool = settings.getCombinedCharacterPool();

        // Проверка: пул символов не должен быть пустым
        if (characterPool.isEmpty()) {
            logger.error("Пул символов пуст. Генерация пароля невозможна.");
            throw new IllegalStateException("Не задано ни одной группы символов для генерации пароля");
        }

        int passwordLength = settings.getPasswordLength();
        logger.info("Параметры генерации: длина пароля = {}, пул символов содержит {} символов",
                passwordLength, characterPool.length());

        char[] password = new char[passwordLength];

        // Генерация символов пароля
        for (int i = 0; i < passwordLength; i++) {
            password[i] = characterPool.charAt(RANDOM.nextInt(characterPool.length()));
        }

        // Преобразуем массив символов в строку
        String generatedPassword = new String(password);

        logger.info("Пароль успешно сгенерирован. Длина пароля: {}", generatedPassword.length());
        return generatedPassword;
    }
}
