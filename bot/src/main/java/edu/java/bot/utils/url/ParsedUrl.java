package edu.java.bot.utils.url;

public record ParsedUrl(
    String protocol,
    String host,
    String port,
    String path,
    String query
) {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append(protocol).append("://").append(host);
        if (path != null) {
            if (path.charAt(path.length() - 1) == '/') {
                sb.append(path, 0, path.length() - 1);
            } else {
                sb.append(path);
            }
        }
        return sb.toString();
    }
}
