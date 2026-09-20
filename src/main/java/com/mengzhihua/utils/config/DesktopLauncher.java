package com.mengzhihua.utils.config;


import java.awt.Desktop;
import java.net.URI;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Desktop profile: bind localhost and open the office workbench in the system browser.
 */
@Component
public class DesktopLauncher {

    private static final Logger log = LoggerFactory.getLogger(DesktopLauncher.class);

    private final Environment environment;

    public DesktopLauncher(Environment environment) {
        this.environment = environment;
    }

    public static boolean requested(String[] args) {
        if (Boolean.parseBoolean(System.getProperty("utils.desktop", "false"))
                || Boolean.parseBoolean(System.getenv().getOrDefault("UTILS_DESKTOP", "false"))) {
            return true;
        }
        if (args == null) {
            return false;
        }
        for (String arg : args) {
            if ("--desktop".equals(arg) || "--desktop=true".equalsIgnoreCase(arg)) {
                return true;
            }
        }
        return false;
    }

    public static String homeUrl(int port) {
        return "http://127.0.0.1:" + port + "/#/office";
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        if (!isDesktop()) {
            return;
        }
        int port = environment.getProperty("local.server.port", Integer.class,
                environment.getProperty("server.port", Integer.class, 18765));
        String url = homeUrl(port);
        log.info("Desktop console: {}", url);
        if (!Boolean.parseBoolean(environment.getProperty("utils.desktop.open-browser", "true"))) {
            return;
        }
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI.create(url));
                return;
            }
        } catch (Exception ex) {
            log.warn("Desktop browse failed: {}", ex.toString());
        }
        openWithOs(url);
    }

    private boolean isDesktop() {
        if (requested(null) && environment.getActiveProfiles().length == 0) {
            return true;
        }
        for (String profile : environment.getActiveProfiles()) {
            if ("desktop".equalsIgnoreCase(profile)) {
                return true;
            }
        }
        return requested(null);
    }

    private static void openWithOs(String url) {
        String os = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        String[] command;
        if (os.contains("win")) {
            command = new String[] {"cmd", "/c", "start", "", url};
        } else if (os.contains("mac")) {
            command = new String[] {"open", url};
        } else {
            command = new String[] {"xdg-open", url};
        }
        try {
            new ProcessBuilder(command).inheritIO().start();
        } catch (Exception ex) {
            log.warn("Open browser command failed, open {} manually", url);
        }
    }
}
