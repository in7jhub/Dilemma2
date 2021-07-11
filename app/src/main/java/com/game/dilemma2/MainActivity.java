package com.game.dilemma2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;import android.view.WindowManager.LayoutParams;import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;import android.graphics.Matrix;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity{
    final VideoView[] videoView = new VideoView[6];
    ImageView leftPerson;
    ImageView rightPerson;
    ImageView longchim;
    ImageView shortchim;
    ImageView clock;

    boolean gestureOn = false;
    boolean characterMovementOn = false;
    boolean characterVisibilityOn = false;
    boolean spinnerGestureOn = false;
    int imgAlpha = 0;
    boolean charVisVisit = true;
    boolean ending = true;
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
                                hide_firstpass_btn();
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
    Button firstpass_btn;
    Button thirdpass_btn;
    ImageView middleImg;
    EditText et;
    float curX, curY, prevX, prevY;

    boolean tT = true;
    boolean third_related_anim_flag = true;
    boolean img_fade_thd = false;
    boolean timer_on = true;
    int time_cnt = 0;
    int img_alpha = 255;
    boolean fvist = true;
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
                                    }
                                    if(time_cnt > 1450){ // 비디오 끝나는 타이밍
                                        if(fvist) {
                                            videoView[1].setVisibility(View.INVISIBLE);
                                            thirdpass_btn.setVisibility(View.VISIBLE);
                                            fvist = false;
                                        }
                                        time_cnt = 9999;
                                    }
                                    else if(time_cnt > 1400 && prefvisit){
                                        videoView[0].setVisibility(View.INVISIBLE);
                                        middleImg = findViewById(R.id.middle); // 비디오 끝날 시 시작되도록.
                                        middleImg.setVisibility(View.VISIBLE); // 비디오 끝날 시 시작되도록 스레드에 넣기
                                        videoView[2].setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                            @Override
                                            public void onPrepared(MediaPlayer mp) {
                                                // 준비 완료되면 비디오 재생
                                                mp.start();
                                                mp.setLooping(true);
                                            }
                                        });
                                        videoView[2].setVisibility(View.VISIBLE);
                                        prefvisit = false;
                                    }
                                    if(img_fade_thd) {
                                        img_alpha -= 3;
                                        if (img_alpha < 0){
                                            img_fade_thd = false;
                                            third_related_anim_flag = false;
                                            middleImg.setVisibility(View.INVISIBLE);
                                            spinnerGestureOn = true;
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


    double[][] circleCenters;
    double nDividedCircle;
    int iTouchedCircle = 0;
    int iPrevTouchedCircle = 0;
    float leftPersonX = 100;
    float rightPersonX = 1550;
    boolean isDragging = false;
    final int distanceFromOrigin = 250;
    final int nCircle = 20;
    boolean lastVidVisit = true;
    double getDistance(double x, double y, double a, double b){
        return Math.sqrt((x-a)*(x-a)+(y-b)*(y-b));
    }
    void defineCirclePoints(int _originX, int _originY){
        nDividedCircle = Math.PI * 2 / nCircle; // 원을 균등하게 분할
//        Log.d("nD",Double.toString(nDividedCircle));
        circleCenters = new double[nCircle][2]; // 분할한 만큼 나오는 점
        double sumOfDividedCircle = 0;
        for(int i = 0; i < nCircle; i++) {
            // 중심점으로부터 일정한 거리와 각도마다 점을 정의하기
            circleCenters[i][0] = Math.cos(sumOfDividedCircle) * distanceFromOrigin + _originX;
            circleCenters[i][1] = Math.sin(sumOfDividedCircle) * distanceFromOrigin + _originY;
//             Log.d("circleX",Double.toString(circleCenters[i][0]));
//             Log.d("circleY",Double.toString(circleCenters[i][1]));
            sumOfDividedCircle += nDividedCircle;
        }
    }
    void setTouchedCircle(double touchX, double touchY){
        double[] distToPoint = new double[nCircle];
        double compare = 9999;
        int iNearestPoint = 0;
        for(int i = 0; i < nCircle; i++){
            distToPoint[i] = getDistance(curX, curY, circleCenters[i][0], circleCenters[i][1]);
            if(compare > distToPoint[i]){
                compare = distToPoint[i];
                iNearestPoint = i;
            }
        }
        iPrevTouchedCircle = iTouchedCircle;
        iTouchedCircle = iNearestPoint;
        Log.d("Prev",Float.toString(iPrevTouchedCircle));
        Log.d("Curr",Float.toString(iTouchedCircle));
    }
    float shortchim_angle = 0;
    float longchim_angle = 0;
    boolean isGestureCW(){
        if(iPrevTouchedCircle == iTouchedCircle){
            return false;
        }
        if(isDragging){
            if((iPrevTouchedCircle >= nCircle - 3)
               && (iTouchedCircle >= 0 && iTouchedCircle < 3
               || iTouchedCircle > iPrevTouchedCircle)){
                shortchim_angle += 1;
                longchim_angle += 2.2;
                shortchim.setRotation(shortchim_angle);
                longchim.setRotation(longchim_angle);
                return true;
            } else if(iTouchedCircle > iPrevTouchedCircle) {
                shortchim_angle += 1;
                longchim_angle += 2.2;
                shortchim.setRotation(shortchim_angle);
                longchim.setRotation(longchim_angle);
                return true;
            }
        }
        return false;
    }
    boolean isGestureCCW(){
        if(iPrevTouchedCircle == iTouchedCircle){
            return false;
        }
        if(isDragging){
            if(iPrevTouchedCircle < 3
               && (iTouchedCircle > nCircle - 2
               || iTouchedCircle < iPrevTouchedCircle)){
                shortchim_angle -= 1;
                longchim_angle -= 2.2;
                shortchim.setRotation(shortchim_angle);
                longchim.setRotation(longchim_angle);
                return true;
            } else if(iTouchedCircle < iPrevTouchedCircle) {
                shortchim_angle -= 1;
                longchim_angle -= 2.2;
                shortchim.setRotation(shortchim_angle);
                longchim.setRotation(longchim_angle);
                return true;
            }
        }
        return false;
    }
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
                leftPerson.setX(leftPersonX += 2.5);
                rightPerson.setX(rightPersonX -= 2.5);
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
                leftPerson.setX(leftPersonX -= 2.5);
                rightPerson.setX(rightPersonX += 2.5);
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
                            Thread.sleep(5);
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
        if(cntSpin > 16) {
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
            }
            cntVidTime++;
            if(cntVidTime > 0 + 400 && cntVidTime <= 255 + 400){
                img.setImageAlpha(cntVidTime - 400);
            }
            if(cntVidTime > 500){
                if(visvid) {
                    shortchim.setVisibility(View.VISIBLE);
                    longchim.setVisibility(View.VISIBLE);
                    clock.setVisibility(View.VISIBLE);
                    longchim_angle = 0;
                    shortchim_angle = 0;
                    longchim.setRotation(0);
                    shortchim.setRotation(0);
                    characterVisibilityOn = true;
                    visvid = false;
                }
            }
            if(cntVidTime > 900){
                if(vidvisvis) {
                    Log.d("Mooooooooooooooooove","Onnnnnnnnnnnnnnnnnnn");
                    Log.d("spinnerEnd() made character's Visibility", "VISIBLE");
                    Log.d("spinnerEnd() made video[3]'s Visibility", "INVISIBLE");
                    characterMovementOn = true;
                    videoView[3].setVisibility(View.INVISIBLE);
                    vidvisvis = false;
                }
            }
        }
    }

    boolean fhide = false;
    void hide_firstpass_btn(){
        if(fhide) {
            firstpass_btn.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        int originX = size.x / 2;
        int originY = size.y / 2;
        defineCirclePoints(originX, originY);
        sfvisit = false;

        globalThread();



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


        for(int i = 0; i < 5; i++){
            videoView[i].setVisibility(View.INVISIBLE);
        }

        View gestureArea = findViewById(R.id.gestureArea);
        //드래그하고 있는지, 현재 터치하고 있는 iTouchedCircle이 몇 번인지 체크
        gestureArea.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(gestureOn) {
                    prevX = curX;
                    prevY = curY;
                    curX = event.getX();  //눌린 곳의 X좌표
                    curY = event.getY();  //눌린 곳의 Y좌표
                    setTouchedCircle(curX, curY);
//                Log.d("curx",Float.toString(curX));
//                Log.d("cury",Float.toString(curY));
                    if (getDistance(curX, curY, prevX, prevY) > 2) { // 드래그 판정
//                    Log.d("ccw", Boolean.toString(isGestureCCW()));
                        isDragging = true;
                    } else {
                        isDragging = false;
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });

        //첫 번째 전체화면 버튼을 눌러서 동영상을 시작
        firstpass_btn = (Button)findViewById(R.id.firstPass);

        firstpass_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (is_first_video_start_request) {
                    //초기화면 검은색 지우기
//                    TextView initializeText = (TextView) findViewById(R.id.InitializeText);
//                    initializeText.setVisibility(View.INVISIBLE);

                    //반복재생 비디오 시작하기(flag. 첫 번째 요청에만 실행됨)
                    videoView[0].setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            // 준비 완료되면 비디오 재생
                            mp.start();
                            mp.setLooping(true);
                        }
                    });
                    videoView[0].setVisibility(View.VISIBLE);
                    scndpass_btn.setVisibility(View.VISIBLE);
                }
                is_first_video_start_request = false;
                fhide = true;
            }
        });

        scndpass_btn = (Button)findViewById(R.id.scndPass);
        // 첫 번째 퀴즈 "and" 입력 시 비디오 재생
        scndpass_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                View dialog = findViewById(R.id.dialog);
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
        thirdpass_btn.setVisibility(View.INVISIBLE);
        thirdpass_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                img_fade_thd = true; // 100~0 알파줄어드는 스레드 시작
                timer_on = false;
                thirdpass_btn.setVisibility(View.INVISIBLE);
            }
        });
    }
}

