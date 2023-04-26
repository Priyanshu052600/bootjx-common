package com.boot.jx.model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map.Entry;

import javax.activation.DataSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.integration.http.multipart.UploadedMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.boot.jx.dict.FileFormat;
import com.boot.jx.logger.LoggerService;
import com.boot.utils.ArgUtil;

public class CommonFileStream extends CommonFileAbstract<CommonFileStream> {

	private static final long serialVersionUID = 311934636441245812L;
	private static Logger LOGGER = LoggerService.getLogger(CommonFileStream.class);

	private DataSource dataSource;
	private MultipartFile multipartFile;

	@Override
	public MultipartFile toMultipartFile(InputStream inputStream) throws IOException, FileNotFoundException {
		File file = File.createTempFile("tmp", "." + this.getExtension());
		byte[] binary = IOUtils.toByteArray(inputStream);
		FileUtils.writeByteArrayToFile(file, binary);

		String mimeType = this.getContentType();
		if (!ArgUtil.is(this.getFileFormat())) {
			InputStream is = new BufferedInputStream(new FileInputStream(file));
			mimeType = URLConnection.guessContentTypeFromStream(is);
			this.setFileFormat(FileFormat.from(mimeType));
		}

		UploadedMultipartFile multipartFile = new UploadedMultipartFile(file, file.length(), mimeType, "formParameter",
				this.getName());

		return multipartFile;
	}

	@Override
	public InputStream toInputStream() throws IOException {
		if (ArgUtil.is(dataSource)) {
			return dataSource.getInputStream();
		} else if (ArgUtil.is(multipartFile)) {
			return multipartFile.getInputStream();
		} else if (ArgUtil.is(this.url)) {
			URL url = new URL(this.url);
			URLConnection connection = url.openConnection();
			if (ArgUtil.is(this.headers)) {
				for (Entry<String, String> entry : this.headers.entrySet()) {
					connection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			InputStream inputStream = connection.getInputStream();
			return inputStream;
		}
		return null;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public CommonFileStream from(DataSource dataSource) {
		if (ArgUtil.is(dataSource)) {
			FileFormat format = FileFormat.from(dataSource.getContentType());
			this.dataSource = dataSource;
			this.fileType(format.getFileType()).format(format).name(dataSource.getName());
		}
		return this;
	}

	public CommonFileStream from(MultipartFile multipartFile) {
		if (ArgUtil.is(multipartFile)) {
			FileFormat format = FileFormat.from(multipartFile.getContentType());
			this.multipartFile = multipartFile;
			this.fileType(format.getFileType()).format(format).name(multipartFile.getName());
		}
		return this;
	}

}
