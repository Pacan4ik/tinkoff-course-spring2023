package ru.andryxx.habrworker.services.fetchers;

import java.security.MessageDigest;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ru.andryxx.habrworker.services.fetchers.model.HabrDTO;

@Slf4j
public class HabrHtmlParser {
    private final String commentCountSelector;
    private final String contentSelector;
    private final MessageDigest digest;

    public HabrHtmlParser(String commentCountSelector, String contentSelector, MessageDigest digest) {
        this.commentCountSelector = commentCountSelector;
        this.contentSelector = contentSelector;
        this.digest = digest;
    }

    public HabrDTO parse(String html) {
        Document document = Jsoup.parse(html);

        Element commentsCounter = document.selectFirst(commentCountSelector);
        Objects.requireNonNull(commentsCounter, "Comments counter not found");
        long commentCount = Long.parseLong(commentsCounter.text());

        Element content = document.selectFirst(contentSelector);
        Objects.requireNonNull(content, "Content not found");
        byte[] hashBytes = digest.digest(content.text().getBytes());

        String hexString = getHex(hashBytes);

        return new HabrDTO(commentCount, hexString);
    }

    private static @NotNull String getHex(byte[] hashBytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}
