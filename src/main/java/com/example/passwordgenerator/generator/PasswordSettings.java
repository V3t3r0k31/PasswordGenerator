package com.example.passwordgenerator.generator;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для управления настройками генерации паролей.
 * Позволяет задавать длину пароля, символы для генерации и их группы.
 */
public class PasswordSettings {

    private List<String> characterGroups = new ArrayList<>(); // Группы символов
    private int passwordLength = 8; // Длина пароля по умолчанию

    /**
     * Добавляет новую группу символов.
     *
     * @param characters строка, содержащая символы для добавления в группу.
     * @throws IllegalArgumentException если переданная строка пуста или равна null.
     */
    public void addCharacterGroup(String characters) {
        if (characters == null || characters.isEmpty()) {
            throw new IllegalArgumentException("Группа символов не может быть пустой");
        }
        characterGroups.add(characters);
    }

    /**
     * Добавляет символы для указанного языка в настройки.
     */
    public void addLanguage(String language, boolean useUppercase, boolean useLowercase) {
        switch (language.toLowerCase()) {
            case "latin":
                if (useLowercase) addCharacterGroup("abcdefghijklmnopqrstuvwxyz");
                if (useUppercase) addCharacterGroup("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
                break;
            case "cyrillic":
                if (useLowercase) addCharacterGroup("абвгдежзийклмнопрстуфхцчшщъыьэюя");
                if (useUppercase) addCharacterGroup("АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ");
                break;
            default:
                throw new IllegalArgumentException("Неизвестный язык: " + language);
        }
    }

    /**
     * Устанавливает символы для пользовательских спецсимволов.
     *
     * @param specialCharacters строка с набором спецсимволов.
     * @throws IllegalArgumentException если переданная строка пуста или равна null.
     */
    public void setSpecialCharacters(String specialCharacters) {
        addCharacterGroup(specialCharacters);
    }

    /**
     * Устанавливает символы для пользовательских цифр.
     *
     * @param digits строка с набором цифр.
     * @throws IllegalArgumentException если переданная строка пуста или равна null.
     */
    public void setDigits(String digits) {
        addCharacterGroup(digits);
    }

    /**
     * Очищает все группы символов, заданные ранее.
     */
    public void clearCharacterGroups() {
        characterGroups.clear();
    }

    /**
     * Возвращает объединённый пул символов из всех добавленных групп.
     *
     * @return строка, содержащая все символы из всех групп.
     */
    public String getCombinedCharacterPool() {
        StringBuilder combined = new StringBuilder();
        for (String group : characterGroups) {
            combined.append(group);
        }
        return combined.toString();
    }

    /**
     * Возвращает длину пароля.
     *
     * @return текущая длина пароля.
     */
    public int getPasswordLength() {
        return passwordLength;
    }

    /**
     * Устанавливает длину пароля.
     *
     * @param passwordLength длина пароля (должна быть больше 0).
     * @throws IllegalArgumentException если длина пароля меньше или равна 0.
     */
    public void setPasswordLength(int passwordLength) {
        if (passwordLength <= 0) {
            throw new IllegalArgumentException("Длина пароля должна быть больше 0");
        }
        this.passwordLength = passwordLength;
    }
}
