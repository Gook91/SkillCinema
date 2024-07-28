package com.example.skillcinema.data.db

import androidx.room.*
import com.example.skillcinema.data.db.dto.film.FilmLocalDto
import com.example.skillcinema.data.db.dto.film.EpisodeLocalDto
import com.example.skillcinema.entity.film.Episode
import com.example.skillcinema.entity.film.Season

@Dao
interface FilmsDao {

    // Добавление и удаление фильма

    @Insert
    suspend fun addFilm(filmLocalDto: FilmLocalDto)

    @Query("SELECT * FROM films WHERE kinopoisk_id=:kinopoiskId")
    suspend fun getFilm(kinopoiskId: Int): FilmLocalDto?

    // Работа с эпизодами сериалов

    @Insert
    @Transaction
    suspend fun addEpisodes(episodes: List<EpisodeLocalDto>)

    suspend fun addSeasonsFromFilm(kinopoiskId: Int, seasons: List<Season>) {
        val episodes = seasons
            .map { season -> season.episodes }
            .flatten()
            .map { episode -> EpisodeLocalDto(kinopoiskId, episode) }
        addEpisodes(episodes)
    }

    suspend fun getSeasonsByFilm(kinopoiskId: Int): List<Season>? {
        val episodeList = getEpisodesByFilm(kinopoiskId)
        return if (episodeList.isEmpty())
            null
        else
            episodeList
                .map { it.toEpisode() }
                .groupBy { it.seasonNumber }
                .map { entry ->
                    object : Season() {
                        override val number: Int = entry.key
                        override val episodes: List<Episode> = entry.value
                    }
                }
    }

    @Query("SELECT * FROM episodes_by_film WHERE kinopoisk_id=:kinopoiskId")
    suspend fun getEpisodesByFilm(kinopoiskId: Int): List<EpisodeLocalDto>
}