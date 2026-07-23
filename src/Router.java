import java.util.HashMap;
import java.util.Map;

public class Router {
    // Key dạng "GET /hello" -> Handler tương ứng
    private Map<String, HttpHandler> routes = new HashMap<>();

    public void register(String method, String path, HttpHandler handler) {
        String key = buildKey(method, path);
        routes.put(key, handler);
    }

    public HttpResponse route(HttpRequest request) {
        String key = buildKey(request.getMethod(), request.getPath());
        HttpHandler handler = routes.get(key);

        if (handler != null) {
            return handler.handle(request);
        }

        // Nếu không có route khớp và là GET request, thử tìm file tĩnh
        if (request.getMethod().equalsIgnoreCase("GET")) {
            return StaticFileHandler.serve(request.getPath());
        }

        return HttpResponse.notFound();
    }

    private String buildKey(String method, String path) {
        return method.toUpperCase() + " " + path;
    }
}