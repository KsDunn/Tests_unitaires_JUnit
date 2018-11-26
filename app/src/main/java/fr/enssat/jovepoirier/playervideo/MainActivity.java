package fr.enssat.jovepoirier.playervideo;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String VIDEO_SAMPLE = "https://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4";
    private MutableLiveData<String> urlWebView = new MutableLiveData<String>();
    private MutableLiveData<Integer> mCurrentPosition = new MutableLiveData<Integer>();
    private static final String PLAYBACK_TIME = "play_time";
    private TextView mBufferingTextView;
    private VideoView mVideoView;
    private WebView mWebView;
    private PlayerViewModel mPlayer;
    private LinearLayout layout;
    private List<Chapitre> listeChapitre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoView = findViewById(R.id.videoview);
        mWebView = findViewById(R.id.webview);
        mBufferingTextView = findViewById(R.id.buffering_textview);
        layout = findViewById(R.id.linearlayout);

        mWebView.setWebViewClient(new WebViewClient());

        urlWebView.setValue("https://en.wikipedia.org/wiki/Big_Buck_Bunny");
        mCurrentPosition.setValue(0);

        mPlayer = new PlayerViewModel(urlWebView, mCurrentPosition);

        mPlayer.getmUrlWebView().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mWebView.loadUrl(s);
            }
        });
        mPlayer.getmPositionVideo().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer i) {
                mVideoView.seekTo(i);
            }
        });

        if (savedInstanceState != null) {
            mCurrentPosition.setValue(savedInstanceState.getInt(PLAYBACK_TIME));
            mPlayer.setmPositionVideo((mCurrentPosition));
        }

        listeChapitre = new ArrayList<Chapitre>();
        Chapitre chap1 = new Chapitre();
        Chapitre chap2 = new Chapitre("Chapitre 2", 200000, "https://www.wikipedia.org");
        Chapitre chap3 = new Chapitre("Chapitre 3", 400000, "https://fr.wiktionary.org/wiki/cucurbitacée");

        listeChapitre.add(chap1);
        listeChapitre.add(chap2);
        listeChapitre.add(chap3);

        Iterator iter = listeChapitre.iterator();
        while(iter.hasNext()){
            final Chapitre chapitre = (Chapitre)iter.next();
            Button but = new Button(this);
            but.setText(chapitre.getTitre());
            layout.addView(but);

            but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCurrentPosition.setValue(chapitre.getPosition());
                    mPlayer.setmPositionVideo(mCurrentPosition);

                    urlWebView.setValue(chapitre.getUrl());
                    mPlayer.setmUrlWebView(urlWebView);
                }
            });
        }


     /*   but3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentPosition.setValue(chap3.getPosition());
                mPlayer.setmPositionVideo(mCurrentPosition);

                urlWebView.setValue(chap3.getUrl());
                mPlayer.setmUrlWebView(urlWebView);
            }
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();

        initializePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        releasePlayer();
    }

    private void initializePlayer() {
        mBufferingTextView.setVisibility(VideoView.VISIBLE);
        Uri videoUri = getMedia(VIDEO_SAMPLE);
        mVideoView.setVideoURI(videoUri);

        mVideoView.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                            @Override
                            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                                MediaController controller = new MediaController(MainActivity.this);
                                controller.setMediaPlayer(mVideoView);
                                mVideoView.setMediaController(controller);
                                controller.setAnchorView(mVideoView);
                            }
                        });

                        mBufferingTextView.setVisibility(VideoView.INVISIBLE);

                        if (mCurrentPosition.getValue() > 0) {
                            mPlayer.setmPositionVideo(mCurrentPosition);
                        } else {
                            mCurrentPosition.setValue(1);
                            mPlayer.setmPositionVideo(mCurrentPosition);
                        }

                        mVideoView.start();
                    }
                });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Toast.makeText(MainActivity.this, "Vidéo terminée",
                    Toast.LENGTH_SHORT).show();
                mCurrentPosition.setValue(1);
                mPlayer.setmPositionVideo(mCurrentPosition);
            }
        });
    }

    private void releasePlayer() {
        mVideoView.stopPlayback();
    }

    private Uri getMedia(String media) {
        if (URLUtil.isValidUrl(media)) {
            return Uri.parse(media);
        }
        return null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(PLAYBACK_TIME, mVideoView.getCurrentPosition());
    }
}
