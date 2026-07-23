// Interface định nghĩa "chữ ký" của 1 hàm xử lý request
public interface HttpHandler {
    HttpResponse handle(HttpRequest request);
}