# RPC

## 原理

通过 ZooKeeper 实现服务注册与发现<br>
Spring实现Ioc <br>
Netty 异步通信<br>
PB格式序列化对象<br>
随机负载均衡

## Server

@RPCService 用来注解一个要被注册的Server<br>
参数包括value和version

##Client

通过context获取RPCProxy <br>
RPCProxy.create() 创建代理对象
