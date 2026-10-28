package com.example.data

import android.util.Log
import com.example.model.CameraMotion
import com.example.model.VideoStyle
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout

private const val TAG = "GeminiPromptService"

class GeminiPromptAssistantService {

    // Lazy initialization as mandated by Firebase AI Logic skill
    private val model by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            generationConfig = generationConfig {
                temperature = 0.7f
                topP = 0.95f
            }
        )
    }

    suspend fun enhanceVideoPrompt(
        rawIdea: String,
        style: VideoStyle,
        motion: CameraMotion
    ): Result<String> {
        val trimmed = rawIdea.trim()
        if (trimmed.isEmpty()) {
            return Result.failure(IllegalArgumentException("Prompt idea cannot be empty"))
        }

        val prompt = """
            You are an elite cinematic director and AI video generator prompt engineer.
            Transform this user idea into a single, breathtaking, hyper-detailed video prompt:
            User Idea: "$trimmed"
            Video Style: ${style.titleEn} (${style.description})
            Camera Motion: ${motion.labelEn}
            
            Requirements:
            1. Describe the subject, scene, camera angle, lighting, volumetric atmosphere, and movement in detail.
            2. Match the specific aesthetic of ${style.titleEn} (${style.promptBooster}).
            3. Keep the output as a single continuous prompt paragraph under 75 words.
            4. Do not include markdown fences, bullets, or labels. Return ONLY the enhanced prompt text.
        """.trimIndent()

        return try {
            // Mandated: realistic 30-60s timeout for model inference
            val response = withTimeout(45_000L) {
                model.generateContent(prompt)
            }
            val text = response.text?.trim()
            if (!text.isNullOrBlank()) {
                Log.d(TAG, "Gemini enhanced prompt successfully: $text")
                Result.success(text)
            } else {
                Log.w(TAG, "Gemini returned empty response, using template fallback")
                Result.success(PromptAssistantData.expandPrompt(trimmed, style))
            }
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Gemini expansion timed out", e)
            Result.success(PromptAssistantData.expandPrompt(trimmed, style))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Gemini expansion failed, using local cinematic fallback", e)
            Result.success(PromptAssistantData.expandPrompt(trimmed, style))
        }
    }
}
