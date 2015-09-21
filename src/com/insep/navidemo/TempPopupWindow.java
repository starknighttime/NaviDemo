package com.insep.navidemo;

import com.amap.api.maps2d.model.BitmapDescriptorFactory;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.VideoView;

public class TempPopupWindow extends PopupWindow {
	private View contentView;
	private int position;

	public TempPopupWindow(final Activity context, int position) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.position = position;
		contentView = inflater.inflate(R.layout._temp_window, null);
		this.setContentView(contentView);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		contentView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				TempPopupWindow.this.dismiss();
			}
		});
	}

	public void showPopupWindow(View parent) {
		if (!this.isShowing()) {
			this.setWidth(1080);
			this.setHeight(1200);
			this.update();
			// 以下拉方式显示popupwindow

			// tempView.setImageBitmap(BitmapDescriptorFactory.fromResource(
			// R.drawable.login_demo).getBitmap());
			this.showAtLocation(parent, Gravity.CENTER, 0, 137);
		} else {
			this.dismiss();
		}
	}
}