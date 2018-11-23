package fr.enssat.jovepoirier.playervideo;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class PlayerViewModel extends ViewModel {
    private MutableLiveData<String> mUrlWebView;
    private MutableLiveData<Integer> mPositionVideo;

    public MutableLiveData<String> getUrlWebView() {
        if(mUrlWebView == null) {
            mUrlWebView = new MutableLiveData<String>();
        }
        return mUrlWebView;
    }

    public MutableLiveData<Integer> getPositionVideo() {
        if(mPositionVideo == null) {
            mPositionVideo = new MutableLiveData<Integer>();
        }
        return mPositionVideo;
    }

}
