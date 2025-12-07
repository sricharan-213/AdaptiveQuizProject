package utils;

import exceptions.InvalidInputException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static void validateSignup(String username, String password, String email) throws InvalidInputException {

        if (username == null || username.trim().isEmpty()) {
            throw new InvalidInputException("Username cannot be empty");
        }

        if (password == null || password.length() < 4) {
            throw new InvalidInputException("Password must be at least 4 characters");
        }

        if (email == null) {
            throw new InvalidInputException("Email cannot be empty");
        }

        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
            throw new InvalidInputException("Invalid email address");
        }
    }
}
