package com.davinciapp.bigwatson.view.analysis

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davinciapp.bigwatson.model.TwitterUser
import com.davinciapp.bigwatson.repository.AnalysisRepo
import com.davinciapp.bigwatson.repository.InMemoryRepo
import com.davinciapp.bigwatson.repository.UserRepo
import com.davinciapp.bigwatson.repository.WatsonRepo
import com.ibm.watson.personality_insights.v3.model.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import twitter4j.Twitter
import kotlin.math.roundToInt

class AnalysisViewModel @ViewModelInject constructor(
    private val watsonRepo: WatsonRepo,
    private val inMemoryRepo: InMemoryRepo,
    private val userRepo: UserRepo,
    private val analysisRepo: AnalysisRepo
) : ViewModel() {

    val message = MutableLiveData<String>()

    val userLiveData = inMemoryRepo.userSelected

    private var userStored = MutableLiveData<Boolean>()
    val userStoredLiveData: LiveData<Boolean> = userStored

    private val personality = MutableLiveData<List<Pair<String, Int>>>()
    val personalityLiveData: LiveData<List<Pair<String, Int>>> = personality

    fun setSearchStrategy(isFromFavorite: Boolean) {
        if (personalityLiveData.value != null) return //No need for multiple loads when activity destroyed

        if (isFromFavorite) {
            userStored.value = true
            message.value = "FETCHING IN DATABASE"
            fetchFromDb()
        } else {
            message.value = "FETCHING FROM SERVER"
            fetchFromServer()
        }
    }

    fun handleUserStorage() {
        userStored.value ?: return

        if (userStoredLiveData.value!!)
            deleteUserProfile()
        else
            saveUserProfile()
    }

    private fun saveUserProfile() {
        val user = inMemoryRepo.userSelected.value ?: return
        val analysis = personalityLiveData.value ?: return

        val id = user.id
        //Check if exist again ?
        viewModelScope.launch(Dispatchers.IO) {
            userRepo.insert(user)
            analysisRepo.insert(
                Analysis(id, analysis[0].second, analysis[1].second, analysis[2].second,
                    analysis[3].second, analysis[4].second)
            )
            withContext(Dispatchers.Main) {
                userStored.value = true
                message.value = "Added to favorites"
            }
        }
    }

    private fun deleteUserProfile() {
        val user = inMemoryRepo.userSelected.value ?: return

        viewModelScope.launch(Dispatchers.IO) {
            userRepo.delete(user)
            analysisRepo.delete(user.id)

            withContext(Dispatchers.Main) {
                userStored.value = false
                message.value = "Deleted from favorites"
            }
        }
    }

    //--------------------------------------------------------------------------------------------//
    //                                  F R O M    S E R V E R
    //--------------------------------------------------------------------------------------------//
    private fun fetchFromServer() {
        /*
       inMemoryRepo.textToAnalyse.value?.let {text ->
           //DUMMY
           personality.value =  listOf(
               Pair("Agreableness", 10),
               Pair("Conscientiousness", 99),
               Pair("Emotional range", 0),
               Pair("NAnananana", 50),
               Pair("Et oh là oui", 60),
           )

        */
        //Check if stored
        checkIfStoredInDb(inMemoryRepo.userSelected.value!!.id)

        viewModelScope.launch(Dispatchers.IO) {

            val text = inMemoryRepo.textToAnalyse.value
            if (text.isNullOrEmpty()) return@launch

            val profile: Profile
            try {
                profile = watsonRepo.runPersonalityInsight(text)
            } catch (e: RuntimeException) {
                message.postValue("Error loading personality...")
                Log.e("exception", "Error running personality insight.")
                return@launch
            }

            val data = MutableList(5) { Pair("", 0) }

            for (i in 0 until profile.personality.size) {
                val name = profile.personality[i].name
                val percentile = (profile.personality[i].percentile * 100).roundToInt()
                data[i] = Pair(name, percentile)
            }

            withContext(Dispatchers.Main) {
                personality.value = data
            }
        }
    }

    private fun checkIfStoredInDb(userId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val isStored =  userRepo.getAllId().contains(userId)
            withContext(Dispatchers.Main) { userStored.value = isStored }
        }
    }


        /*
                for (p in profile.personality) {
                    val id = p.traitId
                    val name = p.name
                    val perc = p.percentile

                    Log.d("debuglog", "$id $name: $perc")
                }
                Log.d("debuglog", "----------------------")


                for (p in profile.values) {
                    val id = p.traitId
                    val name = p.name
                    val perc = p.percentile

                    Log.d("debuglog", "$id $name: $perc")
                }
                Log.d("debuglog", "----------------------")


                for (p in profile.needs) {
                    val id = p.traitId
                    val name = p.name
                    val perc = p.percentile

                    Log.d("debuglog", "$id $name: $perc")
                }

                 */


    //--------------------------------------------------------------------------------------------//
    //                                  F R O M    D A T A B A S E
    //--------------------------------------------------------------------------------------------//
    private fun fetchFromDb() {
        //DUMMY
        val user = inMemoryRepo.userSelected.value ?: return

        viewModelScope.launch {

            val analysis = analysisRepo.getAnalysis(user.id)

            personality.value = listOf(
                Pair("Openess", analysis.agreeableness),
                Pair("Conscientiousness", analysis.conscientiousness),
                Pair("Extraversion", analysis.emotionalRange),
                Pair("Agreeableness", analysis.extraversion),
                Pair("Emotional Range", analysis.openness)
            )
        }
    }

}