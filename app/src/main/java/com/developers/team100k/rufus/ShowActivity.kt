package com.developers.team100k.rufus

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_show.*
import android.webkit.WebChromeClient
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.developers.team100k.rufus.adapter.MyChromeView
import com.facebook.FacebookSdk.getApplicationContext
import kotlinx.android.synthetic.main.activity_show.view.*


class ShowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)
        Glide.with(this)
                .load("https://thenypost.files.wordpress.com/2018/11/trump-judges.jpg?quality=90&strip=all&w=618&h=410&crop=1")
                .into(title_image)
        val data = intent.getStringExtra("article_html")
        val title = intent.getStringExtra("title")
        val subtitle = intent.getStringExtra("subtitle")
        val regex1 = """mytext""".toRegex()
        val regex2 = """the_title""".toRegex()
        val regex3 = """the_subtitle""".toRegex()

        val stringhtmlbefore =
                "<html>\n" +
                "<head>\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1.0\">\n" +
                "<style> body {font-size: 90%; height: 100%; margin: 0; padding: 16px; padding-top: 24px; }  figure { display: block; max-width: (device-width)px; margin-top: 8px; margin-bottom: 0px; margin-left: auto; margin-right: auto;} img { display: block; max-width: 95%; margin-left: auto; margin-right: auto;} iframe {display: block; margin-left: auto; margin-right: auto;} </style>\n" +
                "</head>\n" +
                "<body>\n" +
