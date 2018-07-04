package com.kaibo.core.util

/**
 * @author kaibo
 * @date 2018/6/20 10:17
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */
object EmulatorCheckUtil {

    //逍遥安卓模拟器能模拟cpu信息
    fun readCpuInfo(): String {
        return CommandUtil.exec("cat /proc/cpuinfo")
    }

    //逍遥安卓模拟器读取不到该文件
    fun readUidInfo(): Boolean {
        return CommandUtil.exec("cat /proc/self/cgroup").isEmpty()
    }

    fun readSysProperty(): Boolean {
        var suspectCount = 0

        val baseBandVersion = CommandUtil.getProperty("gsm.version.baseband")
        if ((baseBandVersion == null) or ("" == baseBandVersion)) ++suspectCount

        val buildFlavor = CommandUtil.getProperty("ro.build.flavor")
        if ((buildFlavor == null) or ("" == buildFlavor) or (buildFlavor != null && buildFlavor.contains("vbox")))
            ++suspectCount

        val productBoard = CommandUtil.getProperty("ro.product.board")
        if ((productBoard == null) or ("" == productBoard)) ++suspectCount

        val boardPlatform = CommandUtil.getProperty("ro.board.platform")
        if ((boardPlatform == null) or ("" == boardPlatform)) ++suspectCount

        if (productBoard != null && boardPlatform != null && productBoard != boardPlatform)
            ++suspectCount

        val filter = CommandUtil.exec("cat /proc/self/cgroup")
        if (filter.isEmpty()) ++suspectCount

        return suspectCount > 2
    }
}
