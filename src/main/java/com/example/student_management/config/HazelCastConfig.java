package com.example.student_management.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.ManagementCenterConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelCastConfig {
    @Bean
    public Config hazelCastConfigurations() {
        Config config = new Config();
        config.setInstanceName("hazelcast-instance");
//        ManagementCenterConfig managementCenterConfig = new ManagementCenterConfig();
//        managementCenterConfig.setScriptingEnabled(true);
//
//        config.setManagementCenterConfig(managementCenterConfig);
        return config;
    }

    @Bean
    public HazelcastInstance hazelcastInstance(Config config) {
        return Hazelcast.newHazelcastInstance(config);
    }
}
