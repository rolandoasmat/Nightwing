package com.asmat.rolando.popularmovies.models;

import com.asmat.rolando.popularmovies.networking.models.Video;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class VideoTest {
    Video video;

    @Before
    public void setUp() {
        video = getTestObject();
    }

    @After
    public void tearDown() {
        video = null;
    }

    @Test
    public void getYouTubeURL() {
        String actual = video.getYouTubeURL();
        String expected = "https://www.youtube.com/watch?v=cSp1dM2Vj48";
        assertEquals(actual, expected);
    }

    @Test
    public void getYouTubeThumbnailURL() {
        String actual = video.getYouTubeThumbnailURL();
        String expected = "http://img.youtube.com/vi/cSp1dM2Vj48/0.jpg";
        assertEquals(actual, expected);
    }

    // Helper method to generate a test object
    private Video getTestObject() {
        String name  = "READY PLAYER ONE - Official Trailer 1 [HD]";
        String site  = "YouTube";
        String key  = "cSp1dM2Vj48";
        String type = "Trailer";
        return new Video(name, site, key, type);
    }
}