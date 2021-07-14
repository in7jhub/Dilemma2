package com.game.dilemma2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class MainActivity extends AppCompatActivity{
    final VideoView[] videoView = new VideoView[6];
    ImageView leftPerson;
    ImageView rightPerson;
    ImageView longchim;
    ImageView shortchim;
    ImageView clock;
    ImageView gestureArea;
    GestureDetector detector;

    boolean gestureOn = false;
    boolean characterMovementOn = false;
    boolean characterVisibilityOn = false;
    boolean spinnerGestureOn = false;
    int imgAlpha = 0;
    boolean charVisVisit = true;
    boolean ending = true;
    double originX, originY;

    //_____________________________________________________   UDP  ____________________________________________________
    public static final int port = 5959;
    boolean appStart = true;
    boolean isUdpReqExists = false;
    DatagramSocket socket;
    boolean isAlertDisplayed = false;
    boolean isPortUnstable = false;

    boolean isWiFiConnected(){
        Context context = getApplicationContext();
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());
        return isConnected;
    }
    void socketInit() {
        try {
            socket = new DatagramSocket(port);
        } catch (Exception e) {
            isPortUnstable = true;
        }
    }
    void run_udp_thd(){
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if(socket == null) {
                            socket = new DatagramSocket(port);
                        }
                        byte[] b = new byte[100];
                        DatagramPacket dp = new DatagramPacket(b, b.length);
                        socket.receive(dp); // 전송 받기
                        String str = new String(dp.getData()).trim();         // 보낸 메세지 출력

                        Log.d("Received data ", str);
                        String data[] = str.split(",");
                        if(data[0].contains("#ANDROID_B")){
                            if(Integer.parseInt(data[1]) == 1 || Integer.parseInt(data[2]) == 1){
                                isUdpReqExists = true;
                                Log.d("yeah!","done");
                            }
                        }
                    }
                    catch(Exception e)
                    {
                        checkUdpConnection();
                        e.printStackTrace();
                    }
                }
            }
        });
        thread3.start();
    }
    //_____________________________________________________   UDP END  ____________________________________________________




    void globalThread(){
        Handler gAlphaHandler = new Handler();
        Thread gAlpha = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        gAlphaHandler.post(new Runnable() {
                            @Override
                            public void run() {
//                                gestureArea = findViewById(R.id.gestureArea);
//                                gestureArea.setOnTouchListener(new View.OnTouchListener() {
//                                    @Override
//                                    public boolean onTouch(View v, MotionEvent event) {
//                                        Log.d("asfkj","sal;djf;asldfj;asldfjadls;fj");
//                                        return true;
//                                    }
//                                });
                                //앱 스타트
                                if(appStart && isUdpReqExists) {
                                    scndpass_btn.setVisibility(View.VISIBLE);
                                    videoView[0].setVisibility(View.VISIBLE);
                                    videoView[0].setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                        @Override
                                        public void onPrepared(MediaPlayer m) {
                                            // 준비 완료되면 비디오 재생
                                            m.setLooping(true);
                                            m.start();
                                        }
                                    });
                                    appStart = false;
                                }
                                if(!charScene && et.getText().toString().equals("91257")){
                                    if(ending) {
                                        InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                                        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                        et.setVisibility(View.INVISIBLE);

                                        videoView[5].setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                            @Override
                                            public void onPrepared(MediaPlayer mp) {
                                                // 준비 완료되면 비디오 재생
                                                mp.start();
                                            }
                                        });
                                        videoView[5].setVisibility(View.VISIBLE);
                                        ending = false;
                                    }
                                }
