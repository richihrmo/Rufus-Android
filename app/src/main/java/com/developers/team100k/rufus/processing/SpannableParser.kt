package com.developers.team100k.rufus.processing

import com.developers.team100k.rufus.entity.Article
import com.developers.team100k.rufus.entity.ContentBlock

/**
 * Created by Richard Hrmo.
 */

class SpannableParser(val article: Article){

    lateinit var array: List<ContentBlock>

    fun parse(){
        array = article.blockArray
        for (i in 0 until array.size){
            array[i]
        }
    }
}