/**
 *
 *  @author Kamiński Patryk S18610
 *
 */

package zad1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ChatServer {

    private final Thread thread;
    private final StringBuilder serverLog;
    private final InetSocketAddress inetSocketAddress;
    private final Map<SocketChannel, String> clients;
    private final Lock lock = new ReentrantLock();
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    public ChatServer(String host, int port) {
        inetSocketAddress = new InetSocketAddress(host, port);
        serverLog = new StringBuilder();
        clients = new HashMap<>();
        thread = serverThread();
    }

    private Thread serverThread() {

        return new Thread(() -> {
            try {
                selector = Selector.open();

                serverSocketChannel = ServerSocketChannel.open();
                serverSocketChannel.bind(inetSocketAddress);
                serverSocketChannel.configureBlocking(false);

                serverSocketChannel.register(selector, serverSocketChannel.validOps(), null);

                while (!thread.isInterrupted()) {
                    selector.select();

                    if (thread.isInterrupted()){
                        break;
                    }

                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();

                        if (key.isAcceptable()) {
                            SocketChannel clientSocket = serverSocketChannel.accept();
                            clientSocket.configureBlocking(false);
                            clientSocket.register(selector, SelectionKey.OP_READ);
                        }

                        if (key.isReadable()) {
                            SocketChannel clientSocket = (SocketChannel) key.channel();

                            int capacity = 1024;
                            ByteBuffer buffer = ByteBuffer.allocateDirect(capacity);

                            StringBuilder clientRequest = new StringBuilder();

                            int readBytes = 0;
                            do {
                                try {
                                    lock.lock();

                                    readBytes = clientSocket.read(buffer);
                                    buffer.flip();
                                    clientRequest.append(StandardCharsets.UTF_8.decode(buffer).toString());
                                    buffer.clear();
                                    readBytes = clientSocket.read(buffer);

                                } catch (Exception exception) {

                                    exception.printStackTrace();
                                } finally {
                                    lock.unlock();
                                }
                            } while (readBytes != 0);

                            String[] parts = clientRequest.toString().split("#");

                            for (String req : parts) {
                                String clientResponse = requestHandler(clientSocket, req).toString();
                                System.out.println(req);

                                for (Map.Entry<SocketChannel, String> entry : clients.entrySet()) {
                                    ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(clientResponse);
                                    entry.getKey().write(byteBuffer);
                                }
                            }
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    private StringBuilder requestHandler(SocketChannel clientSocket, String str) throws IOException {

        StringBuilder response = new StringBuilder();

        if (str.matches("log in .+")) {
            clients.put(clientSocket, str.substring(7));

            serverLog.append(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss.SSS"))).append(" ").append(str.substring(7)).append(" logged in").append("\n");
            response.append(str.substring(7)).append(" logged in").append("\n");

        } else if (str.matches("log out")) {
            serverLog.append(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss.SSS"))).append(" ").append(clients.get(clientSocket)).append(" logged out").append("\n");
            response.append(clients.get(clientSocket)).append(" logged out").append("\n");

            ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(response.toString());
            clientSocket.write(byteBuffer);

            clients.remove(clientSocket);

        } else {
            serverLog.append(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss.SSS"))).append(" ").append(clients.get(clientSocket)).append(": ").append(str).append("\n");
            response.append(clients.get(clientSocket)).append(": ").append(str).append("\n");
        }

        return response;
    }

    public void startServer() {

        thread.start();

        System.out.println("Server started\n");
    }

    public void stopServer() {

        try {
            lock.lock();
            thread.interrupt();
            selector.close();
            serverSocketChannel.close();

            System.out.println("Server stopped");

        } catch (IOException exception) {
            exception.printStackTrace();

        }finally {
            lock.unlock();
        }
    }

    String getServerLog() {

        return serverLog.toString();
    }

}


