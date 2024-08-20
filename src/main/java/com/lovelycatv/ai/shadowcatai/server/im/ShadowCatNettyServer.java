package com.lovelycatv.ai.shadowcatai.server.im;

import com.lovelycatv.ai.shadowcatai.server.entity.SessionEntity;
import com.lovelycatv.ai.shadowcatai.server.entity.UserEntity;
import com.lovelycatv.ai.shadowcatai.server.im.processor.ClientMessageHandler;
import com.lovelycatv.ai.shadowcatai.server.service.MessageService;
import com.lovelycatv.ai.shadowcatai.server.service.SessionService;
import com.lovelycatv.ai.shadowcatai.server.service.UserService;
import com.lovelycatv.ai.shadowcatai.share.im.code.CustomFrameDecoder;
import com.lovelycatv.ai.shadowcatai.share.im.code.MyObjectDecoder;
import com.lovelycatv.ai.shadowcatai.share.im.code.MyObjectEncoder;
import com.lovelycatv.ai.shadowcatai.share.im.message.MessageChain;
import com.lovelycatv.ai.shadowcatai.share.im.message.func.CallBackMessage;
import com.lovelycatv.ai.shadowcatai.share.im.message.func.LoginMessage;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lovelycat
 * @version 1.0
 * @since 2024-08-03 22:01
 */
@Component
public class ShadowCatNettyServer {
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private UserService userService;
    @Resource
    private SessionService sessionService;
    @Resource
    private MessageService messageService;

    private static final Map<String, Long> channelIdWithUserId = new HashMap<>();


    public void start(int port) {
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) {
                        channel.pipeline().addLast(new CustomFrameDecoder());
                        channel.pipeline().addLast(new MyObjectEncoder());
                        channel.pipeline().addLast(new MyObjectDecoder());

                        channel.pipeline().addLast(new SimpleChannelInboundHandler<MessageChain>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, MessageChain msg) {
                                if (msg.isFunc()) {
                                    if (msg.getMessages().get(0) instanceof LoginMessage loginMessage) {
                                        try {
                                            UserEntity user = userService.getByUserNameOrEmail(loginMessage.getUsername());
                                            boolean matches = passwordEncoder.matches(loginMessage.getPassword(), user.getPassword());
                                            if (matches) {
                                                String channelId = ctx.channel().id().asLongText();

                                                channelIdWithUserId.put(channelId, user.getId());
                                                ctx.writeAndFlush(CallBackMessage.build(CallBackMessage.CODE_LOGIN_SUCCESS, "Welcome back!"));
                                            } else {
                                                ctx.writeAndFlush(CallBackMessage.loginFailed());
                                            }
                                        } catch (UsernameNotFoundException e) {
                                            ctx.writeAndFlush(CallBackMessage.loginFailed());
                                        }
                                    }
                                } else {
                                    String channelId = ctx.channel().id().asLongText();
                                    if (channelIdWithUserId.containsKey(channelId)) {
                                        String targetSessionId = msg.getSessionId();
                                        SessionEntity session = sessionService.getDetailedBySessionId(targetSessionId);
                                        if (session == null) {
                                            ctx.writeAndFlush(CallBackMessage.build(CallBackMessage.CODE_SESSION_INVALID, "Invalid session"));
                                            return;
                                        }
                                        ctx.fireChannelRead(msg);
                                    } else {
                                        System.out.println("Unauthorized message received");
                                    }
                                }
                            }
                        });

                        channel.pipeline().addLast(new ClientMessageHandler(channelIdWithUserId::get, sessionService, messageService));

                        channel.closeFuture().addListener((ChannelFutureListener) future -> {
                            String channelId = future.channel().id().asLongText();
                            System.out.printf("%s Disconnected%n", channelId);
                            channelIdWithUserId.remove(channelId);
                        });
                    }
                });
        ChannelFuture channelFuture = serverBootstrap.bind(port);
        System.out.println("Netty Server Started, listening on port " + port);
        channelFuture.channel().closeFuture().addListener((ChannelFutureListener) future -> System.out.println("Netty Server Closed"));

    }

}
