package com.mohamedsayed.picsumphotoviewer.model.objects

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "images")
data class PicsumImage(
    @PrimaryKey
    @SerializedName("id") var id: String = "",
    @SerializedName("author") var author: String? = null,
    @SerializedName("download_url") var downloadUrl: String? = null
) {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is PicsumImage -> {
                this.id == other.id &&
                        this.author == other.author &&
                        this.downloadUrl == other.downloadUrl
            }
            else -> false
        }
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}