import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleHttpServer {

    private static Router router = new Router();
    private static final int THREAD_POOL_SIZE = 200;

    public static void main(String[] args) throws IOException {
        // Đăng ký các route
        router.register("GET", "/", req -> HttpResponse.html("<h1>Trang chủ</h1>"));
        router.register("GET", "/hello", req -> HttpResponse.ok("Hello World"));
        router.register("GET", "/users", req -> HttpResponse.html("<ul><li>User 1</li><li>User 2</li></ul>"));
        router.register("POST", "/echo", req -> HttpResponse.ok("Bạn vừa gửi: " + req.getBody()));

        int port = 8080;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server đang chạy tại http://localhost:" + port);

        // Tạo thread pool cố định 20 thread để xử lý request
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        while (true) {
            Socket clientSocket = serverSocket.accept();

            // Giao việc xử lý client cho thread pool, KHÔNG chờ xử lý xong
            threadPool.submit(() -> {
                try {
                    handleClient(clientSocket);
                } catch (IOException e) {
                    System.out.println("Lỗi khi xử lý request: " + e.getMessage());
                } finally {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        System.out.println("Lỗi khi đóng socket: " + e.getMessage());
                    }
                }
            });
        }
    }

    private static void handleClient(Socket clientSocket) throws IOException {
        InputStream input = clientSocket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        HttpRequest request = HttpRequestParser.parse(reader);
        System.out.println(Thread.currentThread().getName() + " xử lý: " + request.getPath());

        HttpResponse response = router.route(request);

        OutputStream output = clientSocket.getOutputStream();
        response.writeTo(output);
    }
}