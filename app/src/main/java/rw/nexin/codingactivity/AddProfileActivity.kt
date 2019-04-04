package rw.nexin.codingactivity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add_profile.*

class AddProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_profile)

        val preferences: SharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)

        var url = ""

        save_btn.setOnClickListener {
            url = url_view.text.toString()

            if (url.isEmpty()){
                url_view.error = "URL is required"
            } else if (!url.startsWith("https://wakatime.com/share/")){
                url_view.error = "URL is incorrent"
            } else if (!url.endsWith(".json")){
                url_view.error = "URL must be a JSON format"
            } else{
                preferences
                        .edit()
                        .putString("base_url", url)
                        .apply()

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}
