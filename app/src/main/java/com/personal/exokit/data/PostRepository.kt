package com.personal.exokit.data

class PostRepository {

    fun getPosts(): List<TextPost> {
        return listOf(
            TextPost(
                title = "Thoughts from the Pool",
                text = "Is it just me, or does the pool always reflect the existential dread of an uncaring universe? Also, why is the water so cold?"
            ),
            TextPost(
                title = "Princess Carolyn's 100th Call",
                text = "I’m not just juggling plates; I’m juggling plates while riding a unicycle on a tightrope over a canyon. No big deal!"
            ),
            TextPost(
                title = "Diane's Memoir Draft",
                text = "Do I write about the truth or what people want to hear? Also, why does my coffee always go cold before I finish it?"
            ),
            TextPost(
                title = "Peanutbutter’s Morning Pep Talk",
                text = "Every day is a new beginning! Unless it’s a sequel, then it’s just the same beginning rehashed, like a Hollywood movie."
            ),
            TextPost(
                title = "BoJack’s Friday Night",
                text = "Friday nights are like my career: a series of bad decisions fueled by questionable choices and overpriced whiskey."
            ),
            TextPost(
                title = "Todd’s Business Idea #78",
                text = "A theme park where all the rides are office chairs that spin you around. I call it 'Chair-ousel Land.' Investors, call me!"
            ),
            TextPost(
                title = "Sarah Lynn’s Big Plan",
                text = "Party all night, sing all day, and somehow still make people think I’m deep. Spoiler: I’m not deep, I’m just drunk."
            ),
            TextPost(
                title = "Herb’s Script Notes",
                text = "This sitcom needs more heart. And by heart, I mean at least one character who isn’t completely broken inside."
            ),
            TextPost(
                title = "Vincent Adultman’s Office Day",
                text = "Today I did a business deal and then filed my taxes. Definitely not three kids in a trench coat. Nope, not me."
            ),
            TextPost(
                title = "BoJack’s Diet Tip #3",
                text = "Eat all your feelings. When that doesn’t work, add a bag of chips. Emotional stability is overrated anyway."
            ),
            TextPost(
                title = "Peanutbutter’s Inspirational Tweet",
                text = "If life gives you lemons, make lemonade. Then sell it on a TV show about a struggling actor who drinks too much!"
            ),
            TextPost(
                title = "Diane’s Latest Blog",
                text = "Can one person really make a difference? Or is the best we can do just making snarky comments on the internet?"
            ),
            TextPost(
                title = "Todd’s New Invention",
                text = "What if we had a couch that also made toast? I call it the 'Toast Lounger.' Patent pending, obviously."
            ),
            TextPost(
                title = "Princess Carolyn’s Latest Pitch",
                text = "Picture this: A romantic comedy where the leads are too busy texting to actually fall in love. Modern, right?"
            ),
            TextPost(
                title = "BoJack’s Daily Affirmation",
                text = "I am smart. I am capable. I am... kidding myself if I think this affirmation thing is actually working."
            ),
            TextPost(
                title = "Sarah Lynn’s Song Idea",
                text = "What if we made a song where the chorus is just 'La la la' repeated 17 times? Lazy or genius? You decide."
            ),
            TextPost(
                title = "Mr. Peanutbutter’s Latest Idea",
                text = "A reality show where we adopt stray dogs, but the twist is, the dogs adopt us. Working title: 'Who’s a Good Human?'"
            ),
            TextPost(
                title = "Diane’s Favorite Quote",
                text = "Sometimes I wish life were more like a book: well-edited, coherent, and with fewer ads for weight-loss tea."
            ),
            TextPost(
                title = "BoJack’s Apology #12",
                text = "I’m sorry for what I said when I was sober. Actually, no, I’m not. Sober BoJack doesn’t make promises drunk BoJack can’t keep."
            ),
            // ...Add 80 more unique posts
        ) + generateMorePosts()
    }

    private fun generateMorePosts(): List<TextPost> {
        return List(80) { index ->
            TextPost(
                title = "BoJack Post #${index + 21}",
                text = "This is a filler post, but don’t worry—it’s written with the same existential despair as the rest of the show."
            )
        }
    }
}

data class TextPost(
    val title: String,
    val text: String
)

