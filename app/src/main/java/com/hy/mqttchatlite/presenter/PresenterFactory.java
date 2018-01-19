package com.hy.mqttchatlite.presenter;

import android.content.Context;

/**
 * Created by Administrator on 2018/1/14.
 *
 * @author hy 2018/1/14
 */
public class PresenterFactory {

    /**
     * 生成mqtt操作Presenter。
     *
     * @return
     */
    public static MqttPresenter createMqttPresenter(Context context, MqttPresenter.View view) {
        return new MqttPresenterImpl(context, view);
    }


}
