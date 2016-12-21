package com.hereafter.wxarticle;

import java.io.File;
import java.io.IOException;

import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.hereafter.wxarticle.bmob.MyUser;
import com.hereafter.wxarticle.util.ImageLoaderUtil;
import com.hereafter.wxarticle.util.Utils;
import com.hereafter.wxarticle.widget.CircleImageView;
import com.hereafter.wxarticle.widget.ModelPopup;
import com.hereafter.wxarticle.widget.ModelPopup.OnDialogListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.socialize.utils.Log;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;

public class UserActivity extends BaseActivity implements OnDialogListener {
	@ViewInject(R.id.root)
	LinearLayout layout_root;
	@ViewInject(R.id.head)
	private CircleImageView headimg;
	@ViewInject(R.id.nickname)
	private EditText nickname;
	@ViewInject(R.id.birth_year)
	private Spinner birth_year;
	@ViewInject(R.id.sexgroup)
	private RadioGroup sexgroup;
	@ViewInject(R.id.email)
	private EditText email;
	@ViewInject(R.id.submit)
	private TextView submit;
	private String[] yeardata;
	private String selectYear;
	private int selectSex = -1;
	private String emailurl;
	private String headurl;
	private String nicknameStr;

	private MyUser curUser;
	private ModelPopup mPopup;
	public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
	public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
	private static final int CUT_PHOTO = 3;

