package com.example.firebasechatapp

//import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechatapp.Adapters.CommentsRecyclerViewAdapter
import com.example.firebasechatapp.data.Comment
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.repositories.PostRepository
import com.example.firebasechatapp.repositories.UserRepository
import com.example.firebasechatapp.utils.MyUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import com.google.firebase.Timestamp
import com.squareup.picasso.Picasso


class PostDetailActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private fun showLoader() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        progressBar.visibility = View.GONE
    }


//    private lateinit var firestoreReference: CollectionReference
//    private lateinit var storageReference: StorageReference

    private lateinit var postRepository: PostRepository
    private lateinit var userRepository: UserRepository

    private  lateinit var whosePost:User

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var savedCredentials:Triple<String?, String?, User?>

    private lateinit var commentsRecyclerView : RecyclerView
    private lateinit var adapter: CommentsRecyclerViewAdapter
    private lateinit var comments : List<Comment>

    private lateinit var commentInput: EditText
    private lateinit var commentBtn: ImageButton

    private lateinit var detailViewImage:ImageView
    private lateinit var detailNameTextView:TextView
    private lateinit var detailDescriptionTextView:TextView
    private lateinit var detailViewDeleteBtn:Button


    private lateinit var otherUserImageText:TextView
    private lateinit var otherUserUsername:TextView
    private lateinit var otherUserFullName:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        postRepository = PostRepository(this)
        userRepository = UserRepository(this)

        progressBar = findViewById(R.id.loadingProgressBarLayout)


        var postFromIntent = MyUtils.getPostFromIntent(intent)


        sharedPreferencesHelper = SharedPreferencesHelper(this)
        savedCredentials = sharedPreferencesHelper.getSavedCredentials()


        detailViewImage = findViewById(R.id.detail_image_single_car_view)
        detailNameTextView = findViewById(R.id.detail_name_single_car_view)
        detailDescriptionTextView = findViewById(R.id.detail_description_single_car_view)
        detailViewDeleteBtn = findViewById(R.id.deletebtn)

        otherUserImageText = findViewById(R.id.other_user_imageText)
        otherUserUsername = findViewById(R.id.other_user_username)
        otherUserFullName = findViewById(R.id.other_user_fullname)

        commentInput = findViewById(R.id.actual_comment_box)
        commentBtn = findViewById(R.id.comment_send_btn)
        commentsRecyclerView = findViewById(R.id.post_all_comments_recycler)


        detailNameTextView.text = postFromIntent.PostName
        detailDescriptionTextView.text = postFromIntent.PostDescription
        Picasso.get().load(postFromIntent.ImageUri).into(detailViewImage)



        if (postFromIntent.AuthorId == savedCredentials.third!!.UserId) {
            // If the post author is the current user, show the delete button
            detailViewDeleteBtn.visibility = View.VISIBLE
        } else {
            // If the post author is not the current user, hide the delete button
            detailViewDeleteBtn.visibility = View.GONE
        }


        commentsRecyclerView.addItemDecoration(
            DividerItemDecoration(
                commentsRecyclerView.getContext(),
                DividerItemDecoration.HORIZONTAL
            )
        )
        commentsRecyclerView.layoutManager = LinearLayoutManager(this)
        commentsRecyclerView.setHasFixedSize(false)

        adapter = CommentsRecyclerViewAdapter(this)
        commentsRecyclerView.adapter = adapter

        loadData(postFromIntent.PostID)
        loadPostUserDetails(postFromIntent.AuthorId)






        detailViewDeleteBtn.setOnClickListener {

            postRepository.deleteUsersPost(postFromIntent.PostID) {
                success, message ->
                run {
                    if (success) {
//                        goToProfileActivity()
                        finish()
                    } else {
                        myToast(message)
                    }
                }
            }
        }


        commentBtn.setOnClickListener {

            var commentText = commentInput.text.toString()

            if (commentText.isNotEmpty()){

                var commenter = savedCredentials.third!!
                var comment = Comment("", commenter.UserId, commentText, commenter.FullName, commenter.UserName, Timestamp.now())

//                postRepository.addCommentToPost(postFromIntent.PostID, comment) {
                postRepository.addCommentToPostNew(postFromIntent.PostID, comment) {
                        success, message ->
                    run {
                        if (success){
                            commentInput.text.clear()
                            loadData(postFromIntent.PostID)
                        }
                        myToast(message)
                    }
                }
            }


        }

    }

    fun loadPostUserDetails(userID:String){
        userRepository.fetchUserDetailsUsingCollections(userID){
            user -> run {
                whosePost = user!!

                otherUserFullName.text = whosePost.FullName
                otherUserUsername.text = whosePost.UserName
                otherUserImageText.text = MyUtils.getInitials(whosePost.FullName)
            }
        }
    }

    private fun loadData(postId:String) {
        showLoader()
        comments = mutableListOf()

        postRepository.getCommentsForPost(postId){
            success, message, receivedComments ->
            run {
                if (success) {
                    if (receivedComments != null) {
                        comments = receivedComments
                    }
                    adapter.updateCommentsList(comments, postId)

                } else {
                    myToast(message)
                }
            }
        }

        hideLoader()
    }


    private fun myToast(msg:String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun goToProfileActivity() {
        myToast("Deleted Data Successfully")
        val intent = Intent(applicationContext, ProfileActivity::class.java)
        startActivity(intent)
        finishActivity(1)
    }


}