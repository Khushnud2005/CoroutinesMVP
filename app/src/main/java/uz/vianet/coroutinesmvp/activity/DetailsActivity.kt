package uz.vianet.coroutinesmvp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import uz.vianet.coroutinesmvp.databinding.ActivityDetailsBinding
import uz.vianet.coroutinesmvp.model.Post
import uz.vianet.coroutinesmvp.presenter.DetailPresenter
import uz.vianet.coroutinesmvp.utils.Utils.toast
import uz.vianet.coroutinesmvp.view.DetailView

class DetailsActivity : AppCompatActivity(), DetailView {

    var postId:Int = 0
    lateinit var binding: ActivityDetailsBinding
    lateinit var detailPresenter: DetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {

        detailPresenter = DetailPresenter(this)
        val extras = intent.extras
        if (extras != null) {
            Log.d("###", "extras not NULL - ")
            postId = extras.getInt("id")
            detailPresenter.apiLoadPost(postId)
        }
    }

    override fun onLoadSuccess(post: Post?) {
        binding.tvTitle.setText(post!!.title.uppercase())
        binding.tvBody.setText(post.body)
    }

    override fun onLoadFailure(error: String) {
        toast(this,error)
    }
    override fun onDestroy() {
        super.onDestroy()
        detailPresenter.cancelHandleData()
    }
}