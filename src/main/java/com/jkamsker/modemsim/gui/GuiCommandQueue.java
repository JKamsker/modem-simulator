package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.session.SessionResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final class GuiCommandQueue {
    private final ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r, "modemsim-gui-session-queue");
        thread.setDaemon(true);
        return thread;
    });

    SessionResponse submit(GuiSessionCommand command) {
        return submitAsync(command).join();
    }

    CompletableFuture<SessionResponse> submitAsync(GuiSessionCommand command) {
        return CompletableFuture.supplyAsync(command.action(), executor);
    }
}
