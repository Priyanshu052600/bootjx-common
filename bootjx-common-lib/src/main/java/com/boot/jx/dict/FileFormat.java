package com.boot.jx.dict;

import java.util.HashMap;
import java.util.Map;

import com.boot.utils.ArgUtil;
import com.boot.utils.EnumType;
import com.boot.utils.StringUtils;

public enum FileFormat implements EnumType {
	PDF("application/pdf", FileType.DOCUMENT, "pdf"), CSV("text/csv", FileType.DOCUMENT, "csv"),

	PNG("image/png", FileType.IMAGE, "png"), JPEG("image/jpeg", FileType.IMAGE, "jpeg"),
	JPG("image/jpg", FileType.IMAGE, "jpg"), BMP("image/bmp", FileType.IMAGE, "bmp"),
	GIF("image/gif", FileType.IMAGE, "gif"), TIFF("image/tiff", FileType.IMAGE, "tiff"),
	TIF("image/tif", FileType.IMAGE, "tif"),

	WEBP("image/webp", FileType.IMAGE, "webp"),

	// Audio
	MP3("audio/mp3", FileType.AUDIO), aac("audio/aac", FileType.AUDIO, "aac"), AMR("audio/amr", FileType.AUDIO, "amr"),

	OGG("audio/ogg", FileType.AUDIO, "ogg"), OGG_PLUS("audio/ogg;codecs=opus", FileType.AUDIO),

	AUDIO_MP4("audio/mp4", FileType.AUDIO, "m4a"), AUDIO_MPEG("audio/mpeg", FileType.AUDIO, "mp3"),

	AUDIO_WEBM("audio/webm", FileType.AUDIO, "webm"),
	AUDIO_OPUS("audio/opus", FileType.AUDIO, "opus"),
	AUDIO_WEBM_OPUS("audio/webm;codecs=opus", FileType.AUDIO, "webm"),

	// Video
	MP4("video/mp4", FileType.VIDEO, "mp4"), VIDEO_3GPP("video/3gpp", FileType.VIDEO, "3gpp"),

	JSON("application/json", FileType.DOCUMENT, "json"), HTML("text/html", FileType.DOCUMENT, "html"),
	TEXT("text/plain", FileType.TEXT, "txt"), UNKNOWN("application/octet-stream");

	private static final Map<String, FileFormat> TYPEMAP = new HashMap<String, FileFormat>();

	static {
		for (FileFormat fileFormat : FileFormat.values()) {
			TYPEMAP.put(fileFormat.getContentType(), fileFormat);
			if (ArgUtil.is(fileFormat.getExt())) {
				TYPEMAP.put(fileFormat.getExt(), fileFormat);
			}
			// application/vnd.openxmlformats-officedocument.wordprocessingml.document
		}
	}

	String contentType;
	FileType fileType;
	String ext;

	public String getContentType() {
		return contentType;
	}

	FileFormat(String contentType, FileType formatType, String ext) {
		this.contentType = contentType;
		this.fileType = formatType;
		this.ext = ext;
	}

	FileFormat(String contentType, FileType formatType) {
		this(contentType, formatType, null);
	}

	FileFormat(String contentType) {
		this(contentType, FileType.DOCUMENT, null);
	}

	public static FileFormat from(String contentType, FileFormat defaultValue) {
		contentType = StringUtils.toLowerCase(StringUtils.removeSpaces(contentType));
		if (!TYPEMAP.containsKey(contentType)) {
			String[] contentTypes = StringUtils.split(contentType, ";");
			if (TYPEMAP.containsKey(contentTypes[0])) {
				return TYPEMAP.getOrDefault(contentTypes[0], ArgUtil.nonEmpty(defaultValue, UNKNOWN));
			}
		}
		return TYPEMAP.getOrDefault(contentType, ArgUtil.nonEmpty(defaultValue, UNKNOWN));
	}

	public static FileFormat from(String contentType) {
		return from(contentType, UNKNOWN);
	}

	/**
	 * IMAGE,TEXT, DOCUMENT,VIDEO
	 * 
	 * @return
	 */
	public FileType getFileType() {
		return fileType;
	}

	public String getExt() {
		return ext;
	}

}