package com.tencent.matrix.batterycanary;

import android.app.Application;

import com.tencent.matrix.AppActiveMatrixDelegate;
import com.tencent.matrix.batterycanary.monitor.BatteryMonitorConfig;
import com.tencent.matrix.batterycanary.monitor.BatteryMonitorCore;
import com.tencent.matrix.plugin.Plugin;
import com.tencent.matrix.plugin.PluginListener;
import com.tencent.matrix.util.MatrixUtil;

public class BatteryMonitorPlugin extends Plugin {
    final BatteryMonitorCore mDelegate;
    private static String sPackageName = null;
    private static String sProcessName = null;

    public BatteryMonitorPlugin(BatteryMonitorConfig config) {
        mDelegate = new BatteryMonitorCore(config);
    }

    public BatteryMonitorCore core() {
        return mDelegate;
    }

    @Override
    public void init(Application app, PluginListener listener) {
        super.init(app, listener);
        if (!mDelegate.getConfig().isBuiltinForegroundNotifyEnabled) {
            AppActiveMatrixDelegate.INSTANCE.removeListener(this);
        }
    }

    @Override
    public String getTag() {
        return "BatteryMonitorPlugin";
    }

    @Override
    public void start() {
        super.start();
        mDelegate.start();
    }

    @Override
    public void stop() {
        super.stop();
        mDelegate.stop();
    }

    @Override
    public void onForeground(boolean isForeground) {
        mDelegate.onForeground(isForeground);
    }

    @Override
    public boolean isForeground() {
        return mDelegate.isForeground();
    }

    public String getProcessName() {
        if (sProcessName == null) {
            synchronized (this) {
                if (sProcessName == null) {
                    Application application = getApplication();
                    if (application == null) {
                        throw new IllegalStateException(getTag() + " is not yet init!");
                    }
                    sProcessName = MatrixUtil.getProcessName(application);
                }
            }
        }
        return sProcessName;
    }

    public String getPackageName() {
        if (sPackageName == null) {
            synchronized (this) {
                if (sPackageName == null) {
                    Application application = getApplication();
                    if (application == null) {
                        throw new IllegalStateException(getTag() + " is not yet init!");
                    }
                    sPackageName = getApplication().getPackageName();
                }
            }
        }
        return sPackageName;
    }
}