//                                hide_firstpass_btn();
                                if(characterVisibilityOn) {
                                    if(imgAlpha < 255 && !alphaer) {
                                        imgAlpha += 1;
                                    } else {
                                        characterMovementOn = true;
                                        imgAlpha = 255;
                                        if(alphaer){
                                            imgAlpha = 0;
                                        }
                                    }
                                    leftPerson.setImageAlpha(imgAlpha);
                                    rightPerson.setImageAlpha(imgAlpha);
                                } else {
//                                    leftPerson.setVisibility(View.INVISIBLE);
//                                    rightPerson.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        gAlpha.start();
    }


    boolean chatbox_video = false;
    boolean is_first_video_start_request = true;
    boolean is_sncd_video_start_request = true;
    boolean is_third_start_request = true;
    Button scndpass_btn;
//    Button firstpass_btn;
    Button thirdpass_btn;
    ImageView middleImg;
    EditText et;
    float curX, curY, prevX, prevY;

    boolean tT = true;
    boolean third_related_anim_flag = true;
    boolean img_fade_thd = false;
    boolean timer_on = false;
    int time_cnt = 0;
    int img_alpha = 255;
    boolean fvist = true;
    boolean dvisit = true;
    boolean prefvisit = true;
    //and I will 나레이션 끝나는 부분까지(완성)
    void third_related_anim(){
        if(tT) {
            Handler third_rl_hand = new Handler();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (third_related_anim_flag){
                        try {
                            third_rl_hand.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(timer_on){
                                        time_cnt++;
                                        Log.d("timer",Integer.toString(time_cnt));
                                    }
                                    if(time_cnt > 1300){

                                        if(dvisit) {
                                            middleImg = findViewById(R.id.middle);
                                            middleImg.setVisibility(View.VISIBLE); // 비디오 끝나기 전에 배경 이미지 깔기
                                            dvisit = false;
                                        }
                                    }
                                    if(time_cnt > 1450){ // 비디오 끝나는 타이밍
                                        if(fvist) {
//                                            gesturearea.setVisibility(View.VISIBLE);
                                            videoView[1].setVisibility(View.INVISIBLE);
                                            thirdpass_btn.setVisibility(View.VISIBLE);
                                            fvist = false;
                                        }
                                        time_cnt = 9999;
                                    }
                                    else if(time_cnt > 1400 && prefvisit){
                                        videoView[0].setVisibility(View.INVISIBLE);
                                        videoView[2].setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                            @Override
                                            public void onPrepared(MediaPlayer mp) {
                                                // 준비 완료되면 비디오 재생
                                                mp.start();
                                                mp.setLooping(true);
                                            }
                                        });
                                        prefvisit = false;
                                    }
                                    if(img_fade_thd) {
                                        img_alpha -= 3;
                                        if (img_alpha < 0){
                                            img_fade_thd = false;
                                            third_related_anim_flag = false;
                                            middleImg.setVisibility(View.INVISIBLE);
                                            gestureOn = true;
                                            circle_gesture();
                                        } else {
                                            middleImg.setImageAlpha(img_alpha);
                                        }
                                    }
                                }
                            });
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            thread.start();
            tT = false;
        }
    }


//    double[][] circleCenters;
//    double nDividedCircle;
//    int iTouchedCircle = 0;
//    int iPrevTouchedCircle = 0;
    float leftPersonX = 100;
    float rightPersonX = 1550;
    boolean isDragging = false;
//    final int distanceFromOrigin = 250;
//    final int nCircle = 20;
    boolean lastVidVisit = true;
    double getDistance(double x, double y, double a, double b){
        return Math.sqrt((x-a)*(x-a)+(y-b)*(y-b));
    }
