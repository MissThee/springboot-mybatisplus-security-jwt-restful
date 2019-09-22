package com.github.missthee.test.socket.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SocketTCPTool {
    private static final Map<Socket, List<Thread>> socketThreadsMap = new ConcurrentHashMap<>();

    public static void send(Socket socket, String msg) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(msg.getBytes());
        outputStream.flush();
    }

    public static Thread sendStart(Socket socket, String threadName, Boolean alwaysSendSplitSign) {
        Thread thread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(1000);
                    String SplitSign = (alwaysSendSplitSign || Math.random() > 0.8 ? String.valueOf((char) 61) : "");
                    send(socket, "[From " + threadName + "]:" + LocalTime.now() + SplitSign);//发送
                }
            } catch (SocketException | InterruptedException e) {
//                System.out.println("receiveStart连接断开");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
//                System.out.println("ThreadEnd:" + Thread.currentThread().getName());
                removeThread(socket, Thread.currentThread());
            }
        }, threadName + " " + LocalDateTime.now().toString());
        thread.start();
        addThread(socket, thread);
        return thread;
    }

    public static String receive(Socket socket, Integer endCode) throws IOException {
        StringBuilder stringBuffer = new StringBuilder();
        InputStream inputStream = socket.getInputStream();
        int len;
        while ((len = inputStream.read()) != -1) {
            if (endCode != null && endCode.equals(len)) {
                break;
            }
            stringBuffer.append((char) len);
        }
        return stringBuffer.toString();
    }

    public static Thread receiveStart(Socket socket, Integer endCode, String threadName) {
        Thread thread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String receive = receive(socket, endCode);//接收
                    System.out.println(receive + "[接收了结尾标志]");
                }
            } catch (SocketException e) {
//                System.out.println("receiveStart连接断开");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
//                System.out.println("ThreadEnd:" + Thread.currentThread().getName());
                removeThread(socket, Thread.currentThread());
            }
        }, threadName + " " + LocalDateTime.now().toString());
        thread.start();
        addThread(socket, thread);
        return thread;
    }

    public static void close(Socket socket) throws IOException {
        //以下两个方法仅关闭对应的流，socket不会关闭
//                socket.shutdownInput();
//                socket.shutdownOutput();
        //以下三个方法都会关闭socket/输出流/输入流
//                outputStream.close();
//                inputStream.close();
        socket.close();
    }

    private static void addThread(Socket socket, Thread thread) {
        synchronized (socketThreadsMap) {
            if (socketThreadsMap.containsKey(socket)) {
                socketThreadsMap.get(socket).add(thread);
            } else {
                socketThreadsMap.put(socket, new ArrayList<Thread>() {{
                    add(thread);
                }});
            }
        }
        printSocketThreadsMapInfo(true);
    }

    private static void removeThread(Socket socket, Thread thread) {
        synchronized (socketThreadsMap) {
            if (socketThreadsMap.containsKey(socket)) {
                List<Thread> threadListTmp = socketThreadsMap.get(socket);
                if (threadListTmp != null) {
                    Iterator<Thread> threadIterator = threadListTmp.iterator();
                    while (threadIterator.hasNext()) {
                        Thread threadTmp = threadIterator.next();
                        if (threadTmp == thread) {
                            thread.interrupt();
                            threadIterator.remove();
                            break;
                        }
                    }
                }
                if (threadListTmp == null || threadListTmp.size() == 0) {
                    try {
                        socket.close();
                    } catch (IOException ignored) {
                    }
                    socketThreadsMap.remove(socket);
                }
            }
            printSocketThreadsMapInfo(false);
        }
    }

    private static void printSocketThreadsMapInfo(Boolean isAdd) {
        synchronized (socketThreadsMap) {
            Optional<Integer> threadNum = socketThreadsMap.values().stream().map(List::size).reduce((t1, t2) -> t1 + t2);
            System.out.println("[Count " + (isAdd ? "+" : "-") + "] socket: " + (socketThreadsMap.size() == 0 ? "-" : socketThreadsMap.size()) + ", thread: " + (threadNum.isPresent() ? threadNum.get() : "-"));
        }
    }
}
