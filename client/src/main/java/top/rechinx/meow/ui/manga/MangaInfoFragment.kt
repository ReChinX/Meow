package top.rechinx.meow.ui.manga

import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaredrummler.cyanea.Cyanea
import kotlinx.android.synthetic.main.fragment_manga_info.*
import timber.log.Timber
import toothpick.config.Module
import top.rechinx.meow.R
import top.rechinx.meow.domain.chapter.model.Chapter
import top.rechinx.meow.domain.manga.model.Manga
import top.rechinx.meow.glide.GlideApp
import top.rechinx.meow.global.Extras
import top.rechinx.meow.rikka.misc.Resource
import top.rechinx.meow.rikka.viewmodel.getSharedViewModel
import top.rechinx.meow.ui.base.BaseFragment
import top.rechinx.meow.ui.base.viewModel
import javax.inject.Inject
import javax.inject.Provider

class MangaInfoFragment : BaseFragment() {

    private val mangaId: Long by lazy {
        arguments?.getLong(Extras.EXTRA_MANGA_ID, 0) ?: 0
    }

    @Inject lateinit var factorys: MangaInfoViewModelFactory

    private val viewModel by lazy {
        getSharedViewModel<MangaInfoViewModel> {
            factorys.create(mangaId)
        }
    }

    private lateinit var adapter: MangaInfoAdapter

    override fun getModule(): Module? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_manga_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener {
            activity?.finish()
        }

        // Constraint attribution contentScrim of CollapsingToolbarLayout
        collapsing_toolbar_layout.contentScrim =
                ColorDrawable(Cyanea.instance.primary)

        initSubscriptions()
    }

    private fun initSubscriptions() {
        viewModel.stateLiveData.observe(this, Observer { (state, prevState) ->
            if (state.manga != null && state.manga !== prevState?.manga) {
                adapter = MangaInfoAdapter(state.manga!!)
                viewModel.loadChapters()
            }
            if (state.manga != null && (state.chapters !== prevState?.chapters || state.isLoading != prevState.isLoading
                    || state.hasNextPage != prevState.hasNextPage)) {
                setManga(state.manga, state.chapters)
            }
        })
    }

    private fun setLoading() {

    }

    private fun setManga(manga: Manga, chapters: List<Chapter>) {
        toolbar.title = manga.title
        GlideApp.with(view!!)
                .load(manga)
                .into(backed_cover)

        val layoutManager = GridLayoutManager(activity, 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return adapter.getSpanSize(position) ?: spanCount
                }
            }
        }
        recycler.setHasFixedSize(true)
        recycler.layoutManager = layoutManager
        recycler.adapter = adapter

        val newItems = listOf(manga) + chapters
        adapter.submitList(newItems)
    }

    companion object {

        fun newInstance(mangaId: Long): MangaInfoFragment{
            val fragment = MangaInfoFragment()
            val bundle = Bundle()
            bundle.putLong(Extras.EXTRA_MANGA_ID, mangaId)
            fragment.arguments = bundle
            return fragment
        }
    }
}