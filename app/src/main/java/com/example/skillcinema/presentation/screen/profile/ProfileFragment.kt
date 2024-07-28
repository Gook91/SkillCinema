package com.example.skillcinema.presentation.screen.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.App
import com.example.skillcinema.databinding.FragmentProfileBinding
import com.example.skillcinema.entity.collections.TypeCollection
import com.example.skillcinema.presentation.dialogs.AddNewCollectionDialog
import com.example.skillcinema.presentation.lists.adapters.list.CollectionCardAdapter
import com.example.skillcinema.presentation.screen.abstractTemplates.AbstractFragmentWithErrorMessageAndInsets
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ProfileFragment : AbstractFragmentWithErrorMessageAndInsets() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override val viewModel: ProfileViewModel by viewModels {
        (requireContext().applicationContext as App).appComponent.profileViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewedFilmsInView()

        binding.createCollection.setOnClickListener { addNewCollectionFromDialog() }

        setCollectionsInView()

        setInterestedFilmsInView()
    }

    private fun setViewedFilmsInView() {
        binding.viewedCollection.ListWithLinkBuilder { kinopoiskId ->
            val action =
                ProfileFragmentDirections.actionProfileFragmentToFilmInfoFragment(kinopoiskId)
            findNavController().navigate(action)
        }.setOnClickLinkToAll {
            val action =
                ProfileFragmentDirections.actionProfileFragmentToFilmsFromCollectionFragment(
                    TypeCollection.Viewed
                )
            findNavController().navigate(action)
        }.setOnClickFooter {
            viewModel.clearViewedHistory()
        }.build()

        viewModel.countViewedFilmsFlow.filterNotNull().onEach { count ->
            binding.viewedCollection.setCountOnLinkToAll(count)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.viewedFilmsFlow.filterNotNull().onEach { filmList ->
            binding.viewedCollection.submitData(filmList)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun addNewCollectionFromDialog() {
        childFragmentManager.setFragmentResultListener(
            AddNewCollectionDialog.GET_NEW_COLLECTION_NAME_TAG,
            viewLifecycleOwner
        ) { _, bundle ->
            val name = bundle.getString(AddNewCollectionDialog.RETURN_NAME_COLLECTION_TAG)
            name?.let { viewModel.addCollection(name) }
        }
        AddNewCollectionDialog().show(childFragmentManager, "New_collection_dialog")
    }

    private fun setCollectionsInView() {
        val collectionsAdapter = CollectionCardAdapter(
            onClickItem = { typeCollection ->
                val action =
                    ProfileFragmentDirections.actionProfileFragmentToFilmsFromCollectionFragment(
                        typeCollection
                    )
                findNavController().navigate(action)
            },
            onDeleteItem = { collectionId ->
                viewModel.deleteCollection(collectionId)
            })
        binding.collectionsList.adapter = collectionsAdapter
        viewModel.collectionsFlow.filterNotNull().onEach { collections ->
            collectionsAdapter.submitList(collections)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setInterestedFilmsInView() {
        binding.interestedCollection.ListWithLinkBuilder { kinopoiskId ->
            val action =
                ProfileFragmentDirections.actionProfileFragmentToFilmInfoFragment(kinopoiskId)
            findNavController().navigate(action)
        }.setOnClickLinkToAll {
            val action =
                ProfileFragmentDirections.actionProfileFragmentToFilmsFromCollectionFragment(
                    TypeCollection.Interested
                )
            findNavController().navigate(action)
        }.setOnClickFooter {
            viewModel.clearInterestedHistory()
        }.build()

        viewModel.countInterestedFilmsFlow.filterNotNull().onEach { count ->
            binding.interestedCollection.setCountOnLinkToAll(count)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.interestedFilmsFlow.filterNotNull().onEach { filmList ->
            binding.interestedCollection.submitData(filmList)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}