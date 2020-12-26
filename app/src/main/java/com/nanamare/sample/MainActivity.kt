package com.nanamare.sample

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val musics = MutableLiveData<List<Item>>()

    private val musicAdapter by lazy {
        MusicAdapter(
            onHeaderClicked = { header ->
                Toast.makeText(this, header.toString(), Toast.LENGTH_SHORT).show()
            },
            onContentClicked = { content ->
                Toast.makeText(this, content.toString(), Toast.LENGTH_SHORT).show()
            },
            onFooterClicked = { footer ->
                Toast.makeText(this, footer.toString(), Toast.LENGTH_SHORT).show()
            },
            onAdsClicked = {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(LUCAS_LEVER_URL)))
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        loadMusic()
    }

    private fun initView() {
        initToolbar(findViewById(R.id.toolbar))
        setStatusBarColor(window.decorView, R.color.black)
        findViewById<RecyclerView>(R.id.rv_content).adapter = musicAdapter
        musics.observe(this, musicAdapter::submitList)
    }

    @SuppressLint("SetTextI18n")
    private fun initToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        toolbar.findViewById<TextView>(R.id.toolbar_title)?.text = "MUSICS"
    }

    private fun setStatusBarColor(view: View, @ColorRes colorRes: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.setSystemBarsAppearance(0, APPEARANCE_LIGHT_STATUS_BARS)
            } else {
                var flags: Int = view.systemUiVisibility
                flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                view.systemUiVisibility = flags
            }
        }
        window.statusBarColor = ContextCompat.getColor(this, colorRes)
    }

    private fun loadMusic() {
        musics.value =
            listOf(
                Item.Header("Nanamare 가 뽑았다!", "2020 가장 핫한 음악은?", mock),
                Item.Content(mock),
                Item.Ads(
                    Random(Int.MAX_VALUE).nextInt(),
                    "https://s3.orbi.kr/data/file/united/24e1b75faa139d19e10a9873701abe85.jpg",
                ),
                Item.Content(mock),
                Item.Footer("2020 여러분의 Best 음악은 무엇인가요?", mock),
            )
    }

    private val imageUrl = listOf(
        "https://upload.wikimedia.org/wikipedia/en/d/d0/Cloud_Nine_coverartwork.jpg",
        "https://i.pinimg.com/originals/37/e7/ad/37e7ad3d9da0e1b689ff4fb442caec84.jpg",
        "https://img.discogs.com/6XQDCxBJ0ZGmPUP6VCg3OaUGRxM=/fit-in/600x600/filters:strip_icc():format(jpeg):mode_rgb():quality(90)/discogs-images/R-11971959-1525763480-3077.jpeg.jpg",
        "https://img.discogs.com/9KbYUjn1DFjUY4irqyQb20yQ5Cg=/fit-in/300x300/filters:strip_icc():format(jpeg):mode_rgb():quality(40)/discogs-images/R-6709168-1425073258-2372.jpeg.jpg",
        "https://d33nb54eqlwoo9.cloudfront.net/media/l/500x500/1584991249/golden-hour-kygo.jpg",
        "https://m.media-amazon.com/images/I/81TghkkQQtL._SS500_.jpg",
    )

    private val title =
        listOf("Higher Love", "Cloud Nine", "Kids in Love", "Happy Now", "Sorry", "Golden Hour")

    private val name = "KYGO(가이고)"

    private val category = "EDM"

    private val mock
        get() = Music(
            id = Random(Int.MAX_VALUE).nextInt(),
            title = title.random(),
            albumUrl = imageUrl.random(),
            writer = name,
            category = category
        )

    companion object {
        private const val LUCAS_LEVER_URL = "https://jobs.lever.co/mathpresso?lever-via=MLNhnP_zhx"
    }
}