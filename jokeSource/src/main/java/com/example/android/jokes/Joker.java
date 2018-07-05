package com.example.android.jokes;

import java.util.Random;

public class Joker {
    //source: https://short-funny.com
    public static final String[] mJokes = {
            "Can a kangaroo jump higher than a house? - Of course, a house doesn't jump at all.",
            "A wife complains to her husband:\n" +
                    "Just look at that couple down the road, how lovely they are.\n" +
                    "He keeps holding her hand, kissing her, holding the door for her, why can't you do the same?\n\n" +
                    "The husband: Are you mad? I barely know the woman!",
            "Three guys are stranded in a desert. By a stroke of luck, they find a magic genie lamp."+ "\n" +
                    "The genie grants each of them one wish." + "\n" +
                    "The first guy wishes to be back home. Wish granted." + "\n" +
                    "The second guy wishes the same. Wish granted." + "\n" +
                    "The third guy says, It feels very lonely here now, I wish my friends were with me... Wish granted."};

    private int mID;

    public Joker(){
        mID = new Random().nextInt(mJokes.length);
    }

    public String getJoke() {
        return mJokes[mID];
    }
    public int getJokeId(){
        return mID;
    }
}
