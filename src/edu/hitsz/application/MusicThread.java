package edu.hitsz.application;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.*;

/**
 * 改进版音乐控制线程
 * 基于原有预加载逻辑，增加循环播放与中断停止功能
 */
public class MusicThread extends Thread {

    private String filename;
    private AudioFormat audioFormat;
    private byte[] samples;

    // 【新增】循环标志与停止标志
    private boolean loop;
    private volatile boolean isStopped = false;

    /**
     * 修改后的构造函数
     * @param filename 路径
     * @param loop 是否循环 (BGM传true, 音效传false)
     */
    public MusicThread(String filename, boolean loop) {
        this.filename = filename;
        this.loop = loop;
        reverseMusic();
    }

    /**
     * 【新增】外部调用此方法可安全停止线程
     */
    public void stopMusic() {
        this.isStopped = true;
    }

    public void reverseMusic() {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filename));
            audioFormat = stream.getFormat();
            samples = getSamples(stream);
            stream.close();
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getSamples(AudioInputStream stream) {
        int size = (int) (stream.getFrameLength() * audioFormat.getFrameSize());
        byte[] samples = new byte[size];
        try (DataInputStream dataInputStream = new DataInputStream(stream)) {
            dataInputStream.readFully(samples);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return samples;
    }

    public void play(InputStream source) {
        int size = (int) (audioFormat.getFrameSize() * audioFormat.getSampleRate());
        byte[] buffer = new byte[size];
        SourceDataLine dataLine = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

        try {
            dataLine = (SourceDataLine) AudioSystem.getLine(info);
            dataLine.open(audioFormat, size);
            dataLine.start();

            int numBytesRead = 0;
            // 【关键修改】增加 !isStopped 判定
            while (!isStopped && (numBytesRead = source.read(buffer, 0, buffer.length)) != -1) {
                dataLine.write(buffer, 0, numBytesRead);
            }

        } catch (LineUnavailableException | IOException ex) {
            ex.printStackTrace();
        } finally {
            if (dataLine != null) {
                dataLine.drain();
                dataLine.stop();
                dataLine.close();
            }
        }
    }

    @Override
    public void run() {
        // 使用 do-while 实现循环逻辑
        do {
            InputStream stream = new ByteArrayInputStream(samples);
            play(stream);
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 如果不是循环模式，或者已经被叫停，则跳出
        } while (loop && !isStopped);
    }
}