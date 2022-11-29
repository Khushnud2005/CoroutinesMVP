package uz.vianet.coroutinesmvp.view

import uz.vianet.coroutinesmvp.model.Post

interface CreateView {
    fun createPostSuccess(post: Post?)
    fun createPostFailure(error: String)
}