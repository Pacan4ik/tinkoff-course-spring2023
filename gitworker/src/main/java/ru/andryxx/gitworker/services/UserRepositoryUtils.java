package ru.andryxx.gitworker.services;

public class UserRepositoryUtils {
    private UserRepositoryUtils() {
    }

    public static UserRepositoryRecord parse(String url) {
        String[] parts = url.split("/");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid URL: " + url);
        }
        return new UserRepositoryRecord(parts[parts.length - 2], parts[parts.length - 1]);
    }

    public record UserRepositoryRecord(String owner, String repo) {

    }
}
