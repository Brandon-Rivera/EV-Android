package mx.tec.bamxmorelos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.cardview.widget.CardView

class LandingFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater.inflate(R.layout.fragment_landing, container, false)

        val videoView = view.findViewById<VideoView>(R.id.vVideo)
        var mediaController = MediaController(context)
        mediaController.setAnchorView(videoView)

        var uri = Uri.parse("https://bamx.org.mx/wp-content/uploads/2022/08/comprimido.mp4?_=1")

        videoView.setMediaController(mediaController)
        videoView.setVideoURI(uri)
        videoView.requestFocus()
        videoView.start()


        return view
    }
}