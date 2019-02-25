package edu.netcracker.backend.utils;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PasswordGeneratorUtils {

    private static final PasswordGenerator passwordGenerator = new PasswordGenerator();

    private static final int PASSWORD_LENGTH = 30;

    private static final CharacterData[] CHARACTER_DATA = {
            EnglishCharacterData.UpperCase,
            EnglishCharacterData.LowerCase,
            EnglishCharacterData.Digit
    };

    private static final List<CharacterRule> characterRules = createCharacterRules();

    public static String generatePassword() {
        return passwordGenerator.generatePassword(PASSWORD_LENGTH, characterRules);
    }

    private static List<CharacterRule> createCharacterRules() {
        return Arrays.stream(CHARACTER_DATA).
                map(PasswordGeneratorUtils::createCharacterRule).
                collect(Collectors.toList());
    }

    private static CharacterRule createCharacterRule(CharacterData characterData) {
        return new CharacterRule(characterData, PASSWORD_LENGTH / CHARACTER_DATA.length);
    }
}