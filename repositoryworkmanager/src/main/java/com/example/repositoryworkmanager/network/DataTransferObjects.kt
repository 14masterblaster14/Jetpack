package com.example.repositoryworkmanager.network

import com.example.repositoryworkmanager.database.DatabaseVideo
import com.example.repositoryworkmanager.domain.DevByteVideo
import com.squareup.moshi.JsonClass

/**
 * DataTransferObjects go in this file. These are responsible for parsing responses from the server
 * or formatting objects to send to the server. You should convert these to domain objects before
 * using them.
 *
 * @see domain package for
 */

/**
 * VideoHolder holds a list of Videos.
 *
 * This is to parse first level of our network result which looks like
 *
 * {
 *   "videos": []
 * }
 */
@JsonClass(generateAdapter = true)
data class NetworkVideoContainer(val videos: List<NetworkVideo>)

/**
 * Videos represent a devbyte that can be played.
 */
@JsonClass(generateAdapter = true)
data class NetworkVideo(
    val title: String,
    val description: String,
    val url: String,
    val updated: String,
    val thumbnail: String,
    val closedCaptions: String?)

/**
 * Convert Network results to database objects
 * asDomainModel() is a convenience method to convert network results to a list of domain objects.
 * The data transfer objects are different from the domain objects, because they contain extra
 * logic for parsing network results.
 */
fun NetworkVideoContainer.asDomainModel(): List<DevByteVideo> {
    return videos.map {
        DevByteVideo(
            title = it.title,
            description = it.description,
            url = it.url,
            updated = it.updated,
            thumbnail = it.thumbnail)
    }
}

fun NetworkVideoContainer.asDatabaseModel():List<DatabaseVideo>{

    return videos.map{
        DatabaseVideo(
            title = it.title,
            description = it.description,
            url = it.url,
            updated = it.updated,
            thumbnail = it.thumbnail)
    }
}

/*
Tip: It's a best practice to separate the network, domain, and database objects.
This strategy follows the separation of concerns principle. If the network response or
the databaseschema changes, you want to be able to change and manage app components without
updating the entire app's code.
*/
