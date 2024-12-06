package ru.andryxx.gitworker.services;

public class UserRepositoryUtils {

    public static final int MIN_LEN = 3;

    private UserRepositoryUtils() {
    }

    public static UserRepositoryRecord parse(String url) {
        String[] parts = url.split("/");
        if (parts.length < MIN_LEN) {
            throw new IllegalArgumentException("Invalid URL: " + url);
        }
        return new UserRepositoryRecord(parts[parts.length - 2], parts[parts.length - 1]);
    }

    public record UserRepositoryRecord(String owner, String repo) {

    }
}
