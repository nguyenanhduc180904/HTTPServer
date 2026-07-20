import java.io.*;
import java.net.*;

public class SimpleHttpServer {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server đang chạy tại http://localhost:" + port);

        while (true) {
            // Chờ và chấp nhận 1 kết nối từ client (blocking call)
            Socket clientSocket = serverSocket.accept();
            System.out.println("Có client kết nối: " + clientSocket.getInetAddress());

            handleClient(clientSocket);
        }
    }

    private static void handleClient(Socket clientSocket) throws IOException {
        // Đọc dữ liệu client gửi lên (tạm thời chưa parse, chỉ đọc để log)
        InputStream input = clientSocket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        // Đọc dòng đầu tiên của request (request line), ví dụ: "GET / HTTP/1.1"
        String requestLine = reader.readLine();
        System.out.println("Request: " + requestLine);

        // Chuẩn bị nội dung trả về
        String responseBody = "Hello World 666666666666";

        // Viết response theo đúng format chuẩn HTTP
        OutputStream output = clientSocket.getOutputStream();
        PrintWriter writer = new PrintWriter(output, true);

        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: text/plain");
        writer.println("Content-Length: " + responseBody.getBytes().length);
        writer.println(); // dòng trống bắt buộc để phân tách header và body
        writer.println(responseBody);

        writer.flush();
        clientSocket.close();
    }
}