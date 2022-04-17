package com.example.study_o;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.study_o.databinding.FragmentSecondBinding;
import com.example.study_o.util.PrefUtil;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class SecondFragment extends Fragment {

    public enum TimerState {
        Stop, Pause, Play
    }

    private FragmentSecondBinding binding;
    private CountDownTimer timer = null;
    private Long timerLengthSeconds = 0L;
    private TimerState timerState = TimerState.Stop;
    private Button button_start, button_pause, button_stop;
    private MaterialProgressBar progress_circular;
    private TextView text_countdown;

    private Long secondsRemaining = 0L;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view =  inflater.inflate(R.layout.fragment_second, container, false);

        binding = FragmentSecondBinding.inflate(inflater, container, false);

        button_start = (Button) view.findViewById(R.id.button_start);
        button_pause = (Button) view.findViewById(R.id.button_pause);
        button_stop = (Button) view.findViewById(R.id.button_stop);
        progress_circular = (MaterialProgressBar) view.findViewById(R.id.progress_circular);
        text_countdown = (TextView) view.findViewById(R.id.text_countdown);

        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();
                timerState = TimerState.Play;
                updateButtons();
            }
        });

        button_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                timerState = TimerState.Pause;
                updateButtons();
            }
        });

        button_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                timerState = TimerState.Stop;
                onTimerFinished();
            }
        });

        return binding.getRoot();

    }

    @Override
    public void onResume() {
        super.onResume();

        initTimer();

        //TODO: Extra
    }

    @Override
    public void onPause() {
        super.onPause();

        if (timerState == TimerState.Play) {
            timer.cancel();
            //TODO: Extra
        } else if (timerState == TimerState.Pause) {
            //TODO: Extra
        }

        PrefUtil.setPrevTimerLengthSeconds(timerLengthSeconds, requireContext());
        PrefUtil.setSecondsRemaining(secondsRemaining, requireContext());
        PrefUtil.setTimerState(timerState, requireContext());

    }

    private void initTimer() {
        timerState = PrefUtil.getTimerState(requireContext());

        if(timerState == TimerState.Stop)
            setNewTimerLength();
        else
            setPreviousTimerLength();

        secondsRemaining = (timerState == TimerState.Play || timerState == TimerState.Pause)?
                PrefUtil.getSecondsRemaining(requireContext()):timerLengthSeconds;

        if(secondsRemaining <= 0)
            onTimerFinished();
        else if(timerState == TimerState.Play)
            startTimer();

        updateButtons();
        updateCountdownUI();
    }

    private void onTimerFinished(){
        timerState = TimerState.Stop;

        setNewTimerLength();

        progress_circular.setProgress(0);

        PrefUtil.setSecondsRemaining(timerLengthSeconds, requireContext());
        secondsRemaining = timerLengthSeconds;

        updateButtons();
        updateCountdownUI();
    }

    private void startTimer(){
        timerState = TimerState.Play;

        timer = new CountDownTimer(secondsRemaining*1000, 1000) {
            @Override
            public void onTick(long l) {
                secondsRemaining = l / 1000;
                updateCountdownUI();
            }

            @Override
            public void onFinish() {
                onTimerFinished();
            }
        }.start();
    }

    private void setNewTimerLength(){
        int lengthInMinutes = PrefUtil.getTimerLength(requireContext());
        timerLengthSeconds = (lengthInMinutes*60L);
        progress_circular.setMax(timerLengthSeconds.intValue());
    }

    private void setPreviousTimerLength(){
        timerLengthSeconds = PrefUtil.getPrevTimerLengthSeconds(requireContext());
        progress_circular.setMax(timerLengthSeconds.intValue());
    }

    private void updateCountdownUI(){
        int minutesUntilFinished = secondsRemaining.intValue() / 60;
        int secondsInMinuteUntilFinished = secondsRemaining.intValue() - minutesUntilFinished * 60;
        String secondsStr = Integer.toString(secondsInMinuteUntilFinished);

        String newStr = secondsStr.length()==2? secondsStr:"0"+secondsStr;
        text_countdown.setText(minutesUntilFinished+":"+newStr);

        progress_circular.setProgress((timerLengthSeconds.intValue()-secondsRemaining.intValue()));
    }

    private void updateButtons(){
        switch(timerState){
            case Play:
                button_start.setEnabled(false);
                button_pause.setEnabled(true);
                button_stop.setEnabled(true);
                break;

            case Pause:
                button_start.setEnabled(true);
                button_pause.setEnabled(false);
                button_stop.setEnabled(true);
                break;

            case Stop:
                button_start.setEnabled(true);
                button_pause.setEnabled(false);
                button_stop.setEnabled(false);
                break;
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}