package com.jkamsker.modemsim.macros;

import com.jkamsker.modemsim.profiles.Profile;

public record MacroProfilePredicate(String id, String status) {
    public boolean matches(Profile profile) {
        boolean idMatches = id == null || id.isBlank() || id.equals(profile.id());
        boolean statusMatches = status == null || status.isBlank() || status.equals(profile.status());
        return idMatches && statusMatches;
    }
}
