package awslextest;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;
/*
        Define an audio format of the sound source to be captured, using the class AudioFormat.
        Create a DataLine.Info object to hold information of a data line.
        Obtain a TargetDataLine object which represents an input data line from which audio data can be captured, using the method getLineInfo(DataLine.Info) of the AudioSystem class.
        Open and start the target data line to begin capturing audio data.
        Create an AudioInputStream object to read data from the target data line.
        Record the captured sound into a WAV file using the following method of the class AudioSystem:
            write(AudioInputStream, AudioFileFormat.Type, File)
        Note that this method blocks the current thread until the target data line is closed.

        Stop and close the target data line to end capturing and recording.

 */
public class SoundRecorder {
    // record duration, in milliseconds
    static final long RECORD_TIME = 10000;  // 1 minute

    // path of the wav file
    private File wavFile;

    // format of audio file
    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

    // the line from which audio data is captured
    TargetDataLine line;
    AudioFormat format;
    private String sessionId;

    private S3Client s3;
    private static final Region region = Region.AWS_GLOBAL;
    private String bucketName = "demo-bucket-lex";

//    private ArrayList<CompletedPart> completedParts;
//    private int completedPartsCount;
//    private String uploadId;



    SoundRecorder(String sessionId){

        this.wavFile = new File(String.format("voice_sig_files/%s.wav",sessionId));
        this.sessionId = sessionId;

    }
    /**
     * Defines an audio format
     */
    AudioFormat getAudioFormat() {
        float sampleRate = 8000;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                channels, signed, bigEndian);
        return format;
    }

    File getWavFile(){
        return this.wavFile;
    }
    void bootUp(){
        try {
            this.format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, this.format);

            // checks if system supports the data line
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported");
                System.exit(0);
            }
            this.line = (TargetDataLine) AudioSystem.getLine(info);
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Captures the sound and record into a WAV file
     */
    void start() {
        try {
            this.line.open(this.format);
            this.line.start();   // start capturing

            System.out.println("Start capturing user's voice signature...");

            AudioInputStream ais = new AudioInputStream(this.line);

            System.out.println("Start recording user's voice signature...");

            // start recording
            AudioSystem.write(ais, fileType, wavFile);


        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        catch (LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Closes the target data line to finish capturing and recording
     */
    void finish() {
        //line.stop();
        line.close();
        System.out.println("Not recording user's voice signature currently.");
    }

    

    private static ByteBuffer getRandomByteBuffer(int size) throws IOException {
        byte[] b = new byte[size];
        new Random().nextBytes(b);
        return ByteBuffer.wrap(b);
    }

    /**
     * Entry to run the program
     */
    public static void main(String[] args) {
        SoundRecorder recorder = new SoundRecorder("4");

        Thread stopper = new Thread(new Runnable() {
            public void run() {
                    try {
                        Thread.sleep(RECORD_TIME);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    recorder.finish();
                }
            });

            stopper.start();

            // start recording
            recorder.bootUp();
            recorder.start();
        // creates a new thread that waits for a specified
        // of time before stopping
//        Thread stopper = new Thread(new Runnable() {
//            public void run() {
//                try {
//                    Thread.sleep(RECORD_TIME);
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
//                recorder.finish();
//            }
//        });
//
//        stopper.start();
//
//        // start recording
//        recorder.bootUp();
//        recorder.start();
    }
}
