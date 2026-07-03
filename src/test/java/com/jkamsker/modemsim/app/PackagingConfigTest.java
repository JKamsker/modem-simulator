package com.jkamsker.modemsim.app;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class PackagingConfigTest {
    @Test
    void launchersEnableNativeAccessForClasspathSerialLibraries() throws Exception {
        String unix = Files.readString(Path.of("src/main/scripts/modemsim"));
        String windows = Files.readString(Path.of("src/main/scripts/modemsim.cmd"));

        assertThat(unix).contains("--enable-native-access=ALL-UNNAMED");
        assertThat(windows).contains("--enable-native-access=ALL-UNNAMED");
    }

    @Test
    void assemblyIncludesLaunchersAndRuntimeLibraries() throws Exception {
        String assembly = Files.readString(Path.of("src/main/assembly/dist.xml"));

        assertThat(assembly).contains("src/main/scripts/modemsim");
        assertThat(assembly).contains("src/main/scripts/modemsim.cmd");
        assertThat(assembly).contains("${project.build.directory}/dependency");
        assertThat(assembly).contains("${project.build.finalName}.jar");
    }
}
