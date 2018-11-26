package fr.enssat.jovepoirier.playervideo;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class PlayerViewModel extends ViewModel {

    private MutableLiveData<String> mUrlWebView;
    private MutableLiveData<Integer> mPositionVideo;

    public PlayerViewModel(MutableLiveData<String> mUrlWebView, MutableLiveData<Integer> mPositionVideo) {
        this.mUrlWebView = mUrlWebView;
        this.mPositionVideo = mPositionVideo;
    }

    public MutableLiveData<String> getmUrlWebView() {
        return mUrlWebView;
    }

    public void setmUrlWebView(MutableLiveData<String> mUrlWebView) {
        this.mUrlWebView.setValue(mUrlWebView.getValue());
    }

    public MutableLiveData<Integer> getmPositionVideo() {
        return mPositionVideo;
    }

    public void setmPositionVideo(MutableLiveData<Integer> mPositionVideo) {
        this.mPositionVideo.setValue(mPositionVideo.getValue());
    }
}
