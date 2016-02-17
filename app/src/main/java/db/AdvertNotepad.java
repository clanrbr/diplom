package db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by macbook on 2/2/16.
 */
@ModelContainer
@Table(database = LocalEstatePropertiesDB.class)
public class AdvertNotepad extends BaseModel {

    @PrimaryKey(autoincrement = true)
    @Column(name = "advert_auto_id")
    public long advert_auto_id;

    @Column(name = "advert_id")
    public String advert_id;

    @Column(name = "advert_list")
    public String advert_list;

    @Column(name = "advert_note")
    public String advert_note;

    @Column(name = "added_time")
    public long advert_time;

    @Column(name = "advert_version_time")
    public long advert_version_time;

    @Column(name = "order")
    public int order;
}