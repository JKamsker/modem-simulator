package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.transport.SerialConfig;
import com.jkamsker.modemsim.monitor.EventSink;
import com.jkamsker.modemsim.session.HeadlessSession;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public final class RuntimeSessionLauncher {
    public RuntimeHandle start(Options options) {
        return start(options, null, session -> { });
    }

    public RuntimeHandle start(Options options, EventSink eventSink, Consumer<HeadlessSession> sessionReady) {
        if (!options.hasMainPort()) {
            return RuntimeHandle.noop();
        }
        AtomicReference<Throwable> failure = new AtomicReference<>();
        AtomicReference<HeadlessSession> session = new AtomicReference<>();
        BlockingQueue<com.jkamsker.modemsim.parser.RawBytes> dceWrites = new LinkedBlockingQueue<>();
        CountDownLatch started = new CountDownLatch(1);
        Thread thread = new Thread(() -> run(options, eventSink, sessionReady, session, failure, started, dceWrites),
                "modemsim-gui-runtime");
        thread.setDaemon(true);
        thread.start();
        RuntimeHandle handle = new RuntimeHandle(thread, failure, session, started, dceWrites);
        handle.awaitStarted();
        return handle;
    }

    private void run(
            Options options, EventSink eventSink, Consumer<HeadlessSession> sessionReady,
            AtomicReference<HeadlessSession> session, AtomicReference<Throwable> failure,
            CountDownLatch started, BlockingQueue<com.jkamsker.modemsim.parser.RawBytes> dceWrites) {
        try {
            new ModemRuntime().run(config(options), List.of(), -1, eventSink, active -> {
                session.set(active);
                sessionReady.accept(active);
                started.countDown();
            }, dceWrites);
        } catch (Throwable t) {
            failure.set(t);
        } finally {
            started.countDown();
            sessionReady.accept(null);
        }
    }

    private RuntimeConfig config(Options options) {
        return new RuntimeConfig(
                options.seed(),
                com.jkamsker.modemsim.scheduler.ClockMode.MONOTONIC,
                false,
                options.allowUnsafeDceTransmit(),
                null,
                options.initialScenario(),
                null,
                List.of(),
                options.serialLine(),
                ports(options));
    }

    private List<PortBinding> ports(Options options) {
        return List.of(
                new PortBinding("gui-main", EndpointType.SERIAL, PortRole.MODEM_SIMULATION,
                        options.mainPort(), true, options.profile(), options.initialScenario(), "tagged-text"),
                new PortBinding("gui-sniffer", EndpointType.SERIAL, PortRole.SNIFFER,
                        blank(options.snifferPort()) ? null : options.snifferPort(),
                        !blank(options.snifferPort()), options.profile(), null, "tagged-text"),
                new PortBinding("gui-manual-dce", EndpointType.SERIAL, PortRole.MANUAL_DCE_INJECTION,
                        blank(options.manualDcePort()) ? null : options.manualDcePort(),
                        !blank(options.manualDcePort()), options.profile(), null, "tagged-text"));
    }

    private boolean blank(String value) {
        return value == null || value.isBlank();
    }

    public record Options(
            String profile,
            String mainPort,
            String snifferPort,
            String manualDcePort,
            Path initialScenario,
            SerialConfig serialLine,
            long seed,
            boolean allowUnsafeDceTransmit
    ) {
        boolean hasMainPort() {
            return mainPort != null && !mainPort.isBlank() && !mainPort.equalsIgnoreCase("headless");
        }
    }

    public static final class RuntimeHandle implements AutoCloseable {
        private final Thread thread;
        private final AtomicReference<Throwable> failure;
        private final AtomicReference<HeadlessSession> session;
        private final CountDownLatch started;
        private final BlockingQueue<com.jkamsker.modemsim.parser.RawBytes> dceWrites;

        private RuntimeHandle(
            Thread thread, AtomicReference<Throwable> failure,
                AtomicReference<HeadlessSession> session, CountDownLatch started,
                BlockingQueue<com.jkamsker.modemsim.parser.RawBytes> dceWrites) {
            this.thread = thread;
            this.failure = failure;
            this.session = session;
            this.started = started;
            this.dceWrites = dceWrites;
        }

        static RuntimeHandle noop() {
            return new RuntimeHandle(null, new AtomicReference<>(), new AtomicReference<>(), new CountDownLatch(0), new LinkedBlockingQueue<>());
        }

        public boolean running() {
            return thread != null && thread.isAlive();
        }

        public Throwable failure() {
            return failure.get();
        }

        public HeadlessSession session() {
            return session.get();
        }

        public void writeDce(com.jkamsker.modemsim.parser.RawBytes bytes) {
            if (running()) {
                dceWrites.offer(bytes);
            }
        }

        private void awaitStarted() {
            try {
                started.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        @Override
        public void close() {
            if (thread == null) {
                return;
            }
            thread.interrupt();
            try {
                thread.join(250);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
