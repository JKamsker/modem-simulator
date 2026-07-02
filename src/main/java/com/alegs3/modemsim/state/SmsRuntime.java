package com.alegs3.modemsim.state;

public record SmsRuntime(boolean textMode, String smsc, SmsStorage storage, int nextMessageReference) {
    public static SmsRuntime defaults() {
        return new SmsRuntime(true, "+491710760000", SmsStorage.ME, 42);
    }

    public SmsRuntime withTextMode(boolean value) {
        return new SmsRuntime(value, smsc, storage, nextMessageReference);
    }

    public SmsRuntime withSmsc(String value) {
        return new SmsRuntime(textMode, value, storage, nextMessageReference);
    }

    public SmsRuntime withStorage(SmsStorage value) {
        return new SmsRuntime(textMode, smsc, value, nextMessageReference);
    }

    public SmsRuntime incrementReference() {
        return new SmsRuntime(textMode, smsc, storage, nextMessageReference + 1);
    }
}
