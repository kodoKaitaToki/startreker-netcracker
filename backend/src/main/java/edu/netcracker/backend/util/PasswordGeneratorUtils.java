package edu.netcracker.backend.util;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class PasswordGeneratorUtils {

    private static final PasswordGenerator passwordGenerator = new PasswordGenerator();

    private static final int PASSWORD_LENGTH = 30;

    private static final CharacterData[] CHARACTER_DATA = {
            EnglishCharacterData.UpperCase,
            EnglishCharacterData.LowerCase,
            EnglishCharacterData.Digit
    };

    public static String generatePassword() {
        return passwordGenerator.generatePassword(PASSWORD_LENGTH, createCharacterRules());
    }

    private static List<CharacterRule> createCharacterRules() {
        List<CharacterRule> characterRules = new ArrayList<>();
        IntStream.range(0, CHARACTER_DATA.length).forEach(i ->
                characterRules.add(createCharacterRule(CHARACTER_DATA[i])));

        return characterRules;
    }

    private static CharacterRule createCharacterRule(CharacterData characterData) {
        return new CharacterRule(characterData, PASSWORD_LENGTH / CHARACTER_DATA.length);
    }

}