	private Uri photoUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		ViewUtils.inject(this);
		initToolBar(getString(R.string.user_title));
		submit.setOnClickListener(this);
		initSpinner();
		initRadio();
		mPopup = new ModelPopup(this, this, false);
		headimg.setOnClickListener(this);
		curUser = BmobUser.getCurrentUser(this, MyUser.class);
		selectYear = curUser.getBirth_year();
		selectSex = curUser.getSex();
		headurl = curUser.getHeadimg();
		emailurl = curUser.getEmail();
		nicknameStr = curUser.getNickname();
		if (selectYear != null) {
			int year = Integer.parseInt(selectYear);
			int position = year - 1975 + 1;
			birth_year.setSelection(position);
		}
		if (selectSex != -1) {
			if (selectSex == 0) {
				sexgroup.check(R.id.male);
			} else {
				sexgroup.check(R.id.female);
			}
		}
		if (!TextUtils.isEmpty(headurl)) {
			ImageLoaderUtil.displayImage(headurl, headimg, this);
		}
		if (!TextUtils.isEmpty(emailurl)) {
			email.setText(emailurl);
		}
		if (!TextUtils.isEmpty(nicknameStr)) {
			nickname.setText(curUser.getNickname());
		}

	}

	private void initSpinner() {
		// TODO Auto-generated method stub
		int curyear = 2011;
		yeardata = new String[curyear - 1975 + 1];
		yeardata[0] = getString(R.string.user_selectbirth);
		for (int i = 1; i < yeardata.length; i++) {
			yeardata[i] = String.valueOf(1975 + i - 1);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yeardata);
		birth_year.setAdapter(adapter);
		birth_year.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				selectYear = yeardata[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void initRadio() {
		// TODO Auto-generated method stub
		sexgroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == R.id.male) {
					// selectSex =
				} else if (checkedId == R.id.female) {

				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.head:
			mPopup.showAtLocation(layout_root, Gravity.BOTTOM, 0, 0);
			break;
		case R.id.submit:
			int yearpos = birth_year.getSelectedItemPosition();
			selectYear = yeardata[yearpos];
			int sexid = -1;
			switch (sexgroup.getCheckedRadioButtonId()) {
			case R.id.male:
				sexid = 0;
				break;
			case R.id.female:
				sexid = 1;
				break;
			}
			emailurl = email.getText().toString().trim();
			nicknameStr = nickname.getText().toString().trim();
			if (TextUtils.isEmpty(headurl)) {
				showShortToast(R.string.user_selectheadimg);
			} else if (yearpos == 0) {
				showShortToast(R.string.user_selectbirth);
			} else if (sexid == -1) {
				showShortToast(R.string.user_selectsex);
			} else if (TextUtils.isEmpty(emailurl) || !Utils.isEmail(emailurl)) {
				showShortToast(R.string.user_inputemail);
			} else if (TextUtils.isEmpty(nicknameStr)) {
				showShortToast(R.string.user_inputnick);
			} else {
				curUser.setHeadimg(headurl);
				curUser.setBirth_year(selectYear);
				curUser.setSex(sexid);
				curUser.setEmail(emailurl);
				curUser.update(this, new UpdateListener() {
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						showLongToast(R.string.user_update_success);
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						showLongToast(R.string.user_update_fail);
					}
				});
			}
			break;

		default:
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case SELECT_PIC_BY_TACK_PHOTO:
				// 选择自拍结果
				beginCrop(photoUri);
				break;
			case SELECT_PIC_BY_PICK_PHOTO:
				// 选择图库图片结果
				beginCrop(intent.getData());
				break;
			case CUT_PHOTO:
				handleCrop(intent);
				break;
			}

		}
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void beginCrop(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY
		// 是裁剪图片宽高，注意如果return-data=true情况下,其实得到的是缩略图，并不是真实拍摄的图片大小，
		// 而原因是拍照的图片太大，所以这个宽高当你设置很大的时候发现并不起作用，就是因为返回的原图是缩略图，但是作为头像还是够清晰了
		intent.putExtra("outputX", 108);
		intent.putExtra("outputY", 108);
		// 返回图片数据
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CUT_PHOTO);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param result
	 */
	private void handleCrop(Intent result) {
		Bundle extras = result.getExtras();
		if (extras != null) {
			Bitmap bmp = extras.getParcelable("data");
			File file = null;
			try {
				file = Utils.saveMyBitmap(bmp, curUser.getUsername());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (file != null && file.exists()) {
				final ProgressDialog prodialog = new ProgressDialog(this);
				prodialog.setMessage(getString(R.string.user_upload_headimg));
				BTPFileResponse response = BmobProFile.getInstance(this).upload(file.getAbsolutePath(),
						new UploadListener() {

							@Override
							public void onError(int statuscode, String errormsg) {
								// TODO Auto-generated method stub
								prodialog.dismiss();
								showShortToast(R.string.user_upload_fail);
							}

							@Override
							public void onProgress(int progress) {
								// TODO Auto-generated method stub
								prodialog.setMessage(getString(R.string.user_upload_doing) + progress + "%");
							}

							@Override
							public void onSuccess(String fileName, String url, BmobFile file) {
								// TODO Auto-generated method stub
								prodialog.dismiss();
								showShortToast(R.string.user_upload_success);
								Log.e("hjq", "headurl=" + file.getUrl());
								headurl = file.getUrl();
								ImageLoaderUtil.displayImage(headurl, headimg, UserActivity.this);
							}
						});
			}
		}
	}

	@Override
	public void onChoosePhoto() {
		// TODO Auto-generated method stub
		Intent choosePictureIntent = new Intent(Intent.ACTION_PICK,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(choosePictureIntent, SELECT_PIC_BY_PICK_PHOTO);
	}

	@Override
	public void onTakePhoto() {
		// TODO Auto-generated method stub
		String SDState = Environment.getExternalStorageState();
		if (!SDState.equals(Environment.MEDIA_MOUNTED)) {
			showShortToast(R.string.user_sdcardnoexist);
			return;
		}
		try {
			photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
			if (photoUri != null) {
				Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				i.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
				startActivityForResult(i, SELECT_PIC_BY_TACK_PHOTO);
			} else {
				showShortToast(R.string.user_cant_savetoalbum);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			showShortToast(R.string.user_cant_savetoalbum);
		}
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}
}