//                "                <p>&nbsp;</p>" +
                "<h1>the_title</h1>" +
                "<h4>the_subtitle</h4>" +
                "mytext\n" +
                "</body>\n" +
                "</html>"
        val stringHtml = "\"<p>Hey, ho! Just another way to say. Wow.</p>\n<p>Awful.</p>\n<p>Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.</p>\n<figure><img src=\\\"data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxMTEhUTEhAVFRUWFRUVFRUVFRUVFRcVFRUWFhUVFRUYHSggGBolGxUVITEhJSkrLi4uFx8zODMtNygtLisBCgoKDg0OFxAQFy0dHR0tLS0tKy0tLS0tLS0tLS0tLS0tLS0tLSstLS0tLS0tLS0tLS0tLS0tLS0tLS0tLSstLf/AABEIALcBEwMBIgACEQEDEQH/xAAcAAACAgMBAQAAAAAAAAAAAAAEBQADAQIGBwj/xAA7EAABAwMCAwYEAwYGAwAAAAABAAIDBBEhBTESQVEGImFxgZETMqHBsdHwB0JSYqLxFBUjcpLhFjPS/8QAGQEAAwEBAQAAAAAAAAAAAAAAAQIDAAQF/8QAIhEBAQEBAAMAAgIDAQAAAAAAAAECEQMhMRJBBBMiUWEy/9oADAMBAAIRAxEAPwDHCiYAqQiIQvDy6qJuhqgokBD1LU2gKahyWzuTGpalkyGTQM4K6Bq0CIhVJWq3YJLq1eAbfT80x1GQtZcclys4tucnJV/HOlZdMXKBUhysjcrcYTGFc0qlhVzUDRexoVrI+dsKuM3RkYHigeRhg6K9jOoWzYudkTDD1PqseZUNi6K1sfhfzRjYRvyWHm2yBvxCujAGGW8roaWQ2+UfrzCMe4+Crey4R6H4UB8V36yrI53Dqtvh5VnD4Iz2TWeI6Z1r8/xS6pkvuPcJvHGClVdTnizt5eqeRHTm9WaGjHM59ilzUdrIJf8Ay2x0t/e6FYF0T1Ef20chCjnsQbmppS1oooQoiCK6k+YKpWU57wQvxnYU/wAo8lFpTHujyUXFaq9K+Aro4kR8NbBi8qVRVwoeoajHlA1L0baxXVtSmWNNalyE4U2bwS/4SuiYifhLIjT/AJN0q11/DFc7XF1yDpC5xJ5rsO0rAYHC/MW9CFyUTbLs8H/kK2bGr2RrDUVHGqjGrAro2+CjWK+Jp2CFPI2iRsJ8FrT0/VMYYgOSS1fGG1NFfkj20nVYhARLd90F5lZFTgDYLWakv080Sy3RZdzwjwOlLoAD1WnwhZX1JVNrqZ+A541qY7pmYBa5Q80PMeybNsR3AdNfisfdWa5AA3pcH3Gf+/Qol0X7w36KjtFmEEcgfe1vuuiOHbg6plwc4DseTgcfQIT4aulfsFh3JVRilzUK9qOcULIjKGlDgscCtss2TdKHLFmPcK7hWvCt1nSU0ndHksIWnf3QsrluD9e1OkWvGqONZa5eTmLLHICqRrjhL6kprGLZiqgs1L7Kpj0ZBENWSFS1ysujMgSdqHDgA2ufwXMgWT3tLO35eY+l8rnuIrv8M5kBNO0kp5Bpry29rDklmnNu4XXbsHcHt7K0z0eucdSBu6zG0X8LXPkOXqU01BtgCPZKnb28vplLqLZNKNvFy/XgmUcbef5JTp7zytvb8s8kwtIdmh3jfPphJMq/2c+Dm0w5FWNhKVwyzsPym3iNvDF7phDqJuLi3v8AfKf8Bz5r+xfwDawVXw3ehRZnBsR+vRal17+6FxFM66GMFwfDmh/h2uefJMWSjN97ILvSGzfUoTEpd7sCPkHXObD6Kj4Lr7WHTmncensjzbK0YAU1xEeX6Wx8lXqbQWketvMWP3VzHAO4eVyL/ghNccAHG2eB9/QXA9wmnxDf157UR8Lrb9FGZK3qeXlY+mPsEZptNxFUQUMoS5DVGnOGbLvqDSxbZZq9KFtkYFeZGMjksALrqvR/BBnSfBEpDyVbwnr9HPRA1WnubyW4zEJ7oUWkTTYKJPQvZgro2qtWMK8TLoWPCWVpTB7ktq0axRUIbiRVQg7J4y5jkQ0oaMIlieVnK9qmf6o8W+1sJRG3xun3ast42DJIBJt4nF/qlETV2+O/4wB1FixXV0012tHhlcnTBPIHHryVJTSdX1kh2JS95sc9FdM+/NVOsUlX56O9C+W46p1xtH5pTp0Vm4cDcYG2VVVPlc7uRlw/3AC/S6eDHSQTtPiPIrWrpI3DGCuYqJ6trQY428XMWLrYxnZbafJWuuZ2jwbhp32BHQW5J+Uts/LnsZKTG4DrsiaOXivdCanB8pBIINyDlYp3kZBUt2RXxwTLfZFwVoYLNGeZ/NDltxfe1jbqoIA435fw44R6c1sX0O8+1epdoIYx/qPz0ALug8huPdUadqLZT3Q6x5lpARtZodPKeOVgc4Wsb228ls2NrbNYAAOifXEpdd9lrn98X5k7ZQvaHDSd7gj/AJAj7ousHf35pP2iJLBbp+F0svpLc9uSltdONDGUlkCYaXUcJVI5/wBvSdMAsi6iIWXO6fqAtujX6jjdNC1RVQi61hoR0Vb6sEppRuBWgKP8tB5JdqOkixwuuhjQ9dCCE1oPOXaWL7LC6mSmycLCmLpHMWpRJah5GrxMOhQ9yAqpEXKltUU+owCdypsszFSMJoKyNiIY1aRK1YHIdqbtmvuHNBH4H8FQ2JwDSWnPTl5pr2tgu1j/AOEke/8AZH1cQMUTgMFrc+gXf4r3ENidpPBHdMY3cui2nYA24G2SfDoqHeB3Fx5KprniSLZq1BuFo92ynVZfR/pziWEXxy80fTuLRn1tn6JRpcwtZMg/mh3h856Oi1ADx/5X/BZfXk7Nt42sh2hyMhpSTY48fst+eqf+vM9lILi4k7qtwzgAbXROvShgAbh19vxsltC4kni8wtwem0e2FGjmFbC02236BVzAsdcdMhCQdCIg0739ypKANsLalkD8haVhxZG/B5C2ubcA8wUt1J3LwN/VHSyXS3Xoy23iCtPjl8uXHVAyo0qyqGUMVefHFfphTagWq46x4pSDhUEIlrooNSzuup0auBtleZ/GITrRdTIOSmkB65TTiy0nK5ug1XG6Zw1d1qzV7cqK0gLKQTZaPCw163XhRcDMxKasJ1UJRVlNKxS8LMYWz91kKrLWBWKprlsSgwbUIQ9jmnYiyooKgPpREfmhLmkc7HLXeSJmSCd/w6hp2Eg4D58vsurwXno2LyuhloxwtHWxS+Sm+YjYHHl0Tj4zXRNPMd13mlk9Rw4LTwuxfkHcvRdUro3PXS7iytJCsvW72pKWRZQzWTilqEopY0ZEyxCWxXNdHSTDmiKyusOn65pVSvREreIge60vFOT65/UHOEvxH3sbgeAQB1gtdhji1vzEC4/M+i6+qttvyVFPQtJvwjJTziW+pT9oGFreEi1lQ+sklk7rDw/xEgXJ3sEa3ToWm4ja2+9gBk+C3fGBsfSyNHPaogaY3lpP6KIrXYWs/eAdzGCsTMu1TqvSp17+qnahvdYTzz7hWTDCr7QyBzIuoat+nPv9uNrWoO18I/UFKKAHKvj44dfQckOEK4LoJafCWzUeUxKUP3WYnkHCNmoSEGWWKYppR6o4LotL1W53XGhE0tQWlKL0htcsLkW6rjdYWF6gxyt4kOxX2XgrqZzhJatycTpNVtTZYC8rHEsPC0VWXsKtuho1eE8jKpkp1CDjFuYyPMJrOgW5cB1Kpn1WWUMvHYXsTYPH8wGHBNq4NN2hvIC381rLma4uY/iYOlx5YVkevC9y08XS3Nda+Nyz2qkwSDuCR7IgNu26X/FJJLhYkkkdMoyCXFkKOaIpHZI8k1jjuk0R73qE8p3YSqQbSQ3RDR3irtPZhVujs4oyHtLpHXec88+FlvHM53/qidJkN4v3bk2GdlTUxWcDuCRccyL+Kd0vaFjBw/4d4HFfHBte97cSpjE/aO96k/xnQraCsMgjdGxpI4uK9xb2SnVaesgs957pcW4yBba/PNl1/wD5GHEFsEp5Xs0WvbqdlzWqdpX1PFEIwyMO+c3LiRu0DbfnnCezMifj8nl1qTjbT5+MZG7bnxI/uUexl2IGghtt5eiZE2apWOmX3SSrFrpHVPJOTtgJzXO3SSbmpaS2T6it6B2FK1t1RTkhXx8cevpsoGAlL5atW0lXfdOSipoRZIKynsU+fOEGY+NyYpIYz0WCukdQCyS1tPwlKwNRbWWFme1Mcr2vQwCyHLw+OhZMldUEfI9L6l60jFsyoJW9Q7KqVYy6IohqHiCIaqxg9QgHYN0xnQMiLA9Rqmxs4nnmLeZV8VnAOx+vFc1rlQJZWxg3aCG+Zccrqq6kMBH8BGD9l2YxZjrY3Py4V1Qs4+d/daskst6w96/UKkhBYVFJkeid0cq5xjsprRyHZCnzXXUUoxlXVEgASanqLIp0/EsrPbE7HOabZ8EugqbHgJLHDGbj69E6jGFirpWPHeaD+t00o9ufnwH8KZ4sZnEdOMkexKpjpQ02Lg93Jo/Fx5BQaXY91z7dAT90wpqcNHdaB+Pqt0/53nqcaNi4LDc8z4+C3lqMKStugZHJSX1ANfJulZKv1aWxA6/ZCsKnr6hqsup7hYFFzTCnYj4qYWXTienHq+3IVdCUrc5zN138tDdJ9S0m/JU/FO1zP+LKY6TLc5SytpCwrNFPwlKzrbpLqbLrcahjdZp7PKzFP+GKi6cUQWFmd6AtHhSN6scF4croBzOwldRImVSktSU2fbBpn5Wgcq5ngbm3mhzqETd5G+6vnNvyB02iVznADJsuYqu07W4jbxeJwEirtVllPecbfwjA9l0Y/j6v30W7kdjU6vCL3kb6ZXK6tq5kJDSQzl4+JS0BY4V1Y8Gc3v1O7tSB9ntd0c0+xBXtLaRs0fC4XBH9iF4qWL2LsTVfFpY3cwOF3m3H2XVifolcjrGnPhdwPFxux3IhBtF16rqOlsnZwPHkRu09QvOtX0iSnfwvGD8rhs7/AL8FDy+LnufHR4/L31SxG0UmQENwrZosQVHi/eOlaBw3H681mjqxxWPPqgKOox5oeswbt6ofip+fHYxHCyXHZIdM1S7eEnKasqr2W4tnUo0NK0OOaqNZb1Vb6wDc7LcP1dK7CUfGyXchdYrtSFrA2v8AQdUg1GuuOFuAtxz632+g1ZU8chI2GArIn5CWRuymVFkqXPaWjindZMKeZLQ5WGWy6s3kc/O08ibdZnpLhDafOnDchVntPc44fWtN3wuQqYS0r1LUae4K4jWqOxS6hZSIPTjRXpE/dGUdTwpRdiHBRIW6mos3XoMEiL+IAMkDzK8nqO1dS7Z4YP5Rn3KVVNdJJ88j3ebifouCfw7ftVvkj1PVddp4weKZt+jTxO9guK1XtSXYibwj+I7+3Jc0oujx/wAXGf8Aqd3aulqHON3OJ8zdVl61AWbLqkKnEsFyytSswkBZUCicrC9D/ZRVXMsN+kjfXDvt7rz1Puw2ofBrYXE2a53w3eT8D+rhPomz9Z7f8EjkhtQ09kzCyRtx9QeoPIp0wYWHU4PgVXhXlOs9lpYSXNBez+IDIH8zR+ISQsXtjqQ8srnNZ7KxyEuaPhv8u6f9zfuFHXh/cXx5v1XncVwrCbplX6PJCe+yw5OGWnyP52QBhtlQuOLzfQvwyMgqz/MHt5H0yt5Y0O6mcdvqgPufFx1bH73qEM/UHu2wFGUvU3/BMdJ0Z07rDDB8zvsPFaT/AE11rnuufrdQ4BnJPv5qplYx+xz0Ku/aBC2OqEbBZrYmj3Ljc+K5rg6I3xpTzWHqY0LlzcVS5vimVHqLeeFL+qymvlldE16zNJsgY6hp2crHG9k1N4z7TintO/CQ6aNk8hGFfCPm+pUC4XNatS3uukeUuq4rprEY841Cn4ShWBdTq1De65uRnCbKVM1UWqiUQSllkBZAVOFRSyzZZsjwWLKLNlFgalRguVh5W8JCzLVFLqJgZWWkjINiNj0K1CzdFn0b2X1EVFLDMP32Di8HjDx/yBTYLz/9iVZx080JOY5A9o6NkGf6mu916IWK0vYVGlX8AcMi6HsronIsHqNLa4EDY8jkLltX7H3u6JvCegyw/wDyu5atkLJfoy2fHiVbpzo3Wc0g9D9uoVIadgM+C9e7Rt/0HlsDZn27jHEAE+LjsF5nN2pnpvm0hjLcyXH+sBQvh7fVdGfP6+NtG7ITSm7+4y9yT08Au3ptOZCzhY2wA+vXzVXZPtbDXR2aPhytHfiJuR/M082+KO1GSzSqTEylrd19eC/tCl4q6W3IMb7C/wB1zzCje0EpdVTuPOR3tsPwS4KV+guKwo111gpvoNmvI2JCPptWcLB2R9UtUSXMps6s+PQdC1ON9gHC/Q4K6qlOF4q1xBuDY9V0mj9r5YrB/fb9R+a0nB1r8vr0adtkKRdC0HaKCcd11nfwnBRbDlOmAraa4OFxOsUvCbr0eWO4XK9oKXBwpah441RbujKiQQllLKKKpUsooosyLCiiLMEKlwsbhYUQYRDLdWXUUTT4yXUCiiwO6/Y/qJj1BrL92ZjoyPEDjaf6T7r3h7FlRWz8Cqy1agWUUTAIYlXavtBHQ07p5ATbusaN3PPytvy81FEKLxV/7TdQLi4yRkEkhhjaWtB/dBwSB4lG0X7S5jianieOfBxMPsSQsqJOdMzrUsTGR6rQXjdG8fEZbhDgTwvaRtz5YK9DmrRNAyRu0jGvHk4XUUQz9sB4H2gZaok/3H8UvcFFEmvtZWHWV11FEuRrCiiiYEUUUWZljiDcGxXT6H2pLO7Ndw5HmsKIM7jS9RjnbdhPqCEJq9PcKKLVo512li6iiilw3X//2Q==\\\"/></figure>\\n<p><em>Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.</em></p>\\n<h4><u>Awful. Incredible.Awful. Incredible.Awful. Incredible.</u></h4>\\n<figure><iframe src=\\\"https://www.youtube.com/embed/Acx1SsggJKc\\\" frameborder=\\\"0\\\" allowfullscreen=\\\"true\\\" style=\\\"\\\">&nbsp;</iframe></figure>\\n<ul>\\n  <li>Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.Awful. Incredible.</li>\\n</ul>\""
        val frim = regex1.replaceFirst(stringhtmlbefore, stringHtml)
        val frem = regex2.replaceFirst(frim, title)
        val frek = regex3.replaceFirst(frem, subtitle)
        webview.settings.defaultTextEncodingName = "utf-8"
        webview.settings.javaScriptEnabled = true
        webview.webChromeClient = MyChromeView(window)
        webview.loadData(frek, "text/html; charset=utf-8", null)
    }

    fun rip(v: View) {
        finish()
    }
}
