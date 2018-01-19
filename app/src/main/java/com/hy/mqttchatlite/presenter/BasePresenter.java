package com.hy.mqttchatlite.presenter;

/**
 * 基础Presenter，定义获取状态接口。
 *
 * @author hy 2018/1/15
 */
public interface BasePresenter<S> {

    /**
     * 获取状态。
     *
     * @return
     */
    S state();

    /**
     * 状态更新。
     *
     * @param <S>
     */
    interface BaseView<S> {
        void onStateChanged(S state);
    }

}
