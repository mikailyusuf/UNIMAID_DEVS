package com.unimaiddevs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.imaginativeworld.oopsnointernet.ConnectionCallback
import org.imaginativeworld.oopsnointernet.NoInternetSnackbar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), OnUserClick {


    private lateinit var recyclerView: RecyclerView
   private  lateinit var eventlist: MutableList<DataModel>
    private lateinit var eventsDatabase: DatabaseReference
    var TAG = "HOMEFRAGMENT"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //internet()
        recyclerView = view.findViewById(R.id.recyclerviewId)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager?

        }

        recyclerView.setHasFixedSize(true)
        eventsDatabase = FirebaseDatabase.getInstance().getReference("Events")
        eventlist = mutableListOf()

        eventsDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                // Toast.makeText(this, "Error Encounter Due to " + databaseError.message, Toast.LENGTH_LONG).show()/**/

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    //before fetch we have clear the list not to show duplicate value
                    eventlist.clear()
                    // fetch data & add to list
                    for (data in dataSnapshot.children) {
                        val std = data.getValue(DataModel::class.java)
                        eventlist.add(std!!)
                    }

                    // bind data to adapter
                    val adapter = EventsAdapter(eventlist, this@HomeFragment)
                    recyclerView.adapter = adapter
                    // progressBar.visibility = View.GONE
                    adapter.notifyDataSetChanged()

                } else {

                }
            }
        })
    }

    override fun onUserClick(datamodel: DataModel, position: Int) {

        Log.d(TAG, "THE NAME OF HOST IS : ${datamodel.host}")
        Log.d(TAG, "THE NAME OF VENUE IS : ${datamodel.venue}")
        Log.d(TAG, "THE IMAGE  OF HOST IS : ${datamodel.image}")

        val intent = Intent(activity, EventsActivity::class.java)
        intent.putExtra("host", datamodel.host)
        intent.putExtra("time", datamodel.time)
        intent.putExtra("venue", datamodel.venue)
        intent.putExtra("image", datamodel.image)
        intent.putExtra("date", datamodel.date)
        intent.putExtra("id", datamodel.id)
        intent.putExtra("link", datamodel.link)
        intent.putExtra("description", datamodel.description)
        startActivity(intent)

    }


}






