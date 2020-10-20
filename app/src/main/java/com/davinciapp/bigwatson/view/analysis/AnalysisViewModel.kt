package com.davinciapp.bigwatson.view.analysis

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davinciapp.bigwatson.repository.InMemoryRepo
import com.davinciapp.bigwatson.repository.WatsonRepo
import com.ibm.watson.personality_insights.v3.model.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class AnalysisViewModel @ViewModelInject constructor(
    private val watsonRepo: WatsonRepo,
    inMemoryRepo: InMemoryRepo
) : ViewModel() {

    val message = MutableLiveData<String>()

    val userLiveData = inMemoryRepo.userSelected

    private val personality = MutableLiveData<List<Pair<String, Int>>>()
    val personalityLiveData: LiveData<List<Pair<String, Int>>> = personality

    /*
    val data = MutableLiveData(listOf(
        Pair("Agreableness", 10F),
        Pair("Conscientiousness", 99F),
        Pair("Emotional range", 0F),
        Pair("NAnananana", 50F),
        Pair("Et oh là oui", 60F),
    ))

     */


    init {
        inMemoryRepo.textToAnalyse.value?.let {text ->
            //DUMMY
            personality.value =  listOf(
                Pair("Agreableness", 10),
                Pair("Conscientiousness", 99),
                Pair("Emotional range", 0),
                Pair("NAnananana", 50),
                Pair("Et oh là oui", 60),
            )

            /*
            viewModelScope.launch(Dispatchers.IO) {

                val profile: Profile
                try {
                    profile = watsonRepo.runPersonalityInsight(text)
                } catch (e : RuntimeException) {
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
                
             */



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


        }
    }

}