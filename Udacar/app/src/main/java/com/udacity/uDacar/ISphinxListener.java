package com.udacity.uDacar;

/**
 * Created by ismail.khan2 on 11/21/2016.
 */

public interface ISphinxListener {

    public void sphinxFailedRecognition(String msg);
    public void sphinxError(String msg);
    public void sphinxHintMsg(String msg);
    public void callGoogleSpeechRecognition();
}
