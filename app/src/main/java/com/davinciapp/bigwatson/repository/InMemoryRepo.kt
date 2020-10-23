package com.davinciapp.bigwatson.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.davinciapp.bigwatson.model.TwitterUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryRepo @Inject constructor() {

    //DUMMY
    private val bidenTweets = "When Governor Whitmer worked to protect her state from a deadly pandemic, President Trump issued a call to \"LIBERATE MICHIGAN!\" + That call was heard.He's giving oxygen to the bigotry and hate we see on the march in our country — and we have to stop it.Jill and I are keeping the Gulf Coast in our prayers tonight. If you’re in Hurricane Delta’s path, please heed the advice of local officials, take steps to prepare, and stay safe. https://t.co/GAtLxm1n3STime and time again, President Trump has refused to condemn white supremacy and stoked the flames of hate for political gain.It’s a pattern — and America deserves better. https://t.co/o1zKKd0bpf.@CP3 and @StephenCurry30 know exactly what it means to finish strong down the stretch. We’re in the closing days of this campaign, and it’s time to finish this fight and cast our ballots.Don’t miss your shot: Register to vote today at https://t.co/eoxT07d7QB. https://t.co/Xek40JpcUcAfter last night, I just had to write a note about my friend and running mate, @KamalaHarris. https://t.co/FYPXV5dZZG.@KamalaHarris is right: If you have a pre-existing condition, the Trump Administration is coming for you.Vote like your health care depends on it — because it does.https://t.co/eoxT07d7QB https://t.co/Nf4IJZvL7ULast night, @KamalaHarris chose truth over lies.She chose hope and unity over fear and division.She showed America what true leadership looks like — and I couldn\'t be prouder.RT @CP3: .@JoeBiden great to be with you back home last week in NC!! @stephencurry30 missed you but you know what had to stop by your gym!…RT @cindymccain: Great to welcome \u2066@JoeBiden\u2069 and \u2066@KamalaHarris\u2069 to AZ! First week of voting has started. Please VOTE! https://t.co/A0Pgt6…Tune in as @KamalaHarris and I kick off our Soul of the Nation bus tour from Phoenix, Arizona. https://t.co/0BukV7xBJqEvery generation that has followed Gettysburg has been faced with a moment when it must answer this question: Will we allow the sacrifices made in that battle, and that war, to be in vain? We cannot, and we must not. It's time to come together. https://t.co/jmVGQRS4zyFolks, there was only one person on last night’s debate stage who’s fit to serve as Vice President — and her name is @KamalaHarris.I promise you that as president, I will always appeal to the best in us — not the worst. https://t.co/E07GuV4m7GI’m running as a proud Democrat. But I will govern as an American president. https://t.co/UgNPbQdNZHWe must seek not to build walls, but bridges. We must seek not to clench our fists, but to open our arms. We must seek not to tear each other apart, but to come together.Last night @KamalaHarris showed America exactly why she’ll make an incredible Vice President. https://t.co/9BCqSUrl50RT @TeamJoe: Swats away flies and lies. Get yours today: https://t.co/ehsECKfDPO https://t.co/oVLHHmq85c.@KamalaHarris, you made us all proud tonight.https://t.co/TtWm3i4eaq.RT @KamalaHarris: This is a pattern. He refused to condemn white supremacists—and then he doubled down. https://t.co/UWF6OSeZfI"
    private val dummyUser = TwitterUser(23984L, "https://img.20mn.fr/O90t8duFRemM7wmNOGd4wg/830x532_pieds-nus-jean-lassalle-font-rire-twitter.jpg", "No one", true)

    private val userSelectedMutable = MutableLiveData<TwitterUser>(dummyUser)
    val userSelected: LiveData<TwitterUser> = userSelectedMutable

    private val textToAnalyseMutable = MutableLiveData<String>(bidenTweets)
    val textToAnalyse: LiveData<String> = textToAnalyseMutable

    fun selectUser(user: TwitterUser) {
        userSelectedMutable.value = user
    }

    fun setTextToAnalyse(text: String) {
        textToAnalyseMutable.value = text
    }

}