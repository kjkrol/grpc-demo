package kjkrol.grpc;

import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import kjkrol.grpc.echo.EchoServiceGrpc;
import kjkrol.grpc.echo.EchoServiceImpl;
import kjkrol.grpc.helloworld.HelloWorldServiceGrpc;
import kjkrol.grpc.helloworld.HelloWorldServiceImpl;
import kjkrol.grpc.numseq.NumSeqServiceGrpc;
import kjkrol.grpc.numseq.NumSeqServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * A sample gRPC server that serve the RouteGuide (see route_guide.proto) service.
 */
@Slf4j
public class MyServer {

    private static final int DEFAULT_PORT = 8980;

    private final int port;

    private final Server server;

    public static void main(String[] args) throws Exception {
        final MyServer server = new MyServer(DEFAULT_PORT);
        server.start();
        server.blockUntilShutdown();
    }
    /**
     * Create a server listening on {@code port} using {@code featureFile} database.
     */
    MyServer(int port) throws IOException {
        this.port = port;
        this.server = NettyServerBuilder.forPort(port)
                .addService(EchoServiceGrpc.bindService(new EchoServiceImpl()))
                .addService(HelloWorldServiceGrpc.bindService(new HelloWorldServiceImpl()))
                .addService(NumSeqServiceGrpc.bindService(new NumSeqServiceImpl()))
                .build();
    }

    /**
     * Start serving requests.
     */
    void start() throws IOException, InterruptedException {
        server.start();
        log.debug("Server started, listening on {}", port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may has been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                MyServer.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    /**
     * Stop serving requests and shutdown resources.
     */
    void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

}
