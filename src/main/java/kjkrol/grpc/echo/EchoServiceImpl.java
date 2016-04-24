package kjkrol.grpc.echo;

import io.grpc.stub.StreamObserver;

/**
 * @author Karol Krol
 */
public class EchoServiceImpl implements EchoServiceGrpc.EchoService {

    @Override
    public void echo(EchoRequest request, StreamObserver<EchoResponse> responseObserver) {
        responseObserver.onNext(EchoResponse.newBuilder()
                .setMsg(request.getMsg())
                .build());
        responseObserver.onCompleted();
    }
}
