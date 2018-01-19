package com.hy.mqttchatlite;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.logging.Logger;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 适配mqtt自带的logger。
 * <p>
 * Implementation of the the logger interface that uses java.uti.logging
 * <p>
 * A Logger that utilises Java's built in logging facility - java.util.logging.
 * <p>A sample java.util.logging properties file - jsr47min.properties is provided that demonstrates
 * how to run with a memory based trace facility that runs with minimal performance
 * overhead. The memory buffer can be dumped when a log/trace record is written matching
 * the MemoryHandlers trigger level or when the push method is invoked on the MemoryHandler.
 * {@link org.eclipse.paho.client.mqttv3.util.Debug Debug} provides method to make it easy
 * to dump the memory buffer as well as other useful debug info.
 *
 * @author hy 2018/1/14
 */
public class AndroidLog implements Logger {
    public static final String TAG = "@mqtt";

    /**
     * @param logMsgCatalog   The resource bundle associated with this logger
     * @param loggerID        The suffix for the loggerName (will be appeneded to org.eclipse.paho.client.mqttv3
     * @param resourceContext A context for the logger e.g. clientID or appName...
     */
    @Override
    public void initialise(ResourceBundle logMsgCatalog, String loggerID, String resourceContext) {

    }

    @Override
    public void setResourceName(String logContext) {
    }

    @Override
    public boolean isLoggable(int level) {
        return true;
    }

    @Override
    public void severe(String sourceClass, String sourceMethod, String msg) {
        log(SEVERE, sourceClass, sourceMethod, msg, null, null);
    }

    @Override
    public void severe(String sourceClass, String sourceMethod, String msg, Object[] inserts) {
        log(SEVERE, sourceClass, sourceMethod, msg, inserts, null);
    }

    @Override
    public void severe(String sourceClass, String sourceMethod, String msg, Object[] inserts, Throwable thrown) {
        log(SEVERE, sourceClass, sourceMethod, msg, inserts, thrown);
    }

    @Override
    public void warning(String sourceClass, String sourceMethod, String msg) {
        log(WARNING, sourceClass, sourceMethod, msg, null, null);
    }

    @Override
    public void warning(String sourceClass, String sourceMethod, String msg, Object[] inserts) {
        log(WARNING, sourceClass, sourceMethod, msg, inserts, null);
    }

    @Override
    public void warning(String sourceClass, String sourceMethod, String msg, Object[] inserts, Throwable thrown) {
        log(WARNING, sourceClass, sourceMethod, msg, inserts, thrown);
    }

    @Override
    public void info(String sourceClass, String sourceMethod, String msg) {
        log(INFO, sourceClass, sourceMethod, msg, null, null);
    }

    @Override
    public void info(String sourceClass, String sourceMethod, String msg, Object[] inserts) {
        log(INFO, sourceClass, sourceMethod, msg, inserts, null);
    }

    @Override
    public void info(String sourceClass, String sourceMethod, String msg, Object[] inserts, Throwable thrown) {
        log(INFO, sourceClass, sourceMethod, msg, inserts, thrown);
    }

    @Override
    public void config(String sourceClass, String sourceMethod, String msg) {
        log(CONFIG, sourceClass, sourceMethod, msg, null, null);
    }

    @Override
    public void config(String sourceClass, String sourceMethod, String msg, Object[] inserts) {
        log(CONFIG, sourceClass, sourceMethod, msg, inserts, null);
    }

    @Override
    public void config(String sourceClass, String sourceMethod, String msg, Object[] inserts, Throwable thrown) {
        log(CONFIG, sourceClass, sourceMethod, msg, inserts, thrown);
    }

    @Override
    public void log(int level, String sourceClass, String sourceMethod, String msg, Object[] inserts, Throwable thrown) {
        String s = String.format(Locale.CHINA,
                "%d, class: %s, method: %s, msg: %s, inserts: %s, throwable: %s",
                level,
                clipClassName(sourceClass),
                sourceMethod, msg,
                s(inserts),
                thrown == null ? "null" : thrown.toString());
        if (BuildConfig.DEBUG) {
            Log.d(TAG, s);
        }
    }

    private String s(Object[] inserts) {
        if (inserts == null) {
            return "null";
        }
        if (inserts.length == 0) {
            return "[]";
        }
        StringBuilder s = new StringBuilder("[");
        for (Object o : inserts) {
            if (o != null) {
                Class c = o.getClass();
                s.append(c.getSimpleName()).append(", ");
            }
        }
        s.append("]");
        return s.toString();
    }

    private String clipClassName(String className) {
        String[] ss = className.split("\\.");
        if (ss.length == 0) {
            return className;
        }
        return ss[ss.length - 1];
    }

    @Override
    public void fine(String sourceClass, String sourceMethod, String msg) {
        trace(FINE, sourceClass, sourceMethod, msg, null, null);
    }

    @Override
    public void fine(String sourceClass, String sourceMethod, String msg, Object[] inserts) {
        trace(FINE, sourceClass, sourceMethod, msg, inserts, null);
    }

    @Override
    public void fine(String sourceClass, String sourceMethod, String msg, Object[] inserts, Throwable ex) {
        trace(FINE, sourceClass, sourceMethod, msg, inserts, ex);
    }

    @Override
    public void finer(String sourceClass, String sourceMethod, String msg) {
        trace(FINER, sourceClass, sourceMethod, msg, null, null);
    }

    @Override
    public void finer(String sourceClass, String sourceMethod, String msg, Object[] inserts) {
        trace(FINER, sourceClass, sourceMethod, msg, inserts, null);
    }

    @Override
    public void finer(String sourceClass, String sourceMethod, String msg, Object[] inserts, Throwable ex) {
        trace(FINER, sourceClass, sourceMethod, msg, inserts, ex);
    }

    @Override
    public void finest(String sourceClass, String sourceMethod, String msg) {
        trace(FINEST, sourceClass, sourceMethod, msg, null, null);
    }

    @Override
    public void finest(String sourceClass, String sourceMethod, String msg, Object[] inserts) {
        trace(FINEST, sourceClass, sourceMethod, msg, inserts, null);
    }

    @Override
    public void finest(String sourceClass, String sourceMethod, String msg, Object[] inserts, Throwable ex) {
        trace(FINEST, sourceClass, sourceMethod, msg, inserts, ex);
    }

    @Override
    public void trace(int level, String sourceClass, String sourceMethod, String msg, Object[] inserts, Throwable thrown) {
        String s = String.format(Locale.CHINA,
                "%d, class: %s, method: %s, msg: %s, inserts: %s, throwable: %s",
                level,
                clipClassName(sourceClass),
                sourceMethod, msg,
                s(inserts),
                thrown == null ? "null" : thrown.toString());
        if (BuildConfig.DEBUG) {
            Log.i(TAG, s);
        }
    }

    @Override
    public String formatMessage(String msg, Object[] inserts) {
        return null;
    }

    @Override
    public void dumpTrace() {

    }


}
