package com.example.skillcinema.data.db.dto.film

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.skillcinema.entity.film.Episode

@Entity(tableName = "episodes_by_film")
data class EpisodeLocalDto(
    @PrimaryKey @ColumnInfo(name = "table_id")
    val tableId: Int? = null,
    @ColumnInfo(name = "kinopoisk_id")
    val kinopoiskId: Int,
    @ColumnInfo(name = "season_number")
    val seasonNumber: Int,
    @ColumnInfo(name = "episode_number")
    val episodeNumber: Int,
    @ColumnInfo(name = "name_ru")
    val nameRu: String? = null,
    @ColumnInfo(name = "name_en")
    val nameEn: String? = null,
    @ColumnInfo(name = "synopsis")
    val synopsis: String? = null,
    @ColumnInfo(name = "release_date")
    val releaseDate: String? = null
) {
    constructor(kinopoiskId: Int, episode: Episode) : this(
        kinopoiskId = kinopoiskId,
        seasonNumber = episode.seasonNumber,
        episodeNumber = episode.episodeNumber,
        nameRu = episode.nameRu,
        nameEn = episode.nameEn,
        synopsis = episode.synopsis,
        releaseDate = episode.releaseDate
    )

    fun toEpisode(): Episode = object : Episode() {
        override val seasonNumber = this@EpisodeLocalDto.seasonNumber
        override val episodeNumber = this@EpisodeLocalDto.episodeNumber
        override val nameRu = this@EpisodeLocalDto.nameRu
        override val nameEn = this@EpisodeLocalDto.nameEn
        override val synopsis = this@EpisodeLocalDto.synopsis
        override val releaseDate = this@EpisodeLocalDto.releaseDate
    }
}
