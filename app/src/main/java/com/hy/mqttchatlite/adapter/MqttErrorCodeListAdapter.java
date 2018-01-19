package com.hy.mqttchatlite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hy.mqttchatlite.constant.MqttErrorCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 显示错误代码列表适配器。
 *
 * @author hy 2018/1/18
 */
public class MqttErrorCodeListAdapter extends BaseAdapter {

    private HashMap<Integer, String> map = MqttErrorCode.codeMap;

    private List<Integer> tmpList = new ArrayList<>();
    private List<String> list = new ArrayList<>();

    private LayoutInflater inflater;

    public MqttErrorCodeListAdapter(Context context) {
        Set<Integer> set = map.keySet();
        tmpList.addAll(set);
        Collections.sort(tmpList, (o1, o2) -> o1 - o2);
        for (Integer i : tmpList) {
            String s = "   " + i + " : " + map.get(i);
            list.add(s);
        }
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        H holder;
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.test_list_item, null, false);
            holder = new H(convertView);
            convertView.setTag(holder);
        } else {
            holder = (H) convertView.getTag();
        }
        holder.textView.setText(getItem(position));
        return convertView;
    }

    class H {
        TextView textView;

        public H(View convertView) {
            textView = convertView.findViewById(android.R.id.text1);
        }
    }

}
