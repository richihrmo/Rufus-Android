package com.developers.team100k.rufus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_show.*

class ShowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)
        val data = intent.getStringExtra("article_id")
        val regex = """mytext""".toRegex()
        val stringhtmlbefore = "<html>\n" +
                "                <head>\n" +
                "                <meta name=\"viewport\" content=\"width=\\(String(describing: self?.textContainerView.frame.width)), initial-scale=1, maximum-scale=1.0, user-scalable=no'\">\n" +
                "                <style> body {font-family: -apple-system; font-size: 80%; height: 100%; margin: 0; padding: 0; }  figure { display: block; max-width: \\(String(describing: self?.textContainerView.frame.width))px; margin-top: 0px; margin-bottom: 0px; margin-left: auto; margin-right: auto;} img { display: block; max-width: 100%; margin-left: auto; margin-right: auto;} iframe {display: block; margin-left: auto; margin-right: auto;} </style>\n" +
                "                </head>\n" +
                "                <body>\n" +
                "                 mytext\n" +
                "                </body>\n" +
                "                </html>"
        val stringHtml = "<blockquote>He lay on his armour-like back, and if he lifted his head a little he could see his brown belly, slightly domed and divided by arches into stiff sections. The bedding was hardly able to cover it and seemed ready to slide off any moment.</blockquote>\n<figure><iframe src=\"https://www.youtube.com/embed/feUYwoLhE_4\" frameborder=\"0\" allowfullscreen=\"true\" style=\"\">&nbsp;</iframe></figure>\n<p><br></p>\n<figure><img src=\"https://images.unsplash.com/photo-1542349301445-c5f6ec562729?ixlib=rb-0.3.5&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;s=2185b01070113a920807d6df2de513dc&amp;auto=format&amp;fit=crop&amp;w=815&amp;q=80\"/></figure>\n<p><br></p>\n<figure><iframe src=\"https://player.vimeo.com/video/298639167\" frameborder=\"0\" allowfullscreen=\"true\" style=\"\">&nbsp;</iframe></figure>\n<p><br></p>"
        val frim = regex.replaceFirst(stringhtmlbefore, stringHtml)
        webview.settings.defaultTextEncodingName = "utf-8"
        webview.settings.javaScriptEnabled = true
        webview.loadData(frim, "text/html; charset=utf-8", null)
    }

    fun rip(v: View){
        finish()
    }
}
