package com.example.firstapp.messenger.contacts

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapp.R
import com.example.firstapp.databinding.ItemContactBinding
import com.example.firstapp.messenger.chat.ChatActivity
import com.example.firstapp.messenger.contacts.model.Contact
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class ContactsRecyclerViewAdapter(
    private val activity: Activity,
    private val contactsViewModel: ContactsViewModel
) :
    RecyclerView.Adapter<ContactsRecyclerViewAdapter.ContactViewHolder>() {

    private var contacts: List<Contact> = listOf()
    private var ad: InterstitialAd? = null
    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics
    val IS_LOADED_AD_PARAM = "IS_LOADED_AD_PARAM"

    fun loadAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(activity, "ca-app-pub-3940256099942544/1033173712",
            adRequest, object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    ad = null
                }

                override fun onAdLoaded(loadedAd: InterstitialAd) {
                    ad = loadedAd
                }
            })
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateContacts(contacts: List<Contact>) {
        this.contacts = contacts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemContactBinding.inflate(layoutInflater, parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.name.text = contact.name

        holder.itemView.setOnClickListener {
            val intent = Intent(activity, ChatActivity::class.java)
            intent.putExtra(ChatActivity.EXTRA_USER_ID, contact.userId)

            ad?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    activity.startActivity(intent)
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    activity.startActivity(intent)
                }

                override fun onAdShowedFullScreenContent() {
                    ad = null
                }
            }

            firebaseAnalytics.logEvent(
                FirebaseAnalytics.Event.AD_IMPRESSION,
                bundleOf(
                    FirebaseAnalytics.Param.AD_UNIT_NAME to ad?.adUnitId,
                    IS_LOADED_AD_PARAM to (ad != null)
                )
            )

            ad?.apply {
                show(activity)
            } ?: activity.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener {
            val dialog = AlertDialog.Builder(activity)
            dialog.setTitle("Are you sure you want to remove ${contact.name}?")
            dialog.setPositiveButton(R.string.yes) { _, _ ->
                contactsViewModel.removeContact(contact)

                val snack = Snackbar.make(
                    holder.itemView,
                    "Contact ${contact.name} is removed",
                    Snackbar.LENGTH_SHORT
                )
                snack.setAction("Undo") {
                    contactsViewModel.addContact(contact)
                }
                snack.show()
            }
            dialog.setNegativeButton(R.string.cancel) { _, _ ->
                // pass
            }
            dialog.show()

            true
        }

    }

    override fun getItemCount(): Int = contacts.size

    inner class ContactViewHolder(binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val name: TextView = binding.contactName
        val image: ImageView = binding.contactImage
    }


}