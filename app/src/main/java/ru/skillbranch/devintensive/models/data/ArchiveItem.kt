package ru.skillbranch.devintensive.models.data

data class ArchiveItem (
    val id: String,
    val avatar: String?,
    val initials: String,
    val title: String = "Архив чатов",
    val shortDescription: String?,
    val messageCount: Int = 0,
    val lastMessageDate: String?,
    val isOnline: Boolean = false,
    val chatType : ChatType = ChatType.ARCHIVE,
    var author :String? = null
)