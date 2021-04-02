package com.github.wensimin.boliboli_android.ui.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.github.wensimin.boliboli_android.databinding.FragmentMediaPlayerBinding
import com.github.wensimin.boliboli_android.manager.RestApi
import com.github.wensimin.boliboli_android.utils.logD
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource


class MediaPlayerFragment : Fragment() {
    private var player: SimpleExoPlayer? = null
    private lateinit var binding: FragmentMediaPlayerBinding
    private val args: MediaPlayerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMediaPlayerBinding.inflate(layoutInflater)
        logD("id ${args.id} type ${args.type}")
        lifecycleScope.launchWhenCreated {
            initPlayer()
        }
        return binding.root
    }

    private suspend fun initPlayer() {
        val baseFactory = DefaultHttpDataSource.Factory().apply {
            setDefaultRequestProperties(RestApi.getAuthHeaderInIo().toSingleValueMap())
        }
        val sourceFactory = DefaultDataSourceFactory(requireContext(), baseFactory)
        player = SimpleExoPlayer.Builder(requireContext())
            .setMediaSourceFactory(DefaultMediaSourceFactory(sourceFactory))
            .build().apply {
                addMediaItem(MediaItem.fromUri("${RestApi.RESOURCE_SERVER}/voice/media/${args.id}"))
                playWhenReady = true
                prepare()
            }.also {
                binding.playerView.player = it
            }
    }

    override fun onDestroy() {
        player?.release()
        super.onDestroy()
    }
}