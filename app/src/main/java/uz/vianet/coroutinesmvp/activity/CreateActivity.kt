package uz.vianet.coroutinesmvp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import uz.vianet.coroutinesmvp.databinding.ActivityCreateBinding
import uz.vianet.coroutinesmvp.model.Post
import uz.vianet.coroutinesmvp.presenter.CreatePresenter
import uz.vianet.coroutinesmvp.utils.Utils.toast
import uz.vianet.coroutinesmvp.view.CreateView

class CreateActivity : AppCompatActivity(), CreateView {

    lateinit var binding: ActivityCreateBinding

    lateinit var createPresenter: CreatePresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        createPresenter = CreatePresenter(this)

        binding.btnSubmit.setOnClickListener(View.OnClickListener {
            val title: String = binding.etTitle.getText().toString()
            val body: String = binding.etText.getText().toString().trim { it <= ' ' }
            val id_user: String = binding.etUserId.getText().toString().trim { it <= ' ' }

            if (title.isNotEmpty() && body.isNotEmpty() && id_user.isNotEmpty()){
                val post = Post(id_user.toInt(), title, body)
                createPresenter.apiPostCreate(post)
            }

        })
    }

    override fun createPostSuccess(post: Post?) {
        val intent = Intent()
        intent.putExtra("title",post!!.title)
        setResult(RESULT_OK, intent)
        super@CreateActivity.onBackPressed()
    }

    override fun createPostFailure(error: String) {
        toast(this,error)
    }
    override fun onDestroy() {
        super.onDestroy()
        createPresenter.cancelHandleData()
    }
}