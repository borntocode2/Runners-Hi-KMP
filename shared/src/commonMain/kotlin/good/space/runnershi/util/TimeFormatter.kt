package good.space.runnershi.util

object TimeFormatter {
    /**
     * 초 단위를 mm:ss 문자열로 변환한다.
     */
    fun formatSecondsToTime(totalSeconds: Long): String {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return if (hours > 0) {
            "${pad(hours)}:${pad(minutes)}:${pad(seconds)}"
        } else {
            "${pad(minutes)}:${pad(seconds)}"
        }
    }

    private fun pad(number: Long): String {
        return if (number < 10) "0$number" else "$number"
    }
}

