package com.jkamsker.modemsim.state;

public record SessionSettings(
        boolean echo,
        boolean quiet,
        boolean verbose,
        int cmee,
        int s3,
        int s4,
        int s5,
        int s7,
        int s12,
        int ampD,
        int ampC
) {
    public static SessionSettings defaults() {
        return new SessionSettings(false, false, true, 0, 13, 10, 8, 60, 1000, 2, 1);
    }

    public SessionSettings withEcho(boolean value) {
        return new SessionSettings(value, quiet, verbose, cmee, s3, s4, s5, s7, s12, ampD, ampC);
    }

    public SessionSettings withQuiet(boolean value) {
        return new SessionSettings(echo, value, verbose, cmee, s3, s4, s5, s7, s12, ampD, ampC);
    }

    public SessionSettings withVerbose(boolean value) {
        return new SessionSettings(echo, quiet, value, cmee, s3, s4, s5, s7, s12, ampD, ampC);
    }

    public SessionSettings withCmee(int value) {
        return new SessionSettings(echo, quiet, verbose, value, s3, s4, s5, s7, s12, ampD, ampC);
    }

    public SessionSettings withRegister(int register, int value) {
        return switch (register) {
            case 3 -> new SessionSettings(echo, quiet, verbose, cmee, value, s4, s5, s7, s12, ampD, ampC);
            case 4 -> new SessionSettings(echo, quiet, verbose, cmee, s3, value, s5, s7, s12, ampD, ampC);
            case 5 -> new SessionSettings(echo, quiet, verbose, cmee, s3, s4, value, s7, s12, ampD, ampC);
            case 7 -> new SessionSettings(echo, quiet, verbose, cmee, s3, s4, s5, value, s12, ampD, ampC);
            case 12 -> new SessionSettings(echo, quiet, verbose, cmee, s3, s4, s5, s7, value, ampD, ampC);
            default -> this;
        };
    }

    public int register(int number) {
        return switch (number) {
            case 3 -> s3;
            case 4 -> s4;
            case 5 -> s5;
            case 7 -> s7;
            case 12 -> s12;
            default -> 0;
        };
    }
}
