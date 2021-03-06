package com.shimhg02.jjan.web_communication;

public class WebConstants {


    /* To try the app with Enablex hosted service you need to set the kTry = true */
        public  static  final  boolean kTry = true;

    /*Your webservice host URL, Keet the defined host when kTry = true */

        public static final String kBaseURL = " https://jjan.andy0414.com/enablex/";

    /*The following information required, Only when kTry = true, When you hosted your own webservice remove these fileds*/

    /*Use enablec portal to create your app and get these following credentials*/
        public static final String kAppId = "5f0e41ea90ef80887a2f91c2";
        public static final String kAppkey = "eta9uru9u6e5usa3aGa7uyyGaEeDemuEyseG";


        public static final String getRoomId = "v1/rooms/";
        public static final int getRoomIdCode = 1;
        public static final String validateRoomId = "v1/rooms/";
        public static final int validateRoomIdCode = 6;
        public static final String getTokenURL = "v1/rooms/tokens/";
        public static final int getTokenURLCode = 3;
}
