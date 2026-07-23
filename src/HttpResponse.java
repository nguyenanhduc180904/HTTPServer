import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponse {
    private int statusCode;
    private String statusText;
    private Map<String, String> headers = new LinkedHashMap<>();
    private byte[] body = new byte[0];

    public HttpResponse(int statusCode, String statusText) {
        this.statusCode = statusCode;
        this.statusText = statusText;
    }

    public HttpResponse setHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    // Set body dạng text (tiện dụng cho HTML/JSON/plain text)
    public HttpResponse setBody(String bodyText) {
        this.body = bodyText.getBytes(StandardCharsets.UTF_8);
        return this;
    }

    // Set body dạng bytes thô (dùng cho file binary: ảnh, pdf...)
    public HttpResponse setBody(byte[] bodyBytes) {
        this.body = bodyBytes;
        return this;
    }

    public static HttpResponse ok(String body) {
        return new HttpResponse(200, "OK")
                .setHeader("Content-Type", "text/plain")
                .setBody(body);
    }

    public static HttpResponse html(String body) {
        return new HttpResponse(200, "OK")
                .setHeader("Content-Type", "text/html")
                .setBody(body);
    }

    public static HttpResponse notFound() {
        return new HttpResponse(404, "Not Found")
                .setHeader("Content-Type", "text/plain")
                .setBody("404 Not Found");
    }

    public static HttpResponse badRequest(String message) {
        return new HttpResponse(400, "Bad Request")
                .setHeader("Content-Type", "text/plain")
                .setBody(message);
    }

    public static HttpResponse serverError(String message) {
        return new HttpResponse(500, "Internal Server Error")
                .setHeader("Content-Type", "text/plain")
                .setBody(message);
    }

    // Ghi toàn bộ response (header + body) ra OutputStream dưới dạng bytes
    public void writeTo(OutputStream out) throws IOException {
        headers.put("Content-Length", String.valueOf(body.length));

        StringBuilder headerBuilder = new StringBuilder();
        headerBuilder.append("HTTP/1.1 ").append(statusCode).append(" ").append(statusText).append("\r\n");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            headerBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }
        headerBuilder.append("\r\n");

        // Ghi phần header dưới dạng bytes (dùng US-ASCII vì header luôn là ASCII chuẩn)
        out.write(headerBuilder.toString().getBytes(StandardCharsets.US_ASCII));
        // Ghi phần body dưới dạng bytes thô
        out.write(body);
        out.flush();
    }
}