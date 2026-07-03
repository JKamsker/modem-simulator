package com.jkamsker.modemsim.state;

public record SessionSettings(
        boolean echo,
        boolean quiet,
        boolean verbose,
        int cmee,
        int s0,
        int s2,
        int s3,
        int s4,
        int s5,
        int s6,
        int s7,
        int s8,
        int s12,
        int ampD,
        int ampC
) {
    public static SessionSettings defaults() {
        return new SessionSettings(false, false, true, 0, 0, 43, 13, 10, 8, 2, 60, 2, 50, 2, 1);
    }

    public SessionSettings withEcho(boolean value) {
        return new SessionSettings(value, quiet, verbose, cmee, s0, s2, s3, s4, s5, s6, s7, s8, s12, ampD, ampC);
    }

    public SessionSettings withQuiet(boolean value) {
        return new SessionSettings(echo, value, verbose, cmee, s0, s2, s3, s4, s5, s6, s7, s8, s12, ampD, ampC);
    }

    public SessionSettings withVerbose(boolean value) {
        return new SessionSettings(echo, quiet, value, cmee, s0, s2, s3, s4, s5, s6, s7, s8, s12, ampD, ampC);
    }

    public SessionSettings withCmee(int value) {
        return new SessionSettings(echo, quiet, verbose, value, s0, s2, s3, s4, s5, s6, s7, s8, s12, ampD, ampC);
    }

    public SessionSettings withRegister(int register, int value) {
        return switch (register) {
            case 0 -> copy(value, s2, s3, s4, s5, s6, s7, s8, s12);
            case 2 -> copy(s0, value, s3, s4, s5, s6, s7, s8, s12);
            case 3 -> copy(s0, s2, value, s4, s5, s6, s7, s8, s12);
            case 4 -> copy(s0, s2, s3, value, s5, s6, s7, s8, s12);
            case 5 -> copy(s0, s2, s3, s4, value, s6, s7, s8, s12);
            case 6 -> copy(s0, s2, s3, s4, s5, value, s7, s8, s12);
            case 7 -> copy(s0, s2, s3, s4, s5, s6, value, s8, s12);
            case 8 -> copy(s0, s2, s3, s4, s5, s6, s7, value, s12);
            case 12 -> copy(s0, s2, s3, s4, s5, s6, s7, s8, value);
            default -> this;
        };
    }

    public SessionSettings withAmpD(int value) {
        return new SessionSettings(echo, quiet, verbose, cmee, s0, s2, s3, s4, s5, s6, s7, s8, s12, value, ampC);
    }

    public SessionSettings withAmpC(int value) {
        return new SessionSettings(echo, quiet, verbose, cmee, s0, s2, s3, s4, s5, s6, s7, s8, s12, ampD, value);
    }

    public int register(int number) {
        return switch (number) {
            case 0 -> s0;
            case 2 -> s2;
            case 3 -> s3;
            case 4 -> s4;
            case 5 -> s5;
            case 6 -> s6;
            case 7 -> s7;
            case 8 -> s8;
            case 12 -> s12;
            default -> 0;
        };
    }

    private SessionSettings copy(int nextS0, int nextS2, int nextS3, int nextS4, int nextS5,
                                 int nextS6, int nextS7, int nextS8, int nextS12) {
        return new SessionSettings(echo, quiet, verbose, cmee, nextS0, nextS2, nextS3, nextS4,
                nextS5, nextS6, nextS7, nextS8, nextS12, ampD, ampC);
    }
}
