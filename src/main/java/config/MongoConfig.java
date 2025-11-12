package config;

/**
 *
 * @author alfre
 */

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

public class MongoConfig {
    public static final String URI = "mongodb://localhost:27017/";
    public static final String DB_NAME = "ProductosDB";

    public static MongoClientSettings createClientSettings() {
        CodecRegistry defaultCodecRegistry = MongoClientSettings.getDefaultCodecRegistry();
        CodecRegistry pojoCodecRegistry = fromProviders(
                PojoCodecProvider.builder().automatic(true).build()
        );

        CodecRegistry combinedRegistry = fromRegistries(
                defaultCodecRegistry, 
                pojoCodecRegistry
        );
        return MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(URI))
                .codecRegistry(combinedRegistry)
                .build();
    }
}