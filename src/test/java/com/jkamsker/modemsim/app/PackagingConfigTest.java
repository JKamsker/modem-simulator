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
        String guiUnix = Files.readString(Path.of("src/main/scripts/modemsim-gui"));
        String guiWindows = Files.readString(Path.of("src/main/scripts/modemsim-gui.cmd"));

        assertThat(unix).contains("--enable-native-access=ALL-UNNAMED");
        assertThat(windows).contains("--enable-native-access=ALL-UNNAMED");
        assertThat(guiUnix).contains("--enable-native-access=ALL-UNNAMED");
        assertThat(guiWindows).contains("--enable-native-access=ALL-UNNAMED");
        assertThat(unix).contains("-Dmodemsim.home=");
        assertThat(windows).contains("-Dmodemsim.home=");
        assertThat(guiUnix).contains("com.jkamsker.modemsim.gui.SimulatorLauncher");
        assertThat(guiWindows).contains("com.jkamsker.modemsim.gui.SimulatorLauncher");
    }

    @Test
    void assemblyIncludesLaunchersAndRuntimeLibraries() throws Exception {
        String assembly = Files.readString(Path.of("src/main/assembly/dist.xml"));

        assertThat(assembly).contains("src/main/scripts/modemsim");
        assertThat(assembly).contains("src/main/scripts/modemsim.cmd");
        assertThat(assembly).contains("src/main/scripts/modemsim-gui");
        assertThat(assembly).contains("src/main/scripts/modemsim-gui.cmd");
        assertThat(assembly).contains("${project.build.directory}/dependency");
        assertThat(assembly).contains("${project.build.finalName}.jar");
        assertThat(assembly).contains("${project.basedir}/docs");
        assertThat(assembly).contains("<exclude>Tasks/Initial-Spec/references/downloads/**</exclude>");
        assertThat(assembly).contains("<exclude>Tasks/Initial-Spec/Review/**</exclude>");
        assertThat(assembly).contains("${project.basedir}/src/test/resources");
    }

    @Test
    void pomCarriesRuntimeAndReleaseMetadata() throws Exception {
        String pom = Files.readString(Path.of("pom.xml"));

        assertThat(pom).contains("jackson-dataformat-xml");
        assertThat(pom).contains("<organization>");
        assertThat(pom).contains("<name>JKamsker</name>");
        assertThat(pom).contains("<scm>");
        assertThat(pom).contains("<Implementation-Vendor>JKamsker</Implementation-Vendor>");
    }
}
