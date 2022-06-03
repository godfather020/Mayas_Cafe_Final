package com.example.mayasfood.constants;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;

public class CustomTimePicker extends TimePickerDialog {

        private final static int TIME_PICKER_INTERVAL = 5;
        private TimePicker mTimePicker;
        private OnTimeSetListener mTimeSetListener;
        private final boolean mIs24HourView;
        private TimePicker timePicker;

        public CustomTimePicker(Context context, OnTimeSetListener lisner,
                                int hourOfDay, int minute, TimePicker mTimePicker, boolean is24HourView) {
                super(context, lisner, hourOfDay, minute, is24HourView);
                this.mTimePicker = mTimePicker;
                mIs24HourView = is24HourView;
        }

        @Override
        public void updateTime(int hourOfDay, int minuteOfHour) {
                mTimePicker.setCurrentHour(hourOfDay);
                mTimePicker.setCurrentMinute(minuteOfHour);
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                        case BUTTON_POSITIVE:
                                if (mTimeSetListener != null) {
                                        mTimeSetListener.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(),
                                                mTimePicker.getCurrentMinute());
                                }
                                break;
                        case BUTTON_NEGATIVE:
                                cancel();
                                break;
                }
        }

        @Override
        public void onAttachedToWindow() {
                super.onAttachedToWindow();
                try {
                        Class<?> classForid = Class.forName("com.android.internal.R$id");
                        Field timePickerField = classForid.getField("timePicker");
                        this.timePicker = (TimePicker) findViewById(timePickerField
                                .getInt(null));
                        Field field = classForid.getField("hour");


                        final NumberPicker mHourSpinner = (NumberPicker) timePicker
                                .findViewById(field.getInt(null));

                        if (mIs24HourView) {
                                mHourSpinner.setMinValue(15);
                                mHourSpinner.setMaxValue(20);
                        }else {
                                Field amPm1 = classForid.getField("amPm");

                                mHourSpinner.setMinValue(6);
                                final NumberPicker amPm = (NumberPicker) timePicker
                                        .findViewById(amPm1.getInt(null));
                                amPm.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                        @Override
                                        public void onValueChange(NumberPicker np1, int oldVal, int newVal) {
                                                if (newVal == 0) { // case AM
                                                        mHourSpinner.setMinValue(6);
                                                        mHourSpinner.setMaxValue(12);
                                                } else { // case PM
                                                        mHourSpinner.setMinValue(6);
                                                        mHourSpinner.setMaxValue(8);
                                                }
                                        }
                                });

                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }

        }
}
