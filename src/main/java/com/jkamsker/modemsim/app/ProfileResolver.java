package com.jkamsker.modemsim.app;

import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.profiles.Profile;
import com.jkamsker.modemsim.profiles.ProfileXmlLoader;

import java.nio.file.Files;
import java.nio.file.Path;

final class ProfileResolver {
    Profile resolve(String profile) {
        if (profile == null || profile.isBlank()) {
            return BuiltinProfiles.acceptanceSierra();
        }
        Path path = Path.of(profile);
        if (Files.exists(path)) {
            return new ProfileXmlLoader().load(path);
        }
        return BuiltinProfiles.byId(profile);
    }
}
