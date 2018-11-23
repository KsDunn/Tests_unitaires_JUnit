package fr.enssat.jovepoirier.playervideo;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
public class MainActivity extends AppCompatActivity {

    private static final String VIDEO_SAMPLE = "https://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4";
    private static final String PLAYBACK_TIME = "play_time";
    private TextView mBufferingTextView;
    private VideoView mVideoView;
    private WebView mWebView;
    private PlayerViewModel mPlayer;
    private String WEBVIEW_URL = "https://en.wikipedia.org/wiki/Big_Buck_Bunny";
    private int mCurrentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoView = findViewById(R.id.videoview);
        mWebView = findViewById(R.id.webview);
        mWebView.setWebViewClient(new WebViewClient());

        mPlayer = ViewModelProviders.of(this).get(PlayerViewModel.class);

        final Observer<String> urlObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mWebView.loadUrl(s);
            }
        };

        final Observer<Integer> positionObserver = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer i) {
                mVideoView.seekTo(i);
            }
        };

        mPlayer.getUrlWebView().observe(this, urlObserver);

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }

        mBufferingTextView = findViewById(R.id.buffering_textview);
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

                        if (mCurrentPosition > 0) {
                            mVideoView.seekTo(mCurrentPosition);
                        } else {
                            mVideoView.seekTo(1);
                        }

                        mVideoView.start();
                    }
                });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Toast.makeText(MainActivity.this, "Vidéo terminée",
                    Toast.LENGTH_SHORT).show();
                mVideoView.seekTo(1);
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

    public String getWEBVIEW_URL() {
        return WEBVIEW_URL;
    }

    public void setWEBVIEW_URL(String WEBVIEW_URL) {
        this.WEBVIEW_URL = WEBVIEW_URL;
    }


}
