package com.hy.mqttchatlite.bean;

/**
 * Created by Administrator on 2018/1/18.
 *
 * @author hy 2018/1/18
 */
public class MqttOption {

    public String clientId;

    public String userName;

    public char[] userPassword;

    public MqttOption() {
        userPassword = new char[0];
    }
}
