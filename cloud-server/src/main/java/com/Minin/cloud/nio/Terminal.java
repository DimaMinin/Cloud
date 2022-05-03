package com.Minin.cloud.nio;


import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
public class Terminal {


    private Path dir;
    private final ServerSocketChannel serverChannel;
    private final Selector selector;
    private final ByteBuffer buffer = ByteBuffer.allocate(256);

    public Terminal() throws IOException {

        dir = Path.of("files");

        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(8189));
        serverChannel.configureBlocking(false);

        selector = Selector.open();

        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("Server started on port: 8189");

        while (serverChannel.isOpen()) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();
            try {
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isAcceptable()) {
                        handleAccept(key);
                    }
                    if (key.isReadable()) {
                        handleRead(key);
                    }
                    iterator.remove();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        String message = readMessageFromChannel(channel).trim();
        System.out.println("Received: " + message);



        if (message.equals("ls")) {
            channel.write(ByteBuffer.wrap(getLsResultString().getBytes(StandardCharsets.UTF_8))
            );
        } else if (message.startsWith("touch ")) {
            String fileName = message.substring(6);
            createNewFile(fileName, channel);
        } else if (message.startsWith("mkdir ")) {
            String dirName = message.substring(6);
            createNewDirectory(dirName, channel);
        } else if (message.startsWith("cat ")) {
            String listenFile = message.substring(4);
            channel.write(ByteBuffer.wrap(readFileFilling(listenFile).getBytes(StandardCharsets.UTF_8)));
            channel.write(ByteBuffer.wrap("\n\r".getBytes(StandardCharsets.UTF_8)));
        }

        //команду "cd" пока что не сделал

        channel.write(ByteBuffer.wrap("-> ".getBytes(StandardCharsets.UTF_8)));
    }

    private String readFileFilling(String listenFile) throws IOException {
        Path listenedPath = Path.of(dir.toString(), listenFile);
        return Files.readString(listenedPath, StandardCharsets.UTF_8);
    }

    private void createNewDirectory(String dirName, SocketChannel channel) throws IOException {
        Path path = Path.of(dir.toString(), dirName);
        Files.createDirectory(path);
    }

    private void createNewFile(String fileName, SocketChannel channel) throws IOException {
        Path path = Path.of(dir.toString(), fileName);
        Files.createFile(path);
    }

    private String getLsResultString() throws IOException {
        return Files.list(dir)
                .map(p -> p.getFileName().toString())
                .collect(Collectors.joining("\n\r")) + "\n\r";
    }

    private String readMessageFromChannel(SocketChannel channel) throws IOException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            int readCount = channel.read(buffer);
            if (readCount == -1) {
                channel.close();
                break;
            }
            if (readCount == 0) {
                break;
            }
            buffer.flip();
            while (buffer.hasRemaining()) {
                sb.append((char) buffer.get());
            }
            buffer.clear();
        }
        return sb.toString();
    }

    private void handleAccept(SelectionKey key) throws IOException {
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
        System.out.println("Client accepted...");
    }

    public static void main(String[] args) throws IOException {
        new Terminal();
    }

}