//    void defineCirclePoints(int _originX, int _originY){
//        nDividedCircle = Math.PI * 2 / nCircle; // 원을 균등하게 분할
////        Log.d("nD",Double.toString(nDividedCircle));
//        circleCenters = new double[nCircle][2]; // 분할한 만큼 나오는 점
//        double sumOfDividedCircle = 0;
//        for(int i = 0; i < nCircle; i++) {
//            // 중심점으로부터 일정한 거리와 각도마다 점을 정의하기
//            circleCenters[i][0] = Math.cos(sumOfDividedCircle) * distanceFromOrigin + _originX;
//            circleCenters[i][1] = Math.sin(sumOfDividedCircle) * distanceFromOrigin + _originY;
////             Log.d("circleX",Double.toString(circleCenters[i][0]));
////             Log.d("circleY",Double.toString(circleCenters[i][1]));
//            sumOfDividedCircle += nDividedCircle;
//        }
//    }
//    void setTouchedCircle(double touchX, double touchY){
//        double[] distToPoint = new double[nCircle];
//        double compare = 9999;
//        int iNearestPoint = 0;
//        for(int i = 0; i < nCircle; i++){
//            distToPoint[i] = getDistance(curX, curY, circleCenters[i][0], circleCenters[i][1]);
//            if(compare > distToPoint[i]){
//                compare = distToPoint[i];
//                iNearestPoint = i;
//            }
//        }
//        iPrevTouchedCircle = iTouchedCircle;
//        iTouchedCircle = iNearestPoint;
//        Log.d("Prev",Float.toString(iPrevTouchedCircle));
//        Log.d("Curr",Float.toString(iTouchedCircle));
//    }
    public double getAngle(double _curX, double _curY){
        double xdf = originX - _curX;
        double ydf = originY - _curY;
        double radian = Math.atan2(ydf, xdf);
        double degree = radian * 57.3f;   // 57.3f == (180.0f / 3.141592f);
        if(degree < 0){
            degree = 360 + degree;
        }
        return degree;
    }
    float shortchim_angle = 0;
    float longchim_angle = 0;
    boolean isGestureCW() {
        if (curX == prevX && prevY == curY) {
            return false;
        }
        if (getAngle(curX, curY) > getAngle(prevX, prevY)) {
            shortchim_angle += 1;
            longchim_angle += 2.2;
            shortchim.setRotation(shortchim_angle);
            longchim.setRotation(longchim_angle);
        }

        return getAngle(curX, curY) > getAngle(prevX, prevY) ? true : false;
    }
    boolean isGestureCCW(){
        if(curX == prevX && prevY == curY){
            return false;
        }
        if(getAngle(curX, curY) < getAngle(prevX, prevY)){
            shortchim_angle -= 1;
            longchim_angle -= 2.2;
            shortchim.setRotation(shortchim_angle);
            longchim.setRotation(longchim_angle);
        }
        return getAngle(curX, curY) < getAngle(prevX, prevY) ? true : false;
    }
//        if(iPrevTouchedCircle == iTouchedCircle){
//            return false;
//        }
//        if(isDragging){
//            Log.d("isDragging", "true");
//            if((iPrevTouchedCircle >= nCircle - 3)
//               && (iTouchedCircle >= 0 && iTouchedCircle < 3
//               || iTouchedCircle > iPrevTouchedCircle)){

