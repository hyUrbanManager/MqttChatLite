package com.hy.mqttchatlite.constant;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/1/18.
 *
 * @author hy 2018/1/18
 */
public class MqttErrorCode {

    public static final HashMap<Integer, String> codeMap = new HashMap<>();

    static {

        codeMap.put(0x00, "CLIENT EXCEPTION");

        // CONNACK return codes
        /**
         * The protocol version requested is not supported by the server.
         */
        codeMap.put(0x01, "INVALID PROTOCOL VERSION");
        /**
         * The server has rejected the supplied client ID
         */
        codeMap.put(0x02, "INVALID CLIENT ID");
        /**
         * The broker was not available to handle the request.
         */
        codeMap.put(0x03, "BROKER UNAVAILABLE");
        /**
         * Authentication with the server has failed" due to a bad user name or password.
         */
        codeMap.put(0x04, "FAILED AUTHENTICATION");
        /**
         * Not authorized to perform the requested operation
         */
        codeMap.put(0x05, "NOT AUTHORIZED");

        /**
         * An unexpected error has occurred.
         */
        codeMap.put(0x06, "UNEXPECTED ERROR");

        /**
         * Error from subscribe - returned from the server.
         */
        codeMap.put(0x80, "SUBSCRIBE FAILED");

        /**
         * Client timed out while waiting for a response from the server.
         * The server is no longer responding to keep-alive messages.
         */
        codeMap.put(32000, "CLIENT TIMEOUT");

        /**
         * Internal error" caused by no new message IDs being available.
         */
        codeMap.put(32001, "NO MESSAGE IDS AVAILABLE");

        /**
         * Client timed out while waiting to write messages to the server.
         */
        codeMap.put(32002, "WRITE TIMEOUT");

        /**
         * The client is already connected.
         */
        codeMap.put(32100, "CLIENT CONNECTED");

        /**
         * The client is already disconnected.
         */
        codeMap.put(32101, "CLIENT ALREADY DISCONNECTED");
        /**
         * The client is currently disconnecting and cannot accept any new work.
         * This can occur when waiting on a token" and then disconnecting the client.
         * If the message delivery does not complete within the quiesce timeout
         * period" then the waiting token will be notified with an exception.
         */
        codeMap.put(32102, "CLIENT DISCONNECTING");

        /**
         * Unable to connect to server
         */
        codeMap.put(32103, "SERVER CONNECT ERROR");

        /**
         * The client is not connected to the server.  The {@link MqttClient#connect()}
         * or {@link MqttClient#connect(MqttConnectOptions)} method must be called
         * first.  It is also possible that the connection was lost - see
         * {@link MqttClient#setCallback(MqttCallback)} for a way to track lost
         * connections.
         */
        codeMap.put(32104, "CLIENT NOT CONNECTED");

        /**
         * Server URI and supplied <code>SocketFactory</code> do not match.
         * URIs beginning <code>tcp://</code> must use a <code>javax.net.SocketFactory</code>,
         * and URIs beginning <code>ssl://</code> must use a <code>javax.net.ssl.SSLSocketFactory</code>.
         */
        codeMap.put(32105, "SOCKET FACTORY MISMATCH");

        /**
         * SSL configuration error.
         */
        codeMap.put(32106, "SSL CONFIG ERROR");

        /**
         * Thrown when an attempt to call {@link MqttClient#disconnect()} has been
         * made from within a method on {@link MqttCallback}.  These methods are invoked
         * by the client's thread" and must not be used to control disconnection.
         *
         * @see MqttCallback#messageArrived(String" MqttMessage )
         */
        codeMap.put(32107, "CLIENT DISCONNECT PROHIBITED");

        /**
         * Protocol error: the message was not recognized as a valid MQTT packet.
         * Possible reasons for this include connecting to a non-MQTT server" or
         * connecting to an SSL server port when the client isn't using SSL.
         */
        codeMap.put(32108, "INVALID MESSAGE");

        /**
         * The client has been unexpectedly disconnected from the server. The {@link #getCause() cause}
         * will provide more details.
         */
        codeMap.put(32109, "CONNECTION LOST");

        /**
         * A connect operation in already in progress" only one connect can happen
         * at a time.
         */
        codeMap.put(32110, "CONNECT IN PROGRESS");

        /**
         * The client is closed - no operations are permitted on the client in this
         * state.  New up a new client to continue.
         */
        codeMap.put(32111, "CLIENT CLOSED");

        /**
         * A request has been made to use a token that is already associated with
         * another action.  If the action is complete the reset() can ve called on the
         * token to allow it to be reused.
         */
        codeMap.put(32201, "TOKEN INUSE");

        /**
         * A request has been made to send a message but the maximum number of inflight
         * messages has already been reached. Once one or more messages have been moved
         * then new messages can be sent.
         */
        codeMap.put(32202, "MAX INFLIGHT");

        /**
         * The Client has attempted to publish a message whilst in the 'resting' / offline
         * state with Disconnected Publishing enabled" however the buffer is full and
         * deleteOldestMessages is disabled" therefore no more messages can be published
         * until the client reconnects" or the application deletes buffered message
         * manually.
         */
        codeMap.put(32203, "DISCONNECTED BUFFER FULL");

    }
}
