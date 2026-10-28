package com.example.data

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.example.model.AudioTrackOption
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin

class AudioSynthEngine {

    private var audioTrack: AudioTrack? = null
    private var playbackJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    @Volatile
    private var isPlaying = false

    fun playTrack(trackOption: AudioTrackOption, durationSeconds: Int) {
        stop()
        if (trackOption == AudioTrackOption.NONE) return

        isPlaying = true
        playbackJob = scope.launch {
            try {
                val sampleRate = 22050
                val minBufferSize = AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )
                val bufferSize = (minBufferSize * 2).coerceAtLeast(sampleRate / 2)

                audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(bufferSize)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()

                audioTrack?.play()

                val buffer = ShortArray(1024)
                var sampleIndex = 0L
                val maxSamples = durationSeconds * sampleRate.toLong()

                val chordFrequencies = when (trackOption) {
                    AudioTrackOption.EPIC_ORCHESTRAL -> listOf(146.83, 174.61, 220.0, 293.66) // D minor epic
                    AudioTrackOption.BOLLYWOOD_BEATS -> listOf(164.81, 220.0, 246.94, 329.63) // Festive Yaman
                    AudioTrackOption.DEVOTIONAL_FLUTE -> listOf(261.63, 293.66, 329.63, 392.0, 493.88) // Spiritual Kalyan
                    AudioTrackOption.LOFI_CHILL -> listOf(130.81, 196.0, 246.94, 329.63) // Cmaj7 mellow
                    AudioTrackOption.CYBERPUNK_SYNTH -> listOf(110.0, 164.81, 220.0, 440.0) // A minor synth
                    AudioTrackOption.COMEDY_WHIMSICAL -> listOf(261.63, 329.63, 392.0, 523.25) // Playful C major
                    AudioTrackOption.NONE -> emptyList()
                }

                while (isActive && isPlaying && sampleIndex < maxSamples) {
                    val timeSec = sampleIndex.toDouble() / sampleRate
                    for (i in buffer.indices) {
                        val t = (sampleIndex + i).toDouble() / sampleRate
                        var sample = 0.0

                        // Harmonic chord generation
                        if (chordFrequencies.isNotEmpty()) {
                            val chordStep = ((t * 0.8).toInt()) % chordFrequencies.size
                            val baseFreq = chordFrequencies[chordStep]
                            val bassFreq = chordFrequencies[0] * 0.5

                            // Main tone + overtone + sub-bass
                            val wave1 = sin(2.0 * Math.PI * baseFreq * t)
                            val wave2 = 0.5 * sin(2.0 * Math.PI * baseFreq * 1.5 * t)
                            val subBass = 0.6 * sin(2.0 * Math.PI * bassFreq * t)

                            // Rhythmic pulsing envelope
                            val beatEnvelope = when (trackOption) {
                                AudioTrackOption.BOLLYWOOD_BEATS -> {
                                    val beatTime = (t * 2.5) % 1.0
                                    (1.0 - beatTime * 0.8).coerceIn(0.2, 1.0)
                                }
                                AudioTrackOption.CYBERPUNK_SYNTH -> {
                                    val beatTime = (t * 4.0) % 1.0
                                    (1.0 - beatTime * 0.9).coerceIn(0.1, 1.0)
                                }
                                AudioTrackOption.EPIC_ORCHESTRAL -> {
                                    0.8 + 0.2 * sin(2.0 * Math.PI * 0.5 * t)
                                }
                                else -> 0.7 + 0.3 * sin(2.0 * Math.PI * 0.3 * t)
                            }

                            sample = (wave1 + wave2 + subBass) * beatEnvelope * 0.3
                        }

                        // Soft fade out near end of duration
                        val remaining = (maxSamples - (sampleIndex + i)).toDouble() / sampleRate
                        if (remaining < 0.5) {
                            sample *= (remaining / 0.5).coerceIn(0.0, 1.0)
                        }

                        val shortVal = (sample.coerceIn(-1.0, 1.0) * Short.MAX_VALUE).toInt().toShort()
                        buffer[i] = shortVal
                    }

                    audioTrack?.write(buffer, 0, buffer.size)
                    sampleIndex += buffer.size
                }
            } catch (_: Exception) {
                // Audio safely completed or cancelled
            } finally {
                stopTrackOnly()
            }
        }
    }

    private fun stopTrackOnly() {
        try {
            audioTrack?.apply {
                if (playState == AudioTrack.PLAYSTATE_PLAYING) {
                    stop()
                }
                release()
            }
        } catch (_: Exception) {}
        audioTrack = null
    }

    fun stop() {
        isPlaying = false
        playbackJob?.cancel()
        playbackJob = null
        stopTrackOnly()
    }
}
