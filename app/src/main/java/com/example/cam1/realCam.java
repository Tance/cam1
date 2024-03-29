package com.example.cam1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.laifeng.sopcastsdk.camera.CameraListener;
import com.laifeng.sopcastsdk.configuration.AudioConfiguration;
import com.laifeng.sopcastsdk.configuration.CameraConfiguration;
import com.laifeng.sopcastsdk.configuration.VideoConfiguration;
import com.laifeng.sopcastsdk.entity.Watermark;
import com.laifeng.sopcastsdk.entity.WatermarkPosition;
import com.laifeng.sopcastsdk.stream.packer.rtmp.RtmpPacker;
import com.laifeng.sopcastsdk.stream.sender.rtmp.RtmpSender;
import com.laifeng.sopcastsdk.ui.CameraLivingView;
import com.laifeng.sopcastsdk.utils.SopCastLog;
import com.laifeng.sopcastsdk.video.effect.GrayEffect;
import com.laifeng.sopcastsdk.video.effect.NullEffect;

import static com.laifeng.sopcastsdk.constant.SopCastConstant.TAG;

public class realCam extends AppCompatActivity {
    private CameraLivingView mLFLiveView;
    private Button mMicBtn;
    private Button mFlashBtn;
    private Button mFaceBtn;
    private Button mBeautyBtn;
    private Button mFocusBtn;
    private GestureDetector mGestureDetector;
    private GrayEffect mGrayEffect;
    private NullEffect mNullEffect;
    private ImageButton mRecordBtn;
    private boolean isGray;
    private boolean isRecording;
    private ProgressBar mProgressConnecting;
    private RtmpSender mRtmpSender;
    private VideoConfiguration mVideoConfiguration;
    private int mCurrentBps;
    private Dialog mUploadDialog;
    private EditText mAddressET;
    private Button okBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_cam);
        initEffects();
        initViews();
        initListeners();
        initLiveView();
        initRtmpAddressDialog();
    }
    private void initEffects() {
        mGrayEffect = new GrayEffect(this);
        mNullEffect = new NullEffect(this);
    }
    private void initViews() {
        mLFLiveView = (CameraLivingView) findViewById(R.id.liveView);
        mMicBtn = (Button) findViewById(R.id.record_mic_button);
        mFlashBtn = (Button) findViewById(R.id.camera_flash_button);
        mFaceBtn = (Button) findViewById(R.id.camera_switch_button);
        mBeautyBtn = (Button) findViewById(R.id.camera_render_button);
        mFocusBtn = (Button) findViewById(R.id.camera_focus_button);
        mRecordBtn = (ImageButton) findViewById(R.id.btnRecord);
        mProgressConnecting = (ProgressBar) findViewById(R.id.progressConnecting);
        okBtn = findViewById(R.id.okBtn);
    }
    private void initListeners() {
        mMicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLFLiveView.mute(true);
            }
        });
        mFaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLFLiveView.switchCamera();
            }
        });
        mFlashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLFLiveView.switchTorch();
            }
        });
            mFocusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLFLiveView.switchFocusMode();
                }
            });

        mRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecording) {
                    mProgressConnecting.setVisibility(View.GONE);
                    Toast.makeText(realCam.this, "stop living", Toast.LENGTH_SHORT).show();
                    mRecordBtn.setBackgroundResource(R.mipmap.ic_launcher);
                    mLFLiveView.stop();
                    isRecording = false;
                } else {


                }
            }
        });

    }
    private void initRtmpAddressDialog() {

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uploadUrl = "rtmp://192.168.1.54/live/stream";//不是steam啊啊啊啊
                if(TextUtils.isEmpty(uploadUrl)) {
                    Toast.makeText(realCam.this, "Upload address is empty!", Toast.LENGTH_SHORT).show();
                } else {
                    mRtmpSender.setAddress(uploadUrl);
                    mProgressConnecting.setVisibility(View.VISIBLE);
                    Toast.makeText(realCam.this, "start connecting", Toast.LENGTH_SHORT).show();

                    mRtmpSender.connect();
                    isRecording = true;
                }
                //mUploadDialog.dismiss();
            }
        });
    }

    private void initLiveView() {
        SopCastLog.isOpen(true);
        mLFLiveView.init();
        CameraConfiguration.Builder cameraBuilder = new CameraConfiguration.Builder();
        cameraBuilder.setOrientation(CameraConfiguration.Orientation.LANDSCAPE)
                .setFacing(CameraConfiguration.Facing.BACK);
        CameraConfiguration cameraConfiguration = cameraBuilder.build();
        mLFLiveView.setCameraConfiguration(cameraConfiguration);

        VideoConfiguration.Builder videoBuilder = new VideoConfiguration.Builder();
        videoBuilder.setSize(640, 360);
        mVideoConfiguration = videoBuilder.build();
        mLFLiveView.setVideoConfiguration(mVideoConfiguration);

        //设置水印
        Bitmap watermarkImg = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Watermark watermark = new Watermark(watermarkImg, 50, 25, WatermarkPosition.WATERMARK_ORIENTATION_BOTTOM_RIGHT, 8, 8);
        mLFLiveView.setWatermark(watermark);

        //设置预览监听
        mLFLiveView.setCameraOpenListener(new CameraListener() {
            @Override
            public void onOpenSuccess() {
                Toast.makeText(realCam.this, "camera open success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onOpenFail(int error) {
                Toast.makeText(realCam.this, "camera open fail", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCameraChange() {
                Toast.makeText(realCam.this, "camera switch", Toast.LENGTH_LONG).show();
            }
        });

        //设置手势识别
        mGestureDetector = new GestureDetector(this, new GestureListener());
        mLFLiveView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return false;
            }
        });

        //初始化flv打包器
        RtmpPacker packer = new RtmpPacker();
        packer.initAudioParams(AudioConfiguration.DEFAULT_FREQUENCY, 16, false);
        mLFLiveView.setPacker(packer);
        //设置发送器
        mRtmpSender = new RtmpSender();
        mRtmpSender.setVideoParams(360, 640);//改改参数 看看
        mRtmpSender.setAudioParams(AudioConfiguration.DEFAULT_FREQUENCY, 16, false);
        mRtmpSender.setSenderListener(mSenderListener);
        mLFLiveView.setSender(mRtmpSender);
        mLFLiveView.setLivingStartListener(new CameraLivingView.LivingStartListener() {
            @Override
            public void startError(int error) {
                //直播失败
                Toast.makeText(realCam.this, "start living fail", Toast.LENGTH_SHORT).show();
                mLFLiveView.stop();
            }

            @Override
            public void startSuccess() {
                //直播成功
                Toast.makeText(realCam.this, "start living", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private RtmpSender.OnSenderListener mSenderListener = new RtmpSender.OnSenderListener() {
        @Override
        public void onConnecting() {

        }

        @Override
        public void onConnected() {
            mProgressConnecting.setVisibility(View.GONE);
            mLFLiveView.start();
            Toast.makeText(realCam.this, "mLFLIVEstart()", Toast.LENGTH_SHORT).show();
            mCurrentBps = mVideoConfiguration.maxBps;
        }

        @Override
        public void onDisConnected() {
            mProgressConnecting.setVisibility(View.GONE);
            Toast.makeText(realCam.this, "fail to live", Toast.LENGTH_SHORT).show();
            mRecordBtn.setBackgroundResource(R.mipmap.ic_launcher);
            mLFLiveView.stop();
            isRecording = false;
        }

        @Override
        public void onPublishFail() {
            mProgressConnecting.setVisibility(View.GONE);
            Toast.makeText(realCam.this, "fail to publish stream", Toast.LENGTH_SHORT).show();
            mRecordBtn.setBackgroundResource(R.mipmap.ic_launcher);
            isRecording = false;
        }

        @Override
        public void onNetGood() {
            if (mCurrentBps + 50 <= mVideoConfiguration.maxBps){
                SopCastLog.d(TAG, "BPS_CHANGE good up 50");
                int bps = mCurrentBps + 50;
                if(mLFLiveView != null) {
                    boolean result = mLFLiveView.setVideoBps(bps);
                    if(result) {
                        mCurrentBps = bps;
                    }
                }
            } else {
                SopCastLog.d(TAG, "BPS_CHANGE good good good");
            }
            SopCastLog.d(TAG, "Current Bps: " + mCurrentBps);
        }

        @Override
        public void onNetBad() {
            if (mCurrentBps - 100 >= mVideoConfiguration.minBps){
                SopCastLog.d(TAG, "BPS_CHANGE bad down 100");
                int bps = mCurrentBps - 100;
                if(mLFLiveView != null) {
                    boolean result = mLFLiveView.setVideoBps(bps);
                    if(result) {
                        mCurrentBps = bps;
                    }
                }
            } else {
                SopCastLog.d(TAG, "BPS_CHANGE bad down 100");
            }
            SopCastLog.d(TAG, "Current Bps: " + mCurrentBps);
        }
    };

    public class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > 100
                    && Math.abs(velocityX) > 200) {
                // Fling left
                Toast.makeText(realCam.this, "Fling Left", Toast.LENGTH_SHORT).show();
            } else if (e2.getX() - e1.getX() > 100
                    && Math.abs(velocityX) > 200) {
                // Fling right
                Toast.makeText(realCam.this, "Fling Right", Toast.LENGTH_SHORT).show();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLFLiveView.pause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLFLiveView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLFLiveView.stop();
        mLFLiveView.release();
    }


}
