package com.example.branchprodmantest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;

public class mainActivity extends AppCompatActivity {

    public static final String CHECK_KEY = "deep_link_test";
    public static final String KEY_VALUE = "other";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button shareButton = findViewById(R.id.buttonShareLink);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BranchUniversalObject buo = new BranchUniversalObject();

                LinkProperties lp = new LinkProperties()
                        .addControlParameter(CHECK_KEY, KEY_VALUE);

                ShareSheetStyle ss = new ShareSheetStyle(mainActivity.this, "Check this out!", "This stuff is awesome: ")
                        .setCopyUrlStyle(ContextCompat.getDrawable(mainActivity.this, android.R.drawable.ic_menu_send), "Copy", "Added to clipboard")
                        .setMoreOptionStyle(ContextCompat.getDrawable(mainActivity.this, android.R.drawable.ic_menu_search), "Show more")
                        .addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL)
                        .addPreferredSharingOption(SharingHelper.SHARE_WITH.MESSAGE)
                        .addPreferredSharingOption(SharingHelper.SHARE_WITH.HANGOUT)
                        .setAsFullWidthStyle(true)
                        .setSharingTitle("Share With");

                buo.showShareSheet(mainActivity.this, lp,  ss,  new Branch.BranchLinkShareListener() {
                    @Override
                    public void onShareLinkDialogLaunched() {
                    }
                    @Override
                    public void onShareLinkDialogDismissed() {
                    }
                    @Override
                    public void onLinkShareResponse(String sharedLink, String sharedChannel, BranchError error) {
                    }
                    @Override
                    public void onChannelSelected(String channelName) {
                    }

                });
            }
        });
    }

    @Override public void onStart() {
        super.onStart();
        Branch.sessionBuilder(this).withCallback(branchReferralInitListener).withData(getIntent() != null ? getIntent().getData() : null).init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        // if activity is in foreground (or in backstack but partially visible) launching the same
        // activity will skip onStart, handle this case with reInitSession
        Branch.sessionBuilder(this).withCallback(branchReferralInitListener).reInit();
    }



    private final Branch.BranchReferralInitListener branchReferralInitListener = new Branch.BranchReferralInitListener() {
        @Override
        public void onInitFinished(JSONObject linkProperties, BranchError error) {
            // do stuff with deep link data (nav to page, display content, etc)

            if (linkProperties != null && linkProperties.length() != 0) {

                String keyValue = null; //.toString();
                try {
                    keyValue = linkProperties.getString(CHECK_KEY);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (KEY_VALUE.equals(keyValue))
                {
                    //Start otherActivity
                    Intent intent = new Intent(mainActivity.this, otherActivity.class);
                    startActivity(intent);
                }

            }
        }
    };
}