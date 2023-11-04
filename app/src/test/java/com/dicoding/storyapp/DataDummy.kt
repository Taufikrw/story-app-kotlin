package com.dicoding.storyapp

import com.dicoding.storyapp.data.remote.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "https://source.unsplash.com/story",
                "Title $i",
                "Description $i",
                0.0,
                "id $i",
                0.0
            )
            items.add(story)
        }
        return items
    }
}