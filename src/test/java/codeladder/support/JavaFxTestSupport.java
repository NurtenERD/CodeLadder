package codeladder.support;

import javafx.application.Platform;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public final class JavaFxTestSupport {
    private static final AtomicBoolean STARTED = new AtomicBoolean(false);

    private JavaFxTestSupport() {
    }

    public static void initToolkit() {
        if (STARTED.get()) {
            return;
        }
        CountDownLatch latch = new CountDownLatch(1);
        if (STARTED.compareAndSet(false, true)) {
            Platform.startup(latch::countDown);
        } else {
            latch.countDown();
        }
        try {
            latch.await();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("JavaFX toolkit kon niet starten", exception);
        }
    }
}
