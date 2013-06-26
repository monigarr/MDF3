package com.monigarr.hybriddemo;

import java.io.File;

import android.os.Environment;


public class BaseAlbumFactory extends AlbumStorageFactory {

	// location for camera files
	private static final String CAMERA_DIR = "/dcim/";

	@Override
	public File getAlbumStorageDir(String albumName) {
		return new File (
				Environment.getExternalStorageDirectory()
				+ CAMERA_DIR
				+ albumName
		);
	}
}
