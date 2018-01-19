package com.hy.mqttchatlite;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hy.mqttchatlite.adapter.MqttErrorCodeListAdapter;
import com.hy.mqttchatlite.bean.MqttOption;
import com.hy.mqttchatlite.presenter.MqttPresenter;
import com.hy.mqttchatlite.presenter.PresenterFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MqttMvpActivity extends AppCompatActivity implements MqttPresenter.View {

    @BindView(R.id.mEditAddressText)
    EditText mEditAddressText;
    @BindView(R.id.mConnectButton)
    Button mConnectButton;
    @BindView(R.id.mLog)
    TextView mLog;
    @BindView(R.id.mEditTopicText)
    EditText mEditTopicText;
    @BindView(R.id.mEditMessageText)
    EditText mEditMessageText;
    @BindView(R.id.mSubscribeTopicButton)
    Button mSubscribeTopicButton;
    @BindView(R.id.mUnSubscribeTopicButton)
    Button mUnSubscribeTopicButton;
    @BindView(R.id.mPublishMessageButton)
    Button mPublishMessageButton;
    @BindView(R.id.line1)
    LinearLayout line1;

    private MqttPresenter mPresenter;

    private String connectedButtonC = "连接";
    private String connectedButtonUC = "断开";

    // 连接选项。
    private MqttOption option = new MqttOption();

    private AlertDialog connectionOptionDialog;
    private View dialogLayout;
    private EditText mEditClientIdText;
    private EditText mEditUserText;
    private EditText mEditPasswordText;
    private AlertDialog errorCodeDialog;

    private boolean isEnsureExit;

    private Handler mMainHandler;
    private Runnable resetFlagRunnable = () -> isEnsureExit = false;
    private AlertDialog tipDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt_mvp);
        ButterKnife.bind(this);

        mMainHandler = new Handler(Looper.getMainLooper());

        try {
            mPresenter = PresenterFactory.createMqttPresenter(this, this);
        } catch (Exception e) {
            tip("初始化失败" + e.getMessage());
            e.printStackTrace();
            finish();
            return;
        }

        // 初始化参数。
        MqttPresenter.State state = mPresenter.state();
        option.clientId = state.clientId;
        setTitle(state.clientId);

        // 连接。
        mConnectButton.setOnClickListener((v) -> {
            if (mPresenter.state().isConnected) {
                mPresenter.disconnect();
            } else {
                String t = String.valueOf(mEditAddressText.getText());
                if (TextUtils.isEmpty(t)) {
                    t = (String) mEditAddressText.getHint();
                    mEditAddressText.setText(t);
                }

                mPresenter.connect(t, option);
            }
        });

        // 操作。
        mSubscribeTopicButton.setOnClickListener((v) -> {
            String topic = mEditTopicText.getText().toString();
            if (TextUtils.isEmpty(topic)) {
                tip("话题为空，请编辑");
                return;
            }
            mPresenter.subscribeTopic(topic);
        });

        mUnSubscribeTopicButton.setOnClickListener((v) -> {
            String topic = mEditTopicText.getText().toString();
            if (TextUtils.isEmpty(topic)) {
                tip("话题为空，请编辑");
                return;
            }
            mPresenter.unSubscribeTopic(topic);
        });

        mPublishMessageButton.setOnClickListener((v) -> {
            String topic = mEditTopicText.getText().toString();
            String message = mEditMessageText.getText().toString();
            if (TextUtils.isEmpty(topic)) {
                tip("话题为空，请编辑");
                return;
            }
            if (TextUtils.isEmpty(message)) {
                tip("消息为空，请编辑");
                return;
            }
            mPresenter.publishMessage(topic, message);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.clear) {
            mLog.setText("");
            tip("清除消息成功");
        } else if (item.getItemId() == R.id.option) {
            if (connectionOptionDialog == null) {
                // 初始化。
                dialogLayout = LayoutInflater.from(this).inflate(R.layout.layout_mqtt_connect_option, null, false);
                mEditUserText = dialogLayout.findViewById(R.id.mEditUserText);
                mEditPasswordText = dialogLayout.findViewById(R.id.mEditPasswordText);
                mEditClientIdText = dialogLayout.findViewById(R.id.mEditClientIdText);
                connectionOptionDialog = new AlertDialog.Builder(this)
                        .setPositiveButton("确定", (dialog1, which) -> {
                            option.clientId = mEditClientIdText.getText().toString();
                            option.userName = mEditUserText.getText().toString();
                            String p = mEditPasswordText.getText().toString();
                            if (!TextUtils.isEmpty(p)) {
                                option.userPassword = p.toCharArray();
                            }
                        })
                        .setNegativeButton("取消", (dialog12, which) -> {

                        })
                        .setView(dialogLayout)
                        .setTitle("连接选项(客户端id连接后生效)")
                        .create();
            }
            mEditClientIdText.setText(mPresenter.state().clientId);
            connectionOptionDialog.show();
        } else if (item.getItemId() == R.id.errorCode) {
            if (errorCodeDialog == null) {
                errorCodeDialog = new AlertDialog.Builder(this)
                        .setAdapter(new MqttErrorCodeListAdapter(this), null)
                        .create();
            }
            errorCodeDialog.show();
        } else if (item.getItemId() == R.id.tip) {
            if (tipDialog == null) {
                tipDialog = new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage(R.string.operate_tip)
                        .setPositiveButton("我知道了", (dialog, which) -> {
                        })
                        .create();
            }
            tipDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.disconnect();
        mPresenter.release();

        Process.killProcess(Process.myPid());
    }

    @Override
    public void onBackPressed() {
        if (!isEnsureExit) {
            isEnsureExit = true;
            mMainHandler.postDelayed(resetFlagRunnable, 1000);
            Toast.makeText(this, "再次点击返回键退出", Toast.LENGTH_SHORT).show();
        } else {
            mMainHandler.removeCallbacks(resetFlagRunnable);
            super.onBackPressed();
        }
    }

    private void tip(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStateChanged(MqttPresenter.State state) {
        setTitle(state.clientId);
    }

    @Override
    public void onConnected(String serverUrl, boolean isReconnect) {
        mConnectButton.setText(connectedButtonUC);
        String s = (isReconnect ? "重连" : "连接 ") + serverUrl + " 成功" + '\n';
        mLog.append(s);
    }

    @Override
    public void onDisconnected(String serverUrl, boolean isFromUser, String reason) {
        mConnectButton.setText(connectedButtonC);
        String s = "断开 " + serverUrl + " 连接，来自 " + (isFromUser ? "用户" : ("系统, 原因: " + reason)) + '\n';
        mLog.append(s);
    }

    @Override
    public void onSubscribeTopic(String topic) {
        String s = "已订阅主题 " + topic + '\n';
        mLog.append(s);
    }

    @Override
    public void onUnSubscribeTopic(String topic) {
        String s = "已取消订阅主题 " + topic + '\n';
        mLog.append(s);
    }

    @Override
    public void onPublishMessageStatus(String topic, String message, boolean isSuccess, String reason) {
        String s;
        if (isSuccess) {
            s = "推送消息成功 " + topic + ": " + message + '\n';
        } else {
            s = "推送消息失败 " + topic + ": " + message + '\n';
        }
        mLog.append(s);
    }

    @Override
    public void onReceivedMessage(String topic, String message) {
        String s = "收到: " + topic + ": " + message + '\n';
        mLog.append(s);
    }

    @Override
    public void onOperateFail(String operation, String failReason) {
        String s = "执行 " + operation + " 失败，原因: " + failReason + '\n';
        mLog.append(s);
    }
}
