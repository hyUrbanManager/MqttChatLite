package com.hy.mqttchatlite.presenter;

import com.hy.mqttchatlite.bean.MqttOption;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Mqtt自定义操作界面使用MVP模式。
 *
 * @author hy 2018/1/14
 */
public interface MqttPresenter extends BasePresenter<MqttPresenter.State> {

    /**
     * 连接指定的地址，使用指定的选项。
     *
     * @param address
     * @param option
     */
    void connect(String address, MqttOption option);

    /**
     * 断开连接。
     */
    void disconnect();

    /**
     * 订阅某个主题。
     *
     * @param topic
     */
    void subscribeTopic(String topic);

    /**
     * 取消订阅某个主题。
     *
     * @param topic
     */
    void unSubscribeTopic(String topic);

    /**
     * 向某个主题推送指定的消息。
     *
     * @param topic
     * @param message
     */
    void publishMessage(String topic, String message);

    /**
     * 向某个主题推送指定的消息。
     *
     * @param topic
     * @param mqttMessage
     */
    void publishMessage(String topic, MqttMessage mqttMessage);

    interface View extends BasePresenter.BaseView<State> {
        void onConnected(String serverUrl, boolean isReconnect);
        void onDisconnected(String serverUrl, boolean isFromUser, String reason);
        void onSubscribeTopic(String topic);
        void onUnSubscribeTopic(String topic);
        void onPublishMessageStatus(String topic, String message, boolean isSuccess, String reason);
        void onReceivedMessage(String topic, String message);
        void onOperateFail(String operation, String failReason);
    }

    class State {
        public boolean isConnected;
        public String serverUri;
        public String clientId;
    }


}
