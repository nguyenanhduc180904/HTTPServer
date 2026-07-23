import java.io.*;
import java.nio.file.*;

public class StaticFileHandler {

    private static final String STATIC_ROOT = "public";

    public static HttpResponse serve(String path) {
        try {
            if (path.equals("/")) {
                path = "/index.html";
            }

            File file = new File(STATIC_ROOT + path);

            String canonicalPath = file.getCanonicalPath();
            String rootPath = new File(STATIC_ROOT).getCanonicalPath();
            if (!canonicalPath.startsWith(rootPath)) {
                return HttpResponse.badRequest("Đường dẫn không hợp lệ");
            }

            if (!file.exists() || file.isDirectory()) {
                return HttpResponse.notFound();
            }

            byte[] fileBytes = Files.readAllBytes(file.toPath());
            String contentType = guessContentType(path);

            return new HttpResponse(200, "OK")
                    .setHeader("Content-Type", contentType)
                    .setBody(fileBytes); // dùng overload nhận byte[] — giữ nguyên dữ liệu gốc

        } catch (IOException e) {
            return HttpResponse.serverError("Lỗi đọc file: " + e.getMessage());
        }
    }

    private static String guessContentType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".json")) return "application/json";
        return "application/octet-stream";
    }
}