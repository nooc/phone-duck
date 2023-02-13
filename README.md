# phone-duck

Spring WebSocket assignment.

## 1. API

The REST API specifications can be viewed in swagger/openapi. See [Usage](#usage).

The root adress is [http://localhost:8080/](http://localhost:8080/)

**NOTE**

All endpoints (REST and WS) except <i>/auth</i> require the <u>X-Authorization</u> header to be set to the token value gotten from <i>/auth</i> result. Check [swagger ui](#usage).

### 1.1 REST Endpoints

|Method|Path          |Description|
|------|--------------|-----------|
|POST  |/auth         |Get authorization token.|
|GET   |/channels/    |Return a list of channels.|
|POST  |/channels/    |Create a channel.|
|DELETE|/channels/{id}|Delete a channel.|

### 1.2 WebSocket Endpoints

|Method|Path          |Description|
|------|--------------|-----------|
|GET   |/sub/channels/|Receive channel updates.<br/>Connecting here will show all available channels.|
|GET   |/sub/chat/    |Receive message updates.<br/>Connecting here will show all available messages for subscribed channels.|

#### 1.2.1 /sub/chat/

The <i>/sub/chat/</i> endpoint requires the <u>X-Subscriptions</u> header to be set to a space separated list of subscribed channel ids.

To send a message, use the following command:

        MESSAGE <channel id> <message body>


## 2. <a name="usage">Usage</a>

Start the spring boot application and browse to [http://localhost:8080/](http://localhost:8080/).
