package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.profiles.ProfileXmlLoader;

import java.nio.file.Files;
import java.nio.file.Path;

final class ProfileResolver {
    Profile resolve(String profile) {
        if (profile == null || profile.equals(BuiltinProfiles.acceptanceSierra().id())) {
            return BuiltinProfiles.acceptanceSierra();
        }
        if (profile.equals(BuiltinProfiles.westermoTd22().id())) {
            return BuiltinProfiles.westermoTd22();
        }
        Path path = Path.of(profile);
        if (Files.exists(path)) {
            return new ProfileXmlLoader().load(path);
        }
        throw new IllegalArgumentException("Unknown profile: " + profile);
    }
}
