package com.peterung.redditzen.data.api.model;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class Preview {
    public List<Image> images;

    @Parcel
    public static class Image {
        public Info source;
        public List<Info> resolutions;

        @Parcel
        public static class Info {
            public String url;
            public int width;
            public int height;
        }
    }
}
