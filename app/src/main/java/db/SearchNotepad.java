package db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by macbook on 2/18/16.
 */
@ModelContainer
@Table(database = LocalEstatePropertiesDB.class)
public class SearchNotepad extends BaseModel {

    @PrimaryKey(autoincrement = true)
    @Column(name = "search_auto_id")
    public long search_auto_id;

    @Column(name = "search_title")
    public String search_title;

    @Column(name = "search_content")
    public String search_content;

    @Column(name = "search_note")
    public String search_note;

    @Column(name = "search_text_help_text")
    public String search_text_help_text;

    @Column(name = "search_add_time")
    public long search_add_time;
}