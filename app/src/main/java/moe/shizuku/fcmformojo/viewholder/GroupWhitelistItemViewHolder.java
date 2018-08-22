package moe.shizuku.fcmformojo.viewholder;

import android.util.Pair;
import android.view.View;

import com.crashlytics.android.Crashlytics;

import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import moe.shizuku.fcmformojo.R;
import moe.shizuku.fcmformojo.model.openqq.Group;

/**
 * Created by rikka on 2017/8/28.
 */

public class GroupWhitelistItemViewHolder extends WhitelistItemViewHolder<Group> implements View.OnClickListener {

    public static final Creator CREATOR = (Creator<Pair<Group, Boolean>>) (inflater, parent) -> new GroupWhitelistItemViewHolder(inflater.inflate(R.layout.item_blacklist_item, parent ,false));

    public GroupWhitelistItemViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBind() {
        title.setText(getData().first.getName());
        summary.setText(getData().first.getUid() == 0 ? itemView.getContext().getString(R.string.whitelist_group_no_uid) :
                String.format(Locale.ENGLISH, "%d", getData().first.getUid()));
        toggle.setChecked(getData().second);

        mDisposable = Single.just(getData().first.loadIcon(itemView.getContext()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(drawable -> icon.setImageDrawable(drawable), throwable -> {
                    throwable.printStackTrace();

                    Crashlytics.log("load icon");
                    Crashlytics.logException(throwable);
                });

        super.onBind();
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (getData().first.getUid() == 0) {
            enabled = false;
        }

        super.setEnabled(enabled);
    }
}
