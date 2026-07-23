import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponse {
    private int statusCode;
    private String statusText;
    private Map<String, String> headers = new LinkedHashMap<>();
    private String body = "";

    public HttpResponse(int statusCode, String statusText) {
        this.statusCode = statusCode;
        this.statusText = statusText;
    }

    public HttpResponse setHeader(String key, String value) {
        headers.put(key, value);
        return this; // cho phép gọi nối tiếp (method chaining)
    }

    public HttpResponse setBody(String body) {
        this.body = body;
        return this;
    }

    // Các factory method tiện dụng để tạo response nhanh
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

    // Chuyển toàn bộ response thành raw HTTP text để ghi ra socket
    public String toRawResponse() {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 ").append(statusCode).append(" ").append(statusText).append("\r\n");

        // Tự động set Content-Length dựa theo body thực tế
        headers.put("Content-Length", String.valueOf(body.getBytes().length));

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }

        sb.append("\r\n"); // dòng trống phân tách header/body
        sb.append(body);

        return sb.toString();
    }
}