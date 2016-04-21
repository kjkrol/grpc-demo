package kjkrol.grpc;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import kjkrol.grpc.client.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

/**
 * @author Karol Krol
 */
@Slf4j
public class MyServerTest {

    private static final String ECHO_MSG = "Hello, World!";

    private static final String HOST = "localhost";

    private static final int PORT = 19880;

    private MyServer server;

    @Before
    public void setUp() throws Exception {
        server = new MyServer(PORT);
        server.start();
    }

    @After
    public void tearDown() {
        server.stop();
    }

    @Test
    public void testEcho() {
        final ManagedChannel channel = NettyChannelBuilder
                .forAddress(HOST, PORT)
                .negotiationType(NegotiationType.PLAINTEXT)
                .build();
        final EchoServiceGrpc.EchoServiceFutureClient client = EchoServiceGrpc.newFutureStub(channel);
        final EchoRequest echoRequest = EchoRequest.newBuilder().setMsg(ECHO_MSG).build();
        final ListenableFuture<EchoResponse> listenableFuture = client.echo(echoRequest);
        try {
            final EchoResponse echoReply = listenableFuture.get();
            assertEquals(ECHO_MSG, echoReply.getMsg());
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Test
    public void testHelloWorld() {
        final ManagedChannel channel = NettyChannelBuilder
                .forAddress(HOST, PORT)
                .negotiationType(NegotiationType.PLAINTEXT)
                .build();
        final HelloWorldServiceGrpc.HelloWorldServiceFutureClient client = HelloWorldServiceGrpc.newFutureStub(channel);
        final Empty empty = Empty.getDefaultInstance();
        final ListenableFuture<HelloWorldResponse> listenableFuture = client.helloWorld(empty);
        try {
            final HelloWorldResponse helloWorldReply = listenableFuture.get();
            assertEquals(ECHO_MSG, helloWorldReply.getMsg());
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Test
    public void testNumSeq() {
        final ManagedChannel channel = NettyChannelBuilder
                .forAddress(HOST, PORT)
                .negotiationType(NegotiationType.PLAINTEXT)
                .build();
        final NumSeqServiceGrpc.NumSeqServiceFutureClient client = NumSeqServiceGrpc.newFutureStub(channel);
        final NumSeqRequest numSeqRequest = NumSeqRequest.newBuilder()
                .setTotal(10000)
                .build();
        final ListenableFuture<NumSeqResponse> listenableFuture = client.numSeq(numSeqRequest);
        final AtomicInteger counter = new AtomicInteger();
        listenableFuture.addListener(() -> {

                },
                Executors.newSingleThreadExecutor());
    }
}