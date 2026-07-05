package com.jkamsker.modemsim.gui;

import com.jkamsker.modemsim.session.SessionResponse;

record ReplaySummary(String hashStatus, String message, SessionResponse response) {
}
