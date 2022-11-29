package uz.vianet.coroutinesmvp.presenter

import kotlinx.coroutines.*
import uz.vianet.coroutinesmvp.model.Post
import uz.vianet.coroutinesmvp.network.RetrofitHttp
import uz.vianet.coroutinesmvp.presenter.impls.DetailPresentImpl
import uz.vianet.coroutinesmvp.view.DetailView
import kotlin.coroutines.CoroutineContext

class DetailPresenter(var detailView: DetailView): DetailPresentImpl,CoroutineScope {
    var job: Job? = null
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job!!
    init {
        job = Job()
    }
    val handler = CoroutineExceptionHandler{_,error->
        detailView.onLoadFailure(error.message!!)
    }
    override fun apiLoadPost(id: Int) {
        launch(Dispatchers.Main + handler){
            val post = getPost(id)
            detailView.onLoadSuccess(post)
        }
    }
    private suspend fun getPost(id: Int):Post{
        return async(Dispatchers.IO) {
            val resp = RetrofitHttp.postService.detailPost(id).execute()
            return@async resp.body()!!
        }.await()
    }

    fun cancelHandleData() {
        job?.cancel()
        job = null
    }

}