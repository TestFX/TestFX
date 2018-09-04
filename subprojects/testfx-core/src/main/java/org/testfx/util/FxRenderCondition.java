package org.testfx.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;

import javafx.application.Platform;
import javafx.stage.Window;

import org.testfx.internal.JavaVersionAdapter;

/**
 * This class counts the pulses on the Fx-Thread. A pulse is actually triggered,
 * when the scene graph is updated, just before the actual rendering happens.<br>
 * All methods except the constructor have to be called only on the Fx-Thread.
 */
public class FxRenderCondition implements BooleanSupplier {
    
    
    private boolean autoDetect;
    private int nWait;
    
    // access only on Fx-Thread
    final Map<Window, Integer> counters = new HashMap<>();
    final Map<Window, Runnable> listner = new HashMap<>();
    final Map<Window, Long> time = new HashMap<>(); // TODO#615 debug only
    
    
    transient boolean initialized;

    public FxRenderCondition(List<Window> windows, int n) {
        if (Platform.isFxApplicationThread()) {
            throw new RuntimeException("Not allowed to run on FxApplication Thread");
        }
        if (windows == null) {
            throw new RuntimeException("windows may not be null");
        }
        nWait = n;
        Platform.runLater(() -> {
            init(windows, n);
        });
    }
    
    public FxRenderCondition(int n) {
        if (Platform.isFxApplicationThread()) {
            throw new RuntimeException("Not allowed to run on FxApplication Thread");
        }
        autoDetect = true;
        nWait = n;
        Platform.runLater(() -> {
            init(null, n);
        });
    }
    
    // runs on Fx-Thread
    protected void init(List<Window> windows, int n) {
        if (!Platform.isFxApplicationThread()) {
            throw new RuntimeException("Not on FxApplication Thread");
        }
        if (autoDetect) {
            windows = JavaVersionAdapter.getWindows();
        }
        if (WaitForAsyncUtils.debugTestTiming) {
            System.out.println("number of windows to watch " + windows.size());
        }
        for (int i = 0; i < windows.size(); i++) {
            addWindow(windows.get(i));
        }
        initialized = true;
    }
    
    // runs on Fx-Thread
    protected void addWindow(Window w) {
        if (!Platform.isFxApplicationThread()) {
            throw new RuntimeException("Not on FxApplication Thread");
        }
        final int tmp = counters.size();
        final Runnable r = () -> { // runs on Fx-Thread, so safe to read/write
            int n = counters.get(w) - 1;
            counters.put(w, n);
            if (WaitForAsyncUtils.debugTestTiming) {
                System.out.println("Rendered counter[" + tmp + "] to " + n);
            }
            if (n < 1) {
                if (WaitForAsyncUtils.debugTestTiming && time.get(w) != null) {
                    System.out.println("Last frame took " + 
                            (System.currentTimeMillis() - time.get(w)) + " ms");
                }
                JavaVersionAdapter.removePulseListener(w, listner.get(w));
            } else {
                JavaVersionAdapter.requestPulse();
            }
            time.put(w, System.currentTimeMillis());
        };
        if (JavaVersionAdapter.addPulseListener(w, r)) {
            if (WaitForAsyncUtils.debugTestTiming) {
                System.out.println("Adding pulse listener (" + tmp + ")");
            }
            counters.put(w, nWait);
            listner.put(w, r);
        } else {
            if (WaitForAsyncUtils.debugTestTiming) {
                System.out.println("Can not add pulse listener (" + tmp + ")");
            }
            counters.put(w, 0);
        }
    }
    
    
    //running on FxThread
    @Override
    public boolean getAsBoolean() {
        if (!Platform.isFxApplicationThread()) {
            throw new RuntimeException("Not on FxApplication Thread");
        }
        if (!initialized) {
            return false;
        }
        if (counters == null || counters.size() == 0) {
            return true;
        }
        // ensure that components just added to show are rendered too
        if (autoDetect) {
            List<Window> windows = JavaVersionAdapter.getWindows();
            for (Window w : windows) {
                if (w.isShowing() && !counters.containsKey(w)) {
                    addWindow(w);
                }
            }
        }
        boolean ret = true;
        for (Window w : counters.keySet()) {
            if (w.isShowing() && counters.get(w) > 0) {
                ret = false;
                break;
            }
        }
        if (!ret) {
            // trigger pulse hangs in awt headless mode during clean up...
            if (WaitForAsyncUtils.debugTestTiming) {
                for (Window w : counters.keySet()) {
                    System.out.println("Window visibility: " + w.isShowing());
                }
            }
            JavaVersionAdapter.requestPulse(); // force rendering
        }
        // System.out.println("FxRenderCounter done="+ret);
        return ret;
    }

}
