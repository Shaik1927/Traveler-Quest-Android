package com.simats.chatmessenger.adapter

import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.simats.chatmessenger.MyApplication
import com.simats.chatmessenger.R
import com.simats.chatmessenger.SharedPrefs
import com.simats.chatmessenger.Utils
import com.simats.chatmessenger.modal.Messages
import com.simats.chatmessenger.modal.RecentChats
import com.simats.chatmessenger.modal.Users
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import de.hdodenhof.circleimageview.CircleImageView

class RecentChatAdapter : RecyclerView.Adapter<MyChatListHolder>() {

    var listOfChats = listOf<RecentChats>()
    private var listener: onChatClicked? = null
    var chatShitModal = RecentChats()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyChatListHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recentchatlist, parent, false)
        return MyChatListHolder(view)


    }

    override fun getItemCount(): Int {

        return listOfChats.size


    }


    fun setList(list: List<RecentChats>) {
        this.listOfChats = list


    }

    override fun onBindViewHolder(holder: MyChatListHolder, position: Int) {

        val chatlist = listOfChats[position]


        chatShitModal = chatlist


        holder.userName.setText(chatlist.name)


        val themessage = chatlist.message!!.split(" ").take(4).joinToString(" ")
        val makelastmessage = "${chatlist.person}: ${themessage} "

        holder.lastMessage.setText(makelastmessage)

        Glide.with(holder.itemView.context).load(chatlist.friendsimage).into(holder.imageView)

        holder.timeView.setText(chatlist.time!!.substring(0, 5))

        holder.itemView.setOnClickListener {
            listener?.getOnChatCLickedItem(position, chatlist)


        }


    }


    fun setOnChatClickListener(listener: onChatClicked) {
        this.listener = listener
    }



}

class MyChatListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val imageView: CircleImageView = itemView.findViewById(R.id.recentChatImageView)
    val userName: TextView = itemView.findViewById(R.id.recentChatTextName)
    val lastMessage: TextView = itemView.findViewById(R.id.recentChatTextLastMessage)
    val timeView: TextView = itemView.findViewById(R.id.recentChatTextTime)


}


interface onChatClicked {
    fun getOnChatCLickedItem(position: Int, chatList: RecentChats)
}
