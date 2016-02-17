package db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by macbook on 2/2/16.
 */
@Database(name = LocalEstatePropertiesDB.NAME, version = LocalEstatePropertiesDB.VERSION, generatedClassSeparator = "_")
public class LocalEstatePropertiesDB {

    public static final String NAME = "LocalEstateDB";

    public static final int VERSION = 1;
}
