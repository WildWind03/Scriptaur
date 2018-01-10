package ru.nsu.fit.scriptaur;

import android.app.Application;
import android.content.Context;
import org.acra.ACRA;
import org.acra.annotation.AcraDialog;
import org.acra.annotation.AcraMailSender;
import org.acra.config.*;

@AcraMailSender(mailTo = "evt.ilyaa@gmail.com")
@AcraDialog(resText = R.string.dialog_comment,
        resPositiveButtonText = R.string.send,
        resTitle = R.string.dialog_text,
        resTheme = R.style.Theme_AppCompat_Dialog)
public class Scriptaur extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ACRA.init(this);
    }
}
