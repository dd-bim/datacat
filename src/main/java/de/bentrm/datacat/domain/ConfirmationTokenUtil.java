package de.bentrm.datacat.domain;

import org.apache.commons.text.RandomStringGenerator;

public class ConfirmationTokenUtil {

    public static final int CONFIRMATION_TOKEN_LENGTH = 8;
    public static final char[][] CONFIRMATION_TOKEN_PAIRS = {{'a', 'z'}, { '0', '9' }};

    private static final RandomStringGenerator generator = new RandomStringGenerator.Builder()
            .withinRange(CONFIRMATION_TOKEN_PAIRS)
            .build();

    public static String generateConfirmationToken() {
        return generator.generate(CONFIRMATION_TOKEN_LENGTH);
    }

}
