package com.jkamsker.modemsim.replay;

import com.jkamsker.modemsim.parser.RawBytes;
import com.jkamsker.modemsim.session.HeadlessSession;

import java.util.List;

public final class ReplayValidator {
    public ReplayReport validateRecompute(HeadlessSession session, List<ReplayStep> steps) {
        ReplayReport report = new ReplayReport();
        for (int i = 0; i < steps.size(); i++) {
            ReplayStep step = steps.get(i);
            RawBytes output = session.receive(step.input()).output();
            if (step.drainScheduled()) {
                output = output.append(session.drainScheduled().output());
            }
            if (!output.toHex().equals(step.expectedOutput().toHex())) {
                report.divergence("step " + (i + 1) + " expected "
                        + step.expectedOutput().toHex() + " but got " + output.toHex());
            }
        }
        return report;
    }
}
