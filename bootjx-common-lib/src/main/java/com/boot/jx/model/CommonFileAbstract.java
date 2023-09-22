package com.boot.jx.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.boot.jx.dict.FileFormat;
import com.boot.jx.dict.FileType;
import com.boot.jx.logger.LoggerService;
import com.boot.utils.ArgUtil;
import com.boot.utils.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

public class CommonFileAbstract<C extends CommonFileAbstract<C>> implements Serializable {

	private static final long serialVersionUID = 2644466910668360794L;
	private static Logger LOGGER = LoggerService.getLogger(CommonFileStream.class);

	private String content;
	private String path;
	private String name;
	private String title;
	protected FileFormat fileFormat;
	private FileType fileType;
	private String extension;
	private String password;
	protected String url;
	protected String thumb;
	protected long contentLength;
	protected String contentType;
	private CommonTemplateMeta template = null;
	private Map<String, Object> model = new HashMap<String, Object>();
	private Map<String, Object> options = new HashMap<String, Object>();
	private Map<String, Object> meta = new HashMap<String, Object>();
	protected Map<String, String> headers;

	@SuppressWarnings("unchecked")
	public C lang(Object lang) {
		this.template.setLang(ArgUtil.parseAsString(lang));
		return (C) this;
	}

	public Map<String, Object> getModel() {
		return model;
	}

	public void setModel(Map<String, Object> model) {
		this.model = model;
	}

	public Map<String, Object> model() {
		if (this.model == null) {
			this.model = new HashMap<String, Object>();
		}
		return model;
	}

	@JsonIgnore
	public void setObject(Object object) {
		this.model = JsonUtil.toMap(object);
	}

	protected byte[] body;

