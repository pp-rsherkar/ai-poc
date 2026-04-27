package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SoftAssertUtil {

    private final List<String> errors = new ArrayList<>();

    public void assertEquals(String message, Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            errors.add(message + "\nExpected: " + expected + "\nActual:   " + actual);
        }
    }

    public void assertTrue(String message, boolean condition) {
        if (!condition) {
            errors.add(message);
        }
    }

    public void collectAll() {
        if (!errors.isEmpty()) {
            StringBuilder sb = new StringBuilder("Assertion Failures:\n\n");
            for (String err : errors) {
                sb.append("- ").append(err).append("\n\n");
            }
            throw new AssertionError(sb.toString());
        }
    }
}
