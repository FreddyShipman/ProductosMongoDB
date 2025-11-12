package config;

/**
 *
 * @author alfre
 */

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.MongoClientSettings;

public class MongoClientProvider { 
    private static MongoClientProvider instance;
    private final MongoClient mongoClient;
    private final MongoDatabase database;

    private MongoClientProvider() {
        MongoClientSettings settings = MongoConfig.createClientSettings();
        this.mongoClient = MongoClients.create(settings);
        this.database = this.mongoClient.getDatabase(MongoConfig.DB_NAME);
    }

    public static synchronized MongoClientProvider getInstance() {
        if (instance == null) {
            instance = new MongoClientProvider();
        }
        return instance;
    }

    public MongoDatabase getDefaultDatabase() {
        return this.database;
    }

    public <T> MongoCollection<T> getCollection(String collectionName, Class<T> clazz) {
        return this.database.getCollection(collectionName, clazz);
    }

    public void close() {
        this.mongoClient.close();
    }
}