package kjkrol.grpc;

import com.google.protobuf.Empty;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import io.grpc.stub.StreamObserver;
import kjkrol.grpc.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * A sample gRPC server that serve the RouteGuide (see route_guide.proto) service.
 */
@Slf4j
public class MyServer {

    private final int port;

    private final Server server;

    public static void main(String[] args) throws Exception {
        final MyServer server = new MyServer(8980);
        server.start();
        server.blockUntilShutdown();
    }

    /**
     * Create a RouteGuide server listening on {@code port} using {@code featureFile} database.
     */
    public MyServer(int port) throws IOException {
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
    public void start() throws IOException, InterruptedException {
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
    public void stop() {
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

    private static class EchoServiceImpl implements EchoServiceGrpc.EchoService {

        @Override
        public void echo(EchoRequest request, StreamObserver<EchoResponse> responseObserver) {
            responseObserver.onNext(EchoResponse.newBuilder()
                    .setMsg(request.getMsg())
                    .build());
            responseObserver.onCompleted();
        }
    }

    private static class HelloWorldServiceImpl implements HelloWorldServiceGrpc.HelloWorldService {

        @Override
        public void helloWorld(Empty request, StreamObserver<HelloWorldResponse> responseObserver) {
            responseObserver.onNext(HelloWorldResponse.newBuilder()
                    .setMsg("Hello, World!")
                    .build());
            responseObserver.onCompleted();
        }
    }

    private static class NumSeqServiceImpl implements NumSeqServiceGrpc.NumSeqService {

        @Override
        public void numSeq(NumSeqRequest request, StreamObserver<NumSeqResponse> responseObserver) {

        }
    }

}
