package db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by macbook on 2/11/16.
 */
@ModelContainer
@Table(database = LocalEstatePropertiesDB.class)
public class PropertyVisits extends BaseModel {

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

    @Column(name = "advert_appointment_time_date")
    public String advert_appointment_time_date;

    @Column(name = "advert_appointment_time_hour")
    public String advert_appointment_time_hour;

    @Column(name = "advert_phone_number")
    public String advert_phone_number;

    @Column(name = "advert_additional_address")
    public String advert_additional_address;

}
