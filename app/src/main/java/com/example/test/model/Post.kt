package com.example.test.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = false) var roomId: Long,
    @field:SerializedName("id") val id: Long,
    @field:SerializedName("title") val name: String,
    @field:SerializedName("thumbnailUrl") val thumbnail: String
) : Parcelable