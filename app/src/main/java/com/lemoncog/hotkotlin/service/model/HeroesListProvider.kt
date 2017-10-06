package com.lemoncog.hotkotlin.service.model

import com.lemoncog.hotkotlin.model.Group
import com.lemoncog.hotkotlin.model.Hero
import com.lemoncog.hotkotlin.model.HeroesList
import com.lemoncog.hotkotlin.model.fromString
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.util.*


class HeroesListProvider {
    val client = OkHttpClient()

    fun get(success: (HeroesList) -> Unit) {
        Thread({

            val request = Request.Builder()
                    .url("https://api.hotslogs.com/Public/Data/Heroes")
                    .build()

            val response = client.newCall(request).execute()
            val jsonList = JSONArray(response.body()?.string())

            val heroesListArray: MutableList<Hero> = mutableListOf<Hero>()

            var index = 0L
            val rand = Random()

            (0..jsonList.length()-1)
                    .asSequence()
                    .map { jsonList.getJSONObject(it) }
                    .mapTo(heroesListArray)
                    {
                        Hero(index++, it.getString("PrimaryName"), it.getString("SubGroup"), fromString(it.getString("Group")), played = rand.nextInt(10), wins = rand.nextInt(5), losses = rand.nextInt(5))
                    }


            val heroesList = HeroesList(heroesListArray);
            success(heroesList)

        }).start()
    }

}