	public FileFormat getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(FileFormat type) {
		this.fileFormat = type;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public CommonTemplateMeta getTemplate() {
		return template;
	}

	public void setTemplate(CommonTemplateMeta template) {
		this.template = template;
	}

	@JsonSetter
	public CommonTemplateMeta template() {
		if (!ArgUtil.is(this.template)) {
			this.template = new CommonTemplateMeta();
		}
		return this.template;
	}

	public CommonFileAbstract<C> template(CommonTemplateMeta template) {
		this.template = template;
		return this;
	}

	public CommonFileAbstract<C> template(String template) {
		this.template().setCode(template);
		return this;
	}

	public CommonFileAbstract<C> templateId(String templateId) {
		this.template().setCode(templateId);
		return this;
	}

	/**
	 * If file type is json, this should return valid map
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> toMap() {
		return JsonUtil.fromJson(this.content, Map.class);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, Object> getOptions() {
		return options;
	}

	public void setOptions(Map<String, Object> options) {
		this.options = options;
	}

	public Map<String, Object> options() {
		if (this.options == null) {
			this.options = new HashMap<String, Object>();
		}
		return options;
	}

	public FileType getFileType() {
		if (ArgUtil.is(this.fileType)) {
			return this.fileType;
		} else if (ArgUtil.is(this.fileFormat)) {
			return this.fileFormat.getFileType();
		}
		return this.fileType;
	}

	public String getContentType() {
		if (ArgUtil.is(this.fileFormat)) {
			return this.fileFormat.getContentType();
		}
		return this.contentType;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getExtension() {
		if (ArgUtil.is(this.extension)) {
			return this.extension;
		} else if (ArgUtil.is(this.fileFormat)) {
			return this.fileFormat.name().toLowerCase();
		}
		return this.extension;
	}

	public void setFileType(FileType fileType) {
		this.fileType = fileType;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Map<String, String> headers() {
		if (!ArgUtil.is(this.headers)) {
			this.headers = new HashMap<String, String>();
		}
		return this.headers;
	}

	@SuppressWarnings("unchecked")
	public C url(String url) {
		this.setUrl(url);
		try {
			if (ArgUtil.is(url)) {
				URL urlObject = new URL(url);
				if (!ArgUtil.is(this.extension)) {
					this.extension = FilenameUtils.getExtension(urlObject.getPath());
				}
				if (!ArgUtil.is(this.name)) {
					this.name = FilenameUtils.getName(urlObject.getPath());
				}
				if (!ArgUtil.is(this.title)) {
					this.title = FilenameUtils.getBaseName(urlObject.getPath());
				}
				if (!ArgUtil.is(this.fileFormat) && ArgUtil.is(this.name)) {
					String mimeTye = URLConnection.guessContentTypeFromName(this.name);
					if (ArgUtil.is(mimeTye)) {
						this.fileFormat = FileFormat.from(mimeTye);
					} else {
						this.fileFormat = FileFormat.from(this.extension);
					}
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return (C) this;
	}

	public CommonFileAbstract<C> contentType(String contentType) {
		this.fileFormat = FileFormat.from(contentType, this.fileFormat);
		if (ArgUtil.is(this.fileFormat)) {
			this.fileType = this.fileFormat.getFileType();
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	@Deprecated
	public CommonFileAbstract<C> type(FileFormat fileFormat) {
		this.setFileFormat(fileFormat);
		return (C) this;
	}

	@SuppressWarnings("unchecked")
	public C fileType(FileType fileType) {
		this.setFileType(fileType);
		return (C) this;
	}

	public C format(FileFormat format) {
		this.setFileFormat(format);
		return (C) this;
	}

	@SuppressWarnings("unchecked")
	public C path(String path) {
		this.setPath(path);
		return (C) this;
	}

	@SuppressWarnings("unchecked")
	public C name(String name) {
		this.setName(name);
		return (C) this;
	}

	@SuppressWarnings("unchecked")
	public C header(String headerKey, String headerValue) {
		this.headers().put(headerKey, headerValue);
		return (C) this;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	@SuppressWarnings("unchecked")
	public CommonFileAbstract<C> headers(Map<String, String> headers) {
		this.headers = headers;
		return (C) this;
	}

	public Map<String, Object> getMeta() {
		return meta;
	}

	public void setMeta(Map<String, Object> meta) {
		this.meta = meta;
	}

	public Map<String, Object> meta() {
		if (!ArgUtil.is(this.meta)) {
			this.meta = new HashMap<String, Object>();
		}
		return this.meta;
	}

	public MultipartFile toMultipartFile(InputStream inputStream) throws IOException, FileNotFoundException {
		return null;
	}

	public MultipartFile toMultipartFile() throws FileNotFoundException, IOException {
		return toMultipartFile(toInputStream());
	}

	public InputStream toInputStream() throws IOException {
		return null;
	}

	public static <C extends CommonFileAbstract<C>> CommonFileAbstract<C> fromBase64(String base64String) {
		return fromBase64(base64String, FileFormat.TEXT);
	}

	public static <C extends CommonFileAbstract<C>> CommonFileAbstract<C> fromBase64(String base64String,
			FileFormat defaultType) {
		String[] strings = base64String.split(",");
		FileFormat extension;
		String dataPart;
		if (strings.length > 1) {
			dataPart = strings[1];
			switch (strings[0]) {// check image's extension
			case "data:image/jpeg;base64":
				extension = FileFormat.JPEG;
				break;
			case "data:image/png;base64":
				extension = FileFormat.PNG;
				break;
			default:// should write cases for more images types
				extension = FileFormat.JPG;
				break;
			}
		} else {
			extension = defaultType;
			dataPart = strings[0];
		}
		CommonFileAbstract<C> file = new CommonFileAbstract<C>();
		file.setFileFormat(extension);
		file.setBody(DatatypeConverter.parseBase64Binary(dataPart));
		return file;
	}

	// Common Methoeds
	public void create(HttpServletResponse response, Boolean download) throws IOException {
		OutputStream outputStream = null;
		response.setHeader("Cache-Control", "cache, must-revalidate");
		if (this.fileFormat == FileFormat.PDF) {
			response.addHeader("Content-type", "application/pdf");
		} else if (this.fileFormat == FileFormat.PNG) {
			response.addHeader("Content-type", "application/pdf");
		} else if (this.fileFormat == FileFormat.JPEG) {
			response.addHeader("Content-type", "application/jpeg");
		} else if (this.fileFormat == FileFormat.JPG) {
			response.addHeader("Content-type", "application/jpg");
		} else {
			response.addHeader("Content-type", this.getContentType());
		}

		if (download) {
			response.addHeader("Content-Disposition", "attachment; filename=" + getName());
		}
		try {
			byte[] contentBytes = body;
			if (ArgUtil.not(contentBytes) && ArgUtil.is(content)) {
				contentBytes = content.getBytes();
			}

			if (contentBytes != null) {
				outputStream = response.getOutputStream();
				outputStream.write(contentBytes);
				LOGGER.debug("File created successfully :  Template {}", this.getTemplate());
			}
		} finally

		{
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					/* ignore */ }
			}
		}
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}
