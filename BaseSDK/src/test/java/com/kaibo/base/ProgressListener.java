package com.kaibo.base;

/**
 * @author Administrator
 * @date 2017/11/30 0030 14:07
 * @description
 */
public interface ProgressListener {
    /**
     * 上传进度回调
     *
     * @param progress 当前进度    范围  0-1
     */
    void onProgress(float progress);
}
