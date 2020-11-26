package io.github.abdulwahabo.filebox.util;

/**
 * Helper that suppliers constants which are used more than once. This ensures that the values can be easily
 * refactored.
 */
public class Constants {
    public static final String OAUTH_STATE_CACHE = "oauth_state_cache";
    public static final String OAUTH_CALLBACK_URL = "/auth/callback";
    public static final String COOKIE_NAME = "filebox-session";
    public static String COOKIE_CACHE = "session_cookie_cache";
}
