package com.sk.commons.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author sk
 * @desc 自定义输入电话号码框
 */
public class PhoneEditText extends AppCompatEditText {


    private SetValueListener setValueListener;
    private boolean hadSetText;


    public PhoneEditText(Context context) {
        this(context, null);
    }

    public PhoneEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public PhoneEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    String before;
    private void init() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("onTextChanged", "" + s.toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                before = s.toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (hadSetText) {
                    hadSetText = false;
                    return;
                }

                String ss = s.toString();

                if (ss.length() - before.length() == 1) {
                    // 输入操作
                    if (ss.length() == 3 || ss.length() == 9) {

                        hadSetText = true;

                        ss = ss + "  ";
                        setText(ss);
                        setSelection(ss.length());
                    }

                    if (null != setValueListener) {
                        setValueListener.setValue(ss);
                    }

                } else if (before.length() - ss.length() == 1) {
                    // 删除操作
                    if (ss.length() == 4 || ss.length() == 10) {

                        hadSetText = true;

                        ss = ss.substring(0, ss.length() - 2);
                        setText(ss);
                        setSelection(ss.length());
                    }

                    if (null != setValueListener) {
                        setValueListener.setValue(ss);
                    }

                } else if (ss.length() >= 3 && ss.length() < 15) {

                    if ((ss.length() >= 5 && ss.substring(3, 5).equals("  "))
                            || (ss.length() >= 11 && ss.substring(9, 11).equals("  "))) {

                        if (null != setValueListener) setValueListener.setValue(ss);

                    } else {
                        // 去除空格
                        ss = ss.replace(" ", "");
                        // 去除特殊字符
                        ss = extractNumbers(ss);
                        // 只截取前11位数字
                        if (ss.length() > 11) {
                            ss = ss.substring(0, 11);
                        }

                        // 处理复制粘贴操作
                        if (ss.length() < 7) {

                            ss = ss.substring(0, 3) + "  " + s.toString().substring(3);
                            setText(ss);
                            setSelection(ss.length());

                            if (null != setValueListener) {
                                setValueListener.setValue(ss);
                            }

                        } else {

                            hadSetText = true;

                            ss = ss.substring(0, 3) + "  " + ss.substring(3, 7) + "  " + ss.substring(7);
                            setText(ss);
                            setSelection(ss.length());

                            if (null != setValueListener) {
                                setValueListener.setValue(ss);
                            }
                        }
                    }
                } else if (ss.length() == 0) {

                    if (null != setValueListener) {
                        setValueListener.setValue(ss);
                    }
                }
            }
        });
    }

    public String extractNumbers(String str) {
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(str);
        m.find();
        return m.group();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    public void setSetValueListener(SetValueListener setValueListener) {
        this.setValueListener = setValueListener;
    }

    public interface SetValueListener {
        void setValue(String s);
    }
}
