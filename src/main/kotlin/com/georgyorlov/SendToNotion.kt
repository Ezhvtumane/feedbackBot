package com.georgyorlov

import com.georgyorlov.util.PropertiesExtractor
import notion.api.v1.NotionClient
import notion.api.v1.model.blocks.ParagraphBlock
import notion.api.v1.model.pages.PageParent
import notion.api.v1.model.pages.PageProperty
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class SendToNotion {

    private val notionKey =
        PropertiesExtractor.get().getProperty("notion.secret")
    private val dbId =
        PropertiesExtractor.get().getProperty("notion.db.id")
    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss Z")
    fun send(userId: Long, chatId: Long, nickName: String, text: String) {
        val date = ZonedDateTime.now();
        NotionClient(token = notionKey).use { client ->
            val database = client.retrieveDatabase(dbId)
            val newPage = client.createPage(
                // Use the "Test Database" as this page's parent
                parent = PageParent.database(database.id),
                // Set values to the page's properties
                // (Values of referenced options, people, and relations must be pre-defined before this API call!)
                properties = mapOf(
                    "title" to PageProperty(
                        title = listOf(
                            PageProperty.RichText(
                                text = PageProperty.RichText.Text("@$nickName")//надо еще что-то кроме ника. для нескольких сообщений
                            )
                        )
                    )
                ),
                children = listOf(
                    ParagraphBlock(
                        ParagraphBlock.Element(
                            richText = listOf(
                                PageProperty.RichText(
                                    text = PageProperty.RichText.Text(
                                        "At: ${date.format(formatter)}.\n" +
                                                "ChatId: $chatId\n" +
                                                "Message: $text"
                                    )
                                )
                            )
                        )
                    )
                )
            )
        }
    }
}
