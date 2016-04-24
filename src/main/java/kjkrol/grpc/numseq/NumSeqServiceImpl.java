package kjkrol.grpc.numseq;

import io.grpc.stub.StreamObserver;

import java.util.stream.IntStream;

/**
 * @author Karol Krol
 */
public class NumSeqServiceImpl implements NumSeqServiceGrpc.NumSeqService {

    @Override
    public void numSeq(NumSeqRequest request, StreamObserver<NumSeqResponse> responseObserver) {

        IntStream.iterate(0, i -> ++i)
                .boxed()
                .limit(request.getTotal())
                .forEach(value -> responseObserver.onNext(NumSeqResponse.getDefaultInstance().newBuilderForType()
                        .addNumber(value)
                        .build()));
        responseObserver.onCompleted();
    }
}
