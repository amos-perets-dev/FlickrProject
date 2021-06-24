package com.example.flickrproject.screens.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.flickrproject.R
import com.example.flickrproject.data.LoadState
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_home_page.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomePageFragment : Fragment() {

    private val viewModel: HomePageViewModel by viewModel()
    private val photosAdapter: PhotosAdapter by inject()
    private val compositeDisposable = CompositeDisposable()

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                viewModel.contactResult(data, requireContext().contentResolver)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonSearch = view.button_search
        val editTextInput = view.edit_text_input
        val buttonPickContact = view.button_pick_contact
        val progressLoadData = view.progress_bar_load_data

        val recyclerViewPhotos = view.recycler_view_photos
        recyclerViewPhotos.apply {
            adapter = photosAdapter
            setItemViewCacheSize(40)
            setHasFixedSize(true)
        }

        photosAdapter
            .onClick()
            ?.subscribe { url ->
                viewModel.onClickPhoto(url)
            }?.let {
                compositeDisposable.add(
                    it
                )
            }

        recyclerViewPhotos.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (recyclerView.canScrollVertically(1)
                        .not() && newState == RecyclerView.SCROLL_STATE_IDLE
                ) {
                    viewModel.loadData()
                }
            }
        })

        viewModel.photosList.observe(viewLifecycleOwner, Observer(photosAdapter::update))
        viewModel.openContactList.observe(viewLifecycleOwner, Observer(this::openContactList))
        viewModel.loadState.observe(viewLifecycleOwner, { loadState ->
            when (loadState) {
                is LoadState.Load -> {
                    photosAdapter.clear()
                    progressLoadData.visibility = View.VISIBLE
                }
                is LoadState.Finish -> {
                    progressLoadData.visibility = View.INVISIBLE
                    editTextInput.setText("")
                }
            }
        })
        viewModel.photoUrl.observe(viewLifecycleOwner, Observer(this::showPhoto))

        buttonSearch.setOnClickListener {
            viewModel.onClickSearch(editTextInput.text.toString())
        }

        buttonPickContact.setOnClickListener {
            viewModel.onClickPickContact()
        }

    }

    private fun openContactList(ignored: Boolean) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        resultLauncher.launch(intent)
    }

    private fun showPhoto(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }
}