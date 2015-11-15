package com.decoder.util;

public class DecH264 {
    public native int InitDecoder();    //return: >0: success; <0 fail

    public native int UninitDecoder();    //return: always 1

    public native int DecoderNal(byte[] in, int insize, int[] gotPicture, byte[] out);

    static {
        try {
            System.loadLibrary("H264Android");
        } catch (UnsatisfiedLinkError ule) {
            System.out.println("loadLibrary(H264Android)," + ule.getMessage());
        }
    }
}
