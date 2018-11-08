package com.developers.team100k.rufus

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.MovementMethod
import android.text.method.ScrollingMovementMethod
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.animation.Interpolator
import android.widget.Scroller
import android.widget.TextView
import com.developers.team100k.rufus.R.attr.colorAccent
import com.developers.team100k.rufus.entity.ContentBlock
import com.developers.team100k.rufus.entity.Page
import com.developers.team100k.rufus.processing.PagesParser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.observers.DefaultObserver
import kotlinx.android.synthetic.main.activity_show.*

class ShowActivity : AppCompatActivity() {

    private lateinit var mDatabase: DatabaseReference
    private lateinit var pagesParser: PagesParser
    private lateinit var random: Page
    private lateinit var array: List<ContentBlock>
    private lateinit var string: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)

        mDatabase = FirebaseDatabase.getInstance().reference
        pagesParser = PagesParser(mDatabase)
        pagesParser.callDatabase()

        exit.setOnClickListener { finish() }

        val pageObserver = object : DefaultObserver<Any>() {
            override fun onNext(o: Any) {
                random = o as Page
                array = random.article.blockArray
                string = array[0].text
                for (i in 1 until array.size) {
                    string += array[i].text
                }
                val spannable: Spannable = SpannableString(string + string + string)
                mainTextView.setText(spannable, TextView.BufferType.SPANNABLE)

                val spannableText = mainTextView.text as Spannable
                spannableText.setSpan(ForegroundColorSpan(colorAccent), 0, 26,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            override fun onError(e: Throwable) {
                Log.e("Observer", "onError")
            }

            override fun onComplete() {
                Log.e("Observer", "onComplete")
            }
        }
        pagesParser.data.subscribe(pageObserver)
    }
}