//                return true;
//            } else if(iTouchedCircle > iPrevTouchedCircle) {
//                return true;
//            }
//        }
//        return false;


    boolean vidVisit = true;
    boolean alphaer = false;
    boolean charScene = true;
    void moveCharacters(){
        if(isGestureCW()){
            //왼쪽 인물은 오른쪽으로
            //오른쪽 인물은 왼쪽으로
            //캐릭터 간 거리가 X 이하이면 움직이지 않음
            //분침과 시침을 CW로 돌림
            if(500 < getDistance(leftPersonX, 250, rightPersonX, 250)){
                leftPerson.setX(leftPersonX += 4.5);
                rightPerson.setX(rightPersonX -= 4.5);
            } else {
                if(vidVisit){
                    alphaer = true;
                    videoView[4].setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            // 준비 완료되면 비디오 재생
                            mp.start();
                        }
                    });
                    videoView[4].setVisibility(View.VISIBLE);
                    charScene = false;
                    et = (EditText)findViewById(R.id.edittext);
                    et.setVisibility(View.VISIBLE);
                    vidVisit = false;
                }
            }
        } else if(isGestureCCW()){
            //오른쪽 인물은 왼쪽으로
            //왼쪽 인물은 오른쪽으로
            //캐릭터 간 거리가 X 이상이면 움직이지 않음
            //분침과 시침을 CCW로 돌림
            if(1550 > getDistance(leftPersonX, 250, rightPersonX, 250)){
                leftPerson.setX(leftPersonX -= 4.5);
                rightPerson.setX(rightPersonX += 4.5);
            }
        } else {
            // 드래그하고 있지 않으므로 아무런 애니메이션 없음
        }
    }
    ImageView img;
    boolean init = true;
    boolean sfvisit = true;
    //스피너 씬과 시계 씬에 이용되는 원형 제스쳐 판독
    void circle_gesture(){
        if(init) {
            Handler gesture = new Handler();
            Thread thread2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            gesture.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (sfvisit) {
                                        //legacy define scope
                                        longchim_angle = 0;
                                        shortchim_angle = 0;
                                        shortchim.setRotation(shortchim_angle);
                                        longchim.setRotation(longchim_angle);
                                        sfvisit = false;
                                    }
                                    if(characterMovementOn){
                                        moveCharacters();
                                        shortchim.setVisibility(View.VISIBLE);
                                        longchim.setVisibility(View.VISIBLE);
                                        clock.setVisibility(View.VISIBLE);
                                    }
                                    if(spinnerGestureOn){
                                        spinnerEnd();
                                    }
                                }
                            });
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            thread2.start();
            init = false;
        }
    }

    //스피너 씬의 퀴즈 정답 판정 (원형 제스처 이용)
    int cntSpin = 0;
    int cntVidTime = 0;
    boolean vidvisvis = true;
    boolean visvid = true;
    boolean vis = true;
    void spinnerEnd(){
        if(isGestureCW()){
            cntSpin++;
        }
//        Log.d("cntSpin",Integer.toString(cntSpin));
        if(cntSpin > 20) {
            if (lastVidVisit) {
                videoView[3].setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        // 준비 완료되면 비디오 재생
                        mp.start();
                    }
                });
                videoView[3].setVisibility(View.VISIBLE);
                videoView[2].setVisibility(View.INVISIBLE);
                lastVidVisit = false;
//                cntVidTime+=3;
            }
            cntVidTime+=3;
            if(cntVidTime > 0 + 400 && cntVidTime <= 255 + 400){
                shortchim.setVisibility(View.VISIBLE);
                longchim.setVisibility(View.VISIBLE);
                clock.setVisibility(View.VISIBLE);
                img.setImageAlpha(cntVidTime - 400);
                shortchim.setImageAlpha(cntVidTime - 400);
                longchim.setImageAlpha(cntVidTime - 400);
                clock.setImageAlpha(cntVidTime - 400);
                if(visvid) {
                    longchim_angle = 0;
                    shortchim_angle = 0;
                    longchim.setRotation(0);
                    shortchim.setRotation(0);
                    characterVisibilityOn = true;
                    visvid = false;
                }
            }
            if(cntVidTime > 1700){
                if(vidvisvis) {
//                    Log.d("Mooooooooooooooooove","Onnnnnnnnnnnnnnnnnnn");
//                    Log.d("spinnerEnd() made character's Visibility", "VISIBLE");
//                    Log.d("spinnerEnd() made video[3]'s Visibility", "INVISIBLE");
                    characterMovementOn = true;
                    videoView[3].setVisibility(View.INVISIBLE);
                    vidvisvis = false;
                }
            }
        }
    }

    boolean fhide = false;
