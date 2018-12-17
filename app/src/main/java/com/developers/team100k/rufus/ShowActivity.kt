package com.developers.team100k.rufus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_show.*
import com.bumptech.glide.Glide
import com.developers.team100k.rufus.view.MyChromeView
import com.developers.team100k.rufus.viewmodel.ShowViewModel


/**
 * Created by Richard Hrmo.
 * Activity handling article content show.
 */

class ShowActivity : AppCompatActivity() {

    private lateinit var viewModel: ShowViewModel
    private var html = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)

        val data = intent.getStringExtra("article_id")
        val image = intent.getStringExtra("image")
        if (image != null){
            Glide.with(this)
                    .load(image)
                    .into(title_image)
        } else {
            title_image.visibility = View.GONE
            appbar.minimumHeight = 56
        }

        viewModel = ViewModelProviders.of(this).get(ShowViewModel::class.java)
        viewModel.getHtml(data).observe(this, Observer {string ->
            html = string
            update()
        })

        webview.settings.defaultTextEncodingName = "utf-8"
        webview.settings.javaScriptEnabled = true
        webview.webChromeClient = MyChromeView(window)
    }

    private fun update(){
        // header for mobile friendly html formatting
        val stringhtmlbefore =
                        "<html>\n" +
                        "<head>\n" +
                        "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1.0\">\n" +
                        "<style> body {font-size: 90%; height: 100%; margin: 0; padding: 16px; padding-top: 24px; }  figure { display: block; max-width: (device-width)px; margin-top: 8px; margin-bottom: 0px; margin-left: auto; margin-right: auto;} img { display: block; max-width: 95%; margin-left: auto; margin-right: auto;} iframe {display: block; margin-left: auto; margin-right: auto;} </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<h1>the_title</h1>" +
                        "<h4>the_subtitle</h4>" +
                        "mytext\n" +
                        "</body>\n" +
                        "</html>"

        val title = intent.getStringExtra("title")
        val subtitle = intent.getStringExtra("subtitle")
        val regex1 = """mytext""".toRegex()
        val regex2 = """the_title""".toRegex()
        val regex3 = """the_subtitle""".toRegex()

        val frim = regex1.replaceFirst(stringhtmlbefore, html)
        val frem = regex2.replaceFirst(frim, title)
        val frek = regex3.replaceFirst(frem, subtitle)

        webview.loadData(frek, "text/html; charset=utf-8", null)
    }

    fun rip(v: View) {
        finish()
    }
}
