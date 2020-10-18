package com.mamalimomen.base.controllers.utilities;

import com.mamalimomen.base.dtos.BaseDTO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class ServerManager {
    private ServerSocket serverSocket;
    private final Set<ClientThread> clients = new HashSet<>();

    private final short port = 8000;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public ServerManager() {
        executorService.execute(this::turnOnServer);
    }

    private void turnOnServer() {
        try {
            serverSocket = new ServerSocket(port);
            System.err.println("Server is running on port " + port + "...");

            while (true) {
                Socket socket = serverSocket.accept();
                ClientThread client = new ClientThread(socket);
                clients.add(client);
                System.err.println("One client connected!");
                System.err.println("Current clients count : " + clients.size());
                executorService.execute(client);
            }
        } catch (IOException e) {
            System.err.println("Server is shutting down...");
        }
    }

    public void turnOffServer() {
        try {
            for (ClientThread client : clients) {
                client.closeClient();
            }
            serverSocket.close();
            executorService.shutdownNow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientThread extends Thread {
        private final Socket socket;
        private final ObjectInputStream inputObject;
        private final ObjectOutputStream outputObject;

        private Optional<Object> buffer = Optional.empty();
        private final Lock accessLock = new ReentrantLock();
        private final Condition canTalk = accessLock.newCondition();
        private final Condition canListen = accessLock.newCondition();
        private boolean occupiedBuffer = false;
        private boolean connected = true;

        ClientThread(Socket socket) throws IOException {
            this.socket = socket;
            outputObject = new ObjectOutputStream(socket.getOutputStream());
            inputObject = new ObjectInputStream(socket.getInputStream());
        }

        @Override
        public void run() {
            executorService.execute(this::listen);
            executorService.execute(this::talk);
        }

        private void listen() {
            while (true) {
                accessLock.lock();
                try {
                    while (occupiedBuffer && connected) {
                        canListen.await();
                    }
                    if(!connected){
                        break;
                    }
                    buffer = Optional.of(MAMPParser.parse((MAMP<? extends BaseDTO>) inputObject.readObject()));
                    occupiedBuffer = true;
                    canTalk.signalAll();
                } catch (IOException | ClassNotFoundException | InterruptedException e) {
                    clients.remove(this);
                    System.err.println("One client disconnected!");
                    System.err.println("Current clients count : " + clients.size());
                    connected = false;
                    canTalk.signalAll();
                    break;
                } finally {
                    accessLock.unlock();
                }
            }
        }

        private void talk() {
            while (true) {
                accessLock.lock();
                try {
                    while (!occupiedBuffer && connected) {
                        canTalk.await();
                    }
                    if(!connected){
                        break;
                    }
                    occupiedBuffer = false;
                    outputObject.writeObject(buffer.get());
                    buffer = Optional.empty();
                    canListen.signalAll();
                } catch (IOException | InterruptedException e) {
                    clients.remove(this);
                    System.err.println("One client disconnected!");
                    System.err.println("Current clients count : " + clients.size());
                    connected = false;
                    canListen.signalAll();
                    break;
                } finally {
                    accessLock.unlock();
                }
            }
        }

        private void closeClient() throws IOException {
            inputObject.close();
            outputObject.close();
            socket.close();
        }
    }
}
