/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;


/**
 * Basic TTS class containing tts method calling in another thread
 * @author Jachym
 */
public class TextToSpeech implements Runnable
{
    private static final String VOICE_NAME = "kevin16";
    private Thread t;
    private Voice voice;
    private String text;
    
    private TextToSpeech(String s)
    {
        VoiceManager vm = VoiceManager.getInstance();
        voice = vm.getVoice(VOICE_NAME);
        voice.allocate();
        text = s;
    }
    /**
     * speak the s tring in another thread
     * @param s text to speech
     */
    public static void speak(String s)
    {
        TextToSpeech tts = new TextToSpeech(s);
        Thread t;
        try
        {
            t = new Thread (tts, "TTS");
            t.start ();
        }
        catch(Exception e)
        {
        }
    }

    @Override
    public void run()
    {
        try
        {
            voice.speak(text);
        }
        catch (Exception e)
        {
            System.out.println("TTS Thread interrupted.");
        }
    }
}
