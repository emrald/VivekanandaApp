package com.clarusapps.SwamiVivekananda;


import org.json.JSONObject;
import org.json.JSONTokener;

import com.clarusapps.SwamiVivekananda.Twitter.TwitterApp;
import com.clarusapps.SwamiVivekananda.Twitter.TwitterApp.TwDialogListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MoreActivity extends Activity{
	Button btn_rateApp,btn_twitter,btn_mail,btn_facebook;
	ImageView iv_rateApp,iv_twitter,iv_mail,iv_facebook;
	private TwitterApp mTwitter;
	private static final String twitter_consumer_key = "8Q7tu5OoUnKxwEJPT3g";// "cNjYnzVzmyPe3ToNn8lSrQ";
	private static final String twitter_secret_key = "YdrD8VJjOfNT5wUvSX4ewZuwOotGsx7fNtx46xyk";// "qgzVsQdxSZQ1mzKkDZD0WlLxTqYkFOshAUqq9Pblw";
	//Facebook
	public static final String APP_ID = "456328747744179";
	private Facebook mFacebook;
	private String[] mPermissions;
	private ProgressDialog mProgress;
	private Handler mRunOnUi = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		
		btn_rateApp=(Button)findViewById(R.id.share_btn_rateapp);
        btn_twitter=(Button)findViewById(R.id.share_btn_btntwitter);
        btn_mail=(Button)findViewById(R.id.share_btn_btnmail);
		btn_facebook=(Button)findViewById(R.id.share_btn_btnfacebook);
		
		iv_rateApp=(ImageView)findViewById(R.id.share_iv_rate_this_app);
        iv_twitter=(ImageView)findViewById(R.id.share_iv_twitter);
        iv_mail=(ImageView)findViewById(R.id.share_iv_mail);
		iv_facebook=(ImageView)findViewById(R.id.share_iv_facebook);
		
		mTwitter = new TwitterApp(this, twitter_consumer_key,
				twitter_secret_key);
		mProgress       = new ProgressDialog(this);
		mFacebook       = new Facebook(APP_ID);
		mPermissions = new String[] {"publish_stream", "read_stream"};

		btn_mail.setOnClickListener(mailClickListner);
		btn_rateApp.setOnClickListener(rateAppClickListner);
		btn_twitter.setOnClickListener(twitterClickListner);
		btn_facebook.setOnClickListener(facebookClickListner);
		iv_facebook.setOnClickListener(facebookClickListner);
		iv_mail.setOnClickListener(mailClickListner);
		iv_rateApp.setOnClickListener(rateAppClickListner);
		iv_twitter.setOnClickListener(twitterClickListner);
	}
	private OnClickListener facebookClickListner = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			SessionStore.restore(mFacebook, MoreActivity.this);   

			onFacebookClick();
		}
	};
	private OnClickListener twitterClickListner = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			mTwitter.setListener(mTwLoginDialogListener);
			if (mTwitter.hasAccessToken()) {
				showTwittDialog();
			} else {
				mTwitter.authorize();
			}
		}
	};
	private OnClickListener mailClickListner = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			final Intent emailIntent = new Intent(
					android.content.Intent.ACTION_SEND);
			emailIntent.setType("plain/text");
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "");
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					"Thoughts Of Swami Vivekananda");
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
					"This is Thoughts Of Swami Vivekananda Message\nhttps://play.google.com/store/apps/details?id=com.clarusapps.SwamiVivekananda");
			MoreActivity.this.startActivity(Intent.createChooser(
					emailIntent, "Send mail..."));
		}
	};
	private OnClickListener rateAppClickListner = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent browserIntent = new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("https://play.google.com/store/apps/details?id=com.clarusapps.SwamiVivekananda"));
			startActivity(browserIntent);
		}
	};
	private TwDialogListener mTwLoginDialogListener = new TwDialogListener() {

		public void onError(String value) {
			showToast("Login Failed");
			mTwitter.resetAccessToken();
		}

		public void onComplete(String value) {
			showTwittDialog();
		}
	};
	private void showTwittDialog() {
		final Dialog dialog = new Dialog(MoreActivity.this);
		dialog.setContentView(R.layout.dialog);

		Button btnPost = (Button) dialog.findViewById(R.id.btnpost);
		final TextView et = (TextView) dialog.findViewById(R.id.twittext);
		et.setText("Hypnosis app");
		btnPost.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				try {
					mTwitter.updateStatus("https://play.google.com/store/apps/details?id=com.clarusapps.SwamiVivekananda");
					showToast("Posted Successfully");
				} catch (Exception e) {
					if (e.getMessage().toString().contains("duplicate")) {
						showToast("Posting Failed because of duplicate message...");
					}
					e.printStackTrace();
				}
				dialog.dismiss();
			}
		});
		dialog.show();

	}
	void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	private void onFacebookClick() {
		mFacebook.authorize(this, mPermissions, -1, new FbLoginDialogListener());
	}

	private final class FbLoginDialogListener implements DialogListener {
		@Override
		public void onComplete(Bundle values) {
			SessionStore.save(mFacebook, MoreActivity.this);

			getFbName();
			postToFacebook("");
		}

		@Override
		public void onFacebookError(FacebookError error) {
			Toast.makeText(MoreActivity.this, "Facebook connection failed", Toast.LENGTH_SHORT).show();

			// mFacebookBtn.setChecked(false);
		}

		@Override
		public void onError(DialogError error) {
			Toast.makeText(MoreActivity.this, "Facebook connection failed", Toast.LENGTH_SHORT).show();

			// mFacebookBtn.setChecked(false);
		}

		@Override
		public void onCancel() {
			// mFacebookBtn.setChecked(false);
		}


	}

	private void getFbName() {
		mProgress.setMessage("Finalizing ...");
		mProgress.show();

		new Thread() {
			@Override
			public void run() {
				String name = "";
				int what = 1;

				try {
					String me = mFacebook.request("me");

					JSONObject jsonObj = (JSONObject) new JSONTokener(me).nextValue();
					name = jsonObj.getString("name");
					what = 0;
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				mFbHandler.sendMessage(mFbHandler.obtainMessage(what, name));
			}
		}.start();


	}

	private void fbLogout() {
		mProgress.setMessage("Disconnecting from Facebook");
		mProgress.show();

		new Thread() {
			@Override
			public void run() {
				SessionStore.clear(MoreActivity.this);

				int what = 1;

				try {
					mFacebook.logout(MoreActivity.this);

					what = 0;
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				mmHandler.sendMessage(mmHandler.obtainMessage(what));
			}
		}.start();
	}
	private Handler mFbHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mProgress.dismiss();

			if (msg.what == 0) {
				String username = (String) msg.obj;
				username = (username.equals("")) ? "No Name" : username;

				SessionStore.saveName(username, MoreActivity.this);

				//    mFacebookBtn.setText("  Facebook (" + username + ")");

				Toast.makeText(MoreActivity.this, "Connected to Facebook as " + username, Toast.LENGTH_SHORT).show();


			} else {
				Toast.makeText(MoreActivity.this, "Connected to Facebook", Toast.LENGTH_SHORT).show();
			}
		}
	};
	private Handler mmHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mProgress.dismiss();

			if (msg.what == 1) {
				Toast.makeText(MoreActivity.this, "Facebook logout failed", Toast.LENGTH_SHORT).show();
			} else {
				//mFacebookBtn.setChecked(false);
				//  mFacebookBtn.setText("  Facebook (Not connected)");
				//  mFacebookBtn.setTextColor(Color.GRAY);

				Toast.makeText(MoreActivity.this, "Disconnected from Facebook", Toast.LENGTH_SHORT).show();
			}
		}
	};
	private void postToFacebook(String review) {
		mProgress.setMessage("Posting ...");
		mProgress.show();

		AsyncFacebookRunner mAsyncFbRunner = new AsyncFacebookRunner(mFacebook);

		Bundle params = new Bundle();
		params.putString("message", review);
		params.putString("name", "Thoughts of Swami Vivekanand");
		params.putString("caption", "Thoughts of Swami Vivekananda");
		params.putString("link", "https://play.google.com/store/apps/details?id=com.clarusapps.SwamiVivekananda");
		params.putString("description","Inspirational quotes, Motivation quotes, Swami Vivekananda quote, Great quotes Swami Vivekanand�s Life Altering Quotes Motivation is the prime need to achieve the milestones you�ve set in your life. We all have face obstacles and at some point, given up our dreams just because we don�t feel the enthusiasm required to make your remarkable. At that time, motivation plays a crucial role to rejuvenate your energy and force you to start again from zero. Lauruss Infotech has designed an application exclusively dedicated to Swami Vivekanand�s life altering quotes to energize you once again. Swami Vivekanand was an Indian Hindu monk. He was the key person responsible for introducing Indian Philosophies of Vedanta and Yoga to the western world and bringing Hinduism to the status of a major world religion in the late 19th century. Being an incredible source of inspiration, he has given many quotes that boost the confidence and motivate the youth to recognize their strength. Our Swami Vivekanand apps combine some of the popular quotes. An inspiration and wonderful �quote of the day� is enough to energize you and clear your vision at the dark times. The Swami vivekanand quotes app is now available at iTune store and completely compatible with all versions of iPhone. Astonishing Features of the App- Send the Quotes via SMS Apart from viewing the quotes on your iPhone, you can share your favorite quotes with your friends and beloved through SMS. Lauruss Infotech has given this option, so that you could share the positive quotes with the people you love, care or concern through the most convenient medium. Share through Social Networking Sites Today, when everyone spends a good amount of time over the internet, especially social networking site, we cannot avoid its importance. You can share the quotes through the social sites or set them as your status easily. As vivekanad is a legend leader, people love to read his speeches and quotes. Simple, put his quotes on your profile and get remarkable recognition and compliments. Download our Vivekanand quotes apps today, and give a BUZZ to your friends and family. Inspirational quotes, Motivation quotes, Swami Vivekananda quote, Great quotes, Inspiration thoughts, Motivational Thoughts, Best Quotes, Free Quotes, Free thoughts"); 
		params.putString("picture", "http://swamivivekananda.jnanajyoti.com/intro_swami_vivekananda.jpg");
		mAsyncFbRunner.request("me/feed", params, "POST", new WallPostListener());
	}
	private final class WallPostListener extends BaseRequestListener {
		@Override
		public void onComplete(final String response) {
			mRunOnUi.post(new Runnable() {
				@Override
				public void run() {
					mProgress.cancel();

					Toast.makeText(MoreActivity.this, "Posted to Facebook", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
}
