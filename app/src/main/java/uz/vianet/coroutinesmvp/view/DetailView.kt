package uz.vianet.coroutinesmvp.view

import uz.vianet.coroutinesmvp.model.Post


interface DetailView {
    fun onLoadSuccess(post: Post?)
    fun onLoadFailure(error: String)
}