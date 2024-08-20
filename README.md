# ShadowCatAI Server

An Ollama model management tool suitable for mobile devices.

If you want to download the app, turn to [ShadowCatApp](https://github.com/LovelyCatEx/ShadowCatAiApp)

✨ Features:
+ **Simple**: No more configurations. All operations have detailed instructions so that you could learn how to use the app quickly. 😇
+ **Multi-Sessions**: Just like a IM apps, ShadowCatAI provides an environment similar to the IM apps, you could chat with different models. 📌
+ **Multi-Accounts**: The data between users is isolated. You can switch multiple accounts between multiple servers at any time. 🔗
+ **Chat-Operations**: You can withdraw/delete messages during the chat any time. 🪄
+ **Session-Branch**: Quickly create a new session that includes the specified context, the context will be your specified history messages. 🎲
+ **Message-Sync**: All messages will be stored in both local database and remote server, you could sync messages between different devices easily. 🍩
+ **Dark-Theme**: You know, some like dark themes. 🌙

🎯 Future:
+ Preset-Context: You can create a new session through preset context and share your idea with others.
+ What do you want? 😮

🎡 Developer:

This project is using the **AGPL license**.

## Deployment

There are two ways to deploy this project:

### 1. Docker

1. Run `server-build` script to build the project
2. Run `docker-build` script to build docker image
3. Cd `deploy/mysql` and run `mysql-build.bat` script to build mysql image
4. Edit `deplot/docker-compose.yml`, modify env `INIT_USERNAME`, `INIT_PASSWORD` and `INIT_EMAIL` to you want as the initial account.
5. Cd `deploy` and run `start` script to start the shadowcat server

If you want to customize the configuration, just edit the `deplot/docker-compose.yml`.

If your Ollama is running on your localhost machine, use `host.docker.internal` to replace the `localhost`.

### 2. Debug

You need to prepare a Redis and MySQL database, database table structures is in `deploy/mysql/shadowcat.sql`.

Add VM Options:

```
-Dserver.port=8080 
-Dserver.chat-port=8081 
-Dspring.shardingsphere.datasource.master.jdbcUrl=jdbc:mysql://127.0.0.1:3306/shadowcat?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimeZone=UTC 
-Dspring.shardingsphere.datasource.master.url=jdbc:mysql://127.0.0.1:3306/shadowcat?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimeZone=UTC 
-Dspring.shardingsphere.datasource.master.username=root 
-Dspring.shardingsphere.datasource.master.password=12345 
-Dspring.data.redis.host=127.0.0.1 
-Dspring.data.redis.port=6379 
-Dshadowcat.init.username=root 
-Dshadowcat.init.password=root 
-Dshadowcat.init.email=master@root.com 
-Dshadowcat.uploads.path=/path/to/uploads/
-Dspring.ai.ollama.base-url=localhost:11434
```

Tips: The `shadowcat.uploads.path` should be ended with `/`