//    void hide_firstpass_btn(){
//        if(fhide) {
//            firstpass_btn.setVisibility(View.INVISIBLE);
//        }
//    }

    void checkUdpConnection(){
        if( !isAlertDisplayed ) {
            if (isPortUnstable) {
                AlertDialog.Builder alertPort = new AlertDialog.Builder(MainActivity.this);
                alertPort.setTitle("Error : 포트 불안정");
                alertPort.setMessage("태블릿을 재부팅해도 오류가 반복된다면\n공유기룰 재부팅하고 와이파이를 연결하세요.");
                alertPort.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alertPort.show();
                isAlertDisplayed = true;
            }
            if (!isWiFiConnected()) {
                AlertDialog.Builder alertWifi = new AlertDialog.Builder(MainActivity.this);
                alertWifi.setTitle("Error : 와이파이 연결");
                alertWifi.setMessage("태블릿에 와이파이 연결 후 앱을 재시작하세요.");
                alertWifi.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alertWifi.show();
                isAlertDisplayed = true;
            }

        }
    }
    TextView app_kill_cnt_text;
    long app_kill_cnt = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.d("gestureOn",Boolean.toString(gestureOn));
//        if(gestureOn) {
        prevX = curX;
        prevY = curY;
        curX = event.getX();  //눌린 곳의 X좌표
        curY = event.getY();  //눌린 곳의 Y좌표

        if(curX < 100 && curY < 100){
            Log.d("app_kill_cnt",Long.toString(app_kill_cnt));
            app_kill_cnt++;
        }
        if(app_kill_cnt > 500){
            finish();
        } else if (app_kill_cnt > 200){
            app_kill_cnt_text.setText("직원용 히든키 \n500에 종료 : "+Long.toString(app_kill_cnt));
        }
