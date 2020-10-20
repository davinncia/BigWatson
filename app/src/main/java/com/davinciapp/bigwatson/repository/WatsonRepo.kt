package com.davinciapp.bigwatson.repository

import com.ibm.cloud.sdk.core.http.Response
import com.ibm.cloud.sdk.core.security.IamAuthenticator
import com.ibm.watson.personality_insights.v3.PersonalityInsights
import com.ibm.watson.personality_insights.v3.model.Profile
import com.ibm.watson.personality_insights.v3.model.ProfileOptions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WatsonRepo @Inject constructor() {

    //TODO hide
    private val watsonApiKey = "xxx"
    private val watsonUrl = "xxx"

    private val personalityInsight: PersonalityInsights

    init {
        val authenticator = IamAuthenticator(watsonApiKey)
        personalityInsight = PersonalityInsights("2017-10-13", authenticator)
        personalityInsight.serviceUrl = watsonUrl
    }

    suspend fun runPersonalityInsight(text: String) : Profile {

        val profileOptions = ProfileOptions.Builder()
            .text(text)
            //.consumptionPreferences(true)
            //.rawScores(true)
            .build()

            return personalityInsight.profile(profileOptions).execute().result
    }

}