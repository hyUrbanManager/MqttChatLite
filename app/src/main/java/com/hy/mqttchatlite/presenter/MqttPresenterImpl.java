package com.hy.mqttchatlite.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;

import com.hy.mqttchatlite.bean.MqttOption;

import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Presenter实现类。
 *
 * @author hy 2018/1/14
 */
public class MqttPresenterImpl implements MqttPresenter {

    private Context context;
    private View view;
    private Handler mMainHandler;

    private MqttAsyncClient mqttClient;

    private State state;
    private MemoryPersistence persistence;

    private MqttCallback callback = new Callback();

    @SuppressLint("HardwareIds")
    public MqttPresenterImpl(Context context, View view) {
        if (context == null || view == null) {
            throw new RuntimeException("null");
        }
        this.context = context;
        this.view = view;
        this.mMainHandler = new Handler(Looper.getMainLooper());
        this.persistence = new MemoryPersistence();
        state = new State();
        state.clientId = "Android " + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    @Override
    public State state() {
        // 更新状态。
        if (mqttClient != null) {
            state.isConnected = mqttClient.isConnected();
        }
        return state;
    }

    @Override
    public void connect(String address, MqttOption option) {
        boolean isNeedToReInit = false;

        // 是否需要重新初始化客户端。
        if (!state.clientId.equals(option.clientId) || !address.equals(state.serverUri)) {
            isNeedToReInit = true;
            state.serverUri = address;
            state.clientId = option.clientId;
            mMainHandler.post(() -> view.onStateChanged(state));
            if (mqttClient != null) {
                try {
                    mqttClient.close();
                } catch (Exception e) {
                    view.onOperateFail("连接", e.toString());
                    e.printStackTrace();
                }
                mqttClient = null;
            }
        }

        // 初始化客户端。
        if (isNeedToReInit) {
            try {
                mqttClient = new MqttAsyncClient(state.serverUri, state.clientId, persistence);
            } catch (Exception e) {
                view.onOperateFail("连接", e.toString());
                return;
            }
            mqttClient.setCallback(callback);
        }

        // 断开重连，清除会话。
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setMaxInflight(1000);
        if (!TextUtils.isEmpty(option.userName)) {
            mqttConnectOptions.setUserName(option.userName);
            mqttConnectOptions.setPassword(option.userPassword);
        }

        // 连接。
        try {
            mqttClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttClient.setBufferOpts(disconnectedBufferOptions);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    mMainHandler.post(() -> view.onOperateFail("连接", exception.toString()));
                }
            });
        } catch (Exception ex) {
            mMainHandler.post(() -> view.onOperateFail("连接", ex.toString()));
            ex.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        // 退出连接。
        try {
            mqttClient.disconnect(3000, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    mMainHandler.post(() -> view.onDisconnected(state.serverUri, true, null));
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    view.onOperateFail("断开连接", exception.toString());
                }
            });
        } catch (Exception e) {
            view.onOperateFail("断开连接", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void subscribeTopic(String topic) {
        try {
            mqttClient.subscribe(topic, 2, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    mMainHandler.post(() -> view.onSubscribeTopic(asyncActionToken.getTopics()[0]));
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    mMainHandler.post(() -> view.onOperateFail("订阅主题", exception.toString()));
                }
            });
        } catch (Exception e) {
            view.onOperateFail("订阅主题 " + topic, e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void unSubscribeTopic(String topic) {
        try {
            mqttClient.unsubscribe(topic, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    mMainHandler.post(() -> view.onUnSubscribeTopic(asyncActionToken.getTopics()[0]));
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    mMainHandler.post(() -> view.onOperateFail("取消订阅主题", exception.toString()));
                }
            });
        } catch (Exception e) {
            view.onOperateFail("连接", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishMessage(String topic, String message) {
        try {
            MqttMessage mess = new MqttMessage();
            mess.setPayload(message.getBytes());
            mqttClient.publish(topic, mess);
            if (!mqttClient.isConnected()) {
                view.onPublishMessageStatus(topic, message, false, "未连接");
            }
        } catch (Exception e) {
            view.onOperateFail("推送消息", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void publishMessage(String topic, MqttMessage mqttMessage) {
        try {
            mqttClient.publish(topic, mqttMessage);
            if (!mqttClient.isConnected()) {
                view.onPublishMessageStatus(topic, "msg id: " + mqttMessage.getId(), false, "未连接");
            }
        } catch (Exception e) {
            view.onOperateFail("推送消息", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void release() {
        if (mqttClient != null) {
            try {
                mqttClient.close();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    class Callback implements MqttCallbackExtended {
        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            mMainHandler.post(() -> view.onConnected(state.serverUri, reconnect));
        }

        @Override
        public void connectionLost(Throwable cause) {
            mMainHandler.post(() -> view.onDisconnected(state.serverUri, false, cause.toString()));
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            byte[] bytes = message.getPayload();
            String ms;
            ms = new String(bytes);
            mMainHandler.post(() -> view.onReceivedMessage(topic, ms));
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            MqttMessage mqttMessage = null;
            try {
                mqttMessage = token.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }

            String message;
            if (mqttMessage == null) {
                message = token.getResponse().toString();
            } else {
                message = new String(mqttMessage.getPayload());
            }
            String finalS = message;
            mMainHandler.post(() -> view.onPublishMessageStatus(token.getTopics()[0],
                    finalS, true, ""));
        }
    }

}