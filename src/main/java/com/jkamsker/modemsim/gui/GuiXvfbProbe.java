package com.jkamsker.modemsim.gui;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

final class GuiXvfbProbe {
    private GuiXvfbProbe() {
    }

    static void run(String childClasspath) {
        Path outputFile = null;
        try {
            outputFile = Files.createTempFile("modemsim-xvfb-gui-probe", ".log");
            String java = Path.of(System.getProperty("java.home"), "bin", "java").toString();
            Process process = new ProcessBuilder("xvfb-run", "-a", java,
                    "-Dmodemsim.gui.acceptance.child=true", "-cp", childClasspath,
                    GuiAcceptanceHarness.class.getName(), "live-log-filter-export")
                    .redirectErrorStream(true)
                    .redirectOutput(outputFile.toFile())
                    .start();
            if (!process.waitFor(30, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                throw new IllegalStateException("xvfb JavaFX probe timed out: " + read(outputFile));
            }
            require(process.exitValue() == 0, "xvfb JavaFX probe failed: " + read(outputFile));
        } catch (java.io.IOException e) {
            throw new IllegalStateException("Cannot start xvfb JavaFX probe", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while running xvfb JavaFX probe", e);
        } finally {
            delete(outputFile);
        }
    }

    private static String read(Path outputFile) throws java.io.IOException {
        return Files.readString(outputFile, StandardCharsets.UTF_8);
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }

    private static void delete(Path outputFile) {
        if (outputFile == null) {
            return;
        }
        try {
            Files.deleteIfExists(outputFile);
        } catch (java.io.IOException ignored) {
            // Best-effort cleanup for a short-lived acceptance probe log.
        }
    }
}
