package com.example.firstapp.messenger.contacts

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.example.firstapp.R
import com.example.firstapp.databinding.ActivityContactsBinding
import com.example.firstapp.messenger.chat.ChatActivity
import com.example.firstapp.messenger.contacts.model.Contact
import com.example.firstapp.messenger.login.LoginActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth


class ContactsActivity : AppCompatActivity() {

    lateinit var binding: ActivityContactsBinding
    private lateinit var adapter: ContactsRecyclerViewAdapter
    private val contactsViewModel: ContactsViewModel by viewModels()

    companion object {
        const val CHANNEL_ID = "NOTIFICATIONS_CHANNEL_CONTACTS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ContactsRecyclerViewAdapter(this, contactsViewModel)
        binding.contactsRecyclerView.adapter = adapter

        binding.addContactButton.setOnClickListener {
            val newContact = binding.newContact.text.toString()
            binding.newContact.text?.clear()
            contactsViewModel.addContact(Contact(newContact, newContact))

            notifyNewContactAdded(newContact)
        }

        MobileAds.initialize(this)

        contactsViewModel.loadData(this)
        contactsViewModel.contacts.observe(this) {
            adapter.updateContacts(it)
        }

        createNotificationsChannel()
    }

    private fun notifyNewContactAdded(newContact: String) {
        val intent = Intent(this, ChatActivity::class.java).apply {
            // Intent.FLAG_ACTIVITY_NEW_TASK =  indica que la actividad debe iniciarse como la raíz
            // de una nueva tarea. Esto significa que la actividad se colocará en la raíz de la pila
            // de tareas y será el punto de partida para una nueva tarea.


            // Intent.FLAG_ACTIVITY_CLEAR_TASK = indica que todas las actividades en la tarea
            // deben borrarse antes de que se inicie la nueva actividad. Esto significa que cuando
            // se inicie la nueva actividad, será la única actividad en la tarea y el usuario no
            // podrá volver a las actividades anteriores en la tarea utilizando la tecla BACK.

            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(ChatActivity.EXTRA_USER_ID, newContact)
        }

        //  Un PendingIntent es un objeto que se puede utilizar para enviar una intención (un
        //  mensaje que indica a Android que debe realizar una acción) en el futuro.

        // PendingIntent.FLAG_UPDATE_CURRENT si l'activity existeix, la manté però la refresca
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_blur_on_24)
            .setContentTitle(newContact)
            .setContentText("Tap to open chat")

            // Fem que quan es cliqui, s'obri el xat i desaparegui la noti (amb l'autoCancel)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }

    fun createNotificationsChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel =
            NotificationChannel(
                CHANNEL_ID,
                "Add new contact notification",
                NotificationManager.IMPORTANCE_LOW
            )

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
//        }
    }

    override fun onResume() {
        super.onResume()

        val request = AdRequest.Builder().build()
        binding.adView.loadAd(request)

        adapter.loadAd()
    }

    override fun onPause() {
        super.onPause()
        contactsViewModel.saveData(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contacts, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_logout -> logout()
        }
        return true
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut() // Instant operation

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

        finish()
    }
}