//            setTouchedCircle(curX, curY);
//                Log.d("curx",Float.toString(curX));
//                Log.d("cury",Float.toString(curY));
//            if (getDistance(curX, curY, prevX, prevY) > 1) { // 드래그 판정
//                isDragging = true;
//            } else {
//                isDragging = false;
//            }
//            Log.d("CW", Boolean.toString(isGestureCW()));
//            Log.d("C_C_w", Boolean.toString(isGestureCCW()));
//        }
        return super.onTouchEvent(event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        app_kill_cnt_text = findViewById(R.id.appkill);


        checkUdpConnection();


        img = findViewById(R.id.paper);
        rightPerson = findViewById(R.id.rightCharacter);
        leftPerson = findViewById(R.id.leftCharacter);
        leftPerson.setImageAlpha(0);
        rightPerson.setImageAlpha(0);
        rightPerson.setX(1550);
        leftPerson.setX(100);

        longchim = findViewById(R.id.longchim);
        shortchim = findViewById(R.id.shortchim);
        clock = findViewById(R.id.clock);
        img.setVisibility(View.VISIBLE);
        img.setImageAlpha(0);

//        Matrix matrix = new Matrix();
//        longchim.setScaleType(ImageView.ScaleType.MATRIX);   //required
//        matrix.postRotate((float) 23, 0, 0);
//        longchim.setImageMatrix(matrix);
//
//        Matrix matrix2 = new Matrix();
//        shortchim.setScaleType(ImageView.ScaleType.MATRIX);   //required
//        matrix2.postRotate((float) 23, 0, 0);
//        shortchim.setImageMatrix(matrix);
//
//        Matrix matrix3 = new Matrix();
//        clock.setScaleType(ImageView.ScaleType.MATRIX);   //required
//        matrix3.postRotate((float) 0, 0, 0);
//        clock.setImageMatrix(matrix);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        originX = size.x / 2;
        originY = size.y / 2;
//        View vas = findViewById(R.id.dummy);
//        vas.setX((float)originX);
//        vas.setY((float)originY);
//        defineCirclePoints(originX, originY);
//        sfvisit = false;

//        udpThd();
//        gestureOn = true;
//        circle_gesture();


        videoView[0] = (VideoView) findViewById(R.id.chatBox);
        Uri uri = Uri.parse("android.resource://com.game.dilemma2/raw/chatbox");
        videoView[0].setVideoURI(uri);

        videoView[1] = (VideoView) findViewById(R.id.willing);
        Uri uri2 = Uri.parse("android.resource://com.game.dilemma2/raw/willing2");
        videoView[1].setVideoURI(uri2);

        videoView[2] = findViewById(R.id.fourthVid);
        Uri uri3 = Uri.parse("android.resource://com.game.dilemma2/raw/fourth");
        videoView[2].setVideoURI(uri3);

        videoView[3] = (VideoView) findViewById(R.id.spinnerend);
        Uri uri4 = Uri.parse("android.resource://com.game.dilemma2/raw/spinnerend_edit");
        videoView[3].setVideoURI(uri4);

        videoView[4] = (VideoView) findViewById(R.id.last);
        Uri uri5 = Uri.parse("android.resource://com.game.dilemma2/raw/last");
        videoView[4].setVideoURI(uri5);

        videoView[5] = (VideoView) findViewById(R.id.ending);
        Uri uri12 = Uri.parse("android.resource://com.game.dilemma2/raw/ending");
        videoView[5].setVideoURI(uri12);


//
//        gesturearea = findViewById(R.id.gesturearea);
//        //드래그하고 있는지, 현재 터치하고 있는 iTouchedCircle이 몇 번인지 체크
//

        //첫 번째 전체화면 버튼을 눌러서 동영상을 시작 -> udp 시작으로 변경 07/13
//        firstpass_btn = (Button)findViewById(R.id.firstPass);
//
//        firstpass_btn.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//            }
//        });

        scndpass_btn = (Button)findViewById(R.id.scndPass);
        // 첫 번째 퀴즈 "and" 입력 시 비디오 재생
        scndpass_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("정답을 입력해 주세요!");
//                alert.setMessage("Plz, input");
                final EditText answer = new EditText(MainActivity.this);
                alert.setView(answer);


                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String[] answerCase = new String[8];
                        answerCase[0] = "and";
                        answerCase[1] = "And";
                        answerCase[2] = "ANd";
                        answerCase[3] = "AND";
                        answerCase[4] = " AND";
                        answerCase[5] = "AND ";
                        answerCase[6] = " and";
                        answerCase[7] = "and ";
                        Toast toast;
                        for(int i = 0; i < 8; i++){
                            if(answer.getText().toString().equals(answerCase[i])){
                                scndpass_btn.setVisibility(View.INVISIBLE);
                                if (is_sncd_video_start_request) {
                                    //반복재생 비디오 시작하기(flag. 첫 번째 요청에만 실행됨)
                                    timer_on = true;
                                    videoView[1].setVisibility(View.VISIBLE);
                                    videoView[1].setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                        @Override
                                        public void onPrepared(MediaPlayer m) {
                                            // 준비 완료되면 비디오 재생
                                            m.start();
                                        }
                                    });
                                    is_sncd_video_start_request = false;
                                    third_related_anim(); // 비디오 끝날 시 middle 이미지 띄우고, 비디오 끄기, 세 번째 버튼 만들기
                                }
                                break;
                            } else {
                                toast = Toast.makeText(getApplicationContext(), "오답이예요!", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                                toast.show();
                                break;
                            }
                        }
                    }
                });
                alert.setNegativeButton("취소",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast toast = Toast.makeText(getApplicationContext(), "다시 도전해 보세요!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                });
                alert.show();
//                alert.getWindow().setGravity(Gravity.TOP);
            }
        });


        thirdpass_btn = (Button) findViewById(R.id.thirdPass);
        thirdpass_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                img_fade_thd = true; // 100~0 알파줄어드는 스레드 시작
                timer_on = false;
                gestureOn = true;
                spinnerGestureOn = true;
                videoView[2].setVisibility(View.VISIBLE);
                thirdpass_btn.setVisibility(View.INVISIBLE);
            }
        });

        socketInit();
        run_udp_thd();
        globalThread();


    }
}

