package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.extensions.insertIf
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.extensions.shortMessage
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.repositories.ChatRepository

class MainViewModel : ViewModel() {

    private val query = mutableLiveData("")
    private val chatRepository = ChatRepository

    private val chats = Transformations.map(chatRepository.loadChats()) { chats ->
        val allArchivedMessages = chats.filter { it.isArchived }
            .flatMap { it.messages }
            .sortedBy { it.date.time }
        val lastMessage = allArchivedMessages.lastOrNull()
        val (lastMessageShort, lastMessageAuthor) = shortMessage(lastMessage)
        chats.orEmpty()
            .filter { !it.isArchived }
            .map { it.toChatItem() }
            .sortedBy { it.id }
            .toMutableList()
            .insertIf(
                ChatItem.archiveItem(
                    lastMessageShort,
                    allArchivedMessages.size,
                    lastMessage?.date?.shortFormat() ?: "Никогда",
                    lastMessageAuthor
                ),
                0
            ) { chats.any { it.isArchived } }
    }

    fun getChatData(): LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()

        val filterF = {
            val queryStr = query.value!!
            val chats = chats.value!!

            result.value = if (queryStr.isEmpty()) {
                chats
            } else {
                chats.filter { it.title.contains(queryStr, true) }
            }
        }
        result.addSource(chats) { filterF.invoke() }
        result.addSource(query) { filterF.invoke() }
        return result
    }

    fun addToArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = true))
    }

    fun restoreFromArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = false))
    }

    fun handleSearchQuery(text: String) {
        query.value = text
    }
}