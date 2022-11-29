package uz.vianet.coroutinesmvp.presenter

import kotlinx.coroutines.*
import uz.vianet.coroutinesmvp.model.Post
import uz.vianet.coroutinesmvp.network.RetrofitHttp
import uz.vianet.coroutinesmvp.presenter.impls.CreatePresenterImpl
import uz.vianet.coroutinesmvp.view.CreateView
import kotlin.coroutines.CoroutineContext

class CreatePresenter(var createView: CreateView): CreatePresenterImpl,CoroutineScope {
    var job: Job? = null
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job!!
    init {
        job = Job()
    }
    val handler = CoroutineExceptionHandler{_,error->
        createView.createPostFailure(error.message!!)
    }
    override fun apiPostCreate(post: Post?) {
        launch(Dispatchers.Main + handler) {
            val new_post = withContext (Dispatchers.IO) { postCreate(post!!)  }
            createView.createPostSuccess(new_post)
        }
    }
    private suspend fun postCreate(post: Post):Post {
        return async(Dispatchers.IO) {
            val resp = RetrofitHttp.postService.createPost(post).execute()
            return@async resp.body()!!
        }.await()
    }
    fun cancelHandleData() {
        job?.cancel()
        job = null
    }

}