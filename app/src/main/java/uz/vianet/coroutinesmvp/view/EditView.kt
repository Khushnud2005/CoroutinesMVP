package uz.vianet.coroutinesmvp.view

import uz.vianet.coroutinesmvp.model.Post

interface EditView {
    fun onEditSuccess(post: Post)
    fun onEditFailure(error:String)
}