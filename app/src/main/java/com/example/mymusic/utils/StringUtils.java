package com.example.mymusic.utils;

import java.util.concurrent.TimeUnit;

public class StringUtils {
    private static final String TRACK_QUERY_FORMAT = "%s%s%s&%s=%s&%s=%d&%s=%d";
    private static final String BASE_URL = "https://api-v2.soundcloud.com/";
    private static final String MUSIC_GENRE = "charts?kind=top&genre=soundcloud%3Agenres%3A";
    private static final String CLIENT_ID = "client_id";
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";
    private static final String STREAM = "stream";


    public static String convertMilisecondToTimer(int milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds))
        );
    }

}
