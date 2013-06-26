package com.monigarr.hybriddemo;

import java.io.File;

abstract class AlbumStorageFactory {
	public abstract File getAlbumStorageDir(String albumName);
}