package uz.vianet.coroutinesmvp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import uz.vianet.coroutinesmvp.databinding.ActivityEditBinding
import uz.vianet.coroutinesmvp.model.Post
import uz.vianet.coroutinesmvp.presenter.EditPresenter
import uz.vianet.coroutinesmvp.view.EditView

class EditActivity : AppCompatActivity(), EditView {

    lateinit var id: String
    lateinit var editPresenter: EditPresenter
    lateinit var binding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initViews()
    }
    fun initViews(){
        val extras = intent.extras

        editPresenter = EditPresenter(this)

        if (extras != null) {
            Log.d("###", "extras not NULL - ")
            binding.etUserId.setText(extras.getString("user_id"))
            binding.etTitle.setText(extras.getString("title"))
            binding.etBody.setText(extras.getString("body"))
            id = extras.getString("id")!!
        }
        binding.btnSubmit.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val body = binding.etBody.text.toString().trim { it <= ' ' }
            val id_user = binding.etUserId.text.toString().trim { it <= ' ' }
            if (title.isNotEmpty() && body.isNotEmpty() && id_user.isNotEmpty()){
                val post = Post(id.toInt(),id_user.toInt(), title, body)
                editPresenter.apiEditPost(post)
            }


        }
    }

    override fun onEditSuccess(post: Post) {
        val intent = Intent(this@EditActivity, MainActivity::class.java)
        intent.putExtra("message", post.title + " Edited")
        startActivity(intent)
    }

    override fun onEditFailure(error: String) {
        val intent = Intent(this@EditActivity, MainActivity::class.java)
        intent.putExtra("message", "Post Not Edited : $error")
        startActivity(intent)
    }
    override fun onDestroy() {
        super.onDestroy()
        editPresenter.cancelHandleData()
    }
}