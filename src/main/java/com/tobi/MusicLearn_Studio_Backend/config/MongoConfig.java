package com.tobi.MusicLearn_Studio_Backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.tobi.MusicLearn_Studio_Backend.modules.*.repository")
@EnableMongoAuditing
public class MongoConfig {
    // MongoDB configuration with auditing enabled
}
