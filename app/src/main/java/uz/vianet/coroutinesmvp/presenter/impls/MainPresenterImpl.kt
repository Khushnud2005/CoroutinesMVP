package uz.vianet.coroutinesmvp.presenter.impls

import uz.vianet.coroutinesmvp.model.Post


interface MainPresenterImpl {
    fun apiPostList()
    fun apiPostDelete(post: Post)
}