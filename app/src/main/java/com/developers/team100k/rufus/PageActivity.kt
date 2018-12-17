package com.developers.team100k.rufus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.developers.team100k.rufus.adapter.TabLayoutPagerAdapter
import com.developers.team100k.rufus.entity.Page
import com.developers.team100k.rufus.processing.PagesParser
import com.developers.team100k.rufus.view.MyChromeView
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.observers.DefaultObserver
import kotlinx.android.synthetic.main.activity_show.*

class PageActivity : AppCompatActivity() {

    lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page)

        webView = findViewById(R.id.page)

        val mDatabase = FirebaseDatabase.getInstance().reference
        val pagesParser = PagesParser(mDatabase)
        pagesParser.callDatabase()

        webView.settings.defaultTextEncodingName = "utf-8"
        webView.webChromeClient = WebChromeClient()

        val pagesObserver = object : DefaultObserver<Any>() {
            override fun onNext(o: Any) {
                val random = o as Page
                update(random)
//                webView.loadData(random.article, "text/html; charset=utf-8", null)
                Log.e("categoryObserver", "onNext")
            }

            override fun onError(e: Throwable) {
                Log.e("Observer", "onError")
            }

            override fun onComplete() {
                Log.e("Observer", "onComplete")
            }
        }
        pagesParser.data.subscribe(pagesObserver)
    }

    fun update(page: Page){
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

        val title = page.title
        val subtitle = page.subtitle
        val regex1 = """mytext""".toRegex()
        val regex2 = """the_title""".toRegex()
        val regex3 = """the_subtitle""".toRegex()

        val frim = regex1.replaceFirst(stringhtmlbefore, page.article)
        val frem = regex2.replaceFirst(frim, title)
        val frek = regex3.replaceFirst(frem, subtitle)

        webView.loadData(frek, "text/html; charset=utf-8", null)
    }

    fun rip(view: View){
        finish()
    }
}
