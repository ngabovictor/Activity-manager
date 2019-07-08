package rw.nexin.codingactivity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.interfaces.JSONObjectRequestListener
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import com.androidnetworking.error.ANError
import org.json.JSONArray
import com.androidnetworking.interfaces.JSONArrayRequestListener
import kotlinx.android.synthetic.main.toolbar_layout.*
import org.json.JSONObject
import rw.nexin.codingactivity.adapters.RecordsAdapter
import java.util.logging.Logger


class MainActivity : AppCompatActivity() {

    lateinit var preferences: SharedPreferences
    lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)

        val url = preferences.getString("base_url", "")

        if (url!!.isBlank()){
            startActivity(Intent(this, AddProfileActivity::class.java))
            finish()
        }

        this.url = url


        val okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .build()

        AndroidNetworking.initialize(applicationContext, okHttpClient)

        records_recycler_view.layoutManager = LinearLayoutManager(this)
        records_recycler_view.setHasFixedSize(true)

        swipe_refresh_layout.setOnRefreshListener {
            swipe_refresh_layout.isRefreshing = true
            loadData()
        }

        loadData()
    }

    fun loadData(){

        swipe_refresh_layout.isRefreshing = true

        AndroidNetworking.get(url)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {

                        //Logger.getLogger(MainActivity::class.java.name).warning(response.toString())


                        swipe_refresh_layout.isRefreshing = false

                        if (response != null) {
                            parseData(response)
                        }
                    }

                    override fun onError(anError: ANError?) {
                        swipe_refresh_layout.isRefreshing = false
                        //Logger.getLogger(MainActivity::class.java.name).warning(anError.toString())
                    }
                })




    }

    fun parseData(data: JSONObject){
        var weekly_data: JSONArray = data.getJSONArray("data")

        val reversedData: JSONArray = JSONArray()

        // Reversing the data to have today at the top

        for (i in weekly_data.length() - 1 downTo 0){
            reversedData.put(weekly_data[i])


        }

        records_recycler_view.adapter = RecordsAdapter(this, reversedData)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item != null) {
            if (item.itemId == R.id.action_remove_url){
                preferences.edit().clear().apply()
                startActivity(Intent(this, AddProfileActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
