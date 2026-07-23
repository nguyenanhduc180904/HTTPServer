import java.io.*;
import java.net.*;

public class SimpleHttpServer {

    private static Router router = new Router();

    public static void main(String[] args) throws IOException {
        // Đăng ký các route
        router.register("GET", "/", req -> HttpResponse.html("<h1>Trang chủ</h1>"));
        router.register("GET", "/hello", req -> HttpResponse.ok("Hello World"));
        router.register("GET", "/users", req -> HttpResponse.html("<ul><li>User 1</li><li>User 2</li></ul>"));
        router.register("POST", "/echo", req -> HttpResponse.ok("Bạn vừa gửi: " + req.getBody()));

        int port = 8080;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server đang chạy tại http://localhost:" + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Có client kết nối: " + clientSocket.getInetAddress());

            try {
                handleClient(clientSocket);
            } catch (IOException e) {
                System.out.println("Lỗi khi xử lý request: " + e.getMessage());
            } finally {
                clientSocket.close(); // đảm bảo socket luôn được đóng dù thành công hay lỗi
            }
        }
    }

    private static void handleClient(Socket clientSocket) throws IOException {
        InputStream input = clientSocket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        HttpRequest request = HttpRequestParser.parse(reader);
        System.out.println(request);

        HttpResponse response = router.route(request);

        OutputStream output = clientSocket.getOutputStream();
        PrintWriter writer = new PrintWriter(output, true);
        writer.print(response.toRawResponse());
        writer.flush();
    }
}