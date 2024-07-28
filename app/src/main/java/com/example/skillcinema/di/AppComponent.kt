package com.example.skillcinema.di

import com.example.skillcinema.presentation.dialogs.collections.CollectionsDialogViewModel
import com.example.skillcinema.presentation.welcomeActivity.WelcomeViewModelFactory
import com.example.skillcinema.presentation.screen.episodesFromSerial.EpisodesFromSerialViewModel
import com.example.skillcinema.presentation.screen.filmInfo.FilmInfoViewModel
import com.example.skillcinema.presentation.screen.filmList.FilmListViewModel
import com.example.skillcinema.presentation.screen.filmsFromCollection.FilmsFromCollectionViewModel
import com.example.skillcinema.presentation.screen.filmsFromStaff.FilmsFromStaffViewModel
import com.example.skillcinema.presentation.screen.galleryFromFilm.GalleryFromFilmViewModel
import com.example.skillcinema.presentation.screen.galleryFromFilm.galleryPagerFragment.GalleryPagerViewModel
import com.example.skillcinema.presentation.screen.main.MainViewModel
import com.example.skillcinema.presentation.screen.profile.ProfileViewModelFactory
import com.example.skillcinema.presentation.screen.search.SearchViewModelFactory
import com.example.skillcinema.presentation.screen.staffInfo.StaffInfoViewModel
import com.example.skillcinema.presentation.screen.staffList.StaffListViewModel
import dagger.Component

@Component(
    modules = [
        RetrofitModule::class,
        DatabaseModule::class,
        MemoryStorageModule::class,
        SharedPreferencesModule::class
    ]
)
interface AppComponent {
    fun welcomeViewModelFactory(): WelcomeViewModelFactory

    fun mainViewModelFactory(): MainViewModel.Factory

    fun searchViewModelFactory(): SearchViewModelFactory

    fun profileViewModelFactory(): ProfileViewModelFactory

    fun filmInfoViewModelFactory(): FilmInfoViewModel.Factory

    fun filmListViewModelFactory(): FilmListViewModel.Factory

    fun staffListViewModelFactory(): StaffListViewModel.Factory

    fun staffInfoViewModelFactory(): StaffInfoViewModel.Factory

    fun filmsFromStaffViewModelFactory(): FilmsFromStaffViewModel.Factory

    fun episodesFromSerialViewModelFactory(): EpisodesFromSerialViewModel.Factory

    fun galleryFromFilmViewModelFactory(): GalleryFromFilmViewModel.Factory

    fun galleryPagerViewModelFactory(): GalleryPagerViewModel.Factory

    fun filmsFromCollectionViewModelFactory(): FilmsFromCollectionViewModel.Factory

    fun collectionsDialogViewModelFactory(): CollectionsDialogViewModel.Factory
}