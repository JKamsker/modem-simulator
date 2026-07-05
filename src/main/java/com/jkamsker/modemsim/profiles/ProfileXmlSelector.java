package com.jkamsker.modemsim.profiles;

import com.jkamsker.modemsim.validation.Dom;
import org.w3c.dom.Element;

import java.util.List;

final class ProfileXmlSelector {
    private ProfileXmlSelector() {
    }

    static Element single(Element root) {
        List<Element> profiles = Dom.children(root, "profile");
        if (profiles.size() != 1) {
            throw new IllegalArgumentException("Profile XML contains multiple profiles; select an explicit profile id");
        }
        return profiles.getFirst();
    }

    static Element byId(Element root, String profileId) {
        return Dom.children(root, "profile").stream()
                .filter(profile -> profile.getAttribute("id").equals(profileId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Profile id not found: " + profileId));
    }
}
