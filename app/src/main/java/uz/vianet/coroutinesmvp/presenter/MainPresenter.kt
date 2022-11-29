package uz.vianet.coroutinesmvp.presenter

import kotlinx.coroutines.*
import uz.vianet.coroutinesmvp.model.Post
import uz.vianet.coroutinesmvp.network.RetrofitHttp
import uz.vianet.coroutinesmvp.presenter.impls.MainPresenterImpl
import uz.vianet.coroutinesmvp.view.MainView
import kotlin.coroutines.CoroutineContext

class MainPresenter(var mainView: MainView) : MainPresenterImpl,CoroutineScope {
    var job: Job? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job!!
    init {
        job = Job()
    }
    val handlerList = CoroutineExceptionHandler{_,exception->
        mainView.onPostListFailure(exception.message!!)
    }
    val handlerDel = CoroutineExceptionHandler{_,error->
        mainView.onPostDeleteFailure(error.message!!)
    }
    override fun apiPostList() {
        launch(Dispatchers.Main + handlerList) {
            val postsList = withContext (Dispatchers.IO) { getPostList()  }
            mainView.onPostListSuccess(postsList)
        }

    }
    private suspend fun getPostList():ArrayList<Post>{
        return async(Dispatchers.IO) {
            val response = RetrofitHttp.postService.listPost().execute()
            return@async response.body()!!
        }.await()
    }

    override fun apiPostDelete(post: Post) {
        launch(Dispatchers.Main + handlerDel) {
            deletePost(post)
            mainView.onPostDeleteSuccess(post)
        }
    }

    private suspend fun deletePost(post: Post):Post{
        return async(Dispatchers.IO) {
            val response = RetrofitHttp.postService.deletePost(post.id).execute()
            return@async response.body()!!
        }.await()
    }
    fun cancelHandleData() {
        job?.cancel()
        job = null
    }
}