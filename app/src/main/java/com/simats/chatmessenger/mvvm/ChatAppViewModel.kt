package com.simats.chatmessenger.mvvm

import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.chatmessenger.MyApplication
import com.simats.chatmessenger.R
import com.simats.chatmessenger.SharedPrefs
import com.simats.chatmessenger.Utils
import com.simats.chatmessenger.modal.Messages
import com.simats.chatmessenger.modal.RecentChats
import com.simats.chatmessenger.modal.Users
import com.simats.chatmessenger.notifications.FirebaseService.Companion.token
import com.simats.chatmessenger.notifications.entity.NotificationData
import com.simats.chatmessenger.notifications.entity.PushNotification
import com.simats.chatmessenger.notifications.entity.Token
import com.simats.chatmessenger.notifications.network.RetrofitInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import java.util.*
import kotlin.math.max
import kotlin.math.min

class ChatAppViewModel : ViewModel() {


    val message = MutableLiveData<String>()
    val firestore = FirebaseFirestore.getInstance()
    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val phone = MutableLiveData<String>()
    val imageUrl = MutableLiveData<String>()


    val usersRepo = UsersRepo()
    val messageRepo = MessageRepo()
    var token: String? = null
    val chatlistRepo = ChatListRepo()

    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }


    init {

        getCurrentUser()
        getRecentUsers()
    }

    fun getUsers(): LiveData<List<Users>> {
        return usersRepo.getUsers()


    }


    // sendMessage

    fun sendMessage(sender: String, receiver: String, friendname: String, friendimage: String) =
        viewModelScope.launch(Dispatchers.IO) {

            val context = MyApplication.instance.applicationContext

            val hashMap = hashMapOf<String, Any>(
                "sender" to sender,
                "receiver" to receiver,
                "message" to message.value!!,
                "time" to Utils.getTime()
            )


            val uniqueId = listOf(sender, receiver).sorted()
            uniqueId.joinToString(separator = "")


            val friendnamesplit = friendname.split("\\s".toRegex())[0]
            val mysharedPrefs = SharedPrefs(context)
            mysharedPrefs.setValue("friendid", receiver)
            mysharedPrefs.setValue("chatroomid", uniqueId.toString())
            mysharedPrefs.setValue("friendname", friendnamesplit)
            mysharedPrefs.setValue("friendimage", friendimage)




            firestore.collection("Messages").document(uniqueId.toString()).collection("chats")
                .document(Utils.getTime()).set(hashMap).addOnCompleteListener { taskmessage ->


                    val setHashap = hashMapOf<String, Any>(
                        "friendid" to receiver,
                        "time" to Utils.getTime(),
                        "sender" to Utils.getUidLoggedIn(),
                        "message" to message.value!!,
                        "friendsimage" to friendimage,
                        "name" to friendname,
                        "person" to "you"
                    )


                    firestore.collection("Conversation${Utils.getUidLoggedIn()}").document(receiver)
                        .set(setHashap)



                    firestore.collection("Conversation${receiver}").document(Utils.getUidLoggedIn())
                        .update(
                            "message",
                            message.value!!,
                            "time",
                            Utils.getTime(),
                            "person",
                            name.value!!
                        )



                    firestore.collection("Tokens").document(receiver).addSnapshotListener { value, error ->


                        if (value != null && value.exists()) {


                            val tokenObject = value.toObject(Token::class.java)


                            token = tokenObject?.token!!


                            val loggedInUsername =
                                mysharedPrefs.getValue("username")!!.split("\\s".toRegex())[0]



                            if (message.value!!.isNotEmpty() && receiver.isNotEmpty()) {

                                PushNotification(
                                    NotificationData(loggedInUsername, message.value!!), token!!
                                ).also {
                                    sendNotification(it)
                                }

                            } else {


                                Log.e("ChatAppViewModel", "NO TOKEN, NO NOTIFICATION")
                            }


                        }

                        Log.e("ViewModel", token.toString())



                        if (taskmessage.isSuccessful){

                            message.value = ""



                        }


                    }
                }





        }


    // getting messages

    fun getMessages(friend: String): LiveData<List<Messages>> {

        return messageRepo.getMessages(friend)
    }


    // get RecentUsers


    fun getRecentUsers(): LiveData<List<RecentChats>> {


        return chatlistRepo.getAllChatList()

    }


    fun sendNotification(notification: PushNotification) = viewModelScope.launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
        } catch (e: Exception) {

            Log.e("ViewModelError", e.toString())
            // showToast(e.message.toString())
        }
    }


    fun getCurrentUser() = viewModelScope.launch(Dispatchers.IO) {

        val context = MyApplication.instance.applicationContext


        firestore.collection("Users").document(Utils.getUidLoggedIn())
            .addSnapshotListener { value, error ->


                if (value!!.exists() && value != null) {

                    val users = value.toObject(Users::class.java)
                    name.value = users?.username!!
                    email.value = users?.useremail!!
                    phone.value = users?.userphone!!
                    imageUrl.value = users.imageUrl!!


                    val mysharedPrefs = SharedPrefs(context)
                    mysharedPrefs.setValue("username", users.username!!)
                    mysharedPrefs.setValue("useremail", users.useremail!!)
                    mysharedPrefs.setValue("userphone", users.userphone!!)


                }


            }


    }


    fun updateProfile() = viewModelScope.launch(Dispatchers.IO) {

        val context = MyApplication.instance.applicationContext

        val hashMapUser =
            hashMapOf<String, Any>("username" to name.value!!,"useremail" to email.value!!,"userphone" to phone.value!!, "imageUrl" to imageUrl.value!!)

        firestore.collection("Users").document(Utils.getUidLoggedIn()).update(hashMapUser).addOnCompleteListener {

            if (it.isSuccessful){

                Toast.makeText(context, "Updated", Toast.LENGTH_SHORT ).show()


            }

        }


        val mysharedPrefs = SharedPrefs(context)
        val friendid = mysharedPrefs.getValue("friendid")

        val hashMapUpdate = hashMapOf<String, Any>("friendsimage" to imageUrl.value!!, "name" to name.value!!,"email" to email.value!!, "phone" to phone.value!!, "person" to name.value!!)



        // updating the chatlist and recent list message, image etc

        firestore.collection("Conversation${friendid}").document(Utils.getUidLoggedIn()).update(hashMapUpdate)

        firestore.collection("Conversation${Utils.getUidLoggedIn()}").document(friendid!!).update("person", "you")



    }


}