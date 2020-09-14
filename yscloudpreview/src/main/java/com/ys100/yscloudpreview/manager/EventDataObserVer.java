package com.ys100.yscloudpreview.manager;

import com.ys100.yscloudpreview.bean.EventData;

import java.util.ArrayList;
import java.util.Observer;

/**
 * Author 邓杰
 * Email: jie.deng@ys100.com
 * Date: 2020/3/20
 * Description:
 */
public class EventDataObserVer {

    private ArrayList<Observer> observers = new ArrayList<>();


    private static class Holder {
        private static EventDataObserVer obserVer = new EventDataObserVer();
    }

    public static EventDataObserVer getInstance() {
        return Holder.obserVer;
    }

    public void addObserver(Observer obs) {
        if (!this.observers.contains(obs))
            this.observers.add(obs);
    }

    public boolean isHasObserver() {
        return observers.size() > 0;
    }

    public void removeObserver(Observer obs) {
        this.observers.remove(obs);
    }

    public EventDataObserVer notifyObservers(EventData eventData) {
        if (observers.size() > 0) {
            for (Observer obs : this.observers) {
                if (obs != null)
                    obs.update(null, eventData);
            }
        }
        return this;
    }
}
