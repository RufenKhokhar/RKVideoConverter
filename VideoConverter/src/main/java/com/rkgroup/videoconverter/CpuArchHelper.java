package com.rkgroup.videoconverter;

import android.os.Build;
import android.util.Log;

public class CpuArchHelper {
    public static final String X86_CPU = "x86";
    public static final String X86_64_CPU = "x86_64";
    public static final String ARM_64_CPU = "arm64-v8a";
    public static final String ARM_V7_CPU = "armeabi-v7a";
    private static final String TAG = "CpuArchHelper";

    public static CpuArch getCpuArch() {
        Log.d(TAG, "Build.CPU_ABI : " + Build.CPU_ABI);

        switch (Build.CPU_ABI) {
            case X86_CPU:
            case X86_64_CPU:
                return CpuArch.x86;
            case ARM_64_CPU:
            case ARM_V7_CPU:
                return CpuArch.ARMv7;
            default:
                return CpuArch.NONE;
        }
    }
}