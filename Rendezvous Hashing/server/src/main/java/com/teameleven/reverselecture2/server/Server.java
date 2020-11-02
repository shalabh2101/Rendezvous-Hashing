package com.teameleven.reverselecture2.server;

import com.teameleven.reverselecture2.server.api.Controller;
import com.teameleven.reverselecture2.server.entities.Entry;
import com.teameleven.reverselecture2.server.repository.ChronicleMapService;
import com.teameleven.reverselecture2.server.repository.ServiceInterface;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import net.openhft.chronicle.map.ChronicleMapBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;

public class Server extends Service<Configuration> {

    private static String serverName;

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws Exception {
        serverName = args[1];
        serverName = serverName.replace(".yml", "");
        serverName = serverName.replace("/Users/mitashgod/Downloads/CMPE-273-Lab-3-master/server/config/", "");
        serverName = serverName.replace("_config", "");

        new Server().run(args);
    }

    private Map<Long, Entry> createChronicleMap() {
        Map<Long, Entry> builder;
        File temp = new File("/Users/mitashgod/Downloads/" + serverName + ".txt");

        try {
            builder = ChronicleMapBuilder.of(Long.class, Entry.class).createPersistedTo(temp);
            return builder;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
        bootstrap.setName("server");
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        Map<Long, Entry> map = createChronicleMap();
        ServiceInterface cache = new ChronicleMapService(map);
        environment.addResource(new Controller(cache));
        LOGGER.info("Resources loaded.");
    }
}
