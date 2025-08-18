package com.example.weatherinsight.dto.geocoder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeocoderResponse {
    private Response response;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Service service;
        private String status;
        private Input input;
        private Refined refined;
        private Result result;
        private Record record;
        private Page page;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Service {
            private String name;
            private String version;
            private String operation;
            private String time;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Input {
            private String type;
            private String address;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Refined {
            private String text;
            private Structure structure;

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            @Builder
            public static class Structure {
                private String level0;
                private String level1;
                private String level2;
                private String level3;
                private String level4L;
                private String level4LC;
                private String level4A;
                private String level4AC;
                private String level5;
                private String detail;
            }
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Result {
            private String crs;
            private Point point;

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            @Builder
            public static class Point {
                private Double x;
                private Double y;
            }
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Record {
            private String total;
            private String current;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Page {
            private String total;
            private String current;
            private String size;
        }
    }
}