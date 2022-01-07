package com.mohamedsayed.picsumphotoviewer.model.objects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "images")
data class PicsumImage(
    @PrimaryKey
    @SerializedName("id") var id: String = "",
    @SerializedName("author") var author: String? = null,
    @SerializedName("download_url") var downloadUrl: String? = null
)