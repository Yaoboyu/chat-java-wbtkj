package com.wbtkj.chat.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 * <pre>
 * &#64;7 定义服务，用于描述要生成的API接口，类似于Java的业务逻辑接口类
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.0.0)",
    comments = "Source: msg.proto")
public class MsgServiceGrpc {

  private MsgServiceGrpc() {}

  public static final String SERVICE_NAME = "msg.MsgService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<MsgProto.MsgRequest,
      MsgProto.MsgResponse> METHOD_GET_MSG =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "msg.MsgService", "GetMsg"),
          io.grpc.protobuf.ProtoUtils.marshaller(MsgProto.MsgRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(MsgProto.MsgResponse.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MsgServiceStub newStub(io.grpc.Channel channel) {
    return new MsgServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MsgServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new MsgServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static MsgServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new MsgServiceFutureStub(channel);
  }

  /**
   * <pre>
   * &#64;7 定义服务，用于描述要生成的API接口，类似于Java的业务逻辑接口类
   * </pre>
   */
  public static abstract class MsgServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * imgIdentify 方法名 ImgRequest 传入参数  ImgResponse 返回响应
     *注意：这里是returns 不是return
     * </pre>
     */
    public void getMsg(MsgProto.MsgRequest request,
                       io.grpc.stub.StreamObserver<MsgProto.MsgResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_MSG, responseObserver);
    }

    @Override public io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_GET_MSG,
            asyncUnaryCall(
              new MethodHandlers<
                MsgProto.MsgRequest,
                MsgProto.MsgResponse>(
                  this, METHODID_GET_MSG)))
          .build();
    }
  }

  /**
   * <pre>
   * &#64;7 定义服务，用于描述要生成的API接口，类似于Java的业务逻辑接口类
   * </pre>
   */
  public static final class MsgServiceStub extends io.grpc.stub.AbstractStub<MsgServiceStub> {
    private MsgServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MsgServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected MsgServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MsgServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * imgIdentify 方法名 ImgRequest 传入参数  ImgResponse 返回响应
     *注意：这里是returns 不是return
     * </pre>
     */
    public void getMsg(MsgProto.MsgRequest request,
                       io.grpc.stub.StreamObserver<MsgProto.MsgResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_MSG, getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * &#64;7 定义服务，用于描述要生成的API接口，类似于Java的业务逻辑接口类
   * </pre>
   */
  public static final class MsgServiceBlockingStub extends io.grpc.stub.AbstractStub<MsgServiceBlockingStub> {
    private MsgServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MsgServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected MsgServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MsgServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * imgIdentify 方法名 ImgRequest 传入参数  ImgResponse 返回响应
     *注意：这里是returns 不是return
     * </pre>
     */
    public MsgProto.MsgResponse getMsg(MsgProto.MsgRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_MSG, getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * &#64;7 定义服务，用于描述要生成的API接口，类似于Java的业务逻辑接口类
   * </pre>
   */
  public static final class MsgServiceFutureStub extends io.grpc.stub.AbstractStub<MsgServiceFutureStub> {
    private MsgServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MsgServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected MsgServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MsgServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * imgIdentify 方法名 ImgRequest 传入参数  ImgResponse 返回响应
     *注意：这里是returns 不是return
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<MsgProto.MsgResponse> getMsg(
        MsgProto.MsgRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_MSG, getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_MSG = 0;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final MsgServiceImplBase serviceImpl;
    private final int methodId;

    public MethodHandlers(MsgServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_MSG:
          serviceImpl.getMsg((MsgProto.MsgRequest) request,
              (io.grpc.stub.StreamObserver<MsgProto.MsgResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    return new io.grpc.ServiceDescriptor(SERVICE_NAME,
        METHOD_GET_MSG);
  }

}
