import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String path;
    private String httpVersion;
    private Map<String, String> headers = new HashMap<>();
    private String body;

    // Getters
    public String getMethod() { return method; }
    public String getPath() { return path; }
    public String getHttpVersion() { return httpVersion; }
    public Map<String, String> getHeaders() { return headers; }
    public String getHeader(String name) { return headers.get(name); }
    public String getBody() { return body; }

    // Setters (dùng nội bộ khi parse)
    public void setMethod(String method) { this.method = method; }
    public void setPath(String path) { this.path = path; }
    public void setHttpVersion(String httpVersion) { this.httpVersion = httpVersion; }
    public void setBody(String body) { this.body = body; }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", httpVersion='" + httpVersion + '\'' +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}