package com.jkamsker.modemsim.testkit;

import com.jkamsker.modemsim.profiles.BuiltinProfiles;
import com.jkamsker.modemsim.replay.ReplayStepLoader;
import com.jkamsker.modemsim.replay.ReplayValidator;
import com.jkamsker.modemsim.session.HeadlessSession;
import com.jkamsker.modemsim.validation.SchemaLocator;

import java.util.List;

final class AcceptanceHayesChecks {
    private static final String PROFILE_ID = "generic-hayes-v250";
    private static final List<String> TRANSCRIPT_FILES = List.of(
            "ate0.yaml",
            "ate1.yaml",
            "atq0.yaml",
            "atq1.yaml",
            "atv0.yaml",
            "atv1.yaml",
            "atz.yaml",
            "at-and-f.yaml",
            "at-and-v.yaml",
            "at-and-w.yaml");

    void run() {
        for (String file : TRANSCRIPT_FILES) {
            runTranscript(file);
        }
    }

    private void runTranscript(String file) {
        var path = SchemaLocator.projectPath("tests/golden/hayes-v250", file);
        var transcript = new ReplayStepLoader().loadTranscript(path);
        require(PROFILE_ID.equals(transcript.profile()), file + " must use " + PROFILE_ID);
        require("A02".equals(transcript.metadata().get("acceptanceCase")), file + " missing A02 metadata");
        var report = new ReplayValidator().validateRecompute(session(), transcript.steps());
        require(report.valid(), file + " diverged: " + report.divergences());
    }

    private HeadlessSession session() {
        return new HeadlessSession("acceptance-hayes", BuiltinProfiles.byId(PROFILE_ID), 12345);
    }

    private void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
