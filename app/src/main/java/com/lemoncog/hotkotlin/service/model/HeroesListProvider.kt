package com.lemoncog.hotkotlin.service.model

import com.lemoncog.hotkotlin.model.Group
import com.lemoncog.hotkotlin.model.Hero
import com.lemoncog.hotkotlin.model.HeroesList
import com.lemoncog.hotkotlin.model.fromString
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray


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

//            for (i in 0..jsonList.length()) {
//                val heroObj = jsonList.getJSONObject(i)
//
//                val primaryName = heroObj.getString("PrimaryName");
//
//                heroesListArray.add(Hero(primaryName))
//            }


            (0..jsonList.length()-1)
                    .asSequence()
                    .map { jsonList.getJSONObject(it) }
                    .mapTo(heroesListArray)
                    {
                        Hero(it.getString("PrimaryName"), it.getString("SubGroup"), fromString(it.getString("Group")))
                    }


            val heroesList = HeroesList(heroesListArray);
            success(heroesList)

        }).start()
    }

}