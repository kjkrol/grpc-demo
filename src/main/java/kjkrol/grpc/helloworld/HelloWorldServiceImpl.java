package kjkrol.grpc.helloworld;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;

/**
 * @author Karol Krol
 */
public class HelloWorldServiceImpl implements HelloWorldServiceGrpc.HelloWorldService {

    @Override
    public void helloWorld(Empty request, StreamObserver<HelloWorldResponse> responseObserver) {
        responseObserver.onNext(HelloWorldResponse.newBuilder()
                .setMsg("Hello, World!")
                .build());
        responseObserver.onCompleted();
    }
}
