package com.udacity.uDacar;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

/**
 * Created by ismail.khan2 on 11/21/2016.
 */

public class SphinxSpeechRecognition implements RecognitionListener {

    private static final String TAG = SphinxSpeechRecognition.class.getSimpleName();

    /* Named searches allow to quickly reconfigure the decoder */
    private static final String KWS_SEARCH = "wakeup";
    //private static final String DIGITS_SEARCH = "digits";
    /* Keyword we are looking for to activate menu */
    private static final String KEYPHRASE = "directions";

    /* Used to handle permission request */

    private SpeechRecognizer recognizer;

    Context mContext;

    ISphinxListener mListener;

    public SphinxSpeechRecognition(Context context, ISphinxListener listener){
        mContext = context;
        mListener = listener;
    }

    public void runRecognizer(){
        runRecognizerSetup();
    }

    private void runRecognizerSetup() {
        // Recognizer initialization is a time-consuming and it involves IO,
        // so we execute it in async task
        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(mContext);
                    File assetDir = assets.syncAssets();
                    setupRecognizer(assetDir);
                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    if(mListener != null){
                        mListener.sphinxFailedRecognition("Failed to init recognizer " + result);
                    }

                } else {
                    switchSearch(KWS_SEARCH);
                }
            }
        }.execute();
    }

    public void startListeningWakeup(){
        if(recognizer != null){
            recognizer.startListening(KWS_SEARCH);
        }
    }
    private void switchSearch(String searchName) {
        //recognizer.stop();


        // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
        if (searchName.equals(KWS_SEARCH))
            recognizer.startListening(searchName);
        else
            recognizer.startListening(searchName, 10000);

        //String caption = getResources().getString(captions.get(searchName));
        if(mListener != null){
            mListener.sphinxHintMsg("Say 'directions' to activate speech recognition");
        }
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them

        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))

                .setRawLogDir(assetsDir) // To disable logging of raw audio comment out this call (takes a lot of space on the device)
                //.setKeywordThreshold(1e-20f)
                .getRecognizer();
        recognizer.addListener(this);

        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    /**
     * We stop recognizer here to get a final result
     */
    @Override
    public void onEndOfSpeech() {
        switchSearch(KWS_SEARCH);
    }

    /**
     * In partial result we get quick updates about current hypothesis. In
     * keyword spotting mode we can react here, in other modes we need to wait
     * for final result in onResult.
     */
    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;

        String text = hypothesis.getHypstr();
        Log.d(TAG, "onPartialResult text: " + text);
        Toast.makeText(mContext,
                "onPartialResult: " + text,
                Toast.LENGTH_SHORT).show();
        if (text.equals(KEYPHRASE)) {
            if(mListener != null){
                recognizer.stop();
                mListener.callGoogleSpeechRecognition();
            }
           /* //switchSearch(MENU_SEARCH);



            */
        }
    }

    /**
     * This callback is called when we stop the recognizer.
     */
    @Override
    public void onResult(Hypothesis hypothesis) {
        //((TextView) findViewById(R.id.result_text)).setText("");
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            /*Toast.makeText(mContext,
                    "onResult: " + text,
                    Toast.LENGTH_SHORT).show();*/
        }
    }

    @Override
    public void onError(Exception e) {
        if(mListener != null){
            mListener.sphinxError(e.getMessage());
        }
    }

    @Override
    public void onTimeout() {
        switchSearch(KWS_SEARCH);
    }

    public void stop(){
        if (recognizer != null) {
            recognizer.stop();
        }
    }
    public void destroy(){
        if (recognizer != null) {
            recognizer.cancel();
            recognizer.shutdown();
        }
    }

}

