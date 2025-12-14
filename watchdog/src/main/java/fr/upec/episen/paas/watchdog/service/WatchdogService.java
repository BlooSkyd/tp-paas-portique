package fr.upec.episen.paas.watchdog.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WatchdogService {

    private static final Logger log = LogManager.getLogger(WatchdogService.class);
    private final List<String> cores;
    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedDelay = 5000)
    public void monitor() {
        for (String host : cores) {
            String port = System.getenv("CORE_PORT");
            String container = System.getenv("CORE_NAME");

            String url = "http://" + host + ":" + port + "/admin/health";

            if (isUp(url)) {
                log.info("{} is UP", host);
            } else {
                log.warn("{} is DOWN → restarting container", host);
                restartDocker(host, container);
            }
        }
    }

    private boolean isUp(String url) {
        try {
            restTemplate.getForEntity(url, String.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void restartDocker(String host, String container) {
    try {
        String privateKeyPath = System.getenv("SSH_PRIVATE_KEY");
        if (privateKeyPath == null) {
            log.error("SSH_PRIVATE_KEY not set");
            return;
        }

        JSch jsch = new JSch();
        jsch.addIdentity(privateKeyPath);

        Session session = jsch.getSession(System.getenv("SSH_USER"), host, 22);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(3000);

        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        channel.setCommand("docker restart " + container);
        channel.setErrStream(System.err);
        channel.connect();

        while (!channel.isClosed()) {
            Thread.sleep(100);
        }

        log.info("Restart {} on {} exit code {}", container, host, channel.getExitStatus());

        channel.disconnect();
        session.disconnect();
    } catch (Exception e) {
        log.error("SSH restart failed for " + host, e);
    }
}

}
