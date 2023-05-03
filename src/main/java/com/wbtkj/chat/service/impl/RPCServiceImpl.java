package com.wbtkj.chat.service.impl;

import com.wbtkj.chat.grpc.MsgProto;
import com.wbtkj.chat.grpc.MsgServiceGrpc;
import com.wbtkj.chat.service.RPCService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class RPCServiceImpl implements RPCService {

    @GrpcClient("grpc-server")
    private MsgServiceGrpc.MsgServiceBlockingStub msgStub;

    @Override
    public void test() {
        MsgProto.MsgRequest msgRequest = MsgProto.MsgRequest.newBuilder()
                .setName("图片信息")
                .build();
        MsgProto.MsgResponse msgResponse = msgStub.getMsg(msgRequest);
        System.out.println(msgResponse.getMsg());
    }
}
