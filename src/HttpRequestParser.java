import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequestParser {

    public static HttpRequest parse(BufferedReader reader) throws IOException {
        HttpRequest request = new HttpRequest();

        String requestLine = reader.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            throw new IOException("Request rỗng hoặc không hợp lệ");
        }

        String[] parts = requestLine.split(" ");
        if (parts.length != 3) {
            throw new IOException("Request line không đúng định dạng: " + requestLine);
        }

        request.setMethod(parts[0]);
        request.setPath(parts[1]);
        request.setHttpVersion(parts[2]);

        String headerLine;
        while ((headerLine = reader.readLine()) != null && !headerLine.isEmpty()) {
            int colonIndex = headerLine.indexOf(":");
            if (colonIndex != -1) {
                String key = headerLine.substring(0, colonIndex).trim();
                String value = headerLine.substring(colonIndex + 1).trim();
                request.getHeaders().put(key, value);
            }
        }

        String contentLengthStr = request.getHeader("Content-Length");
        if (contentLengthStr != null) {
            int contentLength = Integer.parseInt(contentLengthStr);
            char[] bodyChars = new char[contentLength];
            reader.read(bodyChars, 0, contentLength);
            request.setBody(new String(bodyChars));
        }

        return request;
    }
}