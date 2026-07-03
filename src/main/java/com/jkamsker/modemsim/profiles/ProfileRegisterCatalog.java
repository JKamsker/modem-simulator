package com.jkamsker.modemsim.profiles;

import com.jkamsker.modemsim.state.SessionSettings;

import java.util.List;
import java.util.Set;

public final class ProfileRegisterCatalog {
    private static final Set<Integer> SUPPORTED = Set.of(0, 2, 3, 4, 5, 6, 7, 8, 12);

    private ProfileRegisterCatalog() {
    }

    public static ProfileRegister find(Profile profile, int number) {
        for (ProfileRegister register : profile.registers()) {
            Integer parsed = number(register.name());
            if (parsed != null && parsed == number) {
                return register;
            }
        }
        return null;
    }

    public static boolean accepts(ProfileRegister register, int value) {
        return (register.min() == null || value >= register.min())
                && (register.max() == null || value <= register.max());
    }

    public static boolean supports(ProfileRegister register) {
        Integer parsed = number(register.name());
        return parsed != null && SUPPORTED.contains(parsed);
    }

    public static SessionSettings applyDefaults(SessionSettings settings, List<ProfileRegister> registers) {
        SessionSettings result = settings;
        for (ProfileRegister register : registers) {
            Integer parsed = number(register.name());
            if (parsed != null) {
                result = result.withRegister(parsed, register.defaultValue());
            }
        }
        return result;
    }

    static Integer number(String name) {
        if (name == null || !name.matches("S[0-9]+")) {
            return null;
        }
        return Integer.valueOf(name.substring(1));
    }
}
