package com.mayakplay.aclf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 07.07.2019.
 */
public class TestClient {

    public static void main(String[] args) throws IOException {

        final Socket socket = new Socket("127.0.0.1", TestServer.SERVER_PORT);

        final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        reader.lines().forEach(System.out::println);

    }

}