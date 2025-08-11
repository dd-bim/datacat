package de.bentrm.datacat.auth;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Simple password generator only used during account initialization by configuration.
 */
public final class PasswordGenerator {

        private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
        private static final String DIGITS = "0123456789";
        private static final String SPECIAL = "!@#$%^&*()-_=+[]{}|;:,.<>?";
    
        private static SecureRandom random = new SecureRandom();

    public static String generate() {
        String upperCaseLetters = secure(2, UPPER);
        String lowerCaseLetters = secure(2, LOWER);
        String numbers = secure(2, DIGITS);
        String specialChar = secure(2, SPECIAL);
        String totalChars = secure(2, UPPER + LOWER + DIGITS + SPECIAL);

        String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
                .concat(numbers)
                .concat(specialChar)
                .concat(totalChars);
        List<Character> pwdChars = combinedChars.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(pwdChars);
        return pwdChars.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    private PasswordGenerator() {
    }


    public static String secure(int length, String characters) {
        return random.ints(length, 0, characters.length())
                     .mapToObj(characters::charAt)
                     .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                     .toString();
    }
}
