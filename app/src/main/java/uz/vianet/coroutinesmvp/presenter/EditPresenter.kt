package uz.vianet.coroutinesmvp.presenter

import kotlinx.coroutines.*
import uz.vianet.coroutinesmvp.model.Post
import uz.vianet.coroutinesmvp.network.RetrofitHttp
import uz.vianet.coroutinesmvp.presenter.impls.EditPresenterImpl
import uz.vianet.coroutinesmvp.view.EditView
import kotlin.coroutines.CoroutineContext

class EditPresenter(var editView: EditView) : EditPresenterImpl, CoroutineScope {
    var job: Job? = null
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job!!
    init {
        job = Job()
    }
    val handler = CoroutineExceptionHandler{_,error->
        editView.onEditFailure(error.message!!)
    }
    override fun apiEditPost(post: Post?) {
        launch(Dispatchers.Main + handler) {
            val updatedPost = editPost(post!!)
            editView.onEditSuccess(updatedPost)
        }
    }
    private suspend fun editPost(post: Post):Post{
        return async(Dispatchers.IO) {
            val response = RetrofitHttp.postService.updatePost(post.id,post).execute()

            return@async response.body()!!
        }.await()
    }
    fun cancelHandleData() {
        job?.cancel()
        job = null